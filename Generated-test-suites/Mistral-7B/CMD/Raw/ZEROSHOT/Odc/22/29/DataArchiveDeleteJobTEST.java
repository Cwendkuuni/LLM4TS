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
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * DataArchiveDeleteJobTest class
 */
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private ObjectMapper objectMapper;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    }

    @Test
    public void testExecuteWithValidData() throws Exception {
        // given
        String jsonString = "{\"dataArchiveTaskId\":1,\"parametersJson\":\"{\"taskName\":\"testTaskName\",\"dataArchiveTaskId\":1}\"}";
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1);
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(jsonString);
        scheduleTaskEntity.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED)))
                .thenReturn(1);
        when(scheduleTaskRepository.updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.PREPARING)))
                .thenReturn(1);
        when(scheduleTaskRepository.updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.DONE)))
                .thenReturn(1);

        // when
        dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.PREPARING);

        verifyTasksExecuted(taskUnits);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecuteWithDataArchiveTaskNotFound() throws Exception {
        // given
        String jsonString = "{\"dataArchiveTaskId\":1,\"parametersJson\":\"{\"taskName\":\"testTaskName\",\"dataArchiveTaskId\":1}\"}";
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1);
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(jsonString);
        scheduleTaskEntity.setStatus(TaskStatus.FAILED);

        // when
        dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecuteWithDataArchiveTaskNotFinished() throws Exception {
        // given
        String jsonString = "{\"dataArchiveTaskId\":1,\"parametersJson\":\"{\"taskName\":\"testTaskName\",\"dataArchiveTaskId\":1}\"}";
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1);
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(jsonString);
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

        // when
        dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    private void verifyTasksExecuted(List<DlmTask> taskUnits) {
        for (DlmTask dlmTask : taskUnits) {
            Mockito.verify(dataArchiveDeleteJob, Mockito.times(1))
                    .executeTask(Mockito.eq(scheduleTaskEntity.getId()), Mockito.argThat(task -> task.getId().equals(dlmTask.getId())));
            Mockito.verify(dataArchiveDeleteJob, Mockito.times(1))
                    .getTaskStatus(Mockito.eq(taskUnits));
        }
    }
}