package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobDataMap;
import org.quartz.impl.JobExecutionContextImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    DataArchiveRollbackJob dataArchiveRollbackJob;
    
    @Mock
    JobExecutionContextImpl jobExecutionContext;
    
    @Mock
    ScheduleTaskEntity taskEntity;
    
    @Mock
    ScheduleTaskEntity dataArchiveTask;
    
    @Mock
    DlmTask taskUnit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(4L);
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(taskEntity.getId()).thenReturn(4L);
        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(Arrays.asList(taskUnit)));
        when(dataArchiveTaskOption.get()).thenReturn(dataArchiveTask);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);
        when(taskUnit.getTargetDatabaseId()).thenReturn(2L);
        when(taskUnit.getSourceDatabaseId()).thenReturn(3L);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(taskEntity, times(1)).getId();
        verify(taskUnit, times(1)).setSourceDatabaseId(taskUnit.getTargetDatabaseId());
        verify(taskUnit, times(1)).setTargetDatabaseId(taskUnit.getSourceDatabaseId());
        verify(taskUnit, times(1)).setJobType(JobType.ROLLBACK);
        verify(taskUnit, times(1)).setStatus(taskUnit.getStatus() == TaskStatus.PREPARING ? TaskStatus.DONE : TaskStatus.PREPARING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), taskStatus);
    }
}
