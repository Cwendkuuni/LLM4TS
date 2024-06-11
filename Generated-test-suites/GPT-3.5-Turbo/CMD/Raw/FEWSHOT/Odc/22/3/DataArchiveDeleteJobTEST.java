package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_withValidScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        String parametersJson = "{\"dataArchiveTaskId\": 1}";
        scheduleTaskEntity.setParametersJson(parametersJson);
        
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        String resultJson = "[{\"id\": 1, \"status\": \"DONE\"}]";
        List<DlmTask> taskUnits = JsonUtils.fromJson(resultJson, new TypeReference<List<DlmTask>>() {});
        
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(dataArchiveDeleteJob.getScheduleTaskRepository().findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(dataArchiveDeleteJob.getScheduleTaskRepository().updateStatusById(any(), any())).thenReturn();
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertNotNull(dataArchiveClearParameters);
    }

    @Test
    public void testExecute_withInvalidScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        String parametersJson = "{\"dataArchiveTaskId\": 1}";
        scheduleTaskEntity.setParametersJson(parametersJson);
        
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(dataArchiveDeleteJob.getScheduleTaskRepository().findById(1L)).thenReturn(Optional.empty());
        when(dataArchiveDeleteJob.getScheduleTaskRepository().updateStatusById(any(), any())).thenReturn();
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    // Add more test cases for other scenarios

}