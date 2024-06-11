import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.impl.StdSchedulerFactory;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJobTest;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private AbstractDlmJob abstractDlmJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        abstractDlmJob = mock(AbstractDlmJob.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setAbstractDlmJob(abstractDlmJob);

        scheduleTaskEntity = new ScheduleTaskEntity();
        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
    }

    @Test
    public void testExecute_TaskNotPresent() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("1");
        doReturn(Optional.empty()).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TaskNotFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("1");
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId("1");
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TasksClearSuccess() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("1");
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId("1");
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

        List<DlmTask> tasks = new ArrayList<>();
        DlmTask task1 = new DlmTask();
        task1.setStatus(TaskStatus.PREPARING);
        tasks.add(task1);
        DlmTask task2 = new DlmTask();
        task2.setStatus(TaskStatus.RUNNING);
        tasks.add(task2);
        DlmTask task3 = new DlmTask();
        task3.setStatus(TaskStatus.DONE);
        tasks.add(task3);

        doReturn(tasks).when(dataArchiveDeleteJob).executeTask(scheduleTaskEntity.getId(), tasks);

        TaskStatus expectedStatus = TaskStatus.DONE;

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), expectedStatus);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_TasksClearFailed() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("1");
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId("1");
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

        List<DlmTask> tasks = new ArrayList<>();
        DlmTask task1 = new DlmTask();
        task1.setStatus(TaskStatus.PREPARING);
        tasks.add(task1);
        DlmTask task2 = new DlmTask();
        task2.setStatus(TaskStatus.RUNNING);
        tasks.add(task2);
        DlmTask task3 = new DlmTask();
        task3.setStatus(TaskStatus.FAILED);
        tasks.add(task3);

        doThrow(new RuntimeException("Task clearing failed")).when(dataArchiveDeleteJob).executeTask(scheduleTaskEntity.getId(), tasks);

        dataArchiveDeleteJob.execute(jobExecutionContext);
    }

    @Test
    public void testGetTaskStatus_AllTasksRunning() {
        List<DlmTask> tasks = new ArrayList<>();
        DlmTask task1 = new DlmTask();
        task1.setStatus(TaskStatus.RUNNING);
        tasks.add(task1);
        DlmTask task2 = new DlmTask();
        task2.setStatus(TaskStatus.RUNNING);
        tasks.add(task2);
        DlmTask task3 = new DlmTask();
        task3.setStatus(TaskStatus.RUNNING);
        tasks.add(task3);

        TaskStatus expectedStatus = TaskStatus.RUNNING;

        TaskStatus actualStatus = dataArchiveDeleteJob.getTaskStatus(tasks);

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void testGetTaskStatus_AllTasksDone() {
        List<DlmTask> tasks = new ArrayList<>();
        DlmTask task1 = new DlmTask();
        task1.setStatus(TaskStatus.DONE);
        tasks.add(task1);
        DlmTask task2 = new DlmTask();
        task2.setStatus(TaskStatus.DONE);
        tasks.add(task2);
        DlmTask task3 = new DlmTask();
        task3.setStatus(TaskStatus.DONE);
        tasks.add(task3);

        TaskStatus expectedStatus = TaskStatus.DONE;

        TaskStatus actualStatus = dataArchiveDeleteJob.getTaskStatus(tasks);

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void testGetTaskStatus_TasksMixed() {
        List<DlmTask> tasks = new ArrayList<>();
        DlmTask task1 = new DlmTask();
        task1.setStatus(TaskStatus.RUNNING);
        tasks.add(task1);
        DlmTask task2 = new DlmTask();
        task2.setStatus(TaskStatus.DONE);
        tasks.add(task2);
        DlmTask task3 = new DlmTask();
        task3.setStatus(TaskStatus.RUNNING);
        tasks.add(task3);

        TaskStatus expectedStatus = TaskStatus.RUNNING;

        TaskStatus actualStatus = dataArchiveDeleteJob.getTaskStatus(tasks);

        assertEquals(expectedStatus, actualStatus);
    }
}