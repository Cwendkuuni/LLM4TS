import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {

    @Test
    public void testAddInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        dataset.add(instance);
        
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testAddAllInstances() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}));
        instances.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}));
        
        dataset.addAll(instances);
        
        assertEquals(3, dataset.size());
        assertEquals(instances.get(0), dataset.instance(0));
        assertEquals(instances.get(1), dataset.instance(1));
        assertEquals(instances.get(2), dataset.instance(2));
    }
    
    @Test
    public void testAddAllInstancesAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}));
        
        dataset.add(new DenseInstance(new double[]{0.0, 0.0, 0.0}));
        dataset.addAll(0, instances);
        
        assertEquals(3, dataset.size());
        assertEquals(instances.get(0), dataset.instance(0));
        assertEquals(instances.get(1), dataset.instance(1));
        assertEquals(0.0, dataset.instance(2).values()[0], 0.0001);
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        
        dataset.clear();
        
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        dataset.addElement(instance);
        
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{0.0, 0.0, 0.0}));
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        dataset.insertElementAt(instance, 1);
        
        assertEquals(2, dataset.size());
        assertEquals(instance, dataset.instance(1));
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{0.0, 0.0, 0.0}));
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        dataset.setElementAt(instance, 0);
        
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}));
        dataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}));
        
        SortedSet<Object> expected = new TreeSet<>();
        expected.add(1.0);
        expected.add(4.0);
        expected.add(7.0);
        
        assertEquals(expected, dataset.classes());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}));
        dataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}));
        
        DistanceMeasure measure = new EuclideanDistance();
        Instance instance = new DenseInstance(new double[]{3.0, 4.0, 5.0});
        
        Set<Instance> nearest = dataset.kNearest(2, instance, measure);
        
        assertEquals(2, nearest.size());
        assertTrue(nearest.contains(dataset.instance(0)));
        assertTrue(nearest.contains(dataset.instance(1)));
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}));
        dataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}));
        dataset.add(new DenseInstance(new double[]{10.0, 11.0, 12.0}));
        
        int numFolds = 2;
        Random random = new Random();
        Dataset[] folds = dataset.folds(numFolds, random);
        
        assertEquals(numFolds, folds.length);
        
        // Verify that each fold has the expected number of instances
        for (Dataset fold : folds) {
            assertEquals(dataset.size() / numFolds + 1, fold.size());
        }
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        
        assertEquals(3, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}));
        dataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}));
        
        assertEquals(0, dataset.classIndex(1.0));
        assertEquals(1, dataset.classIndex(4.0));
        assertEquals(2, dataset.classIndex(7.0));
        assertEquals(-1, dataset.classIndex(10.0));
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}));
        dataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}));
        
        assertEquals(1.0, dataset.classValue(0));
        assertEquals(4.0, dataset.classValue(1));
        assertEquals(7.0, dataset.classValue(2));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}));
        dataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}));
        
        Dataset copy = dataset.copy();
        
        assertEquals(dataset.size(), copy.size());
        
        for (int i = 0; i < dataset.size(); i++) {
            assertEquals(dataset.instance(i), copy.instance(i));
            assertNotSame(dataset.instance(i), copy.instance(i));
        }
    }
}