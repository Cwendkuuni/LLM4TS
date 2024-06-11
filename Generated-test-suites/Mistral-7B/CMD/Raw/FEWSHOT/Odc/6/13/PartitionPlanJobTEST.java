package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

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
import com.oceanbase.tools.migrator.common.util.JsonUtilsTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    private ScheduleEntity scheduleEntity;

    private DatabasePartitionPlanEntity databasePartitionPlan;

    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void init() {
        partitionPlanJob = new PartitionPlanJob();

        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);

        SpringContextUtil mockSpringContextUtil = mock(SpringContextUtil.class);
        when(mockSpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(mockSpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(mockSpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        SpringContextUtil.setContext(mockSpringContextUtil);

        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleService = scheduleService;

        scheduleEntity = new ScheduleEntity();
        databasePartitionPlan = new DatabasePartitionPlanEntity();
        tablePartitionPlans = JsonUtilsTest.createValidJsonToList(
                "[{\"tableName\":\"testTable1\", \"databaseName\":\"testDatabase1\"}]", TablePartitionPlanEntity.class);
    }

    @Test
    public void testExecute() {
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\":1,\"isConfigEnabled\":true}");
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(null);

        assertNotNull(partitionPlanJob.getPartitionPlanService());
        assertNotNull(partitionPlanJob.getPartitionPlanTaskService());
        assertNotNull(partitionPlanJob.getScheduleService());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}