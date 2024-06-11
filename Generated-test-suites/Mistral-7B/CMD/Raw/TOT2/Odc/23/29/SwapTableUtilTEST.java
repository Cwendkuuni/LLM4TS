import static org.junit.Assert.*;
import static com.oceanbase.odc.service.onlineschemachange.rename.SwapTableType.*;
import static com.oceanbase.odc.core.shared.constant.TaskStatus.*;

import org.junit.Test;

public class SwapTableUtilTest {
    // Test case for isSwapTableEnable method

    @Test
    public void testIsSwapTableEnable_Normal_Scenario() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean expectedResult = true;
        boolean actualResult = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

        assertEquals(expectedResult, actualResult);
    }

    // Test case for isSwapTableEnable method with different edge cases and exception handling scenarios
    // Here we'll add more test cases in the next steps
}