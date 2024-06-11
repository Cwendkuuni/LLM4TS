package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private PartitionPlanService partitionPlanServiceMock;
    private ScheduleService scheduleServiceMock;

    private JobExecutionContext jobExecutionContextMock;
    
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        this.partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        this.partitionPlanServiceMock = mock(PartitionPlanService.class);
        this.scheduleServiceMock = mock(ScheduleService.class);
        
        this.jobExecutionContextMock = mock(JobExecutionContext.class);
        
        this.partitionPlanJob = new PartitionPlanJob();
        SpringContextUtil springContextUtilMock = mock(SpringContextUtil.class);
        when(springContextUtilMock.getBean(eq(PartitionPlanTaskService.class))).thenReturn(partitionPlanTaskServiceMock);
        when(springContextUtilMock.getBean(eq(PartitionPlanService.class))).thenReturn(partitionPlanServiceMock);
        when(springContextUtilMock.getBean(eq(ScheduleService.class))).thenReturn(scheduleServiceMock);
        
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() throws SchedulerException {
        // Mock data
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntityMock = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParametersMock = mock(PartitionPlanJobParameters.class);
        DatabasePartitionPlanEntity databasePartitionPlanMock = mock(DatabasePartitionPlanEntity.class);
        TablePartitionPlanEntity tablePartitionPlanMock = mock(TablePartitionPlanEntity.class);
        
        // Mock behavior
        when(ScheduleTaskUtils.getScheduleId(eq(jobExecutionContextMock))).thenReturn(scheduleId);
        when(scheduleServiceMock.nullSafeGetById(eq(scheduleId))).thenReturn(scheduleEntityMock);
        when(JsonUtils.fromJson(eq(scheduleEntityMock.getJobParametersJson()), eq(PartitionPlanJobParameters.class))).thenReturn(jobParametersMock);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(eq(jobParametersMock.getDatabasePartitionPlanId()))).thenReturn(databasePartitionPlanMock);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(eq(jobParametersMock.getDatabasePartitionPlanId()))).thenReturn(List.of(tablePartitionPlanMock));
        when(databasePartitionPlanMock.isConfigEnabled()).thenReturn(true);
        
        //Execute
        partitionPlanJob.execute(jobExecutionContextMock);
        
        //Verify
        verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(any(), anyList());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
            fail("Expected UnExpectedException");
        } catch (UnExpectedException e) {
            // Expected exception
            assertTrue(true);
        }
    }
}