package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.remote.JsonUtils;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleTaskEntity taskEntity;
    
    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob(scheduleTaskRepository);
        
        when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
        when(jobDataMap.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobDataMap.get(Mockito.anyString())).thenReturn("test");
    }

    @Test
    public void testExecute() {
        dataArchiveRollbackJob.execute(context);
        verify(taskEntity, times(1)).getParametersJson();
        verify(scheduleTaskRepository, times(1)).findById(anyLong());
    }

    // Create other test scenarios here
}