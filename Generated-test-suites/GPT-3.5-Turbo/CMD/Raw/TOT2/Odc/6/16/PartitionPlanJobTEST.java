import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
    }

    @Test
    public void testExecute() throws SchedulerException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String jobParametersJson = "{}";
        when(scheduleEntity.getJobParametersJson()).thenReturn(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(1L);
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (Exception e) {
            // Expecting UnExpectedException
        }
    }

}