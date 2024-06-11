package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {
    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        // Mocking data
        when(context.getResult()).thenReturn(taskEntity);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(taskEntity.getId()).thenReturn("task123");
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(new DataArchiveClearParameters()));

        DlmTask dlmTask = new DlmTask();
        dlmTask.setJobType(JobType.DELETE);
        dlmTask.setStatus(TaskStatus.PREPARING);
        String taskUnitsJson = JsonUtils.toJson(Arrays.asList(dlmTask));

        dataArchiveTask.setResultJson(taskUnitsJson);
        
        // Call the method to test
        dataArchiveDeleteJob.execute(context);
        
        // Verify interactions
        verify(taskEntity, times(1)).getId();
        verify(taskEntity, times(1)).getParametersJson();
    }

    @Test
    public void testExecuteNoTaskEntity() {
        // Mocking data
        when(context.getResult()).thenReturn(taskEntity);
        
        when(taskEntity.getId()).thenReturn(null);

        // Call the method to test
        dataArchiveDeleteJob.execute(context);

        // Verify that no other interactions happened
        verifyNoMoreInteractions(taskEntity);
    }
}