
package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntityMock;

    private static final Long INSTANCE_ID = 1L;
    private static final FlowNodeType INSTANCE_TYPE = FlowNodeType.BETWEEN_INSTANCE;
    private static final FlowableElementType FLOWABLE_ELEMENT_TYPE = FlowableElementType.FLOW_INSTANCE;
    private static final List<Long> IDS = Arrays.asList(1L, 2L, 3L, 4L);

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, INSTANCE_TYPE, FLOWABLE_ELEMENT_TYPE);
        verify(nodeInstanceEntityMock).findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, INSTANCE_TYPE, FLOWABLE_ELEMENT_TYPE);
    }

    @Test
    public void testFindByFlowInstanceId() {
        nodeInstanceEntityRepository.findByFlowInstanceId(INSTANCE_ID);
        verify(nodeInstanceEntityMock).findByFlowInstanceId(INSTANCE_ID);
    }

    @Test
    public void testFindByIds() {
        nodeInstanceEntityRepository.findByIds(IDS);
        verify(nodeInstanceEntityMock).findByIds(IDS);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(INSTANCE_ID);
        verify(nodeInstanceEntityMock).deleteByFlowInstanceId(INSTANCE_ID);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(INSTANCE_ID, INSTANCE_TYPE);
        verify(nodeInstanceEntityMock).deleteByInstanceIdAndInstanceType(INSTANCE_ID, INSTANCE_TYPE);
    }
}
