package com.oceanbase.odc.metadb.flow;
import org.junit.Test;
import org.mockito.Mock;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeRepo;

    @Mock
    private NodeInstanceEntity node;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeRepo.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Collections.singletonList(node));

        List<NodeInstanceEntity> actualNodeList = nodeRepo.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.TASK);
        assertThat(actualNodeList, is(equalTo(Collections.singletonList(node))));
    }

    // similar tests for findByFlowInstanceId, findByIds, deleteByFlowInstanceId, and deleteByInstanceIdAndInstanceType
    
}