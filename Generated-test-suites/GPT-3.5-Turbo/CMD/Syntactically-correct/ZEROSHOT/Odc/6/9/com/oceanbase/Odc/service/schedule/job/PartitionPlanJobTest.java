import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanTaskService mockPartitionPlanTaskService;
    private PartitionPlanService mockPartitionPlanService;
    private ScheduleService mockScheduleService;
    private JobExecutionContext mockContext;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        mockPartitionPlanTaskService = mock(PartitionPlanTaskService.class);
        mockPartitionPlanService = mock(PartitionPlanService.class);
        mockScheduleService = mock(ScheduleService.class);
        mockContext = mock(JobExecutionContext.class);

        SpringContextUtil.setBean("PartitionPlanTaskService", mockPartitionPlanTaskService);
        SpringContextUtil.setBean("PartitionPlanService", mockPartitionPlanService);
        SpringContextUtil.setBean("ScheduleService", mockScheduleService);
    }

    @Test
    public void testExecute() throws SchedulerException {
        // Setup
        when(ScheduleTaskUtils.getScheduleId(mockContext)).thenReturn(1L);

        ScheduleEntity mockScheduleEntity = new ScheduleEntity();
        when(mockScheduleService.nullSafeGetById(1L)).thenReturn(mockScheduleEntity);

        PartitionPlanJobParameters params = new PartitionPlanJobParameters();
        String json = "{\"databasePartitionPlanId\": 1}";
        params = JsonUtils.fromJson(json, PartitionPlanJobParameters.class);
        mockScheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity mockDatabasePartitionPlan = new DatabasePartitionPlanEntity();
        when(mockPartitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(mockDatabasePartitionPlan);

        // Test
        partitionPlanJob.execute(mockContext);

        // Verify
        verify(mockPartitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteWithEmptyTablePlans() throws SchedulerException {
        // Setup
        when(ScheduleTaskUtils.getScheduleId(mockContext)).thenReturn(1L);

        ScheduleEntity mockScheduleEntity = new ScheduleEntity();
        when(mockScheduleService.nullSafeGetById(1L)).thenReturn(mockScheduleEntity);

        PartitionPlanJobParameters params = new PartitionPlanJobParameters();
        String json = "{\"databasePartitionPlanId\": 1}";
        params = JsonUtils.fromJson(json, PartitionPlanJobParameters.class);
        mockScheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity mockDatabasePartitionPlan = new DatabasePartitionPlanEntity();
        when(mockPartitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(mockDatabasePartitionPlan);

        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<>());

        // Test
        partitionPlanJob.execute(mockContext);
    }

    @Test
    public void testInterrupt() {
        // Test
        try {
            partitionPlanJob.interrupt();
            fail("Expected UnExpectedException");
        } catch (UnExpectedException e) {
            // Success
        }
    }

}