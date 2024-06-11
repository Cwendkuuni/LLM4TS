import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 1 }");
        
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        
        dataArchiveRollbackJob.execute(context);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}