/*
 * Test file for DataArchiveRollbackJob.java
 */
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("jsonString");
        when(scheduleTaskEntity.getId()).thenReturn("id");
        when(scheduleTaskEntity.getResultJson()).thenReturn("jsonResultString");
        when(scheduleTaskEntity.getJobName()).thenReturn("jobName");
        when(scheduleTaskEntity.getJobGroup()).thenReturn("jobGroup");
    }

    @Test
    public void testExecute() {
        dataArchiveRollbackJob.execute(jobExecutionContext);
        verify(jobExecutionContext, atLeastOnce()).getResult();
        verify(scheduleTaskEntity, atLeastOnce()).getParametersJson();
    }

    @Test
    public void testPrepareTasksForRollback() {
        dataArchiveRollbackJob.execute(jobExecutionContext);
        verify(scheduleTaskEntity, atLeastOnce()).getResultJson();
    }

    @Test
    public void testTaskExecution() {
        dataArchiveRollbackJob.execute(jobExecutionContext);
        verify(scheduleTaskEntity, atLeastOnce()).getId();
    }

    @Test
    public void testTaskStatusUpdate() {
        dataArchiveRollbackJob.execute(jobExecutionContext);
        verify(scheduleTaskEntity, atLeastOnce()).getJobName();
        verify(scheduleTaskEntity, atLeastOnce()).getJobGroup();
    }
}