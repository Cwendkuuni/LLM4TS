package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
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
    private ScheduleTaskEntity dataArchiveTask;
    private JobExecutionContext context;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        context = new JobExecutionContextImpl();
        dataArchiveTask = new ScheduleTaskEntity();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        assertEquals(TaskStatus.FAILED, dataArchiveTask.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.SCHEDULED);

        dataArchiveDeleteJob.execute(context);

        assertEquals(TaskStatus.FAILED, dataArchiveTask.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        dataArchiveDeleteJob.execute(context);

        for (DlmTask dlmTask : dlmTasks) {
            assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
        }

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        String json = JsonUtils.toJson(dlmTasks);
        dataArchiveTask.setResultJson(json);

        assertEquals(TaskStatus.SUCCESS, dataArchiveTask.getStatus());
    }

    @Test
    public void testExecute_NullDataArchiveTask() {
        dataArchiveDeleteJob.execute(context);

        assertNull(dataArchiveTask);
    }
}