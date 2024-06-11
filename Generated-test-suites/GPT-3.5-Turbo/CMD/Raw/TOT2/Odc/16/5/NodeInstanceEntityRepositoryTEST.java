import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.repository.query.Param;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepoMock;

    private NodeInstanceEntity nodeInstanceEntity;
    private List<NodeInstanceEntity> nodeInstanceList;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setInstanceId(1001L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.INSTANCE_1);

        nodeInstanceList = new ArrayList<>();
        nodeInstanceList.add(nodeInstanceEntity);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1001L, FlowNodeType.INSTANCE_1, FlowableElementType.TYPE_1))
                .thenReturn(nodeInstanceList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepoMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(1001L, FlowNodeType.INSTANCE_1, FlowableElementType.TYPE_1);
        assertEquals(1, result.size());
        assertEquals(1001L, result.get(0).getInstanceId());
        assertEquals(FlowNodeType.INSTANCE_1, result.get(0).getInstanceType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1001L)).thenReturn(nodeInstanceList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepoMock.findByFlowInstanceId(1001L);
        assertEquals(1, result.size());
        assertEquals(1001L, result.get(0).getInstanceId());
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L))).thenReturn(nodeInstanceList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepoMock.findByIds(Arrays.asList(1L, 2L));
        assertEquals(1, result.size());
        assertEquals(1001L, result.get(0).getInstanceId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1001L)).thenReturn(1);

        int result = nodeInstanceEntityRepoMock.deleteByFlowInstanceId(1001L);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1001L, FlowNodeType.INSTANCE_1)).thenReturn(1);

        int result = nodeInstanceEntityRepoMock.deleteByInstanceIdAndInstanceType(1001L, FlowNodeType.INSTANCE_1);
        assertEquals(1, result);
    }
}