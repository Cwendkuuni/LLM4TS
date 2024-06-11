package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = mock(JobExecutionContext.class);
        scheduleTaskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void testExecute() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setSourceDatabaseId(1L);
        taskUnit1.setTargetDatabaseId(2L);
        taskUnit1.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit1);

        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {}))).thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);

        assertEquals(Thread.currentThread(), dataArchiveRollbackJob.jobThread);
        assertNotNull(scheduleTaskEntity.getResultJson());
        verify(scheduleTaskRepository, times(2)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}