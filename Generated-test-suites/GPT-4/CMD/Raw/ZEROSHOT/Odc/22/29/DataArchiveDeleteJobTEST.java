
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.quartz.JobExecutionContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

import java.util.*;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertNotNull;

public class DataArchiveDeleteJobTest {
    @InjectMocks
    DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    JobExecutionContext context;
    @Mock
    ScheduleTaskEntity taskEntity;
    @Mock
    ScheduleTaskEntity dataArchiveTask;
   
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void teardown() {
        context = null;
        taskEntity = null;
        dataArchiveTask = null;
    }

    @Test
    public void testDataArchiveDeleteJob_Execute() {
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        taskUnits.add(dlmTask);
        when(dataArchiveTask.getResultJson()).thenReturn("[]");

        dataArchiveDeleteJob.execute(context);
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyString(), any(TaskStatus.class));
    } 

    @Test
    public void testDataArchiveDeleteJob_Execute_DataArchiveTaskNotFound() {
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    } 

    @Test
    public void testDataArchiveDeleteJob_Execute_DataArchiveTaskNotDone() {
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

        dataArchiveDeleteJob.execute(context);
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    } 
}
