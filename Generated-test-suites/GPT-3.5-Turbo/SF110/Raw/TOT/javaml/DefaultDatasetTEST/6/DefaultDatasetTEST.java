import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        // Typical use case
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2)));
        
        // Edge case: adding an empty collection
        DefaultDataset emptyDataset = new DefaultDataset();
        assertFalse(emptyDataset.addAll(Collections.emptyList()));
        
        // Error scenario: adding null collection
        assertFalse(dataset.addAll(null));
    }
    
    @Test
    public void testAddAllIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        dataset.addAll(Arrays.asList(instance1, instance2));
        
        // Typical use case
        assertTrue(dataset.addAll(1, Collections.singletonList(instance3)));
        
        // Edge case: adding at the beginning of the dataset
        Instance instance4 = new Instance();
        assertTrue(dataset.addAll(0, Collections.singletonList(instance4)));
        
        // Error scenario: adding at index out of bounds
        assertFalse(dataset.addAll(10, Collections.singletonList(instance1)));
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        
        // Typical use case
        dataset.clear();
        assertEquals(0, dataset.size());
        
        // Edge case: clearing an empty dataset
        DefaultDataset emptyDataset = new DefaultDataset();
        emptyDataset.clear();
        assertEquals(0, emptyDataset.size());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        // Typical use case
        assertTrue(dataset.add(instance));
        
        // Edge case: adding the same instance again
        assertFalse(dataset.add(instance));
        
        // Error scenario: adding null instance
        assertFalse(dataset.add(null));
    }
    
    @Test
    public void testAddIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        
        // Typical use case
        dataset.add(1, instance2);
        assertEquals(instance2, dataset.get(1));
        
        // Edge case: adding at the beginning of the dataset
        Instance instance3 = new Instance();
        dataset.add(0, instance3);
        assertEquals(instance3, dataset.get(0));
        
        // Error scenario: adding at index out of bounds
        Instance instance4 = new Instance();
        try {
            dataset.add(10, instance4);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        // Typical use case
        dataset.addElement(instance);
        assertTrue(dataset.contains(instance));
        
        // Edge case: adding the same instance again
        dataset.addElement(instance);
        assertEquals(2, dataset.size());
        
        // Error scenario: adding null instance
        assertFalse(dataset.add(null));
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addElement(instance1);
        
        // Typical use case
        dataset.insertElementAt(instance2, 1);
        assertEquals(instance2, dataset.get(1));
        
        // Edge case: inserting at the beginning of the dataset
        Instance instance3 = new Instance();
        dataset.insertElementAt(instance3, 0);
        assertEquals(instance3, dataset.get(0));
        
        // Error scenario: inserting at index out of bounds
        Instance instance4 = new Instance();
        try {
            dataset.insertElementAt(instance4, 10);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addElement(instance1);
        
        // Typical use case
        dataset.setElementAt(instance2, 0);
        assertEquals(instance2, dataset.get(0));
        
        // Edge case: setting element at the end of the dataset
        Instance instance3 = new Instance();
        dataset.setElementAt(instance3, dataset.size() - 1);
        assertEquals(instance3, dataset.get(dataset.size() - 1));
        
        // Error scenario: setting element at index out of bounds
        Instance instance4 = new Instance();
        try {
            dataset.setElementAt(instance4, 10);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));
        DistanceMeasure dm = new DistanceMeasure();
        
        // Typical use case
        Set<Instance> kNearest = dataset.kNearest(2, instance1, dm);
        assertEquals(2, kNearest.size());
        assertTrue(kNearest.contains(instance2));
        assertTrue(kNearest.contains(instance3));
        
        // Edge case: requesting more neighbors than dataset size
        Set<Instance> allNeighbors = dataset.kNearest(dataset.size() + 1, instance1, dm);
        assertEquals(dataset.size() - 1, allNeighbors.size());
        
        // Error scenario: null distance measure
        try {
            dataset.kNearest(1, instance1, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));
        Random rg = new Random();
        
        // Typical use case
        Dataset[] folds = dataset.folds(3, rg);
        assertEquals(3, folds.length);
        assertEquals(1, folds[0].size());
        assertEquals(1, folds[1].size());
        assertEquals(1, folds[2].size());
        
        // Edge case: requesting more folds than dataset size
        Dataset[] largeFolds = dataset.folds(dataset.size() + 1, rg);
        assertEquals(dataset.size(), largeFolds.length);
        
        // Error scenario: null random generator
        try {
            dataset.folds(1, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        
        // Typical use case
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance.noAttributes(), dataset.noAttributes());
        
        // Edge case: no instances in the dataset
        DefaultDataset emptyDataset = new DefaultDataset();
        assertEquals(0, emptyDataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instance1.setClassValue("class1");
        instance2.setClassValue("class2");
        dataset.addAll(Arrays.asList(instance1, instance2));
        
        // Typical use case
        assertEquals(0, dataset.classIndex("class1"));
        assertEquals(1, dataset.classIndex("class2"));
        
        // Edge case: class not present in the dataset
        assertEquals(-1, dataset.classIndex("class3"));
        
        // Error scenario: null class value
        assertEquals(-1, dataset.classIndex(null));
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instance1.setClassValue("class1");
        instance2.setClassValue("class2");
        dataset.addAll(Arrays.asList(instance1, instance2));
        
        // Typical use case
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
        
        // Edge case: index out of bounds
        assertNull(dataset.classValue(2));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addAll(Arrays.asList(instance1, instance2));
        
        // Typical use case
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertEquals(instance1, copy.get(0));
        assertEquals(instance2, copy.get(1));
    }

}