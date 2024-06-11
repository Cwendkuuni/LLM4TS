
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {
    
    @Mock
    JobExecutionContext context;

    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Test
    public void testExecuteWhenDataArchiveTaskNotPresent() {
        when(context.getResult()).thenReturn(new ScheduleTaskEntity());
        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(),eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWhenDataArchiveTaskIsPresent() {
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);

        when(taskEntity.getParametersJson()).thenReturn("");
        when(taskEntity.getResultJson()).thenReturn("[]");
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setStatus(TaskStatus.DONE);
        taskUnits.add(taskUnit);

        when(JsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(2)).updateStatusById(any(), eq(TaskStatus.PREPARING));

    }

    @Test(expected = NullPointerException.class)
    public void testExecuteWhenAnExceptionOccurs() {
        when(context.getResult()).thenThrow(new NullPointerException());
        dataArchiveRollbackJob.execute(context);
    }

}
