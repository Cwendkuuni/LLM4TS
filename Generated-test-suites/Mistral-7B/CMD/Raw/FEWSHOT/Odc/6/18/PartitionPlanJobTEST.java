package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

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

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanServiceMock;
    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private ScheduleService scheduleServiceMock;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);

        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskServiceMock);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanServiceMock);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleServiceMock);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setSpringContextUtil(springContextUtil);
    }

    @After
    public void tearDown() {
        partitionPlanServiceMock = null;
        partitionPlanTaskServiceMock = null;
        scheduleServiceMock = null;
        partitionPlanJob = null;
    }

    @Test
    public void testExecute_databasePartitionPlanIsNull() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();

        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");

        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = UUID.randomUUID().toString();
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID_KEY)).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        verify(log).info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                scheduleId);
        try {
            scheduleServiceMock.terminate(scheduleEntity);
        } catch (SchedulerException e) {
            log.warn("Stop partition plan job failed.", e);
            throw new RuntimeException(e);
        }
        verify(log, times(1)).warn("Schedule not found,scheduleId={}", scheduleId);
    }

    @Test
    public void testExecute_databasePartitionPlanIsDisable() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();

        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(scheduleEntity.getPartitionPlanId()).thenReturn(UUID.randomUUID().toString());
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyString())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = UUID.randomUUID().toString();
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID_KEY)).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        verify(log).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                scheduleId);
        try {
            scheduleServiceMock.terminate(scheduleEntity);
        } catch (SchedulerException e) {
            log.warn("Stop partition plan job failed.", e);
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecute_noValidTablePartitionPlans() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();

        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(scheduleEntity.getPartitionPlanId()).thenReturn(UUID.randomUUID().toString());
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyString())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyString()))
                .thenReturn(new ArrayList<>());

        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = UUID.randomUUID().toString();
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID_KEY)).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        verify(log).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                scheduleId);
        try {
            scheduleServiceMock.terminate(scheduleEntity);
        } catch (SchedulerException e) {
            log.warn("Stop partition plan job failed.", e);
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecute_executePartitionPlanSuccess() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(scheduleEntity.getPartitionPlanId()).thenReturn(UUID.randomUUID().toString());
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyString())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyString()))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = UUID.randomUUID().toString();
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID_KEY)).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskServiceMock).executePartitionPlan(anyLong(), eq(tablePartitionPlans));
    }

    @Test
    public void testGetInstance() {
        PartitionPlanJob partitionPlanJob1 = new PartitionPlanJob();
        PartitionPlanJob partitionPlanJob2 = new PartitionPlanJob();

        assertSame(partitionPlanJob1, partitionPlanJob1);
        assertNotSame(partitionPlanJob1, partitionPlanJob2);
    }
}