package com.oceanbase.odc.service.onlineschemachange.rename;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.mockito.Mockito;

public class SwapTableUtilTest {
    
    @Test
    public void testIsSwapTableEnableTruePath() {
        assertEquals(true, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                FullVerificationResult.CONSISTENT, false));
        assertEquals(true, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                FullVerificationResult.UNCHECK, false));
    }
    
    @Test
    public void testIsSwapTableEnableFalsePath() {
        // Testing when not manual type
        assertEquals(false, SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTOMATIC, TaskStatus.RUNNING, 100.0, 
                FullVerificationResult.CONSISTENT, false));
        // Testing when manual swap has started
        assertEquals(false, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                FullVerificationResult.CONSISTENT, true));
        // Testing when status not running
        assertEquals(false, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.PAUSED, 100.0, 
                FullVerificationResult.CONSISTENT, false));
        // Testing when progress percentage not equals to 100
        assertEquals(false, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9, 
                FullVerificationResult.CONSISTENT, false));
        // Testing when full verification is REBUILDING
        assertEquals(false, SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0, 
                FullVerificationResult.REBUILDING, false));
    }
}