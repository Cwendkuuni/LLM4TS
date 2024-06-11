
import org.junit.*;
import static org.junit.Assert.*;

public class DefaultDatasetTest {
    
    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        DefaultDataset dataset = new DefaultDataset(instances);
        
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testDefaultDatasetEmptyConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        
        assertTrue(dataset.addAll(instances));
        assertEquals(2, dataset.size());
    }
    
    // Test other public methods...
    
}