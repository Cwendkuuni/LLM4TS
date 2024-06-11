import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_SuccessfulRollback() {
        // Prepare
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask(1L, 2L, TaskStatus.PREPARING));
        taskUnits.add(new DlmTask(2L, 3L, TaskStatus.PREPARING));
        
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);

        ScheduleTaskEntity dataArchiveTask = dataArchiveTaskOption.get();
        DataArchiveRollbackParameters expectedRollbackParameters = new DataArchiveRollbackParameters();
        expectedRollbackParameters.setDataArchiveTaskId(1L);

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(eq(2L))).thenReturn(dataArchiveTaskOption);
        
        // Execute
        dataArchiveRollbackJob.execute(context);

        // Verify
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Prepare
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(eq(2L))).thenReturn(Optional.empty());
        
        // Execute
        dataArchiveRollbackJob.execute(context);

        // Verify
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    // Add more test cases for potential edge cases and exceptions here

}