
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @MockBean
    NodeInstanceEntityRepository nodeInstanceRepository;

    @MockBean
    NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        
        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(expected);
        
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SEQUENCE_FLOW, FlowableElementType.BOUNDARY_EVENT);
        
        assertThat(actual, is(equalTo(expected)));

    }

    @Test
    public void testFindByFlowInstanceId() {
        
        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceRepository.findByFlowInstanceId(anyLong())).thenReturn(expected);
        
        List<NodeInstanceEntity> actual = nodeInstanceRepository.findByFlowInstanceId(1L);
        
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testFindByIds() {
        
        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceRepository.findByIds(any(Collection.class))).thenReturn(expected);
        
        List<NodeInstanceEntity> actual = nodeInstanceRepository.findByIds(Arrays.asList(1L, 2L));
        
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        
        when(nodeInstanceRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);
        
        int deleteCount = nodeInstanceRepository.deleteByFlowInstanceId(1L);
    
        assertThat(deleteCount, is(equalTo(1)));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        
        when(nodeInstanceRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class))).thenReturn(1);
        
        int deleteCount = nodeInstanceRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.SEQUENCE_FLOW);

        assertThat(deleteCount, is(equalTo(1)));
    }
}
