
/*
 * Copyright (c) 2023 OceanBase.
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
import static org.junit.Assert.assertEquals;

/**
 * @author yaobin
 * @date 2023-11-08
 * @since 4.2.3
 */
public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable() {
        boolean actualOutput = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
                100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(true, actualOutput);

        actualOutput = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
                100.0, FullVerificationResult.UNCHECK, false);
        assertEquals(true, actualOutput);

        actualOutput = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.FAILED, 
                100.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(false, actualOutput);

        actualOutput = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
                99.0, FullVerificationResult.CONSISTENT, false);
        assertEquals(false, actualOutput);

        actualOutput = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
                100.0, FullVerificationResult.INCONSISTENT, false);
        assertEquals(false, actualOutput);

        actualOutput = SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 
                100.0, FullVerificationResult.CONSISTENT, true);
        assertEquals(false, actualOutput);
    }
}
