import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import java.util.Arrays;

public class DefaultDatasetTest {
    private DefaultDataset dataset;
    
    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> coll = Arrays.asList(instance1, instance2);
        
        dataset = new DefaultDataset(coll);
        
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testDefaultDatasetConstructor() {
        dataset = new DefaultDataset();
        
        assertNotNull(dataset);
    }
    
    @Test
    public void testAddAllToDataset() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> coll = Arrays.asList(instance1, instance2);
        
        dataset = new DefaultDataset();
        assertTrue(dataset.addAll(coll));
    }
    
    // Additional test cases for other methods can be added here
    
}