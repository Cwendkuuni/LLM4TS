package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    
    @Test
    public void testExecuteDataArchiveRollBackJob() {
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{ \"dataArchiveTaskId\": 1 }");
        
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
        
        when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);
        
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask());
        
        when(JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {}))
            .thenReturn(taskUnits);
        
        job.execute(context);
        
        verify(taskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);
    }
}