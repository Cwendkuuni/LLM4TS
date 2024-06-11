package com.oceanbase.odc.service.schedule.job;

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
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        taskEntity = new ScheduleTaskEntity();
        dataArchiveClearParameters = new DataArchiveClearParameters();
    }

    @Test
    public void testExecuteWithTaskNotFound() {
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithTaskNotFinished() {
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        taskEntity.setStatus(TaskStatus.RUNNING);
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithTaskFinished() {
        List<DlmTask> taskUnits = new ArrayList<>();
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        taskEntity.setStatus(TaskStatus.DONE);
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
        assertNotNull(taskUnits.get(0).getId());
        assertEquals(JobType.DELETE, taskUnits.get(0).getJobType());
    }

    @Test
    public void testExecuteWithTaskFinishedUpdateStatus() {
        List<DlmTask> taskUnits = new ArrayList<>();
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        taskEntity.setStatus(TaskStatus.DONE);
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }
}