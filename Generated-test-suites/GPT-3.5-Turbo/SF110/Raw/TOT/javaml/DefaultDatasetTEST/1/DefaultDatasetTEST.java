import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.*;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstructor1() {
        // Test case for DefaultDataset constructor with Collection<Instance> parameter
        // Typical use case
        ArrayList<Instance> instances = new ArrayList<Instance>();
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(instances.size(), dataset.size());
    }
    
    @Test
    public void testDefaultDatasetConstructor2() {
        // Test case for DefaultDataset constructor with no parameter
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.isEmpty());
    }
    
    @Test
    public void testAddAll() {
        // Test case for addAll() method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        ArrayList<Instance> instances = new ArrayList<Instance>();
        instances.add(new Instance());
        instances.add(new Instance());
        dataset.addAll(instances);
        assertEquals(instances.size(), dataset.size());
    }
    
    @Test
    public void testAddAllWithIndex() {
        // Test case for addAll(int index, Collection<Instance> c) method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        ArrayList<Instance> instances = new ArrayList<Instance>();
        instances.add(new Instance());
        instances.add(new Instance());
        instances.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.addAll(1, instances);
        assertEquals(instances.size() + 2, dataset.size());
    }
    
    @Test
    public void testClear() {
        // Test case for clear() method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }
    
    @Test
    public void testAdd() {
        // Test case for add(Instance e) method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddWithIndex() {
        // Test case for add(int index, Instance e) method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.add(0, new Instance());
        dataset.add(1, new Instance());
        dataset.add(1, new Instance());
        assertEquals(4, dataset.size());
    }
    
    @Test
    public void testAddElement() {
        // Test case for addElement(Instance e) method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testInsertElementAt() {
        // Test case for insertElementAt(Instance e, int index) method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        dataset.insertElementAt(new Instance(), 0);
        dataset.insertElementAt(new Instance(), 1);
        dataset.insertElementAt(new Instance(), 1);
        assertEquals(4, dataset.size());
    }
    
    @Test
    public void testSetElementAt() {
        // Test case for setElementAt(Instance e, int index) method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        Instance instance = new Instance();
        dataset.setElementAt(instance, 1);
        assertEquals(instance, dataset.instance(1));
    }
    
    @Test
    public void testClasses() {
        // Test case for classes() method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance("A"));
        dataset.add(new Instance("B"));
        dataset.add(new Instance("A"));
        assertEquals(2, dataset.classes().size());
    }
    
    @Test
    public void testKNearest() {
        // Test case for kNearest(int k, Instance inst, DistanceMeasure dm) method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(1, 2));
        dataset.add(new Instance(2, 3));
        dataset.add(new Instance(3, 4));
        dataset.add(new Instance(4, 5));
        Instance inst = new Instance(1, 2);
        Set<Instance> nearest = dataset.kNearest(2, inst, new EuclideanDistance());
        assertEquals(2, nearest.size());
    }
    
    @Test
    public void testFolds() {
        // Test case for folds(int numFolds, Random rg) method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        Dataset[] folds = dataset.folds(2, new Random());
        assertEquals(2, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        // Test case for noAttributes() method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(new double[] {1, 2, 3}));
        assertEquals(3, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        // Test case for classIndex(Object clazz) method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance("A"));
        dataset.add(new Instance("B"));
        assertEquals(0, dataset.classIndex("A"));
    }
    
    @Test
    public void testNullClassIndex() {
        // Test case for classIndex(Object clazz) method with null parameter
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance("A"));
        dataset.add(new Instance("B"));
        assertEquals(-1, dataset.classIndex(null));
    }
    
    @Test
    public void testClassValue() {
        // Test case for classValue(int index) method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance("A"));
        dataset.add(new Instance("B"));
        dataset.add(new Instance("C"));
        assertEquals("A", dataset.classValue(0));
    }
    
    @Test
    public void testCopy() {
        // Test case for copy() method
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        Dataset copied = dataset.copy();
        assertEquals(dataset.size(), copied.size());
    }

}