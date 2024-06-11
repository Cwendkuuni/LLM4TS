import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {
    // Test the constructor with a collection of instances
    @Test
    public void testConstructorCollection() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        DefaultDataset dataset = new DefaultDataset(instances);
        
        assertEquals(2, dataset.size());
    }
    
    // Test the default constructor
    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertEquals(0, dataset.size());
    }
    
    // Test the addAll method
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        
        assertTrue(dataset.addAll(instances));
        assertEquals(2, dataset.size());
    }
    
    // Test the addAll method at a specific index
    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        
        assertTrue(dataset.addAll(0, instances));
        assertEquals(2, dataset.size());
    }
    
    // Test the clear method
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        dataset.clear();
        assertEquals(0, dataset.size());
    }
    
    // Test the add method
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
    }
    
    // Test the add method at a specific index
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.add(0, instance);
        assertEquals(1, dataset.size());
    }
    
    // Test the addElement method
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
    }
    
    // Test the insertElementAt method
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
    }
    
    // Test the setElementAt method
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.setElementAt(instance, 0);
        assertEquals(instance, dataset.instance(0));
    }
    
    // Test the instance method
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        
        assertEquals(instance, dataset.instance(0));
    }
    
    // Test the classes method
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        
        SortedSet<Object> classes = dataset.classes();
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
    }
    
    // Test the kNearest method
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setAttribute(0, 1.0);
        Instance instance2 = new Instance();
        instance2.setAttribute(0, 2.0);
        Instance instance3 = new Instance();
        instance3.setAttribute(0, 3.0);
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        
        Set<Instance> nearest = dataset.kNearest(2, instance1, new EuclideanDistance());
        assertTrue(nearest.contains(instance2));
        assertTrue(nearest.contains(instance3));
    }
    
    // Test the removeFarthest method
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setAttribute(0, 1.0);
        Instance instance2 = new Instance();
        instance2.setAttribute(0, 2.0);
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(instance1, 1.0);
        vector.put(instance2, 2.0);
        
        double max = dataset.removeFarthest(vector);
        assertEquals(2.0, max, 0.001);
        assertFalse(vector.containsKey(instance2));
    }
    
    // Test the folds method
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        
        Dataset[] folds = dataset.folds(2, new Random());
        assertEquals(2, folds.length);
    }
    
    // Test the noAttributes method
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        instance.setAttribute(0, 1.0);
        dataset.add(instance);
        
        assertEquals(1, dataset.noAttributes());
    }
    
    // Test the classIndex method
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals(0, dataset.classIndex("class1"));
        assertEquals(1, dataset.classIndex("class2"));
    }
    
    // Test the classValue method
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
    }
    
    // Test the copy method
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        
        Dataset copy = dataset.copy();
        assertNotSame(copy, dataset);
        assertEquals(dataset.size(), copy.size());
    }
}