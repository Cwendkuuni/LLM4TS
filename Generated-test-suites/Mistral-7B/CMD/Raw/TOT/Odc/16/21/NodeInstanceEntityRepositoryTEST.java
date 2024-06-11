package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private static final long FLOW_INSTANCE_ID = 1L;
    private static final Long INSTANCE_ID = 1L;
    private static final FlowNodeType INSTANCE_TYPE = FlowNodeType.START_EVENT;
    private static final FlowableElementType FLOWABLE_ELEMENT_TYPE = FlowableElementType.PROCESS;
    private static final long SECOND_INSTANCE_ID = 2L;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(INSTANCE_ID);
        nodeInstanceEntity1.setInstanceType(INSTANCE_TYPE);
        nodeInstanceEntity1.setFlowInstanceId(FLOW_INSTANCE_ID);
        nodeInstanceEntity1.setFlowableElementType(FLOWABLE_ELEMENT_TYPE);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(SECOND_INSTANCE_ID);
        nodeInstanceEntity2.setInstanceType(INSTANCE_TYPE);
        nodeInstanceEntity2.setFlowInstanceId(FLOW_INSTANCE_ID);
        nodeInstanceEntity2.setFlowableElementType(FLOWABLE_ELEMENT_TYPE);

        Mockito.when(nodeInstanceEntityRepository.findAll())
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(FLOW_INSTANCE_ID, INSTANCE_TYPE, FLOWABLE_ELEMENT_TYPE);

        Assertions.assertEquals(2, result.size());
        Assertions.assertAll(() -> Assertions.assertEquals(INSTANCE_ID, result.get(0).getInstanceId()),
                () -> Assertions.assertEquals(SECOND_INSTANCE_ID, result.get(1).getInstanceId()));
    }

    @Test
    public void testFindByFlowInstanceId() {
        SimpleJpaRepository repository = Mockito.mock(SimpleJpaRepository.class);
        NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock = Mockito.spy(new NodeInstanceEntityRepository() {
            {
                this.entityInformation = repository.getEntityInformation(NodeInstanceEntity.class);
            }
        });

        Mockito.when(repository.findAll())
                .thenReturn(Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity(), new NodeInstanceEntity()));

        Pageable pageable = Mockito.mock(Pageable.class);
        Page<NodeInstanceEntity> page = new PageImpl<>(Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity()));

        Mockito.when(nodeInstanceEntityRepositoryMock.findAll(pageable))
                .thenReturn(page);

        Mockito.when(nodeInstanceEntityRepositoryMock.findByFlowInstanceId(FLOW_INSTANCE_ID))
                .thenCallRealMethod();

        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryMock.findByFlowInstanceId(FLOW_INSTANCE_ID);

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(INSTANCE_ID);
        nodeInstanceEntity1.setInstanceType(INSTANCE_TYPE);
        nodeInstanceEntity1.setFlowInstanceId(FLOW_INSTANCE_ID);
        nodeInstanceEntity1.setFlowableElementType(FLOWABLE_ELEMENT_TYPE);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(SECOND_INSTANCE_ID);
        nodeInstanceEntity2.setInstanceType(INSTANCE_TYPE);
        nodeInstanceEntity2.setFlowInstanceId(FLOW_INSTANCE_ID);
        nodeInstanceEntity2.setFlowableElementType(FLOWABLE_ELEMENT_TYPE);

        Collection<Long> ids = Arrays.asList(INSTANCE_ID, SECOND_INSTANCE_ID);

        Mockito.when(nodeInstanceEntityRepository.findAll())
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        Assertions.assertEquals(2, result.size());
        Assertions.assertAll(() -> Assertions.assertEquals(INSTANCE_ID, result.get(0).getInstanceId()),
                () -> Assertions.assertEquals(SECOND_INSTANCE_ID, result.get(1).getInstanceId()));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(INSTANCE_ID);
        nodeInstanceEntity.setInstanceType(INSTANCE_TYPE);
        nodeInstanceEntity.setFlowInstanceId(FLOW_INSTANCE_ID);
        nodeInstanceEntity.setFlowableElementType(FLOWABLE_ELEMENT_TYPE);

        Mockito.when(nodeInstanceEntityRepository.findAll())
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(FLOW_INSTANCE_ID);

        Assertions.assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(INSTANCE_ID);
        nodeInstanceEntity.setInstanceType(INSTANCE_TYPE);
        nodeInstanceEntity.setFlowInstanceId(FLOW_INSTANCE_ID);
        nodeInstanceEntity.setFlowableElementType(FLOWABLE_ELEMENT_TYPE);

        Mockito.when(nodeInstanceEntityRepository.findAll())
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(INSTANCE_ID, INSTANCE_TYPE);

        Assertions.assertEquals(1, result);
    }

    @Test
    public void testFindEmptyListByFlowInstanceId() {
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);

        Mockito.when(repository.findByFlowInstanceId(FLOW_INSTANCE_ID))
                .thenReturn(new ArrayList<>());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(FLOW_INSTANCE_ID);

        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void testDeleteEmptyListByFlowInstanceId() {
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);

        Mockito.when(repository.findAll())
                .thenReturn(Collections.emptyList());

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(FLOW_INSTANCE_ID);

        Assertions.assertEquals(0, result);
    }
}