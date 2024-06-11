import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }
    
    @Test
    public void testExecuteWhenDataArchiveTaskFoundAndDone() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        String parametersJson = "{\"dataArchiveTaskId\": 1}";
        taskEntity.setParametersJson(parametersJson);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        
        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());
        String resultJson = JsonUtils.toJson(taskUnits);
        dataArchiveTask.setResultJson(resultJson);
        
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(context);
        
        assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
    }
    
    @Test
    public void testExecuteWhenDataArchiveTaskNotFound() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        String parametersJson = "{\"dataArchiveTaskId\": 1}";
        taskEntity.setParametersJson(parametersJson);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        
        dataArchiveDeleteJob.execute(context);
        
        assertNull(taskEntity.getStatus());
    }
    
    @Test
    public void testExecuteWhenDataArchiveTaskNotDone() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        String parametersJson = "{\"dataArchiveTaskId\": 1}";
        taskEntity.setParametersJson(parametersJson);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(context);
        
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
}