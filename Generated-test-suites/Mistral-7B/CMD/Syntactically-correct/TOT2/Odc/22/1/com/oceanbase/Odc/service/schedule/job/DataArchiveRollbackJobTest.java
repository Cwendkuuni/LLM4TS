import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verify;
import static org.junit.Assert.assertEquals;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound_thenExecuteRollbackTasks() throws JobExecutionException {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> taskUnits = Mockito.mockList(DlmTask.class);
        given(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L))).willReturn(Optional.of(taskEntity));

        // when
        dataArchiveRollbackJob.execute(ArgumentMatchers.any(JobExecutionContext.class));

        // then
        verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.DONE));
        for (DlmTask dlmTask : taskUnits) {
            verify(dlmTask).setJobType(ArgumentMatchers.eq(JobType.ROLLBACK));
            verify(dlmTask).setStatus(ArgumentMatchers.eq(TaskStatus.DONE));
        }
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenUpdateStatusToFailed() throws JobExecutionException {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        // when
        dataArchiveRollbackJob.execute(ArgumentMatchers.any(JobExecutionContext.class));

        // then
        verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.FAILED));
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_whenTaskUnitsIsNull_thenThrowNullPointerException() throws JobExecutionException {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        given(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L))).willReturn(Optional.of(taskEntity));
        given(JsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>(){})).willReturn(null);

        // when
        dataArchiveRollbackJob.execute(ArgumentMatchers.any(JobExecutionContext.class));
    }

    @Test
    public void testGetTaskStatus_whenAllTasksDone_thenReturnTaskStatusDONE() {
        // given
        List<DlmTask> taskUnits = Mockito.mockList(DlmTask.class);
        for (DlmTask dlmTask : taskUnits) {
            dlmTask.setStatus(TaskStatus.DONE);
        }

        // when
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        // then
        assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenTasksPreparing_thenReturnTaskStatusPREPARING() {
        // given
        List<DlmTask> taskUnits = Mockito.mockList(DlmTask.class);
        for (DlmTask dlmTask : taskUnits) {
            dlmTask.setStatus(TaskStatus.PREPARING);
        }

        // when
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        // then
        assertEquals(TaskStatus.PREPARING, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenTasksFailed_thenReturnTaskStatusFAILED() {
        // given
        List<DlmTask> taskUnits = Mockito.mockList(DlmTask.class);
        for (DlmTask dlmTask : taskUnits) {
            dlmTask.setStatus(TaskStatus.FAILED);
        }

        // when
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        // then
        assertEquals(TaskStatus.FAILED, taskStatus);
    }
}