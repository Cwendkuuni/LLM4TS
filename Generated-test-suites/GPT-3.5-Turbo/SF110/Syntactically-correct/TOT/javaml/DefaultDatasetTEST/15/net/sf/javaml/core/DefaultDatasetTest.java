import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

import java.util.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    
    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }
    
    @Test
    public void testConstructorWithCollection() {
        Collection<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        DefaultDataset dataset = new DefaultDataset(instances);
        
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }
    
    @Test
    public void testConstructorWithoutCollection() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        assertTrue(dataset.addAll(instances));
        
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }
    
    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        dataset.add(new Instance());
        
        assertTrue(dataset.addAll(0, instances));
        
        assertEquals(3, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }
    
    @Test
    public void testClear() {
        dataset.add(new Instance());
        
        dataset.clear();
        
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        
        assertTrue(dataset.add(instance));
        
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddNullInstance() {
        dataset.add(null);
    }
    
    @Test
    public void testAddAtIndex() {
        dataset.add(new Instance());
        Instance instance = new Instance();
        
        dataset.add(0, instance);
        
        assertEquals(2, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddAtIndexOutOfBounds() {
        Instance instance = new Instance();
        
        dataset.add(2, instance);
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        
        dataset.addElement(instance);
        
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddElementNullInstance() {
        dataset.addElement(null);
    }
    
    @Test
    public void testInsertElementAt() {
        dataset.addElement(new Instance());
        Instance instance = new Instance();
        
        dataset.insertElementAt(instance, 0);
        
        assertEquals(2, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testInsertElementAtOutOfBounds() {
        Instance instance = new Instance();
        
        dataset.insertElementAt(instance, 2);
    }
    
    @Test
    public void testSetElementAt() {
        dataset.addElement(new Instance());
        Instance instance = new Instance();
        
        dataset.setElementAt(instance, 0);
        
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetElementAtOutOfBounds() {
        Instance instance = new Instance();
        
        dataset.setElementAt(instance, 2);
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        
        Instance retrievedInstance = dataset.instance(0);
        
        assertEquals(instance, retrievedInstance);
    }
    
    @Test
    public void testClasses() {
        Set<Object> classes = dataset.classes();
        
        assertNotNull(classes);
    }
    
    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));
        
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        
        Set<Instance> nearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
        
        assertNotNull(nearestInstances);
        assertEquals(2, nearestInstances.size());
    }
    
    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(new Instance(), 0.5);
        vector.put(new Instance(), 0.2);
        vector.put(new Instance(), 0.1);
        
        double max = dataset.removeFarthest(vector);
        
        assertEquals(0.5, max, 0.0001);
        assertEquals(2, vector.size());
    }
    
    @Test
    public void testFolds() {
        Random randomGenerator = new Random();
        
        Dataset[] folds = dataset.folds(5, randomGenerator);
        
        assertNotNull(folds);
        assertEquals(5, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        dataset.addElement(new Instance());
        
        int numAttributes = dataset.noAttributes();
        
        assertEquals(0, numAttributes);
    }
    
    @Test
    public void testClassIndex() {
        dataset.addElement(new Instance());
        dataset.addElement(new Instance());
        dataset.addElement(new Instance());
        dataset.addElement(new Instance());
        
        int classIndex = dataset.classIndex(dataset.classValue(2));
        
        assertEquals(2, classIndex);
    }
    
    @Test
    public void testClassIndexWithNullClass() {
        dataset.addElement(new Instance());
        
        int classIndex = dataset.classIndex(null);
        
        assertEquals(-1, classIndex);
    }
    
    @Test
    public void testClassValue() {
        dataset.addElement(new Instance());
        dataset.addElement(new Instance());
        dataset.addElement(new Instance());
        dataset.addElement(new Instance());
        
        Object classValue = dataset.classValue(2);
        
        assertNotNull(classValue);
    }
    
    @Test
    public void testClassValueOutOfBounds() {
        dataset.addElement(new Instance());
        
        Object classValue = dataset.classValue(1);
        
        assertNull(classValue);
    }
    
    @Test
    public void testCopy() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addAll(Arrays.asList(instance1, instance2));
        
        DefaultDataset copiedDataset = (DefaultDataset) dataset.copy();
        
        assertEquals(dataset.size(), copiedDataset.size());
        for (int i = 0; i < dataset.size(); i++) {
            assertNotSame(dataset.instance(i), copiedDataset.instance(i));
            assertEquals(dataset.instance(i), copiedDataset.instance(i));
        }
    }
}