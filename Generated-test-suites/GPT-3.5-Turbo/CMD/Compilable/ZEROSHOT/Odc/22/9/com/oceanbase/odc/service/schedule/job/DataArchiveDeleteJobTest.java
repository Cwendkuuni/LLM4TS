package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);

        // Mock taskEntity
        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");

        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2L);

        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        // Mock dataArchiveTask
        dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
    }

    @Test
    public void testExecuteDataArchiveDeleteJob_TaskNotFound() {
        DataArchiveDeleteJob spyDataArchiveDeleteJob = org.mockito.Mockito.spy(dataArchiveDeleteJob);
        
        when(spyDataArchiveDeleteJob.scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());
        
        spyDataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteDataArchiveDeleteJob_TaskNotDone() {
        DataArchiveDeleteJob spyDataArchiveDeleteJob = org.mockito.Mockito.spy(dataArchiveDeleteJob);
        
        when(spyDataArchiveDeleteJob.scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveTask.setStatus(TaskStatus.FAILED);
        
        spyDataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteDataArchiveDeleteJob_Success() {
        DataArchiveDeleteJob spyDataArchiveDeleteJob = org.mockito.Mockito.spy(dataArchiveDeleteJob);
        
        when(spyDataArchiveDeleteJob.scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));
        
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask());
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        
        spyDataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
    }
}