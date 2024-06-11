import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private JpaRepository<NodeInstanceEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<NodeInstanceEntity> jpaSpecificationExecutor;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Arrange
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT1;

        // Act
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Assert
        assertNotNull(result);
        // Add additional assertions based on the expected behavior
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Arrange
        Long flowInstanceId = 1L;

        // Act
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Assert
        assertNotNull(result);
        // Add additional assertions based on the expected behavior
    }

    @Test
    public void testFindByIds() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // Act
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        // Assert
        assertNotNull(result);
        // Add additional assertions based on the expected behavior
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Arrange
        Long instanceId = 1L;

        // Act
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        // Assert
        assertTrue(result >= 0);
        // Add additional assertions based on the expected behavior
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Arrange
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;

        // Act
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Assert
        assertTrue(result >= 0);
        // Add additional assertions based on the expected behavior
    }
}