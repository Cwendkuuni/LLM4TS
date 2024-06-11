import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset1 = new DefaultDataset();
        dataset1.add(new Instance());
        dataset1.add(new Instance());
        
        DefaultDataset dataset2 = new DefaultDataset();
        dataset2.add(new Instance());
        dataset2.add(new Instance());
        
        assertEquals(4, dataset1.size());
        assertTrue(dataset1.addAll(dataset2));
        assertEquals(6, dataset1.size());
    }
    
    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset1 = new DefaultDataset();
        dataset1.add(new Instance());
        dataset1.add(new Instance());
        
        DefaultDataset dataset2 = new DefaultDataset();
        dataset2.add(new Instance());
        dataset2.add(new Instance());
        
        assertEquals(4, dataset1.size());
        assertTrue(dataset1.addAll(1, dataset2));
        assertEquals(6, dataset1.size());
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        assertEquals(2, dataset.size());
        dataset.clear();
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        assertEquals(2, dataset.size());
        dataset.add(1, new Instance());
        assertEquals(3, dataset.size());
        assertEquals(Instance.class, dataset.get(1).getClass());
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());
        dataset.addElement(new Instance());
        
        assertEquals(2, dataset.size());
        dataset.insertElementAt(new Instance(), 1);
        assertEquals(3, dataset.size());
        assertEquals(Instance.class, dataset.get(1).getClass());
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());
        
        assertEquals(1, dataset.size());
        dataset.setElementAt(new Instance(), 0);
        assertEquals(1, dataset.size());
        assertEquals(Instance.class, dataset.get(0).getClass());
    }
    
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.add(instance1);
        
        assertSame(instance1, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertNotNull(dataset.classes());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        
        Set<Instance> result = dataset.kNearest(1, new Instance(), new DistanceMeasure());
        
        assertNotNull(result);
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        
        Dataset[] folds = dataset.folds(2, new Random());
        
        assertNotNull(folds);
        assertEquals(2, folds.length);
        assertNotNull(folds[0]);
        assertNotNull(folds[1]);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertEquals(-1, dataset.classIndex(null));
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertNull(dataset.classValue(0));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        
        Dataset copy = dataset.copy();
        
        assertNotNull(copy);
        assertEquals(DefaultDataset.class, copy.getClass());
    }

}