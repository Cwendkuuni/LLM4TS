package com.oceanbase.odc.metadb.flow;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepositoryImpl nodeInstanceEntityRepository;

    @Mock
    NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(Arrays.asList(nodeInstanceEntity));
    }

    @Test
    public void testFindNodeInstance() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        Mockito.verify(nodeInstanceEntityRepository).findByFlowInstanceId(1L);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), nodeInstanceEntity);
    }

    // Other tests here
}