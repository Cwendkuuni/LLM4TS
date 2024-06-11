import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_Manual_NotStarted_Running_FullProgress_Consistent() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(true, result);
    }

    @Test
    public void testIsSwapTableEnable_Manual_Started_Running_LessProgress_Uncheck() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.UNCHECK, true);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnable_Auto_NotStarted_Finished_FullProgress_Consistent() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.FINISHED, 100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnable_Manual_NotStarted_Running_LessProgress_Consistent() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 50.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(false, result);
    }

    @Test
    public void testIsSwapTableEnable_Manual_NotStarted_Running_FullProgress_Uncheck() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
        assertEquals(true, result);
    }

    @Test
    public void testIsSwapTableEnable_Auto_NotStarted_Running_FullProgress_Uncheck() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
        assertEquals(false, result);
    }
}