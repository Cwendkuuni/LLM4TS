/*
 * Copyright (c) 2023 Tester.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.JobType;
import com.oceanbase.odc.core.shared.constant.SwapTableType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;

import java.util.Optional;

public class OscServiceTest {

    @InjectMocks
    OscService oscService;

    @Mock
    DatabaseRepository databaseRepository;

    @Mock
    ConnectionService connectionService;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity mockDatabaseEntity = mock(DatabaseEntity.class); 
        oscService.getOscDatabaseInfo(1L);
        
        assertEquals(databaseRepository.findById(1L), mockDatabaseEntity);
        assertNull(oscService.getOscDatabaseInfo(-1L));
    }

    @Test
    public void testSwapTable() {
        ScheduleEntity mockScheduleEntity = mock(ScheduleEntity.class);
        oscService.swapTable(1L);
        
        assertEquals(scheduleTaskRepository.findById(1L), mockScheduleEntity);
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionService mockConnService = mock(ConnectionService.class);

        when(mockConnService.getForConnectionSkipPermissionCheck(1L)).thenReturn(true);
        assertFalse(oscService.getLockUserIsRequired(1L));
    }
}