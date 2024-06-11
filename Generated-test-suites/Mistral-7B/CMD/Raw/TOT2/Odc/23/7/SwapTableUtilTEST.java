import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SwapTableUtilTest {
    // Test case for isSwapTableEnable method with valid input
    @Test
    public void testIsSwapTableEnable_ValidInput() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    // Test case for isSwapTableEnable method with invalid input: swapTableType is NULL
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_NullSwapTableType() {
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        SwapTableUtil.isSwapTableEnable(null, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    }

    // Test case for isSwapTableEnable method with invalid input: taskStatus is NULL
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_NullTaskStatus() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        SwapTableUtil.isSwapTableEnable(swapTableType, null, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
    }

    // Test case for isSwapTableEnable method with invalid input: fullTransferProgressPercentage is NULL
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_NullFullTransferProgressPercentage() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, fullVerificationResult, manualSwapTableStarted);
    }

    // Test case for isSwapTableEnable method with invalid input: fullVerificationResult is NULL
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_NullFullVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        boolean manualSwapTableStarted = false;

        SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, null, manualSwapTableStarted);
    }

    // Test case for isSwapTableEnable method with invalid input: manualSwapTableStarted is NULL
    @Test(expected = NullPointerException.class)
    public void testIsSwapTableEnable_NullManualSwapTableStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

        SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, null);
    }

    // Test case for isSwapTableEnable method with invalid input: swapTableType is NOT MANUAL
    @Test
    public void testIsSwapTableEnable_InvalidSwapTableType() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    // Test case for isSwapTableEnable method with invalid input: manualSwapTableStarted is true
    @Test
    public void testIsSwapTableEnable_ManualSwapTableStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    // Test case for isSwapTableEnable method with invalid input: taskStatus is NOT RUNNING
    @Test
    public void testIsSwapTableEnable_InvalidTaskStatus() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.COMPLETED;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    // Test case for isSwapTableEnable method with invalid input: fullTransferProgressPercentage is less than 100
    @Test
    public void testIsSwapTableEnable_InvalidFullTransferProgressPercentage() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 95.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }

    // Test case for isSwapTableEnable method with invalid input: fullVerificationResult is INVALID
    @Test
    public void testIsSwapTableEnable_InvalidFullVerificationResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INVALID;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
    }
}