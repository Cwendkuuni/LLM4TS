package com.oceanbase.odc.service.schedule.job;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecuteGivenValidScheduleEntity() {
        JobExecutionContext jobContext = Mockito.mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 1}");
        Mockito.when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(Mockito.mock(PartitionPlanTaskService.class));
        Mockito.when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(Mockito.mock(PartitionPlanService.class));
        Mockito.when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(Mockito.mock(ScheduleService.class));
        Mockito.when(ScheduleTaskUtils.getScheduleId(jobContext)).thenReturn(1L);
        
        try {
            partitionPlanJob.execute(jobContext);
        } catch (Exception e) {
            fail("Unexpected exception encountered: " + e.getMessage());
        }
    }

    @Test
    public void testExecuteGivenInvalidScheduleEntity() {
        JobExecutionContext jobContext = Mockito.mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(2L);
        Mockito.when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(Mockito.mock(PartitionPlanService.class));
        Mockito.when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(Mockito.mock(ScheduleService.class));
        Mockito.when(ScheduleTaskUtils.getScheduleId(jobContext)).thenReturn(2L);
        
        try {
            partitionPlanJob.execute(jobContext);
        } catch (Exception e) {
            fail("Unexpected exception encountered: " + e.getMessage());
        }
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
            fail("Expected exception not thrown");
        } catch (UnExpectedException e) {
            // Expected exception
        }
    }
}