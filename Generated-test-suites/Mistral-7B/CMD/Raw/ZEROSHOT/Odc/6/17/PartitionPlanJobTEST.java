package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gson.Gson;
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
import com.oceanbase.odc.service.schedule.job.PartitionPlanJobParametersTest;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private static final String SCHEDULE_ID = "scheduleId";
    private static final String JOB_PARAMETERS_JSON = "{\"databasePartitionPlanId\":1}";
    private static final String DATABASE_PARTITION_PLAN_ID = "1";
    private static final String FLOW_INSTANCE_ID = "flowInstanceId";
    private static final String TABLE_PARTITION_PLAN_ID = "1";
    private static final String JOB_NAME = "PartitionPlanJob";

    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private PartitionPlanJobPartitionPlanServiceMock partitionPlanServiceMock;
    private PartitionPlanJobTaskServiceMock partitionPlanTaskServiceMock;
    private PartitionPlanJobScheduleServiceMock scheduleServiceMock;
    private PartitionPlanJob job;

    @Before
    public void setup() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        partitionPlanServiceMock = new PartitionPlanJobPartitionPlanServiceMock();
        partitionPlanTaskServiceMock = new PartitionPlanJobTaskServiceMock();
        scheduleServiceMock = new PartitionPlanJobScheduleServiceMock();

        when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(partitionPlanServiceMock.getDatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(partitionPlanServiceMock.getTablePartitionPlans());

        when(partitionPlanTaskService.executePartitionPlan(FLOW_INSTANCE_ID,
                partitionPlanServiceMock.getTablePartitionPlans()))
                .thenReturn(true);

        when(scheduleService.nullSafeGetById(Long.parseLong(SCHEDULE_ID)))
                .thenReturn(scheduleServiceMock.getScheduleEntity());
        when(scheduleService.terminate(scheduleServiceMock.getScheduleEntity()))
                .thenReturn(true);

        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
        SpringContextUtil.setBean(ScheduleService.class, scheduleService);

        job = new PartitionPlanJob();
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = new JobExecutionContext();
        context.setJobInstance(job);
        context.setMergedJobDataMap(new Gson().fromJson(JOB_PARAMETERS_JSON,
                PartitionPlanJobParametersTest.class));
        context.setJobName(JOB_NAME);
        context.setTrigger(null);

        job.execute(context);

        assertEquals(true, scheduleServiceMock.isStopped());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        job.interrupt();
    }
}

class PartitionPlanJobPartitionPlanServiceMock {
    DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
    List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

    DatabasePartitionPlanEntity getDatabasePartitionPlanById(String databasePartitionPlanId) {
        assertEquals(DATABASE_PARTITION_PLAN_ID, databasePartitionPlanId);
        return databasePartitionPlanEntity;
    }

    List<TablePartitionPlanEntity> getValidTablePlanByDatabasePartitionPlanId(String databasePartitionPlanId) {
        assertEquals(DATABASE_PARTITION_PLAN_ID, databasePartitionPlanId);
        return tablePartitionPlans;
    }
}

class PartitionPlanJobTaskServiceMock {

}

class PartitionPlanJobScheduleServiceMock {
    ScheduleEntity scheduleEntity = new ScheduleEntity();
    boolean isStopped = false;

    ScheduleEntity nullSafeGetById(Long scheduleId) {
        assertEquals(SCHEDULE_ID, scheduleId.toString());
        return scheduleEntity;
    }

    boolean isConfigEnabled() {
        return true;
    }

    void setStopped(boolean stopped) {
        isStopped = stopped;
    }
}