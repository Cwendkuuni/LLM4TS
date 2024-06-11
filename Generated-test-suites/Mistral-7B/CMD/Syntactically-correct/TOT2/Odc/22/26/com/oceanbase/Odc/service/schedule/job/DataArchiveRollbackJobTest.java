import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobThreadHolder;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for DataArchiveRollbackJob
 */
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobThreadHolder.setInstance(null);
    }

    @Test
    public void testExecuteWithValidData() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
                .thenReturn(1);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.DONE))
                .thenReturn(1);

        // When
        dataArchiveRollbackJob.execute(createJobExecutionContext());

        // Then
        assertEquals(TaskStatus.DONE, getTaskStatus(taskEntity));
    }

    @Test(expected = JobExecutionException.class)
    public void testExecuteWithTaskNotFound() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When & Then
        dataArchiveRollbackJob.execute(createJobExecutionContext());
    }

    @Test
    public void testExecuteWithEmptyTaskList() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
                .thenReturn(1);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.DONE))
                .thenReturn(1);
        when(scheduleTaskRepository.findById(taskEntity.getId()))
                .thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.findByJobNameAndJobGroup(taskEntity.getJobName(), taskEntity.getJobGroup()))
                .thenReturn(Collections.emptyList());

        // When
        dataArchiveRollbackJob.execute(createJobExecutionContext());

        // Then
        assertEquals(TaskStatus.DONE, getTaskStatus(taskEntity));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteWithNullTaskList() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
                .thenReturn(1);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.DONE))
                .thenReturn(1);
        when(scheduleTaskRepository.findById(taskEntity.getId()))
                .thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.findByJobNameAndJobGroup(taskEntity.getJobName(), taskEntity.getJobGroup()))
                .thenThrow(new IllegalStateException());

        // When & Then
        dataArchiveRollbackJob.execute(createJobExecutionContext());
    }

    @Test
    public void testExecuteWithValidTaskList() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
                .thenReturn(1);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.DONE))
                .thenReturn(1);
        when(scheduleTaskRepository.findById(taskEntity.getId()))
                .thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.findByJobNameAndJobGroup(taskEntity.getJobName(), taskEntity.getJobGroup()))
                .thenReturn(Arrays.asList(new ScheduleTaskEntity()));

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findByJobNameAndJobGroupAndTaskStatus(taskEntity.getJobName(), taskEntity.getJobGroup(),
                TaskStatus.PREPARING))
                .thenReturn(dlmTasks);

        // When
        dataArchiveRollbackJob.execute(createJobExecutionContext());

        // Then
        assertEquals(TaskStatus.DONE, getTaskStatus(taskEntity));
    }

    private JobExecutionContext createJobExecutionContext() {
        return new JobExecutionContext() {
            private static final long serialVersionUID = 1L;

            @Override
            public ScheduleTaskEntity getResult() {
                return null;
            }
        };
    }

    private TaskStatus getTaskStatus(ScheduleTaskEntity taskEntity) {
        return taskEntity != null ? taskEntity.getStatus() : TaskStatus.UNKNOWN;
    }
}