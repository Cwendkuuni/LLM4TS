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
        
        assertTrue(dataset.add(instance1));
        assertTrue(dataset.addAll(0, Arrays.asList(instance2)));
        assertEquals(2, dataset.size());
        assertEquals(instance2, dataset.get(0));
        assertEquals(instance1, dataset.get(1));
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.add(instance);
        dataset.clear();
        
        assertEquals(0, dataset.size());
        assertFalse(dataset.contains(instance));
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
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(0, instance2);
        
        assertEquals(2, dataset.size());
        assertEquals(instance2, dataset.get(0));
        assertEquals(instance1, dataset.get(1));
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.addElement(instance);
        
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.lastElement());
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.insertElementAt(instance2, 0);
        
        assertEquals(2, dataset.size());
        assertEquals(instance2, dataset.get(0));
        assertEquals(instance1, dataset.get(1));
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
        
        assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals(0, dataset.kNearest(1, instance1, dm).size());
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Random rg = new Random();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        Dataset[] folds = dataset.folds(2, rg);
        
        assertEquals(2, folds.length);
        assertEquals(1, folds[0].size());
        assertEquals(1, folds[1].size());
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.add(instance);
        
        assertEquals(-1, dataset.classIndex(null));
        assertEquals(0, dataset.classIndex(instance.classValue()));
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertNull(dataset.classValue(-1));
        assertEquals(instance1.classValue(), dataset.classValue(0));
        assertEquals(instance2.classValue(), dataset.classValue(1));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        Dataset copy = dataset.copy();
        
        assertEquals(dataset.size(), copy.size());
        assertEquals(instance1, copy.get(0));
        assertEquals(instance2, copy.get(1));
    }
    
}