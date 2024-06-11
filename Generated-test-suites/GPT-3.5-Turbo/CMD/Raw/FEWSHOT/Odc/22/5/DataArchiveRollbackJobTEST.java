package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Test
    public void testExecute() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // Prepare mock data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(101L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        // Mock behavior
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(101L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Verify task status updated
        assertNotNull(taskEntity.getJobThread());
        assertNotNull(taskEntity.getId());
        assertNotNull(taskUnits.get(0).getId());
        assertNotNull(taskUnits.get(0).getJobType());
        assertNotNull(taskUnits.get(0).getStatus());
        assertNotNull(dataArchiveTask.getStatus());
    }
}