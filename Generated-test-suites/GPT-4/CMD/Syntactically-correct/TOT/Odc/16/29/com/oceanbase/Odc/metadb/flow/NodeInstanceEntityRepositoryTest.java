package com.oceanbase.odc.metadb.flow;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepository repo;

    @Mock
    List<NodeInstanceEntity> mockList;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = mock(FlowNodeType.class);
        FlowableElementType flowableElementType = mock(FlowableElementType.class);
        
        repo.findByInstanceIdAndInstanceTypeAndFlowableElementType(
            instanceId, instanceType, flowableElementType
        );
        verify(mockList, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(
            instanceId, instanceType, flowableElementType
        );
    }

    // more tests for other methods in the interface here
}