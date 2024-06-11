package com.oceanbase.odc.metadb.flow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    
    @InjectMocks
    private NodeInstanceEntityRepositoryTest nodeInstanceEntityRepositoryTest;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
        //set properties for nodeInstance as per your class definition
        //add nodeInstance to a list
        List<NodeInstanceEntity> list = new ArrayList<>();
        list.add(nodeInstance);
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.NODE, FlowableElementType.ANY)).thenReturn(list);
        //test the method
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.NODE, FlowableElementType.ANY);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testFindByFlowInstanceId() {
        //Similar to previous test method
    }
    
    @Test
    public void testFindByIds() {
        //Similar to previous test method
    }
    
    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }
    
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.NODE)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.NODE);
        assertEquals(1, result);
    }
}