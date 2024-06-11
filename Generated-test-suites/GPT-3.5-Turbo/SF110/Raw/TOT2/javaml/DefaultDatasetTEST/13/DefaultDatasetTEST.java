import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;

public class DefaultDatasetTest {
    
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        assertTrue(dataset.addAll(instances));
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.clear();
        
        assertEquals(0, dataset.size());
    }
    
    // Add more individual test cases for other methods
    
    @Test
    public void testAddNullInstance() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertFalse(dataset.add(null));
    }
    
    @Test
    public void testInstanceOutOfBounds() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertNull(dataset.instance(100));
    }
    
    @Test
    public void testClassIndexWithNullClass() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertEquals(-1, dataset.classIndex(null));
    }
    
    // Add more edge cases and error scenarios tests
    
}