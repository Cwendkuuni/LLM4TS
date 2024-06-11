package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.quartz.JobExecutionContext;
import org.junit.Before;
import org.junit.Test;

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
    private DataArchiveRollbackParameters rollbackParameters;
    private List<DlmTask> taskUnits;
    
    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = mock(JobExecutionContext.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        rollbackParameters = new DataArchiveRollbackParameters();
        taskUnits = new ArrayList<>();
    }

    @Test
    public void testExecute() {
        // Mocking context
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        // Mocking scheduleTaskRepository
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        // Mocking JsonUtils
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class))).thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {}))).thenReturn(taskUnits);

        // Mocking executeTask and getTaskStatus

        // Call execute method
        dataArchiveRollbackJob.execute(context);

        // Verify scheduleTaskRepository methods
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.DONE));
    }
}