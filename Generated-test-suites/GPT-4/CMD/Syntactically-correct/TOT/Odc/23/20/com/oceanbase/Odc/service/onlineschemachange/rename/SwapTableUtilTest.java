package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This is a test class for SwapTableUtil
 *
 */
public class SwapTableUtilTest {

    @Before
    public void setUp() throws Exception {
        // Set up any required resources for the tests here.
    }

    @Test
    public void isSwapTableEnableTest1() {
        // Assuming manual swap has not started, task is running and transfer is at 100%
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
                                                          100.0, FullVerificationResult.CONSISTENT, false);

        assertTrue(result);
    }

    @Test
    public void isSwapTableEnableTest2() {
        // Assuming manual swap has started, task is running and transfer is at 100%
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
                                                          100.0, FullVerificationResult.CONSISTENT, true);

        assertFalse(result);
    }

    @Test
    public void isSwapTableEnableTest3() {
        // Assuming manual swap has not started, task is stopped and transfer is at 100%
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 
                                                          100.0, FullVerificationResult.CONSISTENT, false);

        assertFalse(result);
    }

    @Test
    public void isSwapTableEnableTest4() {
        // Assuming manual swap has not started, task is running and transfer is less than 100%
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
                                                          50.0, FullVerificationResult.CONSISTENT, false);

        assertFalse(result);
    }

    @Test
    public void isSwapTableEnableTest5() {
        // Assuming manual swap has not started, task is running, transfer is at 100% and Verification is inconsistent
        boolean result = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
                                                          100.0, FullVerificationResult.INCONSISTENT, false);

        assertFalse(result);
    }
}
