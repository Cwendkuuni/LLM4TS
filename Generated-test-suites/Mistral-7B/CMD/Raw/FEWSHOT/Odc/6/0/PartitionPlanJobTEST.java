package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanServiceMock;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private ScheduleService scheduleServiceMock;

    @Before
    public void setup() {
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);

        SpringContextUtil mockSpringContextUtil = mock(SpringContextUtil.class);
        when(mockSpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanServiceMock);
        when(mockSpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskServiceMock);
        when(mockSpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleServiceMock);

        partitionPlanJob = new PartitionPlanJob() {
            {
                this.partitionPlanTaskService = partitionPlanTaskServiceMock;
                this.partitionPlanService = partitionPlanServiceMock;
                this.scheduleService = scheduleServiceMock;
                this.springContextUtil = mockSpringContextUtil;
            }
        };
    }

    @Test
    public void testExecute() throws SchedulerException, Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan1 = new TablePartitionPlanEntity();
        tablePartitionPlan1.setId(1L);
        tablePartitionPlans.add(tablePartitionPlan1);

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(new JobExecutionContext() {
            {
                this.mergedJobDataMap = new JobDataMap();
            }
        });

        verifyPartitionPlanExecution(databasePartitionPlan, tablePartitionPlans);
    }

    @Test
    public void testExecute_scheduleNotFound() throws SchedulerException {
        Long scheduleId = 1L;

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenThrow(new Exception());

        partitionPlanJob.execute(new JobExecutionContext() {
            {
                this.mergedJobDataMap = new JobDataMap();
            }
        });

        verifyNoPartitionPlanExecution();
    }

    @Test
    public void testExecute_databasePartitionPlanDisabled() throws SchedulerException, Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(new JobExecutionContext() {
            {
                this.mergedJobDataMap = new JobDataMap();
            }
        });

        verifyNoPartitionPlanExecution();
    }

    @Test
    public void testExecute_emptyTablePartitionPlans() throws SchedulerException, Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(new JobExecutionContext() {
            {
                this.mergedJobDataMap = new JobDataMap();
            }
        });

        verifyNoPartitionPlanExecution();
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    private void verifyPartitionPlanExecution(DatabasePartitionPlanEntity databasePartitionPlan,
            List<TablePartitionPlanEntity> tablePartitionPlans) {
        assertSame(databasePartitionPlan.getId(), partitionPlanJob.databasePartitionPlanId);
        assertSame(tablePartitionPlans, partitionPlanJob.tablePartitionPlans);

        partitionPlanTaskServiceMock.executePartitionPlan(Optional.of(databasePartitionPlan.getFlowInstanceId()), tablePartitionPlans);
    }

    private void verifyNoPartitionPlanExecution() {
        assertFalse(partitionPlanJob.databasePartitionPlanId != null);
        assertNull(partitionPlanJob.tablePartitionPlans);
    }
}