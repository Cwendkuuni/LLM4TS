
import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2)));
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        assertTrue(dataset.addAll(0, Arrays.asList(instance1, instance2)));
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(0, instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.setElementAt(instance2, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance2, dataset.get(0));
    }
    
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset.classes());
        assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        Dataset kNearest = dataset.kNearest(3, instance, new EuclideanDistance());
        assertNotNull(kNearest);
        assertEquals(0, kNearest.size());
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Dataset[] folds = dataset.folds(5, new Random());
        assertNotNull(folds);
        assertEquals(5, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.noAttributes());
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance.noAttributes(), dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Object clazz = new Object();
        assertEquals(-1, dataset.classIndex(clazz));
        dataset.classes().add(clazz);
        assertEquals(0, dataset.classIndex(clazz));
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Object clazz = new Object();
        dataset.classes().add(clazz);
        assertEquals(clazz, dataset.classValue(0));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        Dataset copy = dataset.copy();
        assertNotNull(copy);
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.get(0), copy.get(0));
        assertEquals(dataset.get(1), copy.get(1));
    }
}

