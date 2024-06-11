import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Test class for DataArchiveDeleteJob.
 */
@Slf4j
public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private AbstractDlmJob abstractDlmJob;
    @Mock
    private JobExecutionContext context;
    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    @Mock
    private ObjectMapper objectMapper;

    private JsonNode dataArchiveClearParameters;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveClearParameters = JsonUtils.createJsonNode("{\"dataArchiveTaskId\": 1}");
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(dataArchiveClearParameters.toString());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        dataArchiveDeleteJob.execute(context);

        // then
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        // given
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        // when
        dataArchiveDeleteJob.execute(context);

        // then
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_Success() {
        // given
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        List<DlmTask> dlmTasks = Arrays.asList(mock(DlmTask.class));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(jsonUtils.fromJson(scheduleTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        // when
        dataArchiveDeleteJob.execute(context);

        // then
        assertEquals(TaskStatus.COMPLETED, scheduleTaskEntity.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_Exception() {
        // given
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        List<DlmTask> dlmTasks = Arrays.asList(mock(DlmTask.class));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(jsonUtils.fromJson(scheduleTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);
        when(abstractDlmJob.executeTask(1L, dlmTasks)).thenThrow(new RuntimeException());

        // when
        dataArchiveDeleteJob.execute(context);

        // then - exception should be thrown
    }
}