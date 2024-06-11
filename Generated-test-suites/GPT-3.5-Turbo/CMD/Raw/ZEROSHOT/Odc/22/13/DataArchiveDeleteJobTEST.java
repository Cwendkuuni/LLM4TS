package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveClearParameters()));
    }

    @Test
    public void testExecuteWithDataArchiveTaskFoundAndDone() {
        // Arrange
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(dataArchiveDeleteJob.getScheduleTaskRepository()
                .findById(anyLong()))
                .thenReturn(Optional.of(new ScheduleTaskEntity()));
        
        // Act
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert
        verify(dataArchiveDeleteJob.getScheduleTaskRepository(), times(1)).updateStatusById(anyLong(), eq(TaskStatus.PREPARING));
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        // Arrange
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(dataArchiveDeleteJob.getScheduleTaskRepository()
                .findById(anyLong()))
                .thenReturn(Optional.empty());
        
        // Act
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert
        verify(dataArchiveDeleteJob.getScheduleTaskRepository(), times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotDone() {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setStatus(TaskStatus.PROCESSING);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(dataArchiveDeleteJob.getScheduleTaskRepository()
                .findById(anyLong()))
                .thenReturn(Optional.of(taskEntity));
        
        // Act
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert
        verify(dataArchiveDeleteJob.getScheduleTaskRepository(), times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    // Add more test cases based on the functionality of DataArchiveDeleteJob class

}