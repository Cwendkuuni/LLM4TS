
/*
 * Copyright (c) 2025 TestCompany.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oceanbase.odc.service.onlineschemachange.rename;

import org.junit.Test;
import static org.junit.Assert.*;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        // Case when Swap Table should be enabled
        boolean result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false
        );
        assertTrue(result);

        // Case when fullTransferProgressPercentage is less than 100
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                50.0,
                FullVerificationResult.CONSISTENT,
                false
        );
        assertFalse(result);

        // Case when fullVerificationResult is neither CONSISTENT nor UNCHECKED
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.INCONSISTENT,
                false
        );
        assertFalse(result);

        // Case when manualSwapTable is already started
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                true
        );
        assertFalse(result);

        // Case when taskStatus is not RUNNING
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.MANUAL,
                TaskStatus.WAITING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false
        );
        assertFalse(result);

        // Case when swapTableType is not MANUAL
        result = SwapTableUtil.isSwapTableEnable(
                SwapTableType.AUTO,
                TaskStatus.RUNNING,
                100.0,
                FullVerificationResult.CONSISTENT,
                false
        );
        assertFalse(result);
    }

}
