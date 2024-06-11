package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfigConstants;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings({"OptionalGetWithoutIsPresent", "ResultOfMethodCallIgnored"})
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    private Long id = 1L;
    private Long scheduleTaskId = 1L;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        reset(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(database);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(id, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_LockDatabaseUserRequired() {
        Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(database);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(ConnectionConfigConstants.MYSQL);
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);

        when(databaseRepository.findById(id)).thenReturn(database);
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenThrow(new RuntimeException("Test Exception"));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test(expected = RuntimeException.class)
    public void testGetOscDatabaseInfo_ConnectionException() {
        Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(database);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenThrow(new RuntimeException("Test Exception"));

        oscService.getOscDatabaseInfo(id);
    }

    @Test
    public void testSwapTable() {
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        oscService.swapTable(scheduleTaskId);

        assertTrue(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());

        // close manual swap table
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(true);
        scheduleTaskRepository.updateTaskResult(scheduleTaskId, JsonUtils.toJson(result));

        // test swap table again
        oscService.swapTable(scheduleTaskId);

        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidScheduleTask() {
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.empty();
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidSchedule() {
        Optional<ScheduleEntity> scheduleEntityOptional = Optional.empty();
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(scheduleEntityOptional);

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidOscParameters() {
        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(new ScheduleEntity());
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(scheduleEntityOptional);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(null);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidResult() {
        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(new ScheduleEntity());
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(scheduleEntityOptional);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        // result.setManualSwapTableEnabled(null);
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType(ConnectionConfigConstants.MYSQL);

        PreConditions.assertTrue(oscService.getLockUserIsRequired(decryptedConnConfig.getConnectionId()));

        decryptedConnConfig.setDialectType(ConnectionConfigConstants.ORACLE);

        PreConditions.assertFalse(oscService.getLockUserIsRequired(decryptedConnConfig.getConnectionId()));
    }

    @Test
    public void testGetLockUserIsRequired_ConnectionException() {
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType(ConnectionConfigConstants.MYSQL);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenThrow(new RuntimeException("Test Exception"));

        PreConditions.assertTrue(oscService.getLockUserIsRequired(decryptedConnConfig.getConnectionId()));
    }
}