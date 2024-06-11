package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void isSwapTableEnableShouldReturnTrue() {
        assertTrue(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL,
                        TaskStatus.RUNNING,
                        100.0,
                        FullVerificationResult.CONSISTENT,
                        false
                )
        );
    }

    @Test
    public void isSwapTableEnableShouldReturnFalseForNonManualSwapType() {
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.AUTOMATIC,
                        TaskStatus.RUNNING,
                        100.0,
                        FullVerificationResult.CONSISTANT,
                        false
                )
        );
    }

    @Test
    public void isSwapTableEnableShouldReturnFalseForNonRunningStatus() {
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL,
                        TaskStatus.STOPPED,
                        100.0,
                        FullVerificationResult.CONSISTANT,
                        false
                )
        );
    }

    @Test
    public void isSwapTableEnableShouldReturnFalseForProgressLessThan100() {
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL,
                        TaskStatus.RUNNING,
                        99.0,
                        FullVerificationResult.CONSISTANT,
                        false
                )
        );
    }

    @Test
    public void isSwapTableEnableShouldReturnFalseForNoneConsistentVerificationResult() {
        assertFalse(
                SwapTableUtil.isSwapTableEnable(
                        SwapTableType.MANUAL,
                        TaskStatus.RUNNING,
                        100.0,
                        FullVerificationResult.INCONSISTANT,
                        false
                )
        );
    }
}
