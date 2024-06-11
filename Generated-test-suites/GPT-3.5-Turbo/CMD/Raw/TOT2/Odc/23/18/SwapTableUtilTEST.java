import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Typical use case
        assertTrue(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.CONSISTENT, 
            false
        ));
        
        // Edge case: task status other than RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.PENDING, 
            100.0, 
            FullVerificationResult.CONSISTENT, 
            false
        ));
        
        // Edge case: full transfer progress percentage not equal to 100
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            50.0, 
            FullVerificationResult.CONSISTENT, 
            false
        ));
        
        // Edge case: full verification result not CONSISTENT or UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.ERROR, 
            false
        ));
        
        // Edge case: manual swap table already started
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.UNCHECK, 
            true
        ));
    }
}