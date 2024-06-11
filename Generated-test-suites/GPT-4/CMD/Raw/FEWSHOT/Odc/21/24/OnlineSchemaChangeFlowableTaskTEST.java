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

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author yaobin
 * @date 2023-08-23
 * @since 4.2.0
 */
public class OnlineSchemaChangeFlowableTaskTest {
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ConnectionConfig mockedConnectionConfig;
    private OnlineSchemaChangeParameters mockedParameters;
    private DelegateExecution mockedDelegateExecution;
    private ScheduleService mockedScheduleService;
    private ScheduleTaskService mockedScheduleTaskService;
     
    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        mockedConnectionConfig = mock(ConnectionConfig.class);
        mockedParameters = mock(OnlineSchemaChangeParameters.class);
        mockedDelegateExecution = mock(DelegateExecution.class);
        mockedScheduleService = mock(ScheduleService.class);
        mockedScheduleTaskService = mock(ScheduleTaskService.class);
        onlineSchemaChangeFlowableTask.setScheduleService(mockedScheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(mockedScheduleTaskService);
    }

    @Test
    public void testStart() throws Exception {
        ScheduleEntity mockedScheduleEntity = mock(ScheduleEntity.class);
        when(mockedScheduleEntity.getId()).thenReturn(1L);
        when(mockedScheduleService.create(any(ScheduleEntity.class))).thenReturn(mockedScheduleEntity);
       
        onlineSchemaChangeFlowableTask.start(1L, mockedTaskService, mockedDelegateExecution);
        verify(mockedScheduleService).create(any(ScheduleEntity.class));
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.start(1L, mockedTaskService, mockedDelegateExecution);
        TaskStatus status = onlineSchemaChangeFlowableTask.getStatus();
        assertEquals(TaskStatus.DONE.equals(status), onlineSchemaChangeFlowableTask.isSuccessful());
    }
    
    // Similarly add test cases for other methods of the class. 

}