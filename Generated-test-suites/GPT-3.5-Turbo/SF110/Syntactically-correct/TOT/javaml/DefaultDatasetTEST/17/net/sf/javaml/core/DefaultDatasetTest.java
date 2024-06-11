import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
        assertEquals(0, dataset.classes.size());
    }

    @Test
    public void testDefaultDatasetConstructorWithCollection() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<>();
        collection.add(instance1);
        collection.add(instance2);
        
        DefaultDataset dataset = new DefaultDataset(collection);
        assertNotNull(dataset);
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
        assertEquals(0, dataset.classes.size());
    }
    
    @Test
    public void testCheckWithInstances() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.check(instance1);
        
        assertEquals(1, dataset.classes.size());
        assertTrue(dataset.classes.contains(instance1.classValue()));
        assertEquals(0, dataset.maxAttributes);
        
        dataset.check(instance2);
        assertEquals(2, dataset.classes.size());
        assertTrue(dataset.classes.contains(instance1.classValue()));
        assertTrue(dataset.classes.contains(instance2.classValue()));
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testCheckWithCollection() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<>();
        collection.add(instance1);
        collection.add(instance2);
        
        dataset.check(collection);
        
        assertEquals(2, dataset.classes.size());
        assertTrue(dataset.classes.contains(instance1.classValue()));
        assertTrue(dataset.classes.contains(instance2.classValue()));
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<>();
        collection.add(instance1);
        collection.add(instance2);
        
        assertTrue(dataset.addAll(collection));
        assertEquals(2, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(2, dataset.classes.size());
        assertTrue(dataset.classes.contains(instance1.classValue()));
        assertTrue(dataset.classes.contains(instance2.classValue()));
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testAddAllWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        Collection<Instance> collection = new ArrayList<>();
        collection.add(instance2);
        
        assertTrue(dataset.addAll(1, collection));
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
        assertEquals(2, dataset.classes.size());
        assertTrue(dataset.classes.contains(instance1.classValue()));
        assertTrue(dataset.classes.contains(instance2.classValue()));
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        
        dataset.clear();
        
        assertEquals(0, dataset.size());
        assertFalse(dataset.contains(instance));
        assertEquals(0, dataset.classes.size());
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance));
        assertEquals(1, dataset.classes.size());
        assertTrue(dataset.classes.contains(instance.classValue()));
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testAddWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        
        dataset.add(0, instance2);
        assertEquals(2, dataset.size());
        assertEquals(instance2, dataset.instance(0));
        assertEquals(instance1, dataset.instance(1));
        assertEquals(2, dataset.classes.size());
        assertTrue(dataset.classes.contains(instance1.classValue()));
        assertTrue(dataset.classes.contains(instance2.classValue()));
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance));
        assertEquals(1, dataset.classes.size());
        assertTrue(dataset.classes.contains(instance.classValue()));
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addElement(instance1);
        
        dataset.insertElementAt(instance2, 0);
        assertEquals(2, dataset.size());
        assertEquals(instance2, dataset.instance(0));
        assertEquals(instance1, dataset.instance(1));
        assertEquals(2, dataset.classes.size());
        assertTrue(dataset.classes.contains(instance1.classValue()));
        assertTrue(dataset.classes.contains(instance2.classValue()));
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addElement(instance1);
        
        dataset.setElementAt(instance2, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance2, dataset.instance(0));
        assertFalse(dataset.contains(instance1));
        assertEquals(1, dataset.classes.size());
        assertTrue(dataset.classes.contains(instance2.classValue()));
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        SortedSet<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains(instance1.classValue()));
        assertTrue(classes.contains(instance2.classValue()));
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        Set<Instance> nearest = dataset.kNearest(1, instance1, new EuclideanDistance());
        assertEquals(1, nearest.size());
        assertTrue(nearest.contains(instance2));
    }
    
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(instance1, 2.0);
        vector.put(instance2, 3.0);
        
        double result = dataset.removeFarthest(vector);
        assertEquals(3.0, result, 0.0001);
        assertEquals(1, vector.size());
        assertFalse(vector.containsKey(instance2));
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
        assertTrue(folds[0].contains(instance1));
        assertTrue(folds[0].contains(instance2));
        assertTrue(folds[1].contains(instance1));
        assertTrue(folds[1].contains(instance2));
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        instance.addAttribute("attribute1", 1.0);
        instance.addAttribute("attribute2", 2.0);
        dataset.add(instance);
        
        int numAttributes = dataset.noAttributes();
        assertEquals(2, numAttributes);
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instance1.setClassValue("class1");
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        
        int classIndex1 = dataset.classIndex("class1");
        int classIndex2 = dataset.classIndex("class2");
        int classIndex3 = dataset.classIndex("class3");
        assertEquals(0, classIndex1);
        assertEquals(1, classIndex2);
        assertEquals(-1, classIndex3);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instance1.setClassValue("class1");
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        
        Object classValue1 = dataset.classValue(0);
        Object classValue2 = dataset.classValue(1);
        Object classValue3 = dataset.classValue(2);
        assertEquals("class1", classValue1);
        assertEquals("class2", classValue2);
        assertNull(classValue3);
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        
        Dataset copy = dataset.copy();
        assertNotSame(dataset, copy);
        assertEquals(dataset.size(), copy.size());
        assertTrue(copy.contains(instance1));
        assertTrue(copy.contains(instance2));
        assertNotSame(instance1, copy.instance(0));
        assertNotSame(instance2, copy.instance(1));
    }

}