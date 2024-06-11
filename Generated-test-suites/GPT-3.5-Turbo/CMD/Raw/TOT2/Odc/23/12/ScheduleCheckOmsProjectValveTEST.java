import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import java.util.List;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    
    @Mock
    private OscTaskCompleteHandler completeHandler;
    
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Test
    public void testHandleOmsProjectStepResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        
        // Initialize necessary objects for testing
        
        // Test case 1: TaskStatus is DONE and FullVerificationResult is CONSISTENT
        ProjectStepResult projectStepResult1 = new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT);
        OnlineSchemaChangeScheduleTaskResult result1 = new OnlineSchemaChangeScheduleTaskResult();
        SwapTableType swapTableType1 = SwapTableType.AUTOMATIC;
        ScheduleTaskEntity scheduleTask1 = new ScheduleTaskEntity();

        valve.handleOmsProjectStepResult(valveContext, projectStepResult1, result1, swapTableType1, scheduleTask1);

        // Check the result
        assertEquals(true, result1.isManualSwapTableEnabled());
        
        // Test case 2: TaskStatus is not DONE and FullVerificationResult is UNCHECK
        ProjectStepResult projectStepResult2 = new ProjectStepResult(TaskStatus.RUNNING, FullVerificationResult.UNCHECK);
        OnlineSchemaChangeScheduleTaskResult result2 = new OnlineSchemaChangeScheduleTaskResult();
        SwapTableType swapTableType2 = SwapTableType.MANUAL;
        ScheduleTaskEntity scheduleTask2 = new ScheduleTaskEntity();

        valve.handleOmsProjectStepResult(valveContext, projectStepResult2, result2, swapTableType2, scheduleTask2);

        // Check the result
        assertEquals(false, result2.isManualSwapTableEnabled());
    }

    // Add more test cases for other methods as required

}