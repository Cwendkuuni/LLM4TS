import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.distance.DistanceMeasure;

import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        Assert.assertTrue(dataset.addAll(instances));
        Assert.assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddAllAtIndex() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        Assert.assertTrue(dataset.addAll(0, instances));
        Assert.assertEquals(1, dataset.size());
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.clear();
        Assert.assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Assert.assertTrue(dataset.add(new Instance()));
        Assert.assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(0, new Instance());
        Assert.assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());
        Assert.assertEquals(1, dataset.size());
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.insertElementAt(new Instance(), 0);
        Assert.assertEquals(1, dataset.size());
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.setElementAt(new Instance(), 0);
        Assert.assertEquals(1, dataset.size());
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        Assert.assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        Set<Object> classes = dataset.classes();
        Assert.assertNotNull(classes);
    }
    
    @Test
    public void testKNearest() {
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        Set<Instance> nearest = dataset.kNearest(2, inst, dm);
        Assert.assertNotNull(nearest);
    }
    
    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(new Instance(), 2.0);
        vector.put(new Instance(), 1.0);
        DefaultDataset dataset = new DefaultDataset();
        double max = dataset.removeFarthest(vector);
        Assert.assertEquals(2.0, max, 0.001);
        Assert.assertEquals(1, vector.size());
    }
    
    @Test
    public void testFolds() {
        Random rg = new Random();
        DefaultDataset dataset = new DefaultDataset();
        Dataset[] folds = dataset.folds(5, rg);
        Assert.assertNotNull(folds);
        Assert.assertEquals(5, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Assert.assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        DefaultDataset dataset = new DefaultDataset();
        int classIndex = dataset.classIndex(clazz);
        Assert.assertEquals(-1, classIndex);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        Object classValue = dataset.classValue(0);
        Assert.assertNull(classValue);
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        Dataset copy = dataset.copy();
        Assert.assertNotNull(copy);
        Assert.assertEquals(dataset.size(), copy.size());
    }
}