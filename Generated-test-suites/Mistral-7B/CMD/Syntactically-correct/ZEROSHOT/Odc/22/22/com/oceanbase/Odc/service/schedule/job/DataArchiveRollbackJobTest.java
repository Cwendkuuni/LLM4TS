import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DataArchiveRollbackJobTest.
 *
 * @author tinker
 */
@Slf4j
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.dataArchiveRollbackJob = new DataArchiveRollbackJob();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        DlmTask dlmTask2 = new DlmTask();
        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);

        // when
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(ArgumentMatchers.eq(1L));
        when(dlmJobIdUtil.generateHistoryJobId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt())).thenReturn(2L);
        when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.DONE)))
                .thenReturn(1);

        dataArchiveRollbackJob.execute(context);

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L))).thenReturn(Optional.empty());

        // when & then
        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.anyLong(),
                ArgumentMatchers.eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_TaskRollbackSuccess() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        DlmTask dlmTask2 = new DlmTask();
        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);

        when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L))).thenReturn(Optional.of(scheduleTaskEntity));
        when(dlmJobIdUtil.generateHistoryJobId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt())).thenReturn(2L);
        when(executeTask(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(1);
        when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.DONE)))
                .thenReturn(1);

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_TaskRollbackFailed() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        DlmTask dlmTask2 = new DlmTask();
        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);

        when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L))).thenReturn(Optional.of(scheduleTaskEntity));
        when(dlmJobIdUtil.generateHistoryJobId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt())).thenReturn(2L);
        when(executeTask(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenThrow(new RuntimeException("task rollback failed"));
        when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED)))
                .thenReturn(1);

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.anyLong(),
                ArgumentMatchers.eq(TaskStatus.FAILED));
    }
}