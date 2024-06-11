import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext mockContext;
    private ScheduleTaskEntity mockTaskEntity;
    private ScheduleTaskRepository mockScheduleTaskRepository;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        mockContext = mock(JobExecutionContext.class);
        mockTaskEntity = mock(ScheduleTaskEntity.class);
        mockScheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob.setScheduleTaskRepository(mockScheduleTaskRepository);
    }

    @Test
    public void testExecute_Success() {
        // Prepare test data
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(123L);
        String parametersJson = "{\"dataArchiveTaskId\":123}";
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());

        // Mock behavior
        when(mockContext.getResult()).thenReturn(mockTaskEntity);
        when(mockTaskEntity.getParametersJson()).thenReturn(parametersJson);
        when(mockScheduleTaskRepository.findById(123L)).thenReturn(Optional.of(mockTaskEntity));
        when(mockTaskEntity.getResultJson()).thenReturn(JsonUtils.toJsonString(taskUnits));

        // Execute the method
        dataArchiveRollbackJob.execute(mockContext);

        // Verify the task status is updated
        verify(mockScheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.DONE));
    }

    // Add more test cases for edge cases and error scenarios here

}