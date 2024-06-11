package com.oceanbase.odc.service.schedule.job;

import static com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob.JobType.ROLLBACK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private Log log;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        log = mock(Log.class);
        dataArchiveRollbackJob.setLog(log);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
        // given
        String jobName = "testJob";
        String jobGroup = "testGroup";
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveRollbackParameters(1L, Arrays.asList(1L))));

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask(1L, 1L, ROLLBACK))));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        ArgumentCaptor<List<DlmTask>> dlmTaskArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
        verify(dataArchiveRollbackJob).executeTask(1L, dlmTaskArgumentCaptor.capture());
        List<DlmTask> dlmTasks = dlmTaskArgumentCaptor.getValue();
        assertEquals(1, dlmTasks.size());
        DlmTask dlmTask = dlmTasks.get(0);
        assertEquals(1L, dlmTask.getId());
        assertEquals(1L, dlmTask.getSourceDatabaseId());
        assertEquals(1L, dlmTask.getTargetDatabaseId());
        assertEquals(ROLLBACK, dlmTask.getJobType());
        assertEquals(TaskStatus.DONE, dlmTask.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        // given
        String jobName = "testJob";
        String jobGroup = "testGroup";
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName(jobName);
        scheduleTaskEntity.setJobGroup(jobGroup);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveRollbackParameters(1L, null)));

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        // when
        dataArchiveRollbackJob.execute(context);

        // then
        verify(log, times(1)).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId={}",
                1L);
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }
}