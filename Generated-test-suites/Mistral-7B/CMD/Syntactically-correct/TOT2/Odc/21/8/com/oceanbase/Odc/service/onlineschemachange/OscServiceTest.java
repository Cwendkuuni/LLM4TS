import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import static org.mockito.Mockito.*;

public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        oscService = new OscService();
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
    }

    // Test for getOscDatabaseInfo method
    @Test
    public void testGetOscDatabaseInfo() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo expectedResult = new OscLockDatabaseUserInfo();
        expectedResult.setDatabaseId(databaseId);
        expectedResult.setLockDatabaseUserRequired(true);

        OscLockDatabaseUserInfo actualResult = oscService.getOscDatabaseInfo(databaseId);

        verify(databaseRepository, times(1)).findById(databaseId);
        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(databaseRepository);
        verifyNoMoreInteractions(scheduleTaskRepository);

        assertNotNull(actualResult);
        assertEquals(expectedResult.getDatabaseId(), actualResult.getDatabaseId());
        assertEquals(expectedResult.isLockDatabaseUserRequired(), actualResult.isLockDatabaseUserRequired());
    }

    // Test for swapTable method
    @Test
    public void testSwapTable() throws Exception {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("scheduleId");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, anyString())).thenReturn(1);

        OscSwapTableVO expectedResult = new OscSwapTableVO();
        expectedResult.setScheduleTaskId(scheduleTaskId);

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
        verifyNoMoreInteractions(databaseRepository);
        verifyNoMoreInteractions(connectionService);

        assertNotNull(expectedResult);
        assertEquals(expectedResult.getScheduleTaskId(), actualResult.getScheduleTaskId());
    }

    // Test for getLockUserIsRequired method
    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(new ConnectionConfig());
        when(OscDBUserUtil.isLockUserRequired(anyString(), any()))
                .thenReturn(false);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verify(OscDBUserUtil, times(1)).isLockUserRequired(anyString(), any());
        verifyNoMoreInteractions(databaseRepository);
        verifyNoMoreInteractions(connectionService);

        assertFalse(result);
    }

    // Test for getLockUserIsRequired method with exception
    @Test(expected = Exception.class)
    public void testGetLockUserIsRequiredWithException() {
        Long connectionId = 1L;

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenThrow(new Exception());

        oscService.getLockUserIsRequired(connectionId);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verifyNoMoreInteractions(databaseRepository);
        verifyNoMoreInteractions(connectionService);
    }
}