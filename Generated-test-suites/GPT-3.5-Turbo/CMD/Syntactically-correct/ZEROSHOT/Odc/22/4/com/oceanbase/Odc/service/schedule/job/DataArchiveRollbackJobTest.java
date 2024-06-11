package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskEntity taskEntity;
    private DataArchiveRollbackParameters rollbackParameters;

    @Before
    public void setUp() {
        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        rollbackParameters = new DataArchiveRollbackParameters();
    }

    @Test
    public void testExecute() {
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(dataArchiveRollbackJob.getScheduleTaskRepository()).thenReturn(scheduleTaskRepository);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithDataArchiveTaskFound() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);

        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(dataArchiveRollbackJob.getScheduleTaskRepository()).thenReturn(scheduleTaskRepository);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecuteTask() {
        Long taskId = 1L;
        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());

        dataArchiveRollbackJob.executeTask(taskId, taskUnits);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.PREPARING, taskStatus);
    }
}