import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        // Create test instances
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> coll = new ArrayList<>();
        coll.add(instance1);
        coll.add(instance2);
        
        // Create DefaultDataset object
        DefaultDataset dataset = new DefaultDataset(coll);
        
        // Assertions
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }

    @Test
    public void testDefaultDatasetConstructorNoArgs() {
        // Create DefaultDataset object
        DefaultDataset dataset = new DefaultDataset();
        
        // Assertions
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddAll() {
        // Create test instances
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> coll = new ArrayList<>();
        coll.add(instance1);
        coll.add(instance2);
        
        // Create DefaultDataset object
        DefaultDataset dataset = new DefaultDataset();
        
        // Add instances to dataset
        dataset.addAll(coll);
        
        // Assertions
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }

    // Add test methods for other methods in DefaultDataset class

}

