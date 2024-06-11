
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");
        
        when(context.getResult()).thenReturn(taskEntity);
        
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[]");
        
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(context);
        
        // Assert task status is updated
        verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.DONE));
    }
    
    // Add more test cases for edge cases and exceptions
    // e.g. test case for when dataArchiveTask is not present

}
