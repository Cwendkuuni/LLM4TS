
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

public class SwapTableUtilTest {
    private boolean manualSwapTableStarted;

    @Before
    public void setup() {
        manualSwapTableStarted = false;
    }

    @Test
    public void testSwapTableEnableManualRunningConsistent() { 
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, manualSwapTableStarted));
    }

    @Test
    public void testSwapTableEnableManualRunningUncheck() {
        assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, manualSwapTableStarted));
    }

    @Test
    public void testSwapTableEnableManualRunningInProgress() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 90.0, FullVerificationResult.CONSISTENT, manualSwapTableStarted));
    }

    @Test
    public void testSwapTableEnableAutoRunningConsistent() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, manualSwapTableStarted));
    }

    @Test
    public void testSwapTableEnableManualStoppedConsistent() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0, FullVerificationResult.CONSISTENT, manualSwapTableStarted));
    }

    @Test
    public void testSwapTableEnableManualRunningInconsistent() {
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.INCONSISTENT, manualSwapTableStarted));
    }

    @Test
    public void testSwapTableEnableManualRunningStarted(){
        manualSwapTableStarted = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, manualSwapTableStarted));
    }
}
