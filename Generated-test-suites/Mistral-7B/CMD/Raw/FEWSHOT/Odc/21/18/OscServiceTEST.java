package com.oceanbase.odc.service.onlineschemachange;

import static com.oceanbase.odc.core.shared.PreConditions.assertFalse;
import static com.oceanbase.odc.core.shared.PreConditions.assertNull;
import static com.oceanbase.odc.core.shared.PreConditions.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskResult;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

/**
 * Test class for OscService
 */
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> databaseEntity = Optional.of(new DatabaseEntity());

        when(databaseRepository.findById(id)).thenReturn(databaseEntity);

        OscService oscService = new OscService();
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoWithEmptyDatabase() {
        Long id = 1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscService oscService = new OscService();
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNull(result);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(scheduleEntity.getConnectionId())).thenReturn(Optional.of(new DatabaseEntity()));

        OscService oscService = new OscService();
        OscSwapTableVO resultOscSwapTable = oscService.swapTable(scheduleTaskId);

        assertNotNull(resultOscSwapTable);
        assertEquals(scheduleTaskId, resultOscSwapTable.getScheduleTaskId());
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTableWithInvalidScheduleType() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(scheduleEntity.getConnectionId())).thenReturn(Optional.of(new DatabaseEntity()));

        PreConditions.setCurrentErrorCodes(ErrorCodes.BadArgument);

        OscService oscService = new OscService();
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTableWithInvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(scheduleEntity.getConnectionId())).thenReturn(Optional.of(new DatabaseEntity()));

        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

        OscService oscService = new OscService();
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTableWithDisabledManualSwapTable() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(scheduleEntity.getConnectionId())).thenReturn(Optional.of(new DatabaseEntity()));

        result.setManualSwapTableEnabled(false);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleTask.setResultJson(JsonUtils.toJson(result));

        OscService oscService = new OscService();
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        boolean result = new OscService().getLockUserIsRequired(connectionId);

        assertFalse(result);
    }

    @Test
    public void testGetLockUserIsRequiredWithLockUserRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        String version = "1.0.0";
        ConnectionSession connSession = mock(ConnectionSession.class);

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(connectionConfig);
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

        boolean result = new OscService().getLockUserIsRequired(connectionId);

        assertTrue(result);
    }
}