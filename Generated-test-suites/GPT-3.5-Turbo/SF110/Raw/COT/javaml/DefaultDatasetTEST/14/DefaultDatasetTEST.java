import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {

    /** Test case for DefaultDataset(Collection<Instance> coll) constructor */
    @Test
    public void testConstructorWithCollection() {
        // Create a collection of instances
        Collection<Instance> instances = new ArrayList<Instance>();
        instances.add(new Instance());
        instances.add(new Instance());
        
        // Create a DefaultDataset using the collection
        DefaultDataset dataset = new DefaultDataset(instances);
        
        // Check if the instances are added correctly
        assertEquals(instances.size(), dataset.size());
    }
    
    /** Test case for DefaultDataset() constructor */
    @Test
    public void testConstructorWithoutCollection() {
        // Create a DefaultDataset without any instances
        DefaultDataset dataset = new DefaultDataset();
        
        // Check if the dataset is empty
        assertEquals(0, dataset.size());
    }
    
    /** Test case for addAll(Collection<? extends Instance> c) method */
    @Test
    public void testAddAll() {
        // Create a DefaultDataset
        DefaultDataset dataset = new DefaultDataset();
        
        // Create a collection of instances
        Collection<Instance> instances = new ArrayList<Instance>();
        instances.add(new Instance());
        instances.add(new Instance());
        
        // Add the instances to the dataset
        dataset.addAll(instances);
        
        // Check if the instances are added correctly
        assertEquals(instances.size(), dataset.size());
    }
    
    // Add more test cases for other public methods
    
    

    // Test case for add(Instance e) method
    @Test
    public void testAdd() {
        // Create a DefaultDataset
        DefaultDataset dataset = new DefaultDataset();
        
        // Create an instance
        Instance instance = new Instance();
        
        // Add the instance to the dataset
        dataset.add(instance);
        
        // Check if the instance is added correctly
        assertEquals(1, dataset.size());
    }
    
    // Test case for clear() method
    @Test
    public void testClear() {
        // Create a DefaultDataset
        DefaultDataset dataset = new DefaultDataset();
        
        // Create instances and add them to the dataset
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        // Clear the dataset
        dataset.clear();
        
        // Check if the dataset is empty
        assertEquals(0, dataset.size());
    }
    
    // Test case for instance(int index) method
    @Test
    public void testInstance() {
        // Create a DefaultDataset
        DefaultDataset dataset = new DefaultDataset();
        
        // Create instances and add them to the dataset
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        
        // Get the instances from the dataset
        Instance retrievedInstance1 = dataset.instance(0);
        Instance retrievedInstance2 = dataset.instance(1);
        
        // Check if the instances are retrieved correctly
        assertEquals(instance1, retrievedInstance1);
        assertEquals(instance2, retrievedInstance2);
    }
    
    // Test case for classes() method
    @Test
    public void testClasses() {
        // Create a DefaultDataset
        DefaultDataset dataset = new DefaultDataset();
        
        // Create instances with different classes and add them to the dataset
        Instance instance1 = new Instance("class1");
        Instance instance2 = new Instance("class2");
        Instance instance3 = new Instance("class1");
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        
        // Get the unique classes from the dataset
        SortedSet<Object> classes = dataset.classes();
        
        // Check if the unique classes are retrieved correctly
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
    }
    
    // Test case for kNearest(int k, Instance inst, DistanceMeasure dm) method
    @Test
    public void testKNearest() {
        // Create a DefaultDataset
        DefaultDataset dataset = new DefaultDataset();
        
        // Create instances and add them to the dataset
        Instance instance1 = new Instance(new double[]{1.0});
        Instance instance2 = new Instance(new double[]{2.0});
        Instance instance3 = new Instance(new double[]{3.0});
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        
        // Create a test instance
        Instance testInstance = new Instance(new double[]{4.0});
        
        // Create a distance measure (e.g., EuclideanDistance)
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        
        // Get the k-nearest instances to the test instance
        Set<Instance> nearestInstances = dataset.kNearest(2, testInstance, distanceMeasure);
        
        // Check if the k-nearest instances are retrieved correctly
        assertEquals(2, nearestInstances.size());
        assertTrue(nearestInstances.contains(instance2));
        assertTrue(nearestInstances.contains(instance3));
    }
    
    // Test case for copy() method
    @Test
    public void testCopy() {
        // Create a DefaultDataset
        DefaultDataset dataset = new DefaultDataset();
        
        // Create instances and add them to the dataset
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        
        // Create a copy of the dataset
        Dataset copy = dataset.copy();
        
        // Check if the copy contains the same instances
        assertEquals(dataset.size(), copy.size());
        for (int i = 0; i < dataset.size(); i++) {
            assertEquals(dataset.instance(i), copy.instance(i));
        }
    }
    
    
}