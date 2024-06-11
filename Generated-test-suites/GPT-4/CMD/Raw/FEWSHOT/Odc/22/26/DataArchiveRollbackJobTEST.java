package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Test
    public void testExecuteTask() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        DlmTask taskUnit = new DlmTask();   

        // Prepare data
        rollbackParameters.setDataArchiveTaskId(123);
        taskEntity.setId(1);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        taskEntity.setResultJson(JsonUtils.toJson(Arrays.asList(taskUnit)));
        
        // Mock repository responses
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.of(taskEntity));

        dataArchiveRollbackJob.execute(context);

        // Check that status has been updated
        verify(scheduleTaskRepository).updateStatusById(1, taskStatus);
    }

    @Test
    public void testExecuteTask_NoDataArchiveTask() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();

        // Prepare data
        rollbackParameters.setDataArchiveTaskId(123);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        
        // Mock repository responses
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        // Check that status has been updated to FAILED
        verify(scheduleTaskRepository).updateStatusById(1, TaskStatus.FAILED);
    }
}