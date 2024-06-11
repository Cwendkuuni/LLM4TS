import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.*;

public class DefaultDatasetTest {
    
    private DefaultDataset dataset;
    
    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }
    
    // Test for DefaultDataset(Collection<Instance> coll) constructor
    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        assertNull(dataset.instance(0));
        assertEquals(0, dataset.size());
        
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        
        DefaultDataset dataset2 = new DefaultDataset(Arrays.asList(instance1, instance2));
        
        assertEquals(instance1, dataset2.instance(0));
        assertEquals(instance2, dataset2.instance(1));
        assertEquals(2, dataset2.size());
    }
    
    // Test for DefaultDataset() constructor
    @Test
    public void testDefaultDatasetConstructor() {
        assertNull(dataset.instance(0));
        assertEquals(0, dataset.size());
    }
    
    // Test for addAll(Collection<? extends Instance> c) method
    @Test
    public void testAddAll() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        Instance instance3 = new Instance(new double[] {5.0, 6.0}, "ClassC");
        
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2, instance3)));
        assertEquals(3, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
        assertEquals(instance3, dataset.instance(2));
    }
    
    // Test for addAll(int index, Collection<? extends Instance> c) method
    @Test
    public void testAddAllAtIndex() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        Instance instance3 = new Instance(new double[] {5.0, 6.0}, "ClassC");
        
        assertTrue(dataset.addAll(0, Arrays.asList(instance2, instance3)));
        assertEquals(2, dataset.size());
        assertEquals(instance2, dataset.instance(0));
        assertEquals(instance3, dataset.instance(1));
        
        assertTrue(dataset.addAll(0, Arrays.asList(instance1)));
        assertEquals(3, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
        assertEquals(instance3, dataset.instance(2));
    }
    
    // Test for clear() method
    @Test
    public void testClear() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        
        dataset.addAll(Arrays.asList(instance1, instance2));
        
        assertEquals(2, dataset.size());
        dataset.clear();
        assertEquals(0, dataset.size());
        assertNull(dataset.instance(0));
        assertNull(dataset.instance(1));
    }
    
    // Test for add(Instance e) method
    @Test
    public void testAdd() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        
        assertTrue(dataset.add(instance1));
        assertEquals(1, dataset.size());
        assertEquals(instance1, dataset.instance(0));
    }
    
    // Test for add(int index, Instance e) method
    @Test
    public void testAddAtIndex() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        Instance instance3 = new Instance(new double[] {5.0, 6.0}, "ClassC");
        
        dataset.add(instance1);
        assertEquals(1, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        
        dataset.add(0, instance2);
        assertEquals(2, dataset.size());
        assertEquals(instance2, dataset.instance(0));
        assertEquals(instance1, dataset.instance(1));
        
        dataset.add(1, instance3);
        assertEquals(3, dataset.size());
        assertEquals(instance2, dataset.instance(0));
        assertEquals(instance3, dataset.instance(1));
        assertEquals(instance1, dataset.instance(2));
    }
    
    // Test for addElement(Instance e) method
    @Test
    public void testAddElement() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        
        dataset.addElement(instance1);
        assertEquals(1, dataset.size());
        assertEquals(instance1, dataset.instance(0));
    }
    
    // Test for insertElementAt(Instance e, int index) method
    @Test
    public void testInsertElementAt() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        Instance instance3 = new Instance(new double[] {5.0, 6.0}, "ClassC");
        
        dataset.insertElementAt(instance3, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance3, dataset.instance(0));
        
        dataset.insertElementAt(instance2, 1);
        assertEquals(2, dataset.size());
        assertEquals(instance3, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
        
        dataset.insertElementAt(instance1, 1);
        assertEquals(3, dataset.size());
        assertEquals(instance3, dataset.instance(0));
        assertEquals(instance1, dataset.instance(1));
        assertEquals(instance2, dataset.instance(2));
    }
    
    // Test for setElementAt(Instance e, int index) method
    @Test
    public void testSetElementAt() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        Instance instance3 = new Instance(new double[] {5.0, 6.0}, "ClassC");
        
        dataset.addElement(instance1);
        assertEquals(1, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        
        dataset.setElementAt(instance2, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance2, dataset.instance(0));
        
        dataset.setElementAt(instance3, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance3, dataset.instance(0));
    }
    
    // Test for instance(int index) method
    @Test
    public void testInstance() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        
        dataset.addElement(instance1);
        assertEquals(1, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        
        dataset.addElement(instance2);
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }
    
    // Test for classes() method
    @Test
    public void testClasses() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        Instance instance3 = new Instance(new double[] {5.0, 6.0}, "ClassC");
        
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));
        
        SortedSet<Object> classes = dataset.classes();
        assertEquals(3, classes.size());
        assertTrue(classes.contains("ClassA"));
        assertTrue(classes.contains("ClassB"));
        assertTrue(classes.contains("ClassC"));
    }
    
    // Test for kNearest(int k, Instance inst, DistanceMeasure dm) method
    @Test
    public void testKNearest() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        Instance instance3 = new Instance(new double[] {5.0, 6.0}, "ClassC");
        
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));
        
        DistanceMeasure dm = new EuclideanDistance();
        
        Set<Instance> nearest1 = dataset.kNearest(1, instance1, dm);
        assertEquals(1, nearest1.size());
        assertTrue(nearest1.contains(instance2));
        
        Set<Instance> nearest2 = dataset.kNearest(2, instance2, dm);
        assertEquals(2, nearest2.size());
        assertTrue(nearest2.contains(instance1));
        assertTrue(nearest2.contains(instance3));
    }
    
    // Test for classIndex(Object clazz) method
    @Test
    public void testClassIndex() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        Instance instance3 = new Instance(new double[] {5.0, 6.0}, "ClassC");
        
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));
        
        assertEquals(0, dataset.classIndex("ClassA"));
        assertEquals(1, dataset.classIndex("ClassB"));
        assertEquals(2, dataset.classIndex("ClassC"));
        
        assertEquals(-1, dataset.classIndex("ClassD"));
    }
    
    // Test for classValue(int index) method
    @Test
    public void testClassValue() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        Instance instance3 = new Instance(new double[] {5.0, 6.0}, "ClassC");
        
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));
        
        assertEquals("ClassA", dataset.classValue(0));
        assertEquals("ClassB", dataset.classValue(1));
        assertEquals("ClassC", dataset.classValue(2));
        
        assertNull(dataset.classValue(3));
    }
    
    // Test for copy() method
    @Test
    public void testCopy() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        
        dataset.addAll(Arrays.asList(instance1, instance2));
        
        Dataset copy = dataset.copy();
        
        assertEquals(2, copy.size());
        assertEquals(instance1, copy.instance(0));
        assertEquals(instance2, copy.instance(1));
    }
    
    // Test for folds(int numFolds, Random rg) method
    @Test
    public void testFolds() {
        Instance instance1 = new Instance(new double[] {1.0, 2.0}, "ClassA");
        Instance instance2 = new Instance(new double[] {3.0, 4.0}, "ClassB");
        Instance instance3 = new Instance(new double[] {5.0, 6.0}, "ClassC");
        Instance instance4 = new Instance(new double[] {7.0, 8.0}, "ClassD");
        Instance instance5 = new Instance(new double[] {9.0, 10.0}, "ClassE");
        
        dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4, instance5));
        
        Random rg = new Random();
        Dataset[] folds = dataset.folds(3, rg);
        
        assertEquals(3, folds.length);
        assertEquals(2, folds[0].size());
        assertEquals(2, folds[1].size());
        assertEquals(1, folds[2].size());
    }
}