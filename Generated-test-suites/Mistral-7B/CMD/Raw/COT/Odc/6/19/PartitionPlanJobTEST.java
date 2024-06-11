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
import com.oceanbase.tools.mockito.quartz.MockJobExecutionContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanService partitionPlanServiceMock;
    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private ScheduleService scheduleServiceMock;
    private ScheduleEntity scheduleEntityMock;
    private JobExecutionContext contextMock;
    private DatabasePartitionPlanEntity databasePartitionPlanMock;
    private List<TablePartitionPlanEntity> tablePartitionPlansMock;

    @Before
    public void setup() {
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        contextMock = new MockJobExecutionContext();
        scheduleEntityMock = mock(ScheduleEntity.class);
        databasePartitionPlanMock = mock(DatabasePartitionPlanEntity.class);
        tablePartitionPlansMock = new ArrayList<>();

        when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanServiceMock);
        when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskServiceMock);
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleServiceMock);

        when(scheduleEntityMock.getJobParametersJson()).thenReturn("{}");
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanMock);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlansMock);
        when(databasePartitionPlanMock.isConfigEnabled()).thenReturn(true);
    }

    @Test
    public void testExecute_scheduleNotExist() {
        when(scheduleServiceMock.nullSafeGetById(1L)).thenThrow(new Exception());

        partitionPlanJob.execute(contextMock);

        assertNull(contextMock.getMergedJobDataMap().getString("jobStatus"));
        assertNull(contextMock.getMergedJobDataMap().getString("errorMessage"));
        assertNull(contextMock.getMergedJobDataMap().getString("message"));
    }

    @Test
    public void testExecute_databasePartitionPlanIsDisable() {
        when(databasePartitionPlanMock.isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(contextMock);

        assertNull(contextMock.getMergedJobDataMap().getString("jobStatus"));
        assertNull(contextMock.getMergedJobDataMap().getString("errorMessage"));
        assertNull(contextMock.getMergedJobDataMap().getString("message"));
    }

    @Test
    public void testExecute_noTableNeedToProcess() {
        when(tablePartitionPlansMock.isEmpty()).thenReturn(true);

        partitionPlanJob.execute(contextMock);

        assertNull(contextMock.getMergedJobDataMap().getString("jobStatus"));
        assertNull(contextMock.getMergedJobDataMap().getString("errorMessage"));
        assertNull(contextMock.getMergedJobDataMap().getString("message"));
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_exceptionThrown() {
        when(partitionPlanTaskServiceMock.executePartitionPlan(null, null)).thenThrow(new Exception());

        partitionPlanJob.execute(contextMock);
    }

    @Test
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}