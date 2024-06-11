package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_A;

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        // Add more assertions based on the actual data returned from the method
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        // Add more assertions based on the actual data returned from the method
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(result);
        // Add more assertions based on the actual data returned from the method
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(0, result);
        // Add more assertions based on the actual deletion result
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(0, result);
        // Add more assertions based on the actual deletion result
    }

}