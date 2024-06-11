import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveRollbackParameters rollbackParameters;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = new ScheduleTaskEntity();
        rollbackParameters = new DataArchiveRollbackParameters();
        dataArchiveTask = new ScheduleTaskEntity();
        taskUnits = new ArrayList<>();
    }

    @Test
    public void testExecute() {
        // setup data
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(dataArchiveTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        // execute method
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // assert statements
        assertNotNull(dataArchiveRollbackJob.getJobThread());
        verify(dataArchiveTaskRepository, times(1)).findById(anyLong());
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}