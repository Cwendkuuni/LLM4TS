import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultDatasetTest {
    
    // Test addAll() method
    @Test
    public void testAddAll() {
        Collection<Instance> collection = Arrays.asList(new Instance(), new Instance());
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.addAll(collection));
        assertEquals(collection.size(), dataset.size());
    }
    
    // Test addAll(index, c) method
    @Test
    public void testAddAllWithIndex() {
        Collection<Instance> collection = Arrays.asList(new Instance(), new Instance());
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(new Instance(), new Instance()));
        assertTrue(dataset.addAll(1, collection));
        assertEquals(collection.size(), dataset.size());
    }
    
    // Test clear() method
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(new Instance(), new Instance()));
        dataset.clear();
        assertEquals(0, dataset.size());
    }
    
    // Test add() method
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
    }
    
    // Test add(index, e) method
    @Test
    public void testAddWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        Instance instance = new Instance();
        dataset.add(0, instance);
        assertEquals(2, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    // Test addElement() method
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    // Test insertElementAt() method
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(2, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    // Test setElementAt() method
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        Instance instance = new Instance();
        dataset.setElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    // Test instance() method
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }
    
    // Test classes() method
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.classes().isEmpty());
        dataset.add(new Instance("A"));
        assertEquals(new HashSet<Object>(Arrays.asList("A")), dataset.classes());
    }
    
    // Test kNearest() method
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance("A", new double[]{1.0, 2.0, 3.0}));
        dataset.add(new Instance("B", new double[]{4.0, 5.0, 6.0}));
        dataset.add(new Instance("C", new double[]{7.0, 8.0, 9.0}));
        
        Instance instance = new Instance("D", new double[]{10.0, 11.0, 12.0});
        Set<Instance> knn = dataset.kNearest(2, instance, new EuclideanDistance());
        
        assertEquals(2, knn.size());
        assertFalse(knn.contains(instance));
    }
    
    // Test folds() method
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(new Instance(), new Instance(), new Instance(), new Instance(), new Instance(), new Instance()));
        Dataset[] folds = dataset.folds(3, new Random());
        
        assertEquals(3, folds.length);
        assertEquals(2, folds[0].size());
        assertEquals(2, folds[1].size());
        assertEquals(2, folds[2].size());
    }
    
    // Test noAttributes() method
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.noAttributes());
        
        dataset.add(new Instance("A", new double[]{1.0}));
        assertEquals(1, dataset.noAttributes());
    }
    
    // Test classIndex() method
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance("A"));
        assertEquals(0, dataset.classIndex("A"));
        assertEquals(-1, dataset.classIndex("B"));
    }
    
    // Test classValue() method
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance("A"));
        dataset.add(new Instance("B"));
        assertEquals("A", dataset.classValue(0));
    }
    
    // Test copy() method
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance("A"));
        dataset.add(new Instance("B"));
        Dataset copy = dataset.copy();
        
        assertEquals(dataset.size(), copy.size());
        for (int i = 0; i < dataset.size(); i++) {
            assertEquals(dataset.instance(i), copy.instance(i));
        }
    }
}