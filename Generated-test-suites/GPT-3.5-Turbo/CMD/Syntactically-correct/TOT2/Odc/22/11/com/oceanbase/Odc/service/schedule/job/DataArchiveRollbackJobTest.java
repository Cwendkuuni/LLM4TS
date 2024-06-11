import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.quartz.JobExecutionContext;

import lombok.extern.slf4j.Slf4j;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testExecute() {
        // Typical Use Case
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(parametersJson);
        
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        // Edge Case: dataArchiveTaskOption is not present
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        dataArchiveRollbackJob.execute(jobExecutionContext);
        verify(dataArchiveRollbackJob.scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));

        // Edge Case: taskUnits is empty
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        List<DlmTask> taskUnits = new ArrayList<>();
        String resultJson = JsonUtils.toJson(taskUnits);
        when(dataArchiveTask.getResultJson()).thenReturn(resultJson);
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveRollbackJob.execute(jobExecutionContext);
        verify(dataArchiveRollbackJob.scheduleTaskRepository, times(2)).updateStatusById(anyLong(), any());

        // Error Scenario: handle exception when parsing JSON
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn("[{\"id\":1234}]"); // Invalid JSON
        dataArchiveRollbackJob.execute(jobExecutionContext);
        verify(dataArchiveRollbackJob.scheduleTaskRepository, times(3)).updateStatusById(anyLong(), any());
    }
}