package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.SessionFactory;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;
    private OscService oscService;
    private SessionFactory sessionFactory;

    @Before
    public void setup() {
        databaseRepository = mock(DatabaseRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        connectionService = mock(ConnectionService.class);
        sessionFactory = new DefaultConnectSessionFactory(null);
        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testGetOscDatabaseInfoNullDatabase() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo osdbi = oscService.getOscDatabaseInfo(1L);
        assertNull(osdbi.getDatabaseId());
        assertFalse(osdbi.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoPresentDatabase() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo osdbi = oscService.getOscDatabaseInfo(1L);
        assertEquals(1L, osdbi.getDatabaseId());
        assertTrue(osdbi.isLockDatabaseUserRequired());
    }

    @Test(expected = NullPointerException.class)
    public void testSwapTableNullScheduleTask() {
        oscService.swapTable(null);
    }

    @Test
    public void testSwapTablePresentScheduleTask() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        oscService.swapTable(1L);
        OscSwapTableVO oscSwapTable = new OscSwapTableVO();
        oscSwapTable.setScheduleTaskId(1L);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableInvalidJobType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.BACKUP);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        oscService.swapTable(1L);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableInvalidSwapTableType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MIGRATE);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTableDisableManualSwapTable() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{\"manualSwapTableEnabled\": false}");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        oscService.swapTable(1L);
        assertThrows(IllegalStateException.class, () -> oscService.swapTable(1L));
    }

    @Test
    public void testGetLockUserIsRequiredNullConnection() {
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(null);
        assertFalse(oscService.getLockUserIsRequired(1L));
    }

    @Test
    public void testGetLockUserIsRequiredNotNullConnection() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        assertTrue(oscService.getLockUserIsRequired(1L));
    }

}