import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(repository, "instanceType", FlowNodeType.ACTIVITY);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.TASK))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.TASK);

        Mockito.verify(repository).findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.TASK);
        assertNotNull(result);
        assertEquals(expectedList, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        Mockito.when(repository.findByFlowInstanceId(1L)).thenReturn(expectedList);

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);

        Mockito.verify(repository).findByFlowInstanceId(1L);
        assertNotNull(result);
        assertEquals(expectedList, result);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        Mockito.when(repository.findByIds(ids)).thenReturn(expectedList);

        List<NodeInstanceEntity> result = repository.findByIds(ids);

        Mockito.verify(repository).findByIds(ids);
        assertNotNull(result);
        assertEquals(expectedList, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int expectedCount = 1;
        Mockito.when(repository.deleteByFlowInstanceId(1L)).thenReturn(expectedCount);

        int result = repository.deleteByFlowInstanceId(1L);

        Mockito.verify(repository).deleteByFlowInstanceId(1L);
        assertEquals(expectedCount, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int expectedCount = 1;
        Mockito.when(repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK)).thenReturn(expectedCount);

        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);

        Mockito.verify(repository).deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);
        assertEquals(expectedCount, result);
    }
}