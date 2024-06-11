import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

public class OnlineSchemaChangeFlowableTaskTest {
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private ScheduleService scheduleService;
    
    @Mock
    private ScheduleTaskService scheduleTaskService;
    
    @Mock
    private QuartzJobService quartzJobService;
    
    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;
    
    @Mock
    private OscTaskCompleteHandler completeHandler;
    
    @Mock
    private OrganizationService organizationService;
    
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    
    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
        onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandler);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
    }
    
    @Test
    public void testStart() {
        // Testing start method
        
    }
    
    @Test
    public void testIsSuccessful() {
        // Testing isSuccessful method
        
    }
    
    @Test
    public void testIsFailure() {
        // Testing isFailure method
        
    }
    
    @Test
    public void testOnFailure() {
        // Testing onFailure method
        
    }
    
    @Test
    public void testOnSuccessful() {
        // Testing onSuccessful method
        
    }
    
    @Test
    public void testOnTimeout() {
        // Testing onTimeout method
        
    }
    
    @Test
    public void testOnProgressUpdate() {
        // Testing onProgressUpdate method
        
    }
    
    @Test
    public void testCancel() {
        // Testing cancel method
        
    }
    
    @Test
    public void testIsCancelled() {
        // Testing isCancelled method
        
    }

}