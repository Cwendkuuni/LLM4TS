import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobDetailFactory;
import org.quartz.TestObjects.MockitoJobDataMap;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link DataArchiveRollbackJob}.
 */
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskRepository scheduleTaskRepository;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        String taskId = "testTaskId";
        Long dataArchiveTaskId = 1L;

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(taskId);
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.empty());

        JobExecutionContext context = new MockitoJobContextBuilder()
                .withResult(scheduleTaskEntity)
                .build();

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_dataArchiveTaskFound() {
        // given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        String taskId = "testTaskId";
        Long dataArchiveTaskId = 1L;

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(taskId);
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(dataArchiveTaskId.toString());
        dataArchiveTask.setResultJson(objectMapper.writeValueAsString(dlmTasks));

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(dataArchiveTask));

        JobExecutionContext context = new MockitoJobContextBuilder()
                .withResult(scheduleTaskEntity)
                .build();

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.DONE);
        verify(dataArchiveRollbackJob, times(1)).executeTask(taskId, dlmTasks);
    }
}