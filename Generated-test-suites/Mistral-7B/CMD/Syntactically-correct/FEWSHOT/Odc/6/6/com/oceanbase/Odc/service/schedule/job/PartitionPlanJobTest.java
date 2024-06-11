package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PartitionPlanJobTest {

    @Test
    public void testExecute() {
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
         ScheduleTaskUtils scheduleTaskUtils = mock(ScheduleTaskUtils.class);

        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        when(scheduleTaskUtils.getScheduleId(any(JobExecutionContext.class))).thenReturn(1L);

        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);
        partitionPlanJob.setSpringContextUtil(springContextUtil);
        partitionPlanJob.setScheduleTaskUtils(scheduleTaskUtils);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1l);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(null);

        partitionPlanTaskService.executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testExecute_databasePartitionPlanIsDisable() {
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
         ScheduleTaskUtils scheduleTaskUtils = mock(ScheduleTaskUtils.class);

        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        when(springContextUtil.getBean(ScheduleTaskUtils.class)).thenReturn(scheduleTaskUtils);

        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);
        partitionPlanJob.setSpringContextUtil(springContextUtil);
        partitionPlanJob.setScheduleTaskUtils(scheduleTaskUtils);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1l);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(null);

        assertNull(partitionPlanTaskService);
    }

    @Test
    public void testExecute_noTablePartitionPlan() {
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
         ScheduleTaskUtils scheduleTaskUtils = mock(ScheduleTaskUtils.class);

        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        when(springContextUtil.getBean(ScheduleTaskUtils.class)).thenReturn(scheduleTaskUtils);

        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);
        partitionPlanJob.setSpringContextUtil(springContextUtil);
        partitionPlanJob.setScheduleTaskUtils(scheduleTaskUtils);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1l);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        assertThrows(UnExpectedException.class, () -> partitionPlanJob.execute(null));
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }
}