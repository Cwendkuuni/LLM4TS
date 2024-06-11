
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void isSwapTableEnableTest_WhenSwapTableTypeIsManualAndManualSwapTableNotStartedAndTaskStatusIsRunningAndFullTransferProgressPercentageIs100AndFullVerificationResultIsConsistentThenReturnTrue() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
    }
    
    @Test
    public void isSwapTableEnableTest_WhenSwapTableTypeIsManualAndManualSwapTableNotStartedAndTaskStatusIsRunningAndFullTransferProgressPercentageIs100AndFullVerificationResultIsUncheckThenReturnTrue() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
        assertTrue(result);
    }
    
    @Test
    public void isSwapTableEnableTest_ReturnsFalseWhenSwapTableTypeNotManual() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.SEQUENTIAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void isSwapTableEnableTest_ReturnsFalseWhenManualSwapTableIsStarted() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertFalse(result);
    }

    @Test
    public void isSwapTableEnableTest_ReturnsFalseWhenFullTransferProgressPercentageIsNot100() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void isSwapTableEnableTest_ReturnsFalseWhenTaskStatusIsNotRunning() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void isSwapTableEnableTest_ReturnsFalseWhenFullVerificationResultIsInconsistent() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
        assertFalse(result);
    }
}
