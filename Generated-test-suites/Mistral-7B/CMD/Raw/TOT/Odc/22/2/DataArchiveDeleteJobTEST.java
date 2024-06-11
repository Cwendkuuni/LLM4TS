import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_StatusUpdatedToFailed() throws JobExecutionException {
        // Given
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity(1L, "testDataArchiveTaskId");
        DataArchiveClearParameters dataArchiveClearParameters = createDataArchiveClearParameters("testDataArchiveTaskId", 1L);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        // When
        dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        Assertions.assertEquals(TaskStatus.FAILED, Mockito.mock(ScheduleTaskEntity.class).getStatus());
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskAlreadyFinished_StatusUpdated() throws JobExecutionException {
        // Given
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity(1L, "testDataArchiveTaskId");
        DataArchiveClearParameters dataArchiveClearParameters = createDataArchiveClearParameters("testDataArchiveTaskId", 1L);
        ScheduleTaskEntity dataArchiveTask = createScheduleTaskEntity(2L, "testDataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        // When
        dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        Assertions.assertEquals(TaskStatus.PREPARING, Mockito.mock(DlmTask.class).getStatus());
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished_StatusUpdatedToFailed() throws JobExecutionException {
        // Given
        ScheduleTaskEntity taskEntity = createScheduleTaskEntity(1L, "testDataArchiveTaskId");
        DataArchiveClearParameters dataArchiveClearParameters = createDataArchiveClearParameters("testDataArchiveTaskId", 1L);
        ScheduleTaskEntity dataArchiveTask = createScheduleTaskEntity(2L, "testDataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        // When
        dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        Assertions.assertEquals(TaskStatus.FAILED, Mockito.mock(ScheduleTaskEntity.class).getStatus());
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    private ScheduleTaskEntity createScheduleTaskEntity(long id, String dataArchiveTaskId) {
        return new ScheduleTaskEntity() {{
            this.id = id;
            this.jobName = "testJobName";
            this.jobGroup = "testJobGroup";
            this.parametersJson = JsonUtils.toJson(new DataArchiveClearParameters()).orElse("");
            this.resultJson = "";
            this.status = TaskStatus.READY;
            this.dataArchiveTaskId = dataArchiveTaskId;
        }};
    }

    private DataArchiveClearParameters createDataArchiveClearParameters(String dataArchiveTaskId, long dataArchiveTaskId) {
        return new DataArchiveClearParameters() {{
            this.dataArchiveTaskId = dataArchiveTaskId;
        }};
    }
}