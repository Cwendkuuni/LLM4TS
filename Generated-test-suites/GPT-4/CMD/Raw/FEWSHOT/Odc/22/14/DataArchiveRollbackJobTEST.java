
package com.oceanbase.odc.service.schedule.job;
//imports
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;
    
    @Mock
    private DlmTask taskUnit;

    private DataArchiveRollbackJob job;

    @Before
    public void setUp() {
        job = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        DataArchiveRollbackParameters rollbackParameters = mock(DataArchiveRollbackParameters.class);
        when(taskEntity.getParametersJson()).thenReturn("");
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn("1");
        
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        
        when(taskEntity.getResultJson()).thenReturn(Arrays.asList(taskUnit));
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);
        
        job.execute(context);
        
        assertNotNull(taskEntity.getJobName());
        assertNotNull(taskEntity.getJobGroup());
        assertEquals(TaskStatus.DONE, taskUnit.getStatus());
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_NoDataArchiveTask() {
        DataArchiveRollbackParameters rollbackParameters = mock(DataArchiveRollbackParameters.class);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn("1");

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        job.execute(context);
    }
}
