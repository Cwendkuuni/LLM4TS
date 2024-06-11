import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.test.context.junit4.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository repository;
    
    // Entities and data set up here...
    // ...
    
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Prepare test data...

        List<NodeInstanceEntity> entities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Check results...
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Prepare test data...

        List<NodeInstanceEntity> entities = repository.findByFlowInstanceId(instanceId);

        // Check results...
    }

    @Test
    public void testFindByIds() {
        // Prepare test data...

        List<NodeInstanceEntity> entities = repository.findByIds(ids);

        // Check results...
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Prepare test data...

        int deletedEntities = repository.deleteByFlowInstanceId(instanceId);

        // Check results...
    }
    
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Prepare test data...

        int deletedEntities = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Check results...
    }

    // Include edge cases and error scenarios...
}