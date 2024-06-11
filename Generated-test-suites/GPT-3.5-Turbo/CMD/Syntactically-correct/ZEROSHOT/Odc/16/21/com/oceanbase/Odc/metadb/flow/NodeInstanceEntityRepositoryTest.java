import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@EnableJpaRepositories
@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepositoryImpl nodeInstanceEntityRepositoryImpl;

    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;

    @Before
    public void setUp() {
        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setInstanceId(101L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        
        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setInstanceId(102L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.TASK);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntity1));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(101L, FlowNodeType.START, FlowableElementType.ELEMENT1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId().longValue());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(any(Long.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(101L);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(any(Long.class)))
                .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(101L);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class)))
                .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(101L, FlowNodeType.START);
        assertEquals(1, result);
    }
}