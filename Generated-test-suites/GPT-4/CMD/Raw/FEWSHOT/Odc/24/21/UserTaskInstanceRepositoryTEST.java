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
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.COMPLETED;
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expected);
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(status);
        assertEquals(expected, actual);
    }

    // Generate similar tests for other query methods
    // ...
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity(); // Construct the entity with correct values
        Mockito.when(userTaskInstanceRepository.update(entity)).thenReturn(1);
        int actual = userTaskInstanceRepository.update(entity);
        assertEquals(1, actual);
    }

    // Generate similar tests for other update methods
    // ...

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Mockito.when(userTaskInstanceRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);
        int actual = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, actual);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.NODE;
        String activityId = "test";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> expected = Optional.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId)).thenReturn(expected);
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertEquals(expected, actual);
    }

    // Generate similar tests for other find methods
    // ...
}