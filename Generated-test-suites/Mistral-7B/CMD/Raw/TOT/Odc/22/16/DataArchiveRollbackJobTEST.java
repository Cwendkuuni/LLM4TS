import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobDetail;
import org.quartz.MockitoScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataArchiveRollbackJobTest {

    private MockitoScheduler scheduler;
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private StdSchedulerFactory schedulerFactory;
    private ScheduleTaskEntity scheduleTaskEntity;
    private JobExecutionContext context;

    @Before
    public void setup() {
        schedulerFactory = new StdSchedulerFactory();
        scheduler = new MockitoScheduler();
        dataArchiveRollbackJob = new DataArchiveRollbackJob();

        scheduleTaskEntity = new ScheduleTaskEntity();
        context = new MockitoJobExecutionContext();
        context.setResult(scheduleTaskEntity);
    }

    @Test
    public void testExecute_NormalCase() throws Exception {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setStatus(TaskStatus.DONE);
        taskUnits.add(taskUnit);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleTaskRepository.updateStatusById(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(1);

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_TaskNotFound() throws Exception {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
        assertEquals("Data archive task not found,rollback task fast failed.dataArchiveTaskId=" + rollbackParameters.getDataArchiveTaskId(),
                Mockito.getInstance(Log.class).getLog(Mockito.any(Level.class)).get(0).getMessage());
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_ErrorWhenPreparingTasks() throws Exception {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleTaskRepository.updateStatusById(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Error when preparing tasks"));

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_TasksExecutedSuccessfully() throws Exception {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setStatus(TaskStatus.DONE);
        taskUnits.add(taskUnit);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleTaskRepository.updateStatusById(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(1);

        Mockito.when(executionService.executeTask(Mockito.anyLong(), Mockito.anyList()))
                .thenReturn(1);

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
    }
}