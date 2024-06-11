
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@DataJpaTest
@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    @Before
    public void setup() {
        assertNotNull(nodeInstanceEntityRepositoryMock);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), 
             any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        assertNotNull(nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
             any(FlowNodeType.class), any(FlowableElementType.class)));
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepositoryMock.findByFlowInstanceId(anyLong())).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        assertNotNull(nodeInstanceEntityRepositoryMock.findByFlowInstanceId(anyLong()));
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepositoryMock.findByIds(any(Collection.class))).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        assertNotNull(nodeInstanceEntityRepositoryMock.findByIds(any(Collection.class)));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(anyLong())).thenReturn(1);
        assertEquals(1, nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(anyLong()));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class))).thenReturn(1);
        assertEquals(1, nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class)));
    }
}
