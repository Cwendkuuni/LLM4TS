import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

@SuppressWarnings("unchecked")
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertNull(oscDatabaseInfo);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseFound() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertEquals(id, oscDatabaseInfo.getDatabaseId());
        assertTrue(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_jobTypeNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        scheduleTask.setJobName("NOT_ONLINE_SCHEMA_CHANGE");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_jobTypeOnlineSchemaChange_manualSwapTableEnabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{}");
        scheduleTask.setJobName(scheduleEntity.getJobName());
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        doReturn(oscParameters).when(oscService).parseOnlineSchemaChangeParameters(scheduleEntity.getJobParametersJson());
        doReturn(true).when(oscService).isManualSwapTableEnabled(scheduleTask.getResultJson());
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, argThat(json -> {
            OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(json, OnlineSchemaChangeScheduleTaskResult.class);
            assertTrue(result.isManualSwapTableEnabled());
            assertFalse(result.isManualSwapTableStarted());
            return true;
        }));
    }

    @Test
    public void testSwapTable_jobTypeOnlineSchemaChange_manualSwapTableDisabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{}");
        scheduleTask.setJobName(scheduleEntity.getJobName());
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        doReturn(oscParameters).when(oscService).parseOnlineSchemaChangeParameters(scheduleEntity.getJobParametersJson());
        doReturn(false).when(oscService).isManualSwapTableEnabled(scheduleTask.getResultJson());
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, argThat(json -> {
            OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(json, OnlineSchemaChangeScheduleTaskResult.class);
            assertTrue(result.isManualSwapTableEnabled());
            return true;
        }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_swapTableTypeNotManual() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{}");
        scheduleTask.setJobName(scheduleEntity.getJobName());
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTO);
        doReturn(oscParameters).when(oscService).parseOnlineSchemaChangeParameters(scheduleEntity.getJobParametersJson());
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired_connectionIdValid() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = mock(ConnectionConfig.class);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequired_connectionIdInvalid() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = mock(ConnectionConfig.class);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }
}