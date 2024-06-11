
    import org.junit.Test;
    import org.quartz.JobExecutionContext;
    import static org.mockito.Mockito.mock;
  
    public class PartitionPlanJobTest {
        @Test (expected = UnExpectedException.class)
        public void testInterrupt_ThrowsException() {
            new PartitionPlanJob().interrupt(); 
        }
  
        @Test (expected = IllegalArgumentException.class)
        public void testExecute_WhenContextIsNull() {
            new PartitionPlanJob().execute(null); 
        }

        @Test
        public void testExecute_WhenContextIsNotNull() {
            JobExecutionContext context = mock(JobExecutionContext.class);
            new PartitionPlanJob().execute(context); 
        }
    }
    