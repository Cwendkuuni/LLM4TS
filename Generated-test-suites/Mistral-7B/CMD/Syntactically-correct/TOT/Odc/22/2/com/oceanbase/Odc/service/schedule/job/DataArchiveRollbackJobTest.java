import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobThread;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private JobExecutionContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        scheduleTaskEntity = new ScheduleTaskEntity();
        context = Mockito.mock(JobExecutionContext.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        JobThread jobThread = Mockito.mock(JobThread.class);
        dataArchiveRollbackJob.setJobThread(jobThread);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_ExecutionSuccess() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(1L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE))
                .thenReturn(true);
        when(executeTask(scheduleTaskEntity.getId(), dlmTasks)).thenReturn(true);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_ExecutionFailed() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(1L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE))
                .thenReturn(false);
        when(executeTask(scheduleTaskEntity.getId(), dlmTasks)).thenReturn(false);

        try {
            dataArchiveRollbackJob.execute(context);
            fail("Expected JobExecutionException not thrown");
        } catch (JobExecutionException e) {
            // Expected
        }

        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }
}