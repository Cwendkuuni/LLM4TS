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
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
        assertEquals(true, result);
    }
    
    @Test
    public void testIsSwapTableEnable_errorScenario() {
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, FullVerificationResult.INCONSISTENT, true);
        assertEquals(false, result);
    }
    
}