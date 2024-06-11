import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = mock(TaskStatus.class);
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        // Test with null input
        assertFalse(SwapTableUtil.isSwapTableEnable(null, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, null, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, fullVerificationResult, manualSwapTableStarted));

        // Test with invalid TaskStatus
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, null, manualSwapTableStarted));

        // Test with invalid fullVerificationResult
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, mock(FullVerificationResult.class), true));

        // Test valid use cases
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, true));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 50.0, FullVerificationResult.INCONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.PAUSED, 80.0, FullVerificationResult.INCONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 95.0, FullVerificationResult.INCONSISTENT, false));
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, true));

        // Test edge cases
        assertFalse(SwapTableUtil.isSwapTableEnable(null, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, null, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null, fullVerificationResult, manualSwapTableStarted));

        // Test exceptions
        assertNull(SwapTableUtil.isSwapTableEnable(null, null, null, null, manualSwapTableStarted));
    }
}