import static org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {
    // Test for isSwapTableEnable method with valid inputs
    @Test
    public void testIsSwapTableEnable_ValidInputs() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    // Test for isSwapTableEnable method with invalid inputs (SwapTableType null)
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_InvalidInput_SwapTableTypeNull() {
        SwapTableType swapTableType = null;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    }

    // Test for isSwapTableEnable method with invalid inputs (TaskStatus null)
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_InvalidInput_TaskStatusNull() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = null;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    }

    // Test for isSwapTableEnable method with invalid inputs (FullVerificationResult null)
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_InvalidInput_FullVerificationResultNull() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = null;
        boolean manualSwapTableStarted = false;
        SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    }

    // Test for isSwapTableEnable method with invalid inputs (fullTransferProgressPercentage < 0)
    @Test
    public void testIsSwapTableEnable_InvalidInput_FullTransferProgressPercentageLessThanZero() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = -1.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }
}