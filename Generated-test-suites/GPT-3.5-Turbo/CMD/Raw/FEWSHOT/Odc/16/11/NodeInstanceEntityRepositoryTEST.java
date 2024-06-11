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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.TYPE2))
                .thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.TYPE2);

        assertNotNull(actualEntities);
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        Mockito.when(repository.findByFlowInstanceId(1L)).thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = repository.findByFlowInstanceId(1L);

        assertNotNull(actualEntities);
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        Mockito.when(repository.findByIds(Arrays.asList(1L, 2L, 3L))).thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = repository.findByIds(Arrays.asList(1L, 2L, 3L));

        assertNotNull(actualEntities);
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(repository.deleteByFlowInstanceId(1L)).thenReturn(1);

        int result = repository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE1)).thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE1);

        assertEquals(1, result);
    }
}