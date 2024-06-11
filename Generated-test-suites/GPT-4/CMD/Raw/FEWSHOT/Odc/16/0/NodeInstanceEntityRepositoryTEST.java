package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @MockBean
    private NodeInstanceEntityRepository repository;

    @Mock
    private NodeInstanceEntity node;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodes = Arrays.asList(node);
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT))
                .thenReturn(nodes);
        Assert.assertEquals(nodes, repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodes = Arrays.asList(node);
        Mockito.when(repository.findByFlowInstanceId(1L)).thenReturn(nodes);
        Assert.assertEquals(nodes, repository.findByFlowInstanceId(1L));
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodes = Arrays.asList(node);
        Mockito.when(repository.findByIds(Arrays.asList(1L))).thenReturn(nodes);
        Assert.assertEquals(nodes, repository.findByIds(Arrays.asList(1L)));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(repository.deleteByFlowInstanceId(1L)).thenReturn(1);
        Assert.assertEquals(1, repository.deleteByFlowInstanceId(1L));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(repository.deleteByInstanceIdAndInstanceType(1L,FlowNodeType.START_EVENT)).thenReturn(1);
        Assert.assertEquals(1, repository.deleteByInstanceIdAndInstanceType(1L,FlowNodeType.START_EVENT));
    }
}