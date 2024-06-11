import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    }

    @Test
    public void testExecute_DataArchiveTaskFound_TaskStatusDone_DataArchiveDeleteSuccess() {
        // Given
        String taskId = "123";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(Long.parseLong(taskId));
        taskEntity.setStatus(TaskStatus.DONE);
        taskEntity.setResultJson("[]");
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(taskId);
        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask);

        // When
        try {
            dataArchiveDeleteJob.execute(new JobExecutionContext());
        } catch (JobExecutionException e) {
            fail("execute should not throw an exception.");
        }

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.SUCCESS);
        verify(scheduleTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_TaskStatusDONE_RollbackTaskAndUpdateStatus() {
        // Given
        String taskId = "123";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(Long.parseLong(taskId));
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(taskId);
        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When
        try {
            dataArchiveDeleteJob.execute(new JobExecutionContext());
        } catch (Exception e) {
            // Expected
        }

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.FAILED);
    }

    @Test(expected = Exception.class)
    public void testExecute_DataArchiveTaskNotFound_TaskStatusRunning_RollbackTaskAndUpdateStatus() {
        // Given
        String taskId = "123";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(Long.parseLong(taskId));
        taskEntity.setStatus(TaskStatus.RUNNING);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(taskId);
        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        // When
        dataArchiveDeleteJob.execute(new JobExecutionContext());

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_TaskStatusFailed_RollbackTaskAndUpdateStatus() {
        // Given
        String taskId = "123";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(Long.parseLong(taskId));
        taskEntity.setStatus(TaskStatus.FAILED);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(taskId);
        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        // When
        try {
            dataArchiveDeleteJob.execute(new JobExecutionContext());
        } catch (Exception e) {
            // Expected
        }

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskRunning_TaskStatusFailed_RollbackTaskAndUpdateStatus() {
        // Given
        String taskId = "123";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(Long.parseLong(taskId));
        taskEntity.setStatus(TaskStatus.RUNNING);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(taskId);
        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.FAILED);
        taskUnits.add(dlmTask);

        // When
        try {
            dataArchiveDeleteJob.execute(new JobExecutionContext());
        } catch (Exception e) {
            // Expected
        }

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskRunning_TaskStatusSuccess_DataArchiveDeleteSuccess() {
        // Given
        String taskId = "123";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(Long.parseLong(taskId));
        taskEntity.setStatus(TaskStatus.RUNNING);
        taskEntity.setResultJson("[]");
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(taskId);
        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask);

        // When
        try {
            dataArchiveDeleteJob.execute(new JobExecutionContext());
        } catch (JobExecutionException e) {
            fail("execute should not throw an exception.");
        }

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.SUCCESS);
        verify(scheduleTaskRepository, times(1)).findById(taskId);
    }

    @Test
    public void testExecute_DataArchiveTaskRunning_TaskStatusFailed_RollbackDataArchiveTasksAndUpdateStatus() {
        // Given
        String taskId = "123";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(Long.parseLong(taskId));
        taskEntity.setStatus(TaskStatus.RUNNING);
        taskEntity.setResultJson("[]");
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(taskId);
        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("1");
        dlmTask1.setStatus(TaskStatus.DONE);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId("2");
        dlmTask2.setStatus(TaskStatus.RUNNING);
        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);

        // When
        try {
            dataArchiveDeleteJob.execute(new JobExecutionContext());
        } catch (Exception e) {
            // Expected
        }

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.FAILED);
        verify(scheduleTaskRepository, times(2)).findById(eq(taskId));
        verify(scheduleTaskRepository, times(2)).updateStatusById(anyString());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_TaskStatusFailed_RollbackDataArchiveTasksAndUpdateStatus() {
        // Given
        String taskId = "123";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(Long.parseLong(taskId));
        taskEntity.setStatus(TaskStatus.FAILED);
        taskEntity.setResultJson("[]");
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(taskId);
        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("1");
        dlmTask1.setStatus(TaskStatus.DONE);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId("2");
        dlmTask2.setStatus(TaskStatus.RUNNING);
        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);

        // When
        try {
            dataArchiveDeleteJob.execute(new JobExecutionContext());
        } catch (Exception e) {
            // Expected
        }

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.FAILED);
        verify(scheduleTaskRepository, times(2)).findById(eq(taskId));
        verify(scheduleTaskRepository, times(2)).updateStatusById(anyString());
    }
}