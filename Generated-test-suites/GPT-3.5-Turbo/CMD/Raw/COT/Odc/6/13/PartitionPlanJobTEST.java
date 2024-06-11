
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
    private PartitionPlanService partitionPlanServiceMock = mock(PartitionPlanService.class);
    private ScheduleService scheduleServiceMock = mock(ScheduleService.class);
    private PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

    @Test
    public void testExecute_Success() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
		
		ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
		
		DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanEntity);
        
		List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlanEntities);
		
		partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
        partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
        partitionPlanJob.scheduleService = scheduleServiceMock;

        partitionPlanJob.execute(context);
        
        // Assert appropriate logs and methods called
    }

    @Test(expected = Exception.class)
    public void testExecute_Exception() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
		
		ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":2}");
        when(scheduleServiceMock.nullSafeGetById(2L)).thenThrow(new Exception());
		
		partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
        partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
        partitionPlanJob.scheduleService = scheduleServiceMock;

        partitionPlanJob.execute(context);
        
        // Assert appropriate logs and methods called
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_SchedulerException() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
		
		ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":3}");
        when(scheduleServiceMock.nullSafeGetById(3L)).thenReturn(scheduleEntity);
		
		DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(3L)).thenReturn(databasePartitionPlanEntity);
        
		List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
        tablePartitionPlanEntities.add(new TablePartitionPlanEntity());
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(3L)).thenReturn(tablePartitionPlanEntities);
		
		partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
        partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
        partitionPlanJob.scheduleService = scheduleServiceMock;

        partitionPlanJob.execute(context);
        
        // Assert appropriate logs and methods called
    }

    @Test(expected = RuntimeException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
        
        // Assert that UnExpectedException is thrown
    }
}
