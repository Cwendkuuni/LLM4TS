import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository;

    @Before
    public void setUp() {
        repository = new NodeInstanceEntityRepository();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Test normal case
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_A);
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Test edge case with null values
        List<NodeInstanceEntity> resultNull = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
        assertNotNull(resultNull);
        assertEquals(0, resultNull.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Test normal case
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Test edge case with invalid flowInstanceId
        List<NodeInstanceEntity> resultInvalid = repository.findByFlowInstanceId(-1L);
        assertNotNull(resultInvalid);
        assertEquals(0, resultInvalid.size());
    }

    @Test
    public void testFindByIds() {
        // Test normal case
        List<NodeInstanceEntity> result = repository.findByIds(List.of(1L, 2L, 3L));
        assertNotNull(result);
        assertEquals(3, result.size());
        
        // Test edge case with empty id list
        List<NodeInstanceEntity> resultEmpty = repository.findByIds(Collections.emptyList());
        assertNotNull(resultEmpty);
        assertEquals(0, resultEmpty.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Test normal case
        int result = repository.deleteByFlowInstanceId(1L);
        assertEquals(2, result);
        
        // Test edge case with invalid flowInstanceId
        int resultInvalid = repository.deleteByFlowInstanceId(-1L);
        assertEquals(0, resultInvalid);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Test normal case
        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);
        assertEquals(1, result);
        
        // Test edge case with invalid instanceId and instanceType
        int resultInvalid = repository.deleteByInstanceIdAndInstanceType(-1L, FlowNodeType.TYPE_B);
        assertEquals(0, resultInvalid);
    }
}