import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = mock(JobExecutionContext.class);
        taskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void testExecuteDataArchiveRollbackJob() {
        // Mocking data
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(12345L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(12345L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(new DlmTask()));
        
        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
            .thenReturn(Optional.of(dataArchiveTask));
            
        // Test execute method
        dataArchiveRollbackJob.execute(context);
        
        // Assert task status is updated correctly
        verify(dataArchiveRollbackJob.scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.DONE));
    }
}