package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleEntity;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanService partitionPlanServiceMock;
    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private ScheduleService scheduleServiceMock;
    private ScheduleEntity scheduleEntityMock;

    @Before
    public void setup() {
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        scheduleEntityMock = mock(ScheduleEntity.class);

        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
        SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecuteWithValidScheduleEntity() throws Exception {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(new Gson().toJsonTree(new PartitionPlanJobParameters()).getAsJson());
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        partitionPlanJob.execute(context);

        // You can add assertions here to test the behavior of the method
    }

    @Test
    public void testExecuteWithInvalidScheduleEntity() throws Exception {
        when(scheduleServiceMock.nullSafeGetById(1L)).thenThrow(new Exception("Schedule not found"));

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // You can add assertions here to test the behavior of the method
        verify(scheduleServiceMock).terminate(scheduleEntityMock);
    }

    @Test
    public void testExecuteWithDisabledDatabasePartitionPlan() throws Exception {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(new Gson().toJsonTree(new PartitionPlanJobParameters()).getAsJson());
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<>());

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // You can add assertions here to test the behavior of the method
        verify(scheduleServiceMock).terminate(scheduleEntityMock);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}