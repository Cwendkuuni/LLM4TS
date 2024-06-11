import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDatasetTest {
    
    private DefaultDataset dataset;
    
    @Before
    public void setUp()
    {
        dataset = new DefaultDataset();
    }
    
    @Test
    public void testDefaultConstructor()
    {
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }
    
    @Test
    public void testAdd()
    {
        Instance instance = new Instance();
        
        dataset.add(instance);
        
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
        assertTrue(dataset.contains(instance));
    }
    
    @Test
    public void testAddAtIndex()
    {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(0, instance2);
        
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
        assertTrue(dataset.get(0).equals(instance2));
        assertTrue(dataset.get(1).equals(instance1));
    }
    
    @Test
    public void testAddAll()
    {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<>();
        collection.add(instance1);
        collection.add(instance2);
        
        dataset.addAll(collection);
        
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }
    
    @Test
    public void testAddAllAtIndex()
    {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<>();
        collection.add(instance2);
        
        dataset.add(instance1);
        dataset.addAll(0, collection);
        
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
        assertTrue(dataset.get(0).equals(instance2));
        assertTrue(dataset.get(1).equals(instance1));
    }
    
    // continue with tests for the remaining methods
    
    // ...
    
    // ...
    
    // ...
}