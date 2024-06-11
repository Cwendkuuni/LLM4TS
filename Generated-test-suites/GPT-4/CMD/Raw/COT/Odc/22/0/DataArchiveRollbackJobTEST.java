import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;
import org.quartz.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    DataArchiveRollbackParameters rollbackParameters;

    @Mock
    List<DlmTask> taskUnits;

    @InjectMocks
    DataArchiveRollbackJob testJob = new DataArchiveRollbackJob();
    
    @Test
    public void testExecute(){
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        testJob.execute(context);
        verify(taskEntity, times(1)).getId();
    }

    @Test(expected=NullPointerException.class)
    public void testExecuteWithNullContext(){
        JobExecutionContext nullContext = null;
        testJob.execute(nullContext);
    }

    @Test
    public void testExecuteWithNullTaskEntity(){
        when(context.getResult()).thenReturn(null);
        testJob.execute(context);
    }

    @Test
    public void testExecuteWithEmptyDataArchiveTaskOption(){
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        testJob.execute(context);
    }

    @Test
    public void testExecuteWithJsonUtilsReturnsNull(){
        when(context.getResult()).thenReturn(taskEntity);
        PowerMockito.mockStatic(JsonUtils.class);
        when(JsonUtils.fromJson(any(), any())).thenReturn(null);
        testJob.execute(context);
    }

    @Test(expected=NullPointerException.class)
    public void testExecuteWithNullTaskUnits(){
        when(context.getResult()).thenReturn(taskEntity);
        PowerMockito.mockStatic(JsonUtils.class);
        when(JsonUtils.fromJson(any(), any())).thenReturn(null);
        testJob.execute(context);
    }

    @Test(expected=Exception.class)
    public void testExecuteFunctionThrowsException(){
        when(context.getResult()).thenReturn(taskEntity);
        doThrow(new RuntimeException()).when(testJob).executeTask(any(), any());
        testJob.execute(context);
    }
}