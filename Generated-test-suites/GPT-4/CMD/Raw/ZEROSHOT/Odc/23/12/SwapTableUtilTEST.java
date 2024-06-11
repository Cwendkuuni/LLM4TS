
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * SwapTableUtilTest
 * Tests for methods in the {@link SwapTableUtil} class.
 */
public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {

        // Test if swap table is disabled when swapTableType is MANUAL, manualSwapTableStarted is false, 
        // taskStatus is RUNNING, fullTransferProgressPercentage is 100 and fullVerificationResult is CONSISTENT.
        assertTrue(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                        FullVerificationResult.CONSISTENT, false
                )
        );

        // Test if swap table is disabled when swapTableType is MANUAL, manualSwapTableStarted is false, 
        // taskStatus is RUNNING, fullTransferProgressPercentage is 100 and fullVerificationResult is UNCHECK.
        assertTrue(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                        FullVerificationResult.UNCHECK, false
                )
        );

        // Test if swap table is enabled when swapTableType is not MANUAL.
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, 
                        FullVerificationResult.CONSISTENT, false
                )
        );

        // Test if swap table is enabled when taskStatus is not RUNNING.
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, TaskStatus.PENDING, 100.0, 
                        FullVerificationResult.CONSISTENT, false
                )
        );

        // Test if swap table is enabled when fullTransferProgressPercentage is not 100.
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, TaskStatus.RUNNING, 80.0, 
                        FullVerificationResult.CONSISTENT, false
                )
        );

        // Test if swap table is enabled when fullVerificationResult is not either CONSISTENT or UNCHECK.
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                        FullVerificationResult.INCONSISTENT, false
                )
        );

        // Test if swap table is enabled when manualSwapTableStarted is true.
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                        FullVerificationResult.CONSISTENT, true
                )
        );
    }
}
