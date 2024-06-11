package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveDeleteJobTest {
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_ScheduleTaskNotFound() throws Exception {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);
        try {
            dataArchiveDeleteJob.execute(context);
            assertNotNull(context.getMergedJobDataMap().get("status"));
            assertEquals(TaskStatus.FAILED, context.getMergedJobDataMap().get("status"));
        } catch (JobExecutionException e) {
            log.warn("Test execute failed.", e);
        }
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() throws Exception {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        JobExecutionContext context = mock(JobExecutionContext.class);

        try {
            dataArchiveDeleteJob.execute(context);
            assertNotNull(context.getMergedJobDataMap().get("status"));
            assertEquals(TaskStatus.FAILED, context.getMergedJobDataMap().get("status"));
        } catch (JobExecutionException e) {
            log.warn("Test execute failed.", e);
        }
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() throws Exception {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setJobType(JobType.DELETE);
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        JobExecutionContext context = mock(JobExecutionContext.class);

        dataArchiveDeleteJob.execute(context);

        JsonNode actualResultJson = (JsonNode) context.getMergedJobDataMap().get("resultJson");
        JsonNode expectedResultJson = objectMapper.valueToTree(dlmTasks);
        assertEquals(expectedResultJson, actualResultResultJson);
        assertNotNull(context.getMergedJobDataMap().get("status"));
        assertEquals(TaskStatus.SUCCESS, context.getMergedJobDataMap().get("status"));
    }
}