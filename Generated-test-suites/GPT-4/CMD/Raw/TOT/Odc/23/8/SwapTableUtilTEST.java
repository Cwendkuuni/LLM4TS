
import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void isSwapTableEnableTrueTest() {
        assertTrue(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.CONSISTENT,
            false
        ));
    }

    @Test
    public void isSwapTableEnableFalseByManualStartTest() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.CONSISTENT,
            true
        ));
    }

    @Test
    public void isSwapTableEnableFalseByTaskStatusTest() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.PAUSED,
            100.0,
            FullVerificationResult.CONSISTENT,
            false
        ));
    }

    @Test
    public void isSwapTableEnableFalseBySwapTableTypeTest() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.AUTO,
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.CONSISTENT,
            false
        ));
    }

    @Test
    public void isSwapTableEnableFalseByTransferProgressTest() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            99.9,
            FullVerificationResult.CONSISTENT,
            false
        ));
    }

    @Test
    public void isSwapTableEnableFalseByVerificationResultTest() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL,
            TaskStatus.RUNNING,
            100.0,
            FullVerificationResult.INCONSISTENT,
            false
        ));
    }
}
