import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

import org.junit.Before;
import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
    }

    @Test
    public void testHandleOmsProjectStepResult_SuccessfulSwapTableEnable() {
        // Setup
        // Define necessary objects with mocked values

        // Call the method
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(/* pass necessary arguments */);

        // Verify the results
        // Assert that manualSwapTableEnabled is set to true
    }

    @Test
    public void testHandleOmsProjectStepResult_FailedPreCheck() {
        // Setup
        // Define necessary objects with mocked values

        // Call the method and expect OscException
        try {
            scheduleCheckOmsProjectValve.handleOmsProjectStepResult(/* pass necessary arguments */);
            fail("Expected OscException was not thrown");
        } catch (OscException ex) {
            assertEquals(ErrorCodes.OmsPreCheckFailed, ex.getErrorCode());
        }
    }

    // Add more test cases for other methods of ScheduleCheckOmsProjectValve

}