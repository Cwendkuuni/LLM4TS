
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {
    
    @Test
    public void testIsSwapTableEnable() {
        //Test case where all the parameters are at perfect condition for swapping
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false));

        //Test case where swapTableType is not MANUAL
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false));

        //Test case where taskStatus is not RUNNING
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FAILED, 100.0, FullVerificationResult.CONSISTENT, false));

        //Test case where fullTransferProgressPercentage is not 100
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false));

        //Test case where fullVerificationResult is not CONSISTENT or UNCHECK
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false));

        //Test case where manualSwapTableStarted is true
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true));
    }
}
