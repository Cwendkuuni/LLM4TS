import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext mockJobExecutionContext;
    private ScheduleTaskEntity mockTaskEntity;
    private DataArchiveRollbackParameters mockDataArchiveRollbackParameters;
    private List<DlmTask> mockTaskUnits;

    @Before
    public void setUp() {
        mockJobExecutionContext = mock(JobExecutionContext.class);
        mockTaskEntity = mock(ScheduleTaskEntity.class);
        mockDataArchiveRollbackParameters = mock(DataArchiveRollbackParameters.class);
        mockTaskUnits = new ArrayList<>();
    }

    @Test
    public void testExecute_WhenDataArchiveTaskFoundAndRollbackSuccessful_ExpectSuccess() {
        // Setup
        Long dataArchiveTaskId = 1L;
        Long sourceDatabaseId = 2L;
        Long targetDatabaseId = 3L;
        
        mockTaskEntity.setParametersJson(JsonUtils.toJson(mockDataArchiveRollbackParameters));
        when(mockTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(mockDataArchiveRollbackParameters));
        when(mockDataArchiveRollbackParameters.getDataArchiveTaskId()).thenReturn(dataArchiveTaskId);
        
        ScheduleTaskEntity mockDataArchiveTask = mock(ScheduleTaskEntity.class);
        
        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(mockDataArchiveTask));
        when(mockDataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(mockTaskUnits));
        
        DlmTask mockTaskUnit = new DlmTask();
        mockTaskUnit.setSourceDatabaseId(sourceDatabaseId);
        mockTaskUnit.setTargetDatabaseId(targetDatabaseId);
        mockTaskUnit.setStatus(TaskStatus.PREPARING);
        
        mockTaskUnits.add(mockTaskUnit);
        
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);
        
        // Execute
        dataArchiveRollbackJob.execute(mockJobExecutionContext);
        
        // Verify
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.DONE));
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.PREPARING));
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    // Add more test cases for other methods if needed

}