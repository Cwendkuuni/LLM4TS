import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {
    
    private DefaultDataset dataset;
    private Instance instance1;
    private Instance instance2;
    private Instance instance3;
    
    @Before
    public void setUp() {
        Collection<Instance> instances = new ArrayList<>();
        double[] values1 = {1.0, 2.0, 3.0};
        double[] values2 = {4.0, 5.0, 6.0};
        double[] values3 = {7.0, 8.0, 9.0};
        instance1 = new Instance(values1, "A");
        instance2 = new Instance(values2, "B");
        instance3 = new Instance(values3, "C");
        instances.add(instance1);
        instances.add(instance2);
        instances.add(instance3);
        dataset = new DefaultDataset(instances);
    }
    
    @Test
    public void testAddInstanceToEmptyCollection() {
        Instance newInstance = new Instance(new double[] {10.0, 11.0, 12.0}, "D");
        boolean result = dataset.add(newInstance);
        assertTrue(result);
        assertEquals(4, dataset.size());
        assertEquals(newInstance, dataset.instance(3));
    }
    
    @Test
    public void testAddInstancesToNonEmptyCollection() {
        Collection<Instance> additionalInstances = new ArrayList<>();
        double[] values4 = {13.0, 14.0, 15.0};
        double[] values5 = {16.0, 17.0, 18.0};
        Instance instance4 = new Instance(values4, "E");
        Instance instance5 = new Instance(values5, "F");
        additionalInstances.add(instance4);
        additionalInstances.add(instance5);
        boolean result = dataset.addAll(additionalInstances);
        assertTrue(result);
        assertEquals(5, dataset.size());
        assertEquals(instance4, dataset.instance(3));
        assertEquals(instance5, dataset.instance(4));
    }
    
    @Test
    public void testAddAllInstancesAtIndex() {
        Collection<Instance> additionalInstances = new ArrayList<>();
        double[] values4 = {13.0, 14.0, 15.0};
        double[] values5 = {16.0, 17.0, 18.0};
        Instance instance4 = new Instance(values4, "E");
        Instance instance5 = new Instance(values5, "F");
        additionalInstances.add(instance4);
        additionalInstances.add(instance5);
        boolean result = dataset.addAll(1, additionalInstances);
        assertTrue(result);
        assertEquals(5, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance4, dataset.instance(1));
        assertEquals(instance5, dataset.instance(2));
        assertEquals(instance2, dataset.instance(3));
        assertEquals(instance3, dataset.instance(4));
    }
    
    @Test
    public void testClear() {
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Instance newInstance = new Instance(new double[] {10.0, 11.0, 12.0}, "D");
        dataset.addElement(newInstance);
        assertEquals(4, dataset.size());
        assertEquals(newInstance, dataset.instance(3));
    }
    
    @Test
    public void testInsertElementAt() {
        Instance newInstance = new Instance(new double[] {10.0, 11.0, 12.0}, "D");
        dataset.insertElementAt(newInstance, 1);
        assertEquals(4, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(newInstance, dataset.instance(1));
        assertEquals(instance2, dataset.instance(2));
        assertEquals(instance3, dataset.instance(3));
    }
    
    @Test
    public void testSetElementAt() {
        Instance newInstance = new Instance(new double[] {10.0, 11.0, 12.0}, "D");
        dataset.setElementAt(newInstance, 1);
        assertEquals(3, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(newInstance, dataset.instance(1));
        assertEquals(instance3, dataset.instance(2));
    }
    
    @Test
    public void testClasses() {
        SortedSet<Object> classes = dataset.classes();
        assertEquals(3, classes.size());
        assertTrue(classes.contains("A"));
        assertTrue(classes.contains("B"));
        assertTrue(classes.contains("C"));
    }
    
    @Test
    public void testKNearest() {
        DistanceMeasure dm = new EuclideanDistance();
        int k = 2;
        Set<Instance> nearestInstances = dataset.kNearest(k, instance1, dm);
        assertEquals(2, nearestInstances.size());
        assertTrue(nearestInstances.contains(instance2));
        assertTrue(nearestInstances.contains(instance3));
    }
    
    @Test
    public void testFolds() {
        int numFolds = 3;
        Random rg = new Random();
        Dataset[] folds = dataset.folds(numFolds, rg);
        assertEquals(numFolds, folds.length);
        assertNotNull(folds[0]);
        assertNotNull(folds[1]);
        assertNotNull(folds[2]);
    }
    
    @Test
    public void testNoAttributes() {
        int result = dataset.noAttributes();
        assertEquals(3, result);
    }
    
    @Test
    public void testClassIndex() {
        int indexA = dataset.classIndex("A");
        int indexB = dataset.classIndex("B");
        int indexC = dataset.classIndex("C");
        int indexD = dataset.classIndex("D");
        assertEquals(0, indexA);
        assertEquals(1, indexB);
        assertEquals(2, indexC);
        assertEquals(-1, indexD);
    }
    
    @Test
    public void testClassValue() {
        Object valueA = dataset.classValue(0);
        Object valueB = dataset.classValue(1);
        Object valueC = dataset.classValue(2);
        Object valueD = dataset.classValue(3);
        assertEquals("A", valueA);
        assertEquals("B", valueB);
        assertEquals("C", valueC);
        assertNull(valueD);
    }
    
    @Test
    public void testCopy() {
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.instance(0), copy.instance(0));
        assertEquals(dataset.instance(1), copy.instance(1));
        assertEquals(dataset.instance(2), copy.instance(2));
    }
}