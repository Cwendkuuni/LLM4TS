package com.oceanbase.odc.metadb.flow;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityService nodeInstanceEntityService;

    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setUp() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setFlowInstanceId(2L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.CLASSIFIER);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);
    }

    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(), any(), any())).thenReturn(Arrays.asList(nodeInstanceEntity));
        List<NodeInstanceEntity> entities = nodeInstanceEntityService.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.CLASSIFIER, FlowableElementType.TASK);
        Assert.assertNotEquals(0, entities.size());
    }

    @Test
    public void findByFlowInstanceIdTest() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(any())).thenReturn(Arrays.asList(nodeInstanceEntity));
        List<NodeInstanceEntity> entities = nodeInstanceEntityService.findByFlowInstanceId(2L);
        Assert.assertNotEquals(0, entities.size());
    }

    @Test
    public void findByIdsTest() {
        when(nodeInstanceEntityRepository.findByIds(any())).thenReturn(Arrays.asList(nodeInstanceEntity));
        List<NodeInstanceEntity> entities = nodeInstanceEntityService.findByIds(Arrays.asList(1L, 2L));
        Assert.assertNotEquals(0, entities.size());
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(any())).thenReturn(1);
        Integer result = nodeInstanceEntityService.deleteByFlowInstanceId(2L);
        Assert.assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void deleteByInstanceIdAndInstanceTypeTest() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(any(), any())).thenReturn(1);
        Integer result = nodeInstanceEntityService.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.CLASSIFIER);
        Assert.assertEquals(Integer.valueOf(1), result);
    }

}