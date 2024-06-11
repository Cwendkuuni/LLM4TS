package test.com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

import static org.junit.Assert.*;

import org.junit.Test;

public class SwapTableUtilTest {
    @Test
    public void testisSwapTableEnable() {
        assertTrue(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.CONSISTENT, 
            false));
    }

    @Test
    public void testisSwapTableEnable_percentageLt100() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            99.0, 
            FullVerificationResult.CONSISTANT, 
            false));
    }

    @Test
    public void testisSwapTableEnable_notRunning() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.STOPPED, 
            100.0, 
            FullVerificationResult.CONSISTANT, 
            false));
    }

    @Test
    public void testisSwapTableEnable_swapTypeNotManual() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.AUTO, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.CONSISTANT, 
            false));
    }

    @Test
    public void testisSwapTableEnable_manualSwapStarted() {
        assertFalse(SwapTableUtil.isSwapTableEnable(
            SwapTableType.MANUAL, 
            TaskStatus.RUNNING, 
            100.0, 
            FullVerificationResult.CONSISTANT, 
            true));
    }
}