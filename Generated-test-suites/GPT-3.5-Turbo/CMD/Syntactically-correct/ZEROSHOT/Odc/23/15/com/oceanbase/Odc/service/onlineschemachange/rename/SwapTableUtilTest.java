import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_ManualNotStarted() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_AutomaticStarted() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_TaskNotRunning() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.COMPLETED, 100.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_NotFullTransferProgress() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.0, FullVerificationResult.CONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_InconsistentVerification() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, false);
        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnable_InitialVerification() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnable_ManualStarted() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
        assertFalse(result);
    }

}