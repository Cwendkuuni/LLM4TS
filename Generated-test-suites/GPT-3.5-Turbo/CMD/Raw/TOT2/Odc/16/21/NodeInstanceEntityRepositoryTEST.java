import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @MockBean
    private NodeInstanceEntityRepository mockNodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.USER_TASK);

        List<NodeInstanceEntity> expectedEntityList = Arrays.asList(nodeInstanceEntity);

        Mockito.when(mockNodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.USER_TASK))
                .thenReturn(expectedEntityList);

        List<NodeInstanceEntity> actualEntityList = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.USER_TASK);

        assertNotNull(actualEntityList);
        assertEquals(1, actualEntityList.size());
        assertEquals(nodeInstanceEntity.getInstanceId(), actualEntityList.get(0).getInstanceId());
        assertEquals(nodeInstanceEntity.getInstanceType(), actualEntityList.get(0).getInstanceType());
        assertEquals(nodeInstanceEntity.getFlowableElementType(), actualEntityList.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);

        List<NodeInstanceEntity> expectedEntityList = Arrays.asList(nodeInstanceEntity);

        Mockito.when(mockNodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(expectedEntityList);

        List<NodeInstanceEntity> actualEntityList = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertNotNull(actualEntityList);
        assertEquals(1, actualEntityList.size());
        assertEquals(nodeInstanceEntity.getFlowInstanceId(), actualEntityList.get(0).getFlowInstanceId());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);

        Collection<Long> ids = Arrays.asList(1L);

        List<NodeInstanceEntity> expectedEntityList = Arrays.asList(nodeInstanceEntity);

        Mockito.when(mockNodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedEntityList);

        List<NodeInstanceEntity> actualEntityList = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(actualEntityList);
        assertEquals(1, actualEntityList.size());
        assertEquals(nodeInstanceEntity.getId(), actualEntityList.get(0).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Mockito.when(mockNodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        Mockito.when(mockNodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

        assertEquals(1, result);
    }

}