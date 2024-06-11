package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson(JsonUtils.toJson(new ArrayList<DlmTask>()));

        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(context);

        verify(dataArchiveRollbackJob.scheduleTaskRepository, times(3)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}