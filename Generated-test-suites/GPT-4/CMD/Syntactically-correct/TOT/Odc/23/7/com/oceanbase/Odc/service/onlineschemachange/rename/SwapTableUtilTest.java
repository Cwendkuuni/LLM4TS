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

import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;

public class SwapTableUtilTest {

   @Test
   public void testIsSwapTableEnable() {
       // Test case where everything is in order and swap table should be enabled.
       assertTrue(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
               FullVerificationResult.CONSISTENT, false));

       // Test case where transfer is still in progress so swap table should not be enabled.
       assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 99.9,
               FullVerificationResult.CONSISTENT, false));

       // Test case where swap table type is other than MANUAL so swap table should not be enabled.
       assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.AUTO, TaskStatus.RUNNING, 100.0,
               FullVerificationResult.CONSISTENT, false));

       // Test case where task status is not RUNNING so swap table should not be enabled.
       assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.STOPPED, 100.0,
               FullVerificationResult.CONSISTENT, false));

       // Test case where full verification result is inconsistent so swap table should not be enabled.
       assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
               FullVerificationResult.INCONSISTENT, false));

       // Test case where manual swap table has already started so swap table should not be enabled.
       assertFalse(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, TaskStatus.RUNNING, 100.0,
               FullVerificationResult.CONSISTENT, true));
   }
}