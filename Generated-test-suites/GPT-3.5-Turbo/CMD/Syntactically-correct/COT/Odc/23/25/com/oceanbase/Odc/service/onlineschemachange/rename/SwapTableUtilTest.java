import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        
        assertTrue(result);
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testIsSwapTableEnableWithManualTableType() {
        SwapTableType swapTableType = SwapTableType.AUTO; // Should return false for non-manual table type
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsSwapTableEnableWithManualSwapTableStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true; // Should return false when manual swap table is started

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus,
                fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        
        assertFalse(result);
    }
}

