import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    private JobExecutionContext context;

    @Before
    public void setUp() {
        // Initialize the mocks as needed
    }

    @Test
    public void testExecute() {
        // Test the execute method, mocking the context, ScheduleEntity, PartitionPlanJobParameters, and all other dependencies
    }

    @Test
    public void testInterrupt() {
        // Call the interrupt method and verify that it throws an UnExpectedException
    }
}