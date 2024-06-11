import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDatasetTest {
    
    private DefaultDataset dataset;
    
    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClear() {
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
    }
    
    // Test for addAll method
    
    // Test for addAll method with empty collection
    @Test
    public void testAddAll_emptyCollection() {
        assertFalse(dataset.addAll(new ArrayList<>()));
        assertEquals(0, dataset.size());
    }
    
    // Test for addAll method with non-empty collection
    @Test
    public void testAddAll_nonEmptyCollection() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        List<Instance> collection = new ArrayList<>();
        collection.add(instance1);
        collection.add(instance2);
        assertTrue(dataset.addAll(collection));
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }
    
    // Test for addAll method with collection containing instances with classValue
    @Test
    public void testAddAll_collectionWithClassValue() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instance1.setClassValue("A");
        instance2.setClassValue("B");
        List<Instance> collection = new ArrayList<>();
        collection.add(instance1);
        collection.add(instance2);
        assertTrue(dataset.addAll(collection));
        assertTrue(dataset.classes().contains("A"));
        assertTrue(dataset.classes().contains("B"));
    }
    
    // Test for addAll method with collection containing instances with maximum attributes
    @Test
    public void testAddAll_collectionWithMaxAttributes() {
        Instance instance1 = new Instance();
        instance1.addAttribute(new Attribute("a"));
        Instance instance2 = new Instance();
        instance2.addAttribute(new Attribute("a"));
        instance2.addAttribute(new Attribute("b"));
        List<Instance> collection = new ArrayList<>();
        collection.add(instance1);
        collection.add(instance2);
        assertTrue(dataset.addAll(collection));
        assertEquals(2, dataset.noAttributes());
    }
    
    // Other test methods...
    
}