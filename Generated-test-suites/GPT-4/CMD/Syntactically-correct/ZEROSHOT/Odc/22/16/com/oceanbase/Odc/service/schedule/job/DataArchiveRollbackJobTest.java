
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    JobExecutionContext jobExecutionContext;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    ScheduleTaskEntity dataArchiveTask;

    @Mock
    DataArchiveRollbackParameters dataArchiveRollbackParameters;

    @Mock
    DlmTask dlmTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        // teardown operations if any
    }

    @Test
    public void testExecute() {
        // Start of mock setup
        String parametersJson = "json";
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);
        String resultJson = "json";
        when(dataArchiveTask.getResultJson()).thenReturn(resultJson);
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(dataArchiveRollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask);
        when(JsonUtils.fromJson(resultJson, new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);
        // End of mock setup

        // Call the method to be tested
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Verify that entities status updated
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }
}
