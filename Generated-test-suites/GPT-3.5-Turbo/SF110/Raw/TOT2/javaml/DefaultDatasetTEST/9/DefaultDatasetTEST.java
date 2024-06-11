import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultDatasetTest {

    // Test for DefaultDataset(Collection<Instance> coll) constructor
    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        // Create a collection of instances
        Collection<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        // Create a DefaultDataset object
        DefaultDataset dataset = new DefaultDataset(instances);
        
        // Check if instances are added to the dataset correctly
        assertEquals(2, dataset.size());
    }
    
    // Test for addAll method
    @Test
    public void testAddAll() {
        // Create a DefaultDataset object
        DefaultDataset dataset = new DefaultDataset();
        
        // Create a collection of instances
        Collection<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        instances.add(instance1);
        
        // Add instances to the dataset
        dataset.addAll(instances);
        
        // Check if instances are added to the dataset correctly
        assertEquals(1, dataset.size());
    }
    
    // Add more test cases for other methods as per the requirements
    
}