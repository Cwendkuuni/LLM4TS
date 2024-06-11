import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

@pytest
public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Test 
    public void testExecute() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        try { 
			partitionPlanJob.execute(context); 
			fail("Should have thrown an exception");
		} catch (Exception e) { 
			assertTrue(true);
		}
    }
    
    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.interrupt();
    }
}