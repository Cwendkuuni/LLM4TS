import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
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

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private PartitionPlanService partitionPlanServiceMock;

    private ScheduleService scheduleServiceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskServiceMock);
        when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanServiceMock);
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleServiceMock);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecuteWithValidScheduleEntityAndPartitionPlans() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        ScheduleEntity nullScheduleEntity = null;

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(1L);
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = java.util.Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

        ScheduleEntity expectedScheduleEntity = new ScheduleEntity();
        expectedScheduleEntity.setId(scheduleId);

        try {
            when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
            when(scheduleService.getById(scheduleId)).thenReturn(scheduleEntity);
            when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);
            when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn(tablePartitionPlans);

            partitionPlanJob.execute(mock(JobExecutionContext.class));

            verify(scheduleService).terminate(scheduleEntity);
            verify(scheduleService).getById(scheduleId);
            verifyNoMoreInteractions(scheduleService);
            verify(scheduleService).nullSafeGetById(scheduleId);
            verifyNoMoreInteractions(scheduleService);
            verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
            verifyNoMoreInteractions(partitionPlanTaskService);
            verify(partitionPlanService).getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());
            verify(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());
            verifyNoMoreInteractions(partitionPlanService);

        } catch (Exception e) {
            log.warn("Test failed", e);
        }
    }

    @Test
    public void testExecuteWithNullScheduleEntity() {
        Long scheduleId = 1L;

        when(scheduleService.getById(scheduleId)).thenThrow(new RuntimeException());

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verifyZeroInteractions(partitionPlanTaskService);
        verifyZeroInteractions(scheduleService);
        verifyZeroInteractions(partitionPlanService);
    }

    @Test
    public void testExecuteWithEmptyTablePartitionPlansList() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(1L);
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = java.util.Collections.emptyList();

        try {
            when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
            when(scheduleService.getById(scheduleId)).thenThrow(new RuntimeException());
            when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);
            when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn(tablePartitionPlans);

            partitionPlanJob.execute(mock(JobExecutionContext.class));

            verifyZeroInteractions(partitionPlanTaskService);
            verifyZeroInteractions(scheduleService);
            verifyZeroInteractions(partitionPlanService);
            verifyNoMoreInteractions(scheduleService);
            verifyNoMoreInteractions(scheduleService);
            verifyNoMoreInteractions(partitionPlanService);
            verifyNoMoreInteractions(partitionPlanService);

        } catch (Exception e) {
            log.warn("Test failed", e);
        }
    }

    @Test
    public void testExecuteWithDisabledDatabasePartitionPlan() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(1L);
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = java.util.Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

        try {
            when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
            when(scheduleService.getById(scheduleId)).thenThrow(new RuntimeException());
            when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);
            when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn(tablePartitionPlans);

            partitionPlanJob.execute(mock(JobExecutionContext.class));

            verifyZeroInteractions(partitionPlanTaskService);
            verifyZeroInteractions(scheduleService);
            verifyZeroInteractions(partitionPlanService);
            verifyNoMoreInteractions(scheduleService);
            verifyNoMoreInteractions(scheduleService);
            verifyNoMoreInteractions(partitionPlanService);
            verifyNoMoreInteractions(partitionPlanService);

        } catch (Exception e) {
            log.warn("Test failed", e);
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}