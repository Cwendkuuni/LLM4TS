package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

public class DataArchiveDeleteJobTest {

    @InjectMocks
    DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    JobExecutionContext jobExecutionContext;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    ScheduleTaskEntity dataArchiveTask;

    @Mock
    DataArchiveClearParameters dataArchiveClearParameters;

    @Mock
    DlmTask dlmTask;

    @Test
    public void testExecute() {
        MockitoAnnotations.openMocks(this);
        
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{...}");
        when(JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class)).thenReturn(dataArchiveClearParameters);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("1");
        when(scheduleTaskRepository.findById("1")).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn("[...]");
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(Arrays.asList(dlmTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertNotNull(dataArchiveDeleteJob.jobThread);
        assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
        assertEquals(JobType.DELETE, dlmTask.getJobType());
    }
}