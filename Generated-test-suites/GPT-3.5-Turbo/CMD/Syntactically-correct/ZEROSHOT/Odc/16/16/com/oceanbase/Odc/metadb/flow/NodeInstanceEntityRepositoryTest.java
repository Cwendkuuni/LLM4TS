import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    NodeInstanceEntityRepository mockRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        when(mockRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.INSTANCE, FlowableElementType.ELEMENT);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        when(mockRepository.findByFlowInstanceId(any(Long.class)))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        when(mockRepository.findByIds(any(Collection.class)))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByIds(new ArrayList<>());

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int expectedCount = 1;
        when(mockRepository.deleteByFlowInstanceId(any(Long.class)))
                .thenReturn(expectedCount);

        int actualCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int expectedCount = 1;
        when(mockRepository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class)))
                .thenReturn(expectedCount);

        int actualCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.INSTANCE);

        assertEquals(expectedCount, actualCount);
    }
}