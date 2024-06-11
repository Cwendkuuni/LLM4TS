
import static org.junit.Assert.*;
import org.junit.Test;

public class SwapTableUtilTest {

	@Test
	public void testIsSwapTableEnable() {
		// Test when all conditions are met
		boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, false);
		assertTrue(result);
		
		// Test when conditions are not met
		result = SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.FINISHED, 50.0, FullVerificationResult.INCONSISTENT, false);
		assertFalse(result);
	}
	
	@Test
	public void testIsSwapTableEnableEdgeCases() {
		// Test when manualSwapTableStarted is true
		boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.CONSISTENT, true);
		assertFalse(result);
		
		// Test when fullTransferProgressPercentage is not 100
		result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.0, FullVerificationResult.CONSISTENT, false);
		assertFalse(result);
		
		// Test when fullVerificationResult is UNCHECK
		result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, FullVerificationResult.UNCHECK, false);
		assertTrue(result);
	}
}
