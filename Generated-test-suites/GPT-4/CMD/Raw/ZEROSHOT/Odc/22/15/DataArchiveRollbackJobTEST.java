
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    DataArchiveRollbackParameters rollbackParameters;

    @Mock
    JobExecutionContext context;

    @Mock
    DlmTask dlmTask;

    @Test
    public void executeTest() {

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("");
        
        Optional taskOptional = Optional.ofNullable(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(taskOptional);

        List<DlmTask> taskList = new ArrayList<>();
        taskList.add(dlmTask);
        when(JsonUtils.fromJson(anyString(), any())).thenReturn(taskList);

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
        verify(context, times(1)).getResult();
    }
}
