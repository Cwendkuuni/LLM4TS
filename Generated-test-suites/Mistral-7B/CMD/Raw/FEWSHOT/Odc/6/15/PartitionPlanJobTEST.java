package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private PartitionPlanService partitionPlanServiceMock;
    private ScheduleService scheduleServiceMock;
    private ScheduleTaskUtils scheduleTaskUtilsMock;
    private SpringContextUtil springContextUtilMock;

    @Before
    public void setup() {
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        scheduleTaskUtilsMock = mock(ScheduleTaskUtils.class);
        springContextUtilMock = mock(SpringContextUtil.class);

        when(springContextUtilMock.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskServiceMock);
        when(springContextUtilMock.getBean(PartitionPlanService.class)).thenReturn(partitionPlanServiceMock);
        when(springContextUtilMock.getBean(ScheduleService.class)).thenReturn(scheduleServiceMock);
        when(scheduleTaskUtilsMock.getScheduleId(any(JobExecutionContext.class))).thenReturn(1L);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskServiceMock);
        partitionPlanJob.setPartitionPlanService(partitionPlanServiceMock);
        partitionPlanJob.setScheduleService(scheduleServiceMock);
        partitionPlanJob.setScheduleTaskUtils(scheduleTaskUtilsMock);
        partitionPlanJob.setSpringContextUtil(springContextUtilMock);
    }

    @After
    public void teardown() {
        partitionPlanJob = null;
    }

    @Test
    public void testExecute() throws SchedulerException, JobExecutionException, Exception {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new java.util.ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");

        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        partitionPlanJob.execute(null);

        assertNotNull(databasePartitionPlan);
        assertNotNull(tablePartitionPlans);
    }

    @Test
    public void testExecute_databasePartitionPlanDisabled() throws SchedulerException, JobExecutionException, Exception {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = new java.util.ArrayList<>();

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");

        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        partitionPlanJob.execute(null);

        assertNull(databasePartitionPlan);
        assertNull(tablePartitionPlans);
    }

    @Test
    public void testExecute_noTableToProcess() throws SchedulerException, JobExecutionException, Exception {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new java.util.ArrayList<>();

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");

        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        partitionPlanJob.execute(null);

        assertNull(databasePartitionPlan);
        assertNull(tablePartitionPlans);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}