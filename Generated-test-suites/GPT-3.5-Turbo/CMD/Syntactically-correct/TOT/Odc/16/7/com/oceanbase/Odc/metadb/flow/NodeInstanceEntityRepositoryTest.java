import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.CrudRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepositoryImpl nodeInstanceEntityRepositoryImpl;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expected = new ArrayList<>();
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_A))
                .thenReturn(expected);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryImpl.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_A);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expected = new ArrayList<>();
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1L))
                .thenReturn(expected);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryImpl.findByFlowInstanceId(1L);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expected = new ArrayList<>();
        Mockito.when(nodeInstanceEntityRepository.findByIds(new ArrayList<>()))
                .thenReturn(expected);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryImpl.findByIds(new ArrayList<>());

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int expected = 1;
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L))
                .thenReturn(expected);

        int result = nodeInstanceEntityRepositoryImpl.deleteByFlowInstanceId(1L);

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int expected = 1;
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A))
                .thenReturn(expected);

        int result = nodeInstanceEntityRepositoryImpl.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);

        assertEquals(expected, result);
    }
}