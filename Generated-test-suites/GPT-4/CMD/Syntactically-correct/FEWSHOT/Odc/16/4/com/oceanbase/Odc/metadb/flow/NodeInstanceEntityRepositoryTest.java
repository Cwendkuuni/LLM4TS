package com.oceanbase.odc.metadb.flow;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@DataJpaTest
@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository repository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> mockedNodeEntities = new ArrayList<>();
        mockedNodeEntities.add(nodeInstanceEntity);
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(mockedNodeEntities);

        List<NodeInstanceEntity> nodeEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.ACTIVITY);
        Assert.assertEquals(mockedNodeEntities, nodeEntities);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> mockedNodeEntities = new ArrayList<>();
        mockedNodeEntities.add(nodeInstanceEntity);
        when(repository.findByFlowInstanceId(anyLong())).thenReturn(mockedNodeEntities);

        List<NodeInstanceEntity> nodeEntities = repository.findByFlowInstanceId(1L);
        Assert.assertEquals(mockedNodeEntities, nodeEntities);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> mockedNodeEntities = new ArrayList<>();
        mockedNodeEntities.add(nodeInstanceEntity);
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        when(repository.findByIds(ids)).thenReturn(mockedNodeEntities);

        List<NodeInstanceEntity> nodeEntities = repository.findByIds(ids);
        Assert.assertEquals(mockedNodeEntities, nodeEntities);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(repository.deleteByFlowInstanceId(anyLong())).thenReturn(new Random().nextInt());

        int deletedRecords = repository.deleteByFlowInstanceId(1L);
        Assert.assertTrue(deletedRecords >= 0);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(repository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class))).thenReturn(new Random().nextInt());

        int deletedRecords = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        Assert.assertTrue(deletedRecords >= 0);
    }
}