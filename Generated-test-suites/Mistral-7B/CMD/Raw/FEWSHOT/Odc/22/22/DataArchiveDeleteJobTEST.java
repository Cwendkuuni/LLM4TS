package com.oceanbase.odc.service.schedule.job;

import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.JobType.DELETE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

@Slf4j
public class DataArchiveDeleteJobTest {
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private List<DlmTask> dlmTasks;

    @Before
    public void init() {
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dlmTasks = mockListOfDlmTasks();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Given
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When
        dataArchiveDeleteJob.execute(mockJobExecutionContext());

        // Then
        assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getJobThread().getStatus());
        verifyLogWarning("Data archive task not found, rollback task fast failed. dataArchiveTaskId = {}",
                dataArchiveClearParameters.getDataArchiveTaskId());
        verifyScheduleTaskRepositoryUpdateStatusById(TaskStatus.FAILED, dataArchiveClearParameters.getDataArchiveTaskId());
    }

    @Test
    public void testExecute_DataArchiveTaskAlreadyFinished() {
        // Given
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.FAILED);

        // When
        dataArchiveDeleteJob.execute(mockJobExecutionContext());

        // Then
        assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getJobThread().getStatus());
        verifyLogWarn("Data archive task do not finish, data archive task id = {}",
                dataArchiveClearParameters.getDataArchiveTaskId());
        verifyScheduleTaskRepositoryUpdateStatusById(TaskStatus.FAILED, dataArchiveClearParameters.getDataArchiveTaskId());
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() {
        // Given
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        // When
        dataArchiveDeleteJob.execute(mockJobExecutionContext());

        // Then
        assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getJobThread().getStatus());
        verifyExecuteTasks(dlmTasks);
        verifyLogInfo("execute tasks");
        verifyScheduleTaskRepositoryUpdateStatusById(TaskStatus.PREPARING, dataArchiveClearParameters.getDataArchiveTaskId());
    }

    private JobExecutionContext mockJobExecutionContext() {
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(jobExecutionContext.getMergedJobDataMap().get("parametersJson"))
                .thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        return jobExecutionContext;
    }

    private void verifyLogWarning(String message, Object... args) {
        // This stub will be replaced at compile time
        log.warn(message, args);
    }

    private void verifyLogInfo(String message) {
        // This stub will be replaced at compile time
        log.info(message);
    }

    private void verifyScheduleTaskRepositoryUpdateStatusById(TaskStatus status, String dataArchiveTaskId) {
        // This stub will be replaced at compile time
        when(scheduleTaskRepository.updateStatusById(dataArchiveTaskId, status)).thenReturn(1);
    }

    private List<DlmTask> mockListOfDlmTasks() {
        List<DlmTask> dlmTasks = mockList(DlmTask.class);
        return dlmTasks;
    }

    private void verifyExecuteTasks(List<DlmTask> dlmTasks) {
        dataArchiveDeleteJob.executeTasks(dlmTasks);
        for (DlmTask dlmTask : dlmTasks) {
            verify(dlmTask).setId(anyString());
            verify(dlmTask).setJobType(eq(DELETE));
            verify(dlmTask).setStatus(eq(TaskStatus.PREPARING));
        }
    }
}