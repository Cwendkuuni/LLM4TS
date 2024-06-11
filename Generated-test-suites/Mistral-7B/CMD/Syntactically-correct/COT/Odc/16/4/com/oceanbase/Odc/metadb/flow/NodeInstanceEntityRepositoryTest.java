import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowInstance;

@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expected = List.of(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType));
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expected = List.of(new NodeInstanceEntity(1L, 1L, FlowNodeType.START, null), new NodeInstanceEntity(2L, 1L, FlowNodeType.END, null));
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindByIds() {
        Collection<Long> ids = List.of(1L, 2L);
        List<NodeInstanceEntity> expected = List.of(new NodeInstanceEntity(1L, null, null, null), new NodeInstanceEntity(2L, null, null, null));
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(ids);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expected = 2;
        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expected = 1;
        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<NodeInstanceEntity> expected = new PageImpl<>(List.of(new NodeInstanceEntity(1L, null, null, null), new NodeInstanceEntity(2L, null, null, null)), pageable, 2);
        Page<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findAll(pageable);
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    void testSave() {
        NodeInstanceEntity entity = new NodeInstanceEntity(null, new FlowInstance(1L), FlowNodeType.START, null);
        nodeInstanceEntityRepository.save(entity);
        NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.findById(entity.getId()).orElseThrow(Executable. throwing(() -> new RuntimeException("Entity not found")));
        Assertions.assertEquals(entity, savedEntity);
    }

    @Test
    @Transactional
    void testDeleteByFlowInstanceId_Empty() {
        Long instanceId = 1L;
        int expected = 0;
        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDeleteByInstanceIdAndInstanceType_NullInstanceId() {
        FlowNodeType instanceType = FlowNodeType.START;
        int expected = 0;
        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, instanceType);
        Assertions.assertEquals(expected, actual);
    }

}