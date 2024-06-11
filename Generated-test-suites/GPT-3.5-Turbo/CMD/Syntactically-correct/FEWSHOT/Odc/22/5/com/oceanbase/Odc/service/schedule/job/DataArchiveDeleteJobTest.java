import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repo.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveDeleteService;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext mockedJobExecutionContext;
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        mockedJobExecutionContext = mock(JobExecutionContext.class);
        taskEntity = new ScheduleTaskEntity();
        // Initialize taskEntity properties
        taskEntity.setId(1L);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        // Initialize dataArchiveClearParameters
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecuteDataArchiveDeleteJob() {
        // Mocking data for testing
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        // Initialize taskUnit1 properties
        taskUnits.add(taskUnit1);
        // Mocking repository behavior
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenReturn(true);
        // Mocking method calls
        dataArchiveDeleteJob.execute(mockedJobExecutionContext);
        // Assertions
        assertNotNull(dataArchiveDeleteJob.getJobThread());
        assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getTaskStatus(taskUnits));
    }

    @Test
    public void testExecuteDataArchiveDeleteJobTaskNotFound() {
        // Mocking repository behavior
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
        // Mocking method calls
        dataArchiveDeleteJob.execute(mockedJobExecutionContext);
        // Assertions
        assertNull(dataArchiveDeleteJob.getJobThread());
    }
}