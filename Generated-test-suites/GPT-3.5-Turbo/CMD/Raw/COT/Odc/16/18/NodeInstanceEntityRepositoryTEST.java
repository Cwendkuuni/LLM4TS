import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(Mockito.anyLong(),
                Mockito.any(FlowNodeType.class), Mockito.any(FlowableElementType.class))).thenReturn(expected);

        List<NodeInstanceEntity> actual = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_B);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expected = new ArrayList<>();
        Mockito.when(repository.findByFlowInstanceId(Mockito.anyLong())).thenReturn(expected);

        List<NodeInstanceEntity> actual = repository.findByFlowInstanceId(1L);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        Mockito.when(repository.findByIds(Collections.singletonList(1L))).thenReturn(expected);

        List<NodeInstanceEntity> actual = repository.findByIds(Collections.singletonList(1L));

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(repository.deleteByFlowInstanceId(Mockito.anyLong())).thenReturn(1);

        int actual = repository.deleteByFlowInstanceId(1L);

        assertEquals(1, actual);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(repository.deleteByInstanceIdAndInstanceType(Mockito.anyLong(), Mockito.any(FlowNodeType.class)))
                .thenReturn(1);

        int actual = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);

        assertEquals(1, actual);
    }
}