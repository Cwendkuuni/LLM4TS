
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity entity;
    private ScheduleTaskEntity entityOptional;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = mock(JobExecutionContextImpl.class);
        entity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(entity);
        entityOptional = mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testExecute() throws Exception {
        when(entity.getParametersJson()).thenReturn("{}");

        DataArchiveRollbackParameters parameters = new DataArchiveRollbackParameters();
        parameters.setDataArchiveTaskId(1L);
        when(entity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters)); 

        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(entityOptional));

        when(entityOptional.getResultJson()).thenReturn("[{}]");

        dataArchiveRollbackJob.execute(context);

        verify(dataArchiveRollbackJob.scheduleTaskRepository, times(1)).updateStatusById(anyInt(),any());
    }

    @Test
    public void testExecute_NoScheduleTaskEntity(){
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(dataArchiveRollbackJob.scheduleTaskRepository, times(0)).updateStatusById(anyInt(),any());
    }

    @Test
    public void testExecute_NullScheduleTaskEntity(){
        when(entity.getParametersJson()).thenReturn(null);

        dataArchiveRollbackJob.execute(context);

        verify(dataArchiveRollbackJob.scheduleTaskRepository, times(0)).updateStatusById(anyInt(),any());
    }
}
