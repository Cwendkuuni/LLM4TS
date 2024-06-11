package com.oceanbase.odc.metadb.flow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);

    @Before
    public void setUp() throws Exception {
        Long id = 1L;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(id);

        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(id, instanceType, flowableElementType)).thenReturn(Arrays.asList(nodeInstanceEntity));

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(id)).thenReturn(Arrays.asList(nodeInstanceEntity));

        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(Arrays.asList(nodeInstanceEntity));

        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(id)).thenReturn(1);

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(id, instanceType)).thenReturn(1);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        //Your test logic to validate the method
    }

    @Test
    public void testFindByFlowInstanceId() {
        //Your test logic to validate the method
    }

    @Test
    public void testFindByIds() {
        //Your test logic to validate the method
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        //Your test logic to validate the method
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        //Your test logic to validate the method
    }

    @After
    public void afterEachTest() {
        Mockito.reset(nodeInstanceEntityRepository);
    }
}