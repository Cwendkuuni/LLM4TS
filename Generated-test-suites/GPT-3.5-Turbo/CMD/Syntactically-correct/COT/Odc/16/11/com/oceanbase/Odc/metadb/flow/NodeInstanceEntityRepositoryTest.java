
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SOME_TYPE, FlowableElementType.SOME_TYPE);
        assertNotNull(result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertNotNull(result);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = List.of(1L, 2L, 3L);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertNotNull(result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(0, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.SOME_TYPE);
        assertEquals(0, result);
    }
}
