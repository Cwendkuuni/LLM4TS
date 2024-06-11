
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob job;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveRollbackParameters rollbackParameters;
    private DlmTask taskUnit;
    private ScheduleTaskEntity dataArchiveTask;
    
    @Before
    public void setUp() {
        job = new DataArchiveRollbackJob();
        context = mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveTask = mock(ScheduleTaskEntity.class);
        taskUnit = mock(DlmTask.class);
        rollbackParameters = mock(DataArchiveRollbackParameters.class);
        
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(taskEntity.getId()).thenReturn(2L);
        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(Arrays.asList(taskUnit)));
        when(taskUnit.getSourceDatabaseId()).thenReturn(3L);
        when(taskUnit.getTargetDatabaseId()).thenReturn(4L);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);
        when(taskUnit.getJobType()).thenReturn(JobType.ROLLBACK);
    }

    @Test
    public void testExecute() {
        Long sourceDbId = taskUnit.getSourceDatabaseId();
        Long targetDbId = taskUnit.getTargetDatabaseId();
        Mockito.doNothing().when(scheduleTaskRepository).updateStatusById(Mockito.anyLong(), Mockito.any());
        job.execute(context);
        assertEquals(targetDbId, taskUnit.getSourceDatabaseId());
        assertEquals(sourceDbId, taskUnit.getTargetDatabaseId());
        assertEquals(JobType.ROLLBACK, taskUnit.getJobType());
        assertEquals(TaskStatus.DONE, taskUnit.getStatus());
    }
}
