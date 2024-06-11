package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.fasterxml.jackson.core.type.TypeReference;

public class DataArchiveRollbackJobTest {
    
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    
    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = mock(JobExecutionContext.class);
    }
    
    @Test
    public void testExecute() {
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(context.getResult()).thenReturn(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
        
        dataArchiveRollbackJob.execute(context);
        
        // Add assertions as required
        
    }
    
    // Add more test cases for other methods in DataArchiveRollbackJob class
    
}