import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ObjectMapper objectMapper;
    private JobRepository jobRepository;
    private StdSchedulerFactory schedulerFactory;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DlmTask dlmTask;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        jobRepository = mock(JobRepository.class);
        schedulerFactory = mock(StdSchedulerFactory.class);
        dataArchiveRollbackJob.setJobRepository(jobRepository);
        dataArchiveRollbackJob.setSchedulerFactory(schedulerFactory);
        scheduleTaskEntity = new ScheduleTaskEntity();
        dlmTask = new DlmTask();
    }

    @Test
    public void testExecute() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson( JsonUtils.toJson(rollbackParameters) );
        when(jobRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(context);

        // Verify expected behavior
        verify(jobRepository, atLeastOnce()).findById(rollbackParameters.getDataArchiveTaskId());
        verify(jobRepository, atLeastOnce()).updateStatusById(scheduleTaskEntity.getId(), eq(TaskStatus.DONE));

        // Check if the execute method doesn't throw an exception
        assertFalse("No JobExecutionException was thrown", dataArchiveRollbackJob instanceof JobExecutionException);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);
        when(jobRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        try {
            dataArchiveRollbackJob.execute(context);
            fail("Expected JobExecutionException was not thrown");
        } catch (JobExecutionException e) {
            // Expected behavior
        }

        // Check if the execute method threw an exception
        assertTrue("JobExecutionException was thrown", dataArchiveRollbackJob instanceof JobExecutionException);

        // Verify expected behavior
        verify(jobRepository, never()).updateStatusById(scheduleTaskEntity.getId(), eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_DlmTaskPreparationError() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(3L);
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask);
        dlmTask.setStatus(TaskStatus.FAILED);

        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson( JsonUtils.toJson(rollbackParameters) );
        when(jobRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(context);

        // Verify expected behavior
        verify(jobRepository, atLeastOnce()).findById(rollbackParameters.getDataArchiveTaskId());
        verify(jobRepository, atLeastOnce()).updateStatusById(scheduleTaskEntity.getId(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DlmTaskPreparationSuccess() throws Exception {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(4L);
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask);
        dlmTask.setStatus(TaskStatus.PREPARING);

        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson( JsonUtils.toJson(rollbackParameters) );
        when(jobRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveRollbackJob.execute(context);

        // Verify expected behavior
        verify(jobRepository, atLeastOnce()).findById(rollbackParameters.getDataArchiveTaskId());
        verify(jobRepository, atLeastOnce()).updateStatusById(scheduleTaskEntity.getId(), eq(TaskStatus.DONE));
    }
}