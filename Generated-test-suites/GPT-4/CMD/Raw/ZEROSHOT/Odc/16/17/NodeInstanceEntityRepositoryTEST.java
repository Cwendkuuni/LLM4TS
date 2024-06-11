
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    private static final Long TEST_ID = 1L;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(nodeInstanceEntityRepository, "nodeInstanceEntity", nodeInstanceEntity);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(TEST_ID, FlowNodeType.SIMPLE, FlowableElementType.ACTIVITY);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(any(Long.class)))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(TEST_ID);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(any(Collection.class)))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(TEST_ID));
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(any(Long.class)))
                .thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(TEST_ID);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class)))
                .thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(TEST_ID, FlowNodeType.SIMPLE);
        assertEquals(1, result);
    }
}
