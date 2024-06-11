package com.oceanbase.odc.metadb.flow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    @Mock
    private NodeInstanceEntity nodeInstanceEntityMock;

    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
        when(nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null, null)).thenReturn(Arrays.asList(nodeInstanceEntityMock));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null, null);
        assertEquals(1, result.size());
    }

    @Test
    public void findByFlowInstanceIdTest() {
        when(nodeInstanceEntityRepositoryMock.findByFlowInstanceId(1L)).thenReturn(Arrays.asList(nodeInstanceEntityMock));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryMock.findByFlowInstanceId(1L);
        assertEquals(1, result.size());
    }

    @Test
    public void findByIdsTest() {
        when(nodeInstanceEntityRepositoryMock.findByIds(Arrays.asList(1L))).thenReturn(Arrays.asList(nodeInstanceEntityMock));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryMock.findByIds(Arrays.asList(1L));
        assertEquals(1, result.size());
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        when(nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(1L)).thenReturn(1);
        int result = nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void deleteByInstanceIdAndInstanceTypeTest() {
        when(nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(1L, null)).thenReturn(1);
        int result = nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(1L, null);
        assertEquals(1, result);
    }

}