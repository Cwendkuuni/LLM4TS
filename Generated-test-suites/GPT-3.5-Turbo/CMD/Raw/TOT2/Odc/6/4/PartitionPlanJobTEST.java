import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext jobExecutionContext;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    @Mock
    private PartitionPlanService partitionPlanService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws Exception {
        // Arrange
        long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(
            List.of(new TablePartitionPlanEntity()));
        doNothing().when(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());

        // Act
        partitionPlanJob.execute(jobExecutionContext);

        // Assert
        verify(scheduleService).terminate(scheduleEntity);
        verify(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());
        verify(partitionPlanService).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(anyLong());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_SchedulerException() throws Exception {
        // Arrange
        long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(
            List.of(new TablePartitionPlanEntity()));
        doThrow(new SchedulerException()).when(scheduleService).terminate(scheduleEntity);

        // Act
        partitionPlanJob.execute(jobExecutionContext);
    }

    @Test
    public void testInterrupt() {
        // Assert
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }
}