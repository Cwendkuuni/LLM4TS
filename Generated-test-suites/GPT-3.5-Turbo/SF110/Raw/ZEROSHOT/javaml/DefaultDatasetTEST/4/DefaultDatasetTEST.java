import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {
    
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        List<Instance> instances = new ArrayList<Instance>();
        instances.add(instance1);
        instances.add(instance2);
        assertTrue(dataset.addAll(instances));
    }
    
    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        List<Instance> instances = new ArrayList<Instance>();
        instances.add(instance1);
        instances.add(instance2);
        assertTrue(dataset.addAll(0, instances));
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.clear();
        assertEquals(dataset.size(), 0);
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(0, instance);
        assertEquals(dataset.get(0), instance);
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(dataset.get(0), instance);
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(dataset.get(0), instance);
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.setElementAt(instance, 0);
        assertEquals(dataset.get(0), instance);
    }
    
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(dataset.instance(0), instance);
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset.classes());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        Set<Instance> nearest = dataset.kNearest(5, instance, dm);
        assertNotNull(nearest);
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random random = new Random();
        Dataset[] folds = dataset.folds(5, random);
        assertNotNull(folds);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(dataset.noAttributes(), 0);
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Object clazz = new Object();
        int classIndex = dataset.classIndex(clazz);
        assertEquals(classIndex, -1);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Object classValue = dataset.classValue(0);
        assertNull(classValue);
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Dataset copy = dataset.copy();
        assertNotNull(copy);
    }
    
}