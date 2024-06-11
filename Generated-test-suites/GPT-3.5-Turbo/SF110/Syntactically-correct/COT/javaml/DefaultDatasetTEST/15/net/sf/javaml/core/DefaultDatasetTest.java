import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDatasetTest {
    
    @Test
    public void testDefaultDataset() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }
    
    @Test
    public void testDefaultDatasetWithCollection() {
        Collection<Instance> coll = new ArrayList<>();
        coll.add(new Instance());
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset(coll);
        assertNotNull(dataset);
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }
    
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
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.add(instance);
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.classes.size());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.add(new Instance()));
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertEquals(1, dataset.classes.size());
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        assertTrue(dataset.add(0, new Instance()));
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertEquals(2, dataset.classes.size());
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertEquals(1, dataset.classes.size());
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.insertElementAt(new Instance(), 0);
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertEquals(1, dataset.classes.size());
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());
        dataset.addElement(new Instance());
        dataset.setElementAt(new Instance(), 0);
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertEquals(2, dataset.classes.size());
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(instance);
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset.classes());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        assertTrue(dataset.kNearest(5, inst, dm).isEmpty());
    }
    
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(new Instance(), 1.0);
        double max = dataset.removeFarthest(vector);
        assertEquals(1.0, max, 0.01);
        assertTrue(vector.isEmpty());
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        Dataset[] folds = dataset.folds(5, rg);
        assertNotNull(folds);
        assertEquals(5, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.noAttributes());
        dataset.add(new Instance());
        assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(-1, dataset.classIndex(null));
        dataset.classes.add("A");
        dataset.classes.add("B");
        assertEquals(0, dataset.classIndex("A"));
        assertEquals(1, dataset.classIndex("B"));
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        assertNull(dataset.classValue(0));
        dataset.classes.add("A");
        dataset.classes.add("B");
        assertEquals("A", dataset.classValue(0));
        assertEquals("B", dataset.classValue(1));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        assertNotNull(copy);
        assertEquals(0, copy.size());
        assertEquals(0, copy.maxAttributes);
        assertNotNull(copy.classes);
    }
}