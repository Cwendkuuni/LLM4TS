import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE_A, FlowableElementType.TYPE_X);

        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE_A, FlowableElementType.TYPE_X);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        when(nodeInstanceEntityRepository.findByFlowInstanceId(any(Long.class)))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepositoryMock.findByFlowInstanceId(1L);

        verify(nodeInstanceEntityRepository).findByFlowInstanceId(1L);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepositoryMock.findByIds(ids);

        verify(nodeInstanceEntityRepository).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(any(Long.class)))
                .thenReturn(1);

        int result = nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(1L);

        verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class)))
                .thenReturn(1);

        int result = nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);

        verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);
    }
}