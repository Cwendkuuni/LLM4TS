// JUnit 4 test cases for SwapTableUtil class

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_typicalUseCase() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(true, result);
    }

    @Test
    public void testIsSwapTableEnable_edgeCase() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, true);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnable_errorScenario() {
        boolean result = SwapTableUtil.isSwapTableEnable(null, TaskStatus.STOPPED, 50.0, FullVerificationResult.INCONSISTENT, false);
        assertEquals(false, result);
    }

}