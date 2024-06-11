package com.oceanbase.odc.service.schedule.job;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.Arrays;
import java.util.Optional;

public class DataArchiveDeleteJobTest {

    @InjectMocks
    DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    JobExecutionContext jobExecutionContext;

    @Mock
    ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    ScheduleTaskEntity dataArchiveTask;

    @Mock
    DataArchiveClearParameters dataArchiveClearParameters;

    @Test
    public void testExecuteJobWithContext() throws JobExecutionException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("scheduleTaskEntity", scheduleTaskEntity);
        Mockito.when(jobExecutionContext.getMergedJobDataMap()).thenReturn(jobDataMap);
        Mockito.when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
        Mockito.when(scheduleTaskRepository.findById(1)).thenReturn(Optional.ofNullable(dataArchiveTask));
        Mockito.when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        Mockito.when(dataArchiveTask.getResultJson()).thenReturn("[]");
  
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Further testing can be done here based on actual behavior of 'execute' method 
        // and expected state of dataArchiveDeleteJob after 'execute' call.
        // For example, status of scheduleTaskEntity can be asserted based on logic in 'execute' method.
         
    }
    
    @Test
    public void testExecuteJobWithoutContext() throws JobExecutionException {

        Mockito.when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
        Mockito.when(scheduleTaskRepository.findById(1)).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        Mockito.verify(scheduleTaskEntity, Mockito.times(1)).getParametersJson();
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).findById(1);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyInt(), Mockito.eq(TaskStatus.FAILED));
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

}