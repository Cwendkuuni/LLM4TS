import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;

import static org.junit.Assert.*;

public class DefaultDatasetTest {
    
    private DefaultDataset dataset;
    
    @Before
    public void setup() {
        dataset = new DefaultDataset();
    }
    
    @Test
    public void testConstructorWithCollection() {
        Instance instance = new Instance();
        dataset.add(instance);
        DefaultDataset defaultDataset = new DefaultDataset(dataset);
        assertEquals(dataset.size(), defaultDataset.size());
        assertTrue(defaultDataset.contains(instance));
    }
    
    @Test
    public void testConstructorWithoutCollection() {
        DefaultDataset defaultDataset = new DefaultDataset();
        assertNotNull(defaultDataset);
    }
    
    @Test
    public void testAddAll() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2)));
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsAll(Arrays.asList(instance1, instance2)));
    }
    
    @Test
    public void testAddAllAtIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        Instance instance3 = new Instance();
        assertTrue(dataset.addAll(1, Collections.singletonList(instance3)));
        assertEquals(3, dataset.size());
        assertEquals(instance3, dataset.get(1));
    }
    
    @Test
    public void testClear() {
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
        assertFalse(dataset.contains(instance));
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance));
    }
    
    @Test
    public void testAddAtIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(0, instance2);
        assertEquals(instance2, dataset.get(0));
        assertEquals(instance1, dataset.get(1));
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance));
    }
    
    @Test
    public void testInsertElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addElement(instance1);
        dataset.insertElementAt(instance2, 0);
        assertEquals(instance2, dataset.get(0));
        assertEquals(instance1, dataset.get(1));
    }
    
    @Test
    public void testSetElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addElement(instance1);
        dataset.setElementAt(instance2, 0);
        assertEquals(instance2, dataset.get(0));
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        DefaultDataset closest = (DefaultDataset) dataset.kNearest(1, instance1, new DistanceMeasure());
        assertEquals(1, closest.size());
        assertTrue(closest.contains(instance2));
    }
    
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        Map<Instance, Double> map = new HashMap<>();
        map.put(instance1, 1.0);
        map.put(instance2, 2.0);
        double max = dataset.removeFarthest(map);
        assertEquals(2.0, max, 0.0);
        assertEquals(1, map.size());
        assertFalse(map.containsKey(instance2));
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        Dataset[] folds = dataset.folds(2, new Random());
        assertEquals(2, folds.length);
        assertNotNull(folds[0]);
        assertNotNull(folds[1]);
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(0, dataset.classIndex(instance1.classValue()));
        assertEquals(1, dataset.classIndex(instance2.classValue()));
        assertEquals(-1, dataset.classIndex(null));
    }
    
    @Test
    public void testClassValue() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(instance1.classValue(), dataset.classValue(0));
        assertEquals(instance2.classValue(), dataset.classValue(1));
        assertNull(dataset.classValue(-1));
    }
    
    @Test
    public void testCopy() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertTrue(copy.contains(instance1));
        assertTrue(copy.contains(instance2));
    }
}