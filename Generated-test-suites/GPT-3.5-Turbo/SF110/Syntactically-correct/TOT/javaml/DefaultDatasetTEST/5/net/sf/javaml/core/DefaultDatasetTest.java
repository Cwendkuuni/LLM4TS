import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {

    // Test case for the first constructor
    @Test
    public void testConstructor1() {
        ArrayList<Instance> instances = new ArrayList<Instance>();
        instances.add(new Instance());
        instances.add(new Instance());
        
        DefaultDataset dataset = new DefaultDataset(instances);
        
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertTrue(dataset.classes().isEmpty());
    }
    
    // Test case for the second constructor
    @Test
    public void testConstructor2() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertTrue(dataset.classes().isEmpty());
    }
    
    // Test case for addAll method
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        ArrayList<Instance> instances = new ArrayList<Instance>();
        instances.add(new Instance());
        instances.add(new Instance());
        
        assertTrue(dataset.addAll(instances));
        
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertTrue(dataset.classes().isEmpty());
    }
    
    // Test case for addAll method with index
    @Test
    public void testAddAllWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        ArrayList<Instance> instances = new ArrayList<Instance>();
        instances.add(new Instance());
        instances.add(new Instance());
        
        assertTrue(dataset.addAll(0, instances));
        
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertTrue(dataset.classes().isEmpty());
    }
    
    // Test case for clear method
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        dataset.clear();
        
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertTrue(dataset.classes().isEmpty());
    }
    
    // Test case for add method
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        assertTrue(dataset.add(instance));
        
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertEquals(1, dataset.classes().size());
    }
    
    // Test case for add method with index
    @Test
    public void testAddWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(0, instance2);
        
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertEquals(1, dataset.classes().size());
    }
    
    // Test case for addElement method
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.addElement(instance);
        
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertEquals(1, dataset.classes().size());
    }
    
    // Test case for insertElementAt method
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.insertElementAt(instance1, 0);
        dataset.insertElementAt(instance2, 1);
        
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertEquals(1, dataset.classes().size());
    }
    
    // Test case for setElementAt method
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.setElementAt(instance2, 0);
        
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertEquals(1, dataset.classes().size());
    }
    
    // Test case for instance method
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.add(instance);
        
        assertEquals(instance, dataset.instance(0));
    }
    
    // Test case for classes method
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(0));
        dataset.add(new Instance(1));
        dataset.add(new Instance(1));
        dataset.add(new Instance(2));
        dataset.add(new Instance(2));
        
        SortedSet<Object> classes = dataset.classes();
        
        assertEquals(3, classes.size());
        assertEquals(0, classes.first());
        assertEquals(2, classes.last());
    }
    
    // Test case for kNearest method
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(0));
        dataset.add(new Instance(1));
        dataset.add(new Instance(2));
        Instance instance = new Instance(1);
        
        DistanceMeasure dm = new EuclideanDistance();
        
        Set<Instance> nearest = dataset.kNearest(2, instance, dm);
        
        assertEquals(2, nearest.size());
    }
    
    // Test case for removeFarthest method
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance(0);
        Instance instance2 = new Instance(1);
        Instance instance3 = new Instance(2);
        Instance instance4 = new Instance(3);
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        dataset.add(instance4);
        
        HashMap<Instance, Double> vector = new HashMap<Instance, Double>();
        vector.put(instance1, 1.0);
        vector.put(instance2, 2.0);
        vector.put(instance3, 3.0);
        vector.put(instance4, 4.0);
        
        double max = dataset.removeFarthest(vector);
        
        assertEquals(4.0, max, 0.001);
        assertEquals(3, vector.size());
    }
    
    // Test case for folds method
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(0));
        dataset.add(new Instance(1));
        dataset.add(new Instance(2));
        dataset.add(new Instance(3));
        dataset.add(new Instance(4));
        dataset.add(new Instance(5));
        dataset.add(new Instance(6));
        dataset.add(new Instance(7));
        dataset.add(new Instance(8));
        dataset.add(new Instance(9));
        
        Random rg = new Random();
        
        Dataset[] folds = dataset.folds(3, rg);
        
        assertEquals(3, folds.length);
        assertEquals(4, folds[0].size());
        assertEquals(3, folds[1].size());
        assertEquals(3, folds[2].size());
    }
    
    // Test case for noAttributes method
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(0));
        
        assertEquals(0, dataset.noAttributes());
    }
    
    // Test case for classIndex method
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(0));
        dataset.add(new Instance(1));
        dataset.add(new Instance(2));
        dataset.add(new Instance(3));
        dataset.add(new Instance(4));
        
        assertEquals(2, dataset.classIndex(2));
        assertEquals(-1, dataset.classIndex(null));
    }
    
    // Test case for classValue method
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(0));
        dataset.add(new Instance(1));
        dataset.add(new Instance(2));
        dataset.add(new Instance(3));
        dataset.add(new Instance(4));
        
        assertEquals(2, dataset.classValue(2));
        assertNull(dataset.classValue(5));
    }
    
    // Test case for copy method
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(0));
        dataset.add(new Instance(1));
        
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.noAttributes(), copy.noAttributes());
        assertEquals(dataset.classes().size(), copy.classes().size());
        assertEquals(dataset.instance(0), copy.instance(0));
    }
}