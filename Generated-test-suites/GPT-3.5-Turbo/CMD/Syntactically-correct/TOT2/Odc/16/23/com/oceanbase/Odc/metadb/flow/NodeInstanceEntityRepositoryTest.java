import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE_A, FlowableElementType.TYPE_X);
        assertNotNull(result);
    }

    @Test
    public void findByFlowInstanceId() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertNotNull(result);
    }

    @Test
    public void findByIds() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Collections.singleton(1L));
        assertNotNull(result);
    }

    @Test
    public void deleteByFlowInstanceId() {
        int deletedRows = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(0, deletedRows);
    }

    @Test
    public void deleteByInstanceIdAndInstanceType() {
        int deletedRows = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);
        assertEquals(0, deletedRows);
    }
}