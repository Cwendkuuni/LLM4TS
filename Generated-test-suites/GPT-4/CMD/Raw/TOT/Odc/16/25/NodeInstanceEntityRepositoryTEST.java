import org.junit.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import java.util.Arrays;
    import java.util.List;

    import static org.junit.Assert.*;

    public class NodeInstanceEntityRepositoryTest {

        @Autowired
        private NodeInstanceEntityRepository nodeInstanceEntityRepository;

        @Test
        public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
            // Arrange
            Long instanceId = 1L;
            FlowNodeType instanceType = FlowNodeType.SOME_TYPE;
            FlowableElementType flowableElementType = FlowableElementType.SOME_TYPE;

            // Act
            List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

            // Assert
            assertNotNull(nodeInstanceEntities);
            // Add more assertions based on the expected results
        }

        @Test
        public void testFindByFlowInstanceId() {
            // Arrange
            Long flowInstanceId = 1L;

            // Act
            List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

            // Assert
            assertNotNull(nodeInstanceEntities);
            // Add more assertions based on the expected results
        }

        @Test
        public void testFindByIds() {
            // Arrange
            List<Long> ids = Arrays.asList(1L, 2L, 3L);

            // Act
            List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

            // Assert
            assertNotNull(nodeInstanceEntities);
            // Add more assertions based on the expected results
        }

        @Test
        public void testDeleteByFlowInstanceId() {
            // Arrange
            Long instanceId = 1L;

            // Act
            int deleteCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

            // Assert
            assertTrue(deleteCount > 0);
            // Add more assertions based on the expected results
        }

        @Test
        public void testDeleteByInstanceIdAndInstanceType() {
            // Arrange
            Long instanceId = 1L;
            FlowNodeType instanceType = FlowNodeType.SOME_TYPE;

            // Act
            int deleteCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

            // Assert
            assertTrue(deleteCount > 0);
            // Add more assertions based on the expected results
        }
    }