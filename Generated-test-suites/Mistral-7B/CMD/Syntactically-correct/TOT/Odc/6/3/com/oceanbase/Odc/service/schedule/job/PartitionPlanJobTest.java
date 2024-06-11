import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private PartitionPlanService partitionPlanServiceMock;

    private ScheduleService scheduleServiceMock;

    @Before
    public void initMocks() {
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
    public void testExecute_ValidSchedule_PartitionPlanIsExecuted() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        ScheduleEntity expectedScheduleEntity = new ScheduleEntity();
        expectedScheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = java.util.Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

        when(scheduleService.getById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(expectedScheduleEntity);
        when(scheduleService.getById(scheduleId)).thenThrow(new Exception());
        when(scheduleService.getById(anyLong())).thenThrow(new Exception());

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        partitionPlanJob.execute(context);

        verify(partitionPlanTaskServiceMock).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_ScheduleNotFound_ThrowRuntimeException() {
        Long scheduleId = 1L;
        when(scheduleService.getById(scheduleId)).thenReturn(null);
        partitionPlanJob.execute(mock(JobExecutionContext.class));
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_ExceptionThrownDuringGetSchedule_ThrowJobExecutionException() {
        Long scheduleId = 1L;
        when(scheduleService.getById(scheduleId)).thenThrow(new Exception());
        partitionPlanJob.execute(mock(JobExecutionContext.class));
    }

    @Test
    public void testExecute_DatabasePartitionPlanNotFound_DoNothing() {
        Long scheduleId = 1L;
        when(scheduleService.getById(scheduleId)).thenReturn(new ScheduleEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(null);
        partitionPlanJob.execute(mock(JobExecutionContext.class));
        verifyNoInteractions(partitionPlanTaskServiceMock);
    }

    @Test
    public void testExecute_TablePartitionPlanNotFound_DoNothing() {
        Long scheduleId = 1L;
        when(scheduleService.getById(scheduleId)).thenReturn(new ScheduleEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(Collections.emptyList());
        partitionPlanJob.execute(mock(JobExecutionContext.class));
        verifyNoInteractions(partitionPlanTaskServiceMock);
    }

    @Test
    public void testInterrupt() {
        partitionPlanJob.interrupt();
        verify(partitionPlanTaskServiceMock, never()).executePartitionPlan(anyLong(), anyList());
    }
}