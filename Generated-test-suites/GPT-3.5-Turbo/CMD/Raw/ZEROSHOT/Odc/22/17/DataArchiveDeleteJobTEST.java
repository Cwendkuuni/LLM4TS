package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    private DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

    @Test
    public void testExecute() {
        // Mock the context
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        // Mock the data archive clear parameters
        String parametersJson = "{}"; // Set your desired JSON string here
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);

        // Mock the data archive task entity
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        // Mock the task units
        List<DlmTask> taskUnits = new ArrayList<>();
        // Populate taskUnits with mock data
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        // Execute the method under test
        dataArchiveDeleteJob.execute(context);

        // Verify the interactions
        // Add your verification statements here
    }
}