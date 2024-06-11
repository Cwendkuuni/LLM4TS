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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import java.util.Optional;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @MockBean
    private DatabaseRepository databaseRepository;
    
    @MockBean
    private ConnectionService connectionService;
    
    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @MockBean
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfo() {
        Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(anyLong())).thenReturn(database);
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        assertNotNull(oscDatabaseInfo);
    }

    @Test
    public void testSwapTable() {
        Optional<ScheduleTaskEntity> scheduleTaskEntityOptional = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskEntityOptional);
        Optional<ScheduleEntity> scheduleEntity = Optional.of(new ScheduleEntity());
        when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntity);
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNotNull(oscSwapTable);
    }
}