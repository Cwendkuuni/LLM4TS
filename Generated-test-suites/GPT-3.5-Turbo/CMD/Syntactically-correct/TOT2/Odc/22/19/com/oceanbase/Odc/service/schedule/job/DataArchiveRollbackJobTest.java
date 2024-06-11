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

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_WhenDataArchiveTaskNotFound_ThenFailedStatusUpdated() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 100}");
        
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_WhenDataArchiveTaskFound_ThenTasksPreparedForRollback() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 100}");
        
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(100L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(context);

        assertEquals(2L, taskUnits.get(0).getSourceDatabaseId());
        assertEquals(1L, taskUnits.get(0).getTargetDatabaseId());
        assertEquals(JobType.ROLLBACK, taskUnits.get(0).getJobType());
        assertEquals(TaskStatus.DONE, taskUnits.get(0).getStatus());
    }

    // Add more test cases for other methods if needed

}