package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import org.mockito.*;

public class NodeInstanceEntityRepositoryTest {
    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Normal scenario tests
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null, null);
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null, null);
    }

    @Test
    public void testFindByFlowInstanceId() {
        nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(1)).findByFlowInstanceId(1L);
    }

    @Test
    public void testFindByIds() {
        nodeInstanceEntityRepository.findByIds(Collections.singleton(1L));
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(1)).findByIds(Collections.singleton(1L));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(1)).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(1)).deleteByInstanceIdAndInstanceType(1L, null);
    }

    // Edge case tests
    @Test(expected = NullPointerException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNull() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFindByFlowInstanceIdWithNull() {
        nodeInstanceEntityRepository.findByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdsWithEmptyCollection() {
        nodeInstanceEntityRepository.findByIds(new ArrayList<>());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByFlowInstanceIdWithNull() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNull() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, null);
    }
}