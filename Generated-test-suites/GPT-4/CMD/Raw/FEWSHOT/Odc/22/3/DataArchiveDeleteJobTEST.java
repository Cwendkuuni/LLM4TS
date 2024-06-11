package com.oceanbase.odc.service.schedule.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;

import org.mockito.Mockito;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        // Mocking the necessary objects for the test
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = Mockito.mock(DataArchiveClearParameters.class);
        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);

        when(context.getResult()).thenReturn(taskEntity);

        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("testTaskId");

        when(taskEntity.getId()).thenReturn("testTaskId");
       
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        
        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(new ArrayList<>()));

        DataArchiveDeleteJob job = new DataArchiveDeleteJob();  
        job.execute(context);

        // Checking that the method was invoked as expected
        verify(taskEntity, times(2)).getParametersJson();
    }
}