import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultDatasetTest {
    
    // Test addAll method
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        DefaultDataset datasetToAdd = new DefaultDataset();
        datasetToAdd.add(new Instance());
        datasetToAdd.add(new Instance());
        assertTrue(dataset.addAll(datasetToAdd));
        assertEquals(2, dataset.size());
    }
    
    // Test addAll method with index
    @Test
    public void testAddAllWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        DefaultDataset datasetToAdd = new DefaultDataset();
        datasetToAdd.add(new Instance());
        datasetToAdd.add(new Instance());
        assertTrue(dataset.addAll(0, datasetToAdd));
        assertEquals(2, dataset.size());
    }
    
    // Test clear method
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertEquals(0, dataset.size());
    }
    
    // Test add method
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
    }
    
    // Test add method with index
    @Test
    public void testAddWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(0, instance);
        assertEquals(1, dataset.size());
    }
    
    // Test addElement method
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
    }
    
    // Test insertElementAt method
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
    }
    
    // Test setElementAt method
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.setElementAt(new Instance(), 0);
        assertNull(dataset.instance(0));
    }
    
    // Test instance method
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertSame(instance, dataset.instance(0));
    }
    
    // Test classes method
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset.classes());
    }
    
    // Test kNearest method
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        assertNotNull(dataset.kNearest(1, instance, dm));
    }
    
    // Test folds method
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        assertNotNull(dataset.folds(5, rg));
    }
    
    // Test noAttributes method
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.noAttributes());
    }
    
    // Test classIndex method
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(-1, dataset.classIndex(null));
    }
    
    // Test classValue method
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        assertNull(dataset.classValue(0));
    }
    
    // Test copy method
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        DefaultDataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
    }
    
}