import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    
    @Mock
    private OscTaskCompleteHandler completeHandler;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setCompleteHandler(completeHandler);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
    }
    
    @Test
    public void testInvoke() {
        /// Fill details to do when methods are called on the mocked object
        /// when(projectOpenApiService.someMethod()).thenReturn(someValue);
        /// Add the needed changes here and prepare the require object to call the method invoke.
        /// And later verify the methods were called.
        
        OscValveContext context = new OscValveContext(); //Prepare this as required
        scheduleCheckOmsProjectValve.invoke(context);
        verify(projectOpenApiService, times(1)).someMethod(); //Change the method being tested here and the number of times called.
        /// Same for all other dependencies.
    }
}