package com.oceanbase.odc.service.onlineschemachange;

import static com.oceanbase.odc.service.onlineschemachange.constants.ErrorCodes.BadArgument;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.OnlineSchemaChangeParameters;
import com.oceanbase.odc.metadb.schedule.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private OscService oscService;

    private ConnectionSessionFactory connectionSessionFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionSessionFactory = mock(ConnectionSessionFactory.class);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        when(databaseRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(1L);
        assertEquals(new OscLockDatabaseUserInfo(), actual);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseFound() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        when(databaseRepository.findById(anyLong()))
                .thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(databaseId);
        expected.setLockDatabaseUserRequired(true);

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_scheduleTaskNotFound() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_scheduleTaskFound_jobTypeMismatch() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_PREPARE);

        when(scheduleRepository.findById(ArgumentMatchers.eq(Long.parseLong(scheduleTask.getJobName()))))
                .thenReturn(Optional.of(scheduleEntity));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));
    }

    @Test
    public void testSwapTable_scheduleTaskFound_manualSwapTableDisabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        when(scheduleRepository.findById(ArgumentMatchers.eq(Long.parseLong(scheduleTask.getJobName()))))
                .thenReturn(Optional.of(scheduleEntity));

        when(scheduleTaskRepository.updateTaskResult(anyLong(), anyString()))
                .thenAnswer(invocation -> {
                    OnlineSchemaChangeScheduleTaskResult updatedResult = result;
                    updatedResult.setManualSwapTableEnabled(true);
                    updatedResult.setManualSwapTableStarted(true);
                    return null;
                });

        OscSwapTableVO actual = oscService.swapTable(scheduleTaskId);
        assertNull(actual);
    }

    @Test
    public void testSwapTable_scheduleTaskFound_manualSwapTableEnabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleRepository.findById(ArgumentMatchers.eq(Long.parseLong(scheduleTask.getJobName()))))
                .thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO expected = new OscSwapTableVO();
        expected.setScheduleTaskId(scheduleTaskId);

        OscSwapTableVO actual = oscService.swapTable(scheduleTaskId);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLockUserIsRequired_lockUserRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("OBDC");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        String version = "3.0.0";
        when(factory.generateSession())
                .thenReturn(mock(ConnectionSession.class));
        when(ConnectionSessionUtil.getVersion(any(ConnectionSession.class)))
                .thenReturn(version);

        boolean actual = oscService.getLockUserIsRequired(connectionId);
        assertTrue(actual);
    }

    @Test
    public void testGetLockUserIsRequired_noLockUserRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        when(factory.generateSession())
                .thenThrow(new RuntimeException("Failed to create session"));

        boolean actual = oscService.getLockUserIsRequired(connectionId);
        assertFalse(actual);
    }
}