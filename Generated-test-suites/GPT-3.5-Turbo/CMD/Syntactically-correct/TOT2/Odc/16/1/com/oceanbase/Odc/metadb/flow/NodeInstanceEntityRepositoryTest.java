import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;

import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expected = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        expected.add(nodeInstanceEntity1);
        expected.add(nodeInstanceEntity2);

        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.TYPE_B;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, instanceType, flowableElementType);

        assertFalse(actual.isEmpty());
        assertEquals(2, actual.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expected = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        expected.add(nodeInstanceEntity1);
        expected.add(nodeInstanceEntity2);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong()))
                .thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertFalse(actual.isEmpty());
        assertEquals(2, actual.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);

        List<NodeInstanceEntity> expected = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        expected.add(nodeInstanceEntity1);
        expected.add(nodeInstanceEntity2);

        when(nodeInstanceEntityRepository.findByIds(anyCollection()))
                .thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(ids);

        assertFalse(actual.isEmpty());
        assertEquals(2, actual.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(anyLong()))
                .thenReturn(1);

        int result = nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class)))
                .thenReturn(1);

        int result = nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);

        assertEquals(1, result);
    }

}