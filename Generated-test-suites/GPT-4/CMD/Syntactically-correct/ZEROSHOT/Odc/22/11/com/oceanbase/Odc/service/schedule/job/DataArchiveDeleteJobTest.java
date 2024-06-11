package com.oceanbase.odc.service.schedule.job;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext jobExecutionContext;
    
    @Mock
    private ScheduleTaskEntity taskEntity;
    
    @Mock
    private DataArchiveClearParameters params;

    @Before
    public void setUp() {
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
    }

    @Test
    public void executeDataArchiveTaskNotFound() {
        // Setup
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        // Execute
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        // Verify
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void executeDataArchiveTaskNotDone() {
        // Setup
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PENDING);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

        // Execute
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Verify
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void executeDataArchiveTaskDone() {
        // Setup
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        DlmTask mockTask = new DlmTask();
        mockTask.setStatus(TaskStatus.PREPARING);
        mockTask.setId("dummyId");
        mockTask.setJobType(JobType.DELETE);
        when(JsonUtils.fromJson(anyString(), any())).thenReturn(Arrays.asList(mockTask));

        // Execute
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Verify
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), any(TaskStatus.class));
    }
}
