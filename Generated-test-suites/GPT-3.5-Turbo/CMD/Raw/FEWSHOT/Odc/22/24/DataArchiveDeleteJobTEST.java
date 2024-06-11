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
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {
    
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() {
        // Set up test data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1234L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        taskEntity.setId(5678L);
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1234L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setStatus(TaskStatus.PENDING);
        taskUnits.add(taskUnit);

        // Mock dependencies
        dataArchiveDeleteJob.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(1234L)).thenReturn(Optional.of(dataArchiveTask));
        
        // Test execution
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        // Verify results
        assertEquals(Thread.currentThread(), dataArchiveDeleteJob.jobThread);
        assertNotNull(dataArchiveDeleteJob.scheduleTaskRepository);
        assertEquals(TaskStatus.PREPARING, taskUnit.getStatus());
    }
}