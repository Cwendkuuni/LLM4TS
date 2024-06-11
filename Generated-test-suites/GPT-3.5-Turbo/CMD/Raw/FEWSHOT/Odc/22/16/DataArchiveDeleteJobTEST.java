package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        
        taskEntity.setStatus(TaskStatus.DONE);
        when(context.getResult()).thenReturn(taskEntity);
        
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnits.add(taskUnit);
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));
        
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        
        job.setScheduleTaskRepository(scheduleTaskRepository);
        
        job.execute(context);
        
        assertNotNull(taskEntity);
        assertEquals(1L, taskEntity.getId().longValue());
        assertEquals("DELETE", taskUnit.getJobType().toString());
        assertEquals(TaskStatus.PREPARING, taskUnit.getStatus());
    }
}