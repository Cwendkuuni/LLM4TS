import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_ManualNotStarted_Running_FullTransferComplete_ConsistentResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, 
        		fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsSwapTableEnable_AutoNotStarted_Running_FullTransferComplete_UncheckResult() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;
        
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, 
        		fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsSwapTableEnable_ManualStarted_Running_FullTransferIncomplete_ConsistentResult() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 90.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;
        
        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, 
        		fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
        
        assertFalse(result);
    }
}