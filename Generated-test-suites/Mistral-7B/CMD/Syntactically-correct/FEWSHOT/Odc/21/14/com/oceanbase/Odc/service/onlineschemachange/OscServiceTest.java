package com.oceanbase.odc.service.onlineschemachange;

import static com.oceanbase.odc.core.shared.PreConditions.assertFalse;
import static com.oceanbase.odc.core.shared.PreConditions.assertNull;
import static com.oceanbase.odc.core.shared.PreConditions.assertEquals;
import static com.oceanbase.odc.core.shared.PreConditions.assertFalse;
import static com.oceanbase.odc.core.shared.PreConditions.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setupMocks() {
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        OscService oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);

        PreConditions.expectException(IllegalArgumentException.class);

        oscService.getOscDatabaseInfo(-1L);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseFound() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        OscService oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertNotNull(oscDatabase);
        assertEquals(1L, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_ScheduleTaskNotFound() {
        PreConditions.expectException(IllegalArgumentException.class);

        OscService oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);

        oscService.swapTable(-1L);
    }

    @Test
    public void testSwapTable_ScheduleTaskFound_InvalidJobType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("test");
        scheduleEntity.setJobType(JobType.BACKUP);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscService oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);

        PreConditions.expectArgumentState(ErrorCodes.BadArgument, new Object[] {JobType.BACKUP},
                "Task type is not " + TaskType.ONLINE_SCHEMA_CHANGE.name());

        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTable_ScheduleTaskFound_InvalidSwapTableType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("test");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTO);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());

        OscService oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setOscDBUserUtil(mock(OscDBUserUtil.class));

        PreConditions.expectArgumentState(ErrorCodes.BadArgument, new Object[] {SwapTableType.AUTO},
                "Swap table type is not " + SwapTableType.MANUAL.name());

        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTable_ScheduleTaskFound_InvalidManualSwapTableStatus() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("test");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());

        OscService oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setOscDBUserUtil(mock(OscDBUserUtil.class));

        PreConditions.expectArgumentState(ErrorCodes.BadArgument, new Object[] {false},
                "Manual Swap table type is not enable ");

        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTable_ScheduleTaskFound_Success() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("test");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());

        OscService oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setOscDBUserUtil(mock(OscDBUserUtil.class));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNotNull(oscSwapTable);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionNotFound() {
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();

        PreConditions.expectArgumentState(ErrorCodes.BadArgument, new Object[] {() -> {
            OscService.getLockUserIsRequired(null);
        }}, "connectionId is null");

        OscService.getLockUserIsRequired(null);
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionFound_NoDialectType() {
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType(null);

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = mock(ConnectionSession.class);

        when(factory.generateSession()).thenReturn(connSession);

        assertFalse(OscDBUserUtil.isLockUserRequired(null, () -> {
            ConnectionSessionUtil.getVersion(connSession);
        }));
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionFound_NoLockUser() {
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = mock(ConnectionSession.class);

        when(factory.generateSession()).thenReturn(connSession);

        assertFalse(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionUtil.getVersion(connSession);
        }));
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionFound_LockUserRequired() {
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("Oracle");

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = mock(ConnectionSession.class);

        when(factory.generateSession()).thenReturn(connSession);

        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn("12.1.0.2.0");

        assertTrue(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            ConnectionSessionUtil.getVersion(connSession);
        }));
    }
}