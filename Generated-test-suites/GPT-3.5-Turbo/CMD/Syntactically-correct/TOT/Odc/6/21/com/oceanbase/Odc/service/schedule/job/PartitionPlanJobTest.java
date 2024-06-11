
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        // Mocking Spring Context Util
        when(SpringContextUtil.getBean(eq(PartitionPlanTaskService.class))).thenReturn(partitionPlanTaskService);
        when(SpringContextUtil.getBean(eq(PartitionPlanService.class))).thenReturn(partitionPlanService);
        when(SpringContextUtil.getBean(eq(ScheduleService.class))).thenReturn(scheduleService);
    }

    @Test
    public void testExecute_SuccessfulExecution() throws SchedulerException {
        // Mocking job parameters
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
      
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        // Mocking methods of PartitionPlanService
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
            .thenReturn(List.of(new TablePartitionPlanEntity()));

        // Invoke the method
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Verify the interactions
        verify(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_SchedulerException() throws SchedulerException {
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new SchedulerException(""));

        partitionPlanJob.execute(mock(JobExecutionContext.class));
    }

    @Test
    public void testExecute_DisabledConfig() throws SchedulerException {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(scheduleService).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testInterrupt_UnexpectedException() {
        // Mocking the exception and verifying it is thrown
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            return;
        }
        fail("Expected UnExpectedException to be thrown");
    }
}

