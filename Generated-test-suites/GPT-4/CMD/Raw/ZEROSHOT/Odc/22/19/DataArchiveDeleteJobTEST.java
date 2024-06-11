
package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    
    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        context = Mockito.mock(JobExecutionContext.class);
        taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        dataArchiveClearParameters = Mockito.mock(DataArchiveClearParameters.class);
    }

    @Test
    public void testExecuteWithUnfinishedTask() {
    	when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": \"fake id\"}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("fake id");
        when(scheduleTaskRepository.findById("fake id")).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        
        dataArchiveDeleteJob.execute(context);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
    
    @Test
    public void testExecuteWithValidTask() {
    	when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": \"fake id\"}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("fake id");
        when(scheduleTaskRepository.findById("fake id")).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        
        DlmTask dlmTask = new DlmTask();
        String taskResultJson = JsonUtils.toJson(Arrays.asList(dlmTask));

        when(taskEntity.getResultJson()).thenReturn(taskResultJson);
        when(DlmJobIdUtil.generateHistoryJobId(Mockito.anyString(), 
            Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt())).thenReturn("fake_job_id");
            
        dataArchiveDeleteJob.execute(context);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), taskStatus);
    }
}
