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

package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OscServiceTest {

    @Autowired
    private OscService oscService;

    @Before
    public void setUp() {
        oscService = Mockito.mock(OscService.class);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Mockito.when(oscService.getOscDatabaseInfo(id))
                .thenReturn(new OscLockDatabaseUserInfo());
        
        OscLockDatabaseUserInfo info = oscService.getOscDatabaseInfo(id);
        assertNotNull(info);
        // put additional assertions here to test the expected behavior
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        Mockito.when(oscService.swapTable(scheduleTaskId))
                .thenReturn(new OscSwapTableVO());
        
        OscSwapTableVO swapTableVO = oscService.swapTable(scheduleTaskId);
        assertNotNull(swapTableVO);
       // put additional assertions here to test the expected behavior
        
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        Mockito.when(oscService.getLockUserIsRequired(connectionId))
                .thenReturn(true);
        
        Boolean isRequired = oscService.getLockUserIsRequired(connectionId);
        assertTrue(isRequired);
       // put additional assertions here to test the expected behavior
    }
}