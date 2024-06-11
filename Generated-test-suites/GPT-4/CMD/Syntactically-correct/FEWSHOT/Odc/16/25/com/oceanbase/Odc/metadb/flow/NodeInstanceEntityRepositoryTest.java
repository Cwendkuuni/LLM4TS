package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.Before;

@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    EntityManager em;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        when(em.createQuery(any(String.class))).thenReturn(Arrays.asList(entity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.USER_TASK);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        when(em.createQuery(any(String.class))).thenReturn(Arrays.asList(entity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        when(em.createQuery(any(String.class))).thenReturn(Arrays.asList(entity));
        List<Long> ids = LongStream.range(1, 10).boxed().collect(Collectors.toList());
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(em.createQuery(any(String.class))).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(em.createQuery(any(String.class))).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.SERVICE_TASK);
        assertEquals(1, result);
    }

}