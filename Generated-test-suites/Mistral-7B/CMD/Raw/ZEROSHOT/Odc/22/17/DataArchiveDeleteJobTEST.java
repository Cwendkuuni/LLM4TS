import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Test class for DataArchiveDeleteJob.
 */
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private ScheduleTaskEntity scheduleTaskEntity;

    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    /**
     * Test when data archive task is not found.
     */
    @Test
    public void testDataArchiveTaskNotFound() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");

        doReturn(Optional.empty()).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

        try {
            dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
            assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getOne(1L).getStatus());
        } catch (JobExecutionException e) {
            // ignore
        }
    }

    /**
     * Test when data archive task is already done.
     */
    @Test
    public void testDataArchiveTaskAlreadyDone() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

        try {
            dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
            assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getOne(1L).getStatus());
        } catch (JobExecutionException e) {
            // ignore
        }
    }

    /**
     * Test when executing tasks.
     */
    @Test
    public void testExecuteTasks() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        DlmTask dlmTask2 = new DlmTask();
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

        try {
            dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
            TaskStatus expectedTaskStatus = TaskStatus.SUCCEEDED;
            assertEquals(expectedTaskStatus, scheduleTaskRepository.getOne(1L).getStatus());
        } catch (JobExecutionException e) {
            // ignore
        }

        Mockito.verify(scheduleTaskRepository).updateStatusById(any(Long.class), any(TaskStatus.class));

        Mockito.verify(scheduleTaskRepository).saveAll(dlmTasks);
    }
}