import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoAnnotations;
import org.quartz.impl.JobDetail;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Test class for DataArchiveRollbackJob
 */
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        // Given
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Given
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TaskUnitsRollbackSuccess() {
        // Given
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setStatus(TaskStatus.DONE);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setStatus(TaskStatus.FAILED);
        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        ObjectMapper objectMapper = new ObjectMapper();
        String resultJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskUnits);
        taskEntity.setResultJson(resultJson);

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_TaskUnitsRollbackFailed() {
        // Given
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setStatus(TaskStatus.FAILED);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        ObjectMapper objectMapper = new ObjectMapper();
        String resultJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskUnits);
        taskEntity.setResultJson(resultJson);

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
}