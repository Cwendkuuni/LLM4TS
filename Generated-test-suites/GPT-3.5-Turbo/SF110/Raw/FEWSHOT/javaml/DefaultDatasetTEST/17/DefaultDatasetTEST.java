package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DefaultDatasetTest {
    @Test
    public void testConstructor() {
        DefaultDataset dataset1 = new DefaultDataset();
        DefaultDataset dataset2 = new DefaultDataset(new ArrayList<Instance>());
        
        assertEquals(0, dataset1.size());
        assertEquals(0, dataset2.size());
        assertNull(dataset1.classes());
        assertNull(dataset2.classes());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }
    
    @Test
    public void testAddAll() {
        DefaultDataset dataset1 = new DefaultDataset();
        DefaultDataset dataset2 = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        List<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);
        
        dataset1.addAll(instances);
        dataset2.addAll(0, instances);
        
        assertEquals(2, dataset1.size());
        assertEquals(instance1, dataset1.get(0));
        assertEquals(instance2, dataset1.get(1));
        
        assertEquals(2, dataset2.size());
        assertEquals(instance1, dataset2.get(0));
        assertEquals(instance2, dataset2.get(1));
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        dataset.clear();
        
        assertEquals(0, dataset.size());
        assertNull(dataset.classes());
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.insertElementAt(instance1, 0);
        dataset.insertElementAt(instance2, 1);
        
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        Instance newInstance = new Instance();
        
        dataset.setElementAt(newInstance, 1);
        
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(newInstance, dataset.get(1));
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
        
        assertNull(dataset.classes());
    }
    
    @Test
    public void testKNarest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        DistanceMeasure measure = new EuclideanDistance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        Set<Instance> nearest = dataset.kNearest(1, instance1, measure);
        
        assertEquals(1, nearest.size());
        assertTrue(nearest.contains(instance2));
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random random = new Random();
        
        Dataset[] folds = dataset.folds(3, random);
        
        assertEquals(3, folds.length);
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
        Object clazz = new Object();
        
        int classIndex = dataset.classIndex(clazz);
        
        assertEquals(-1, classIndex);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Object clazz1 = new Object();
        Object clazz2 = new Object();
        
        dataset.classes().add(clazz1);
        dataset.classes().add(clazz2);
        
        assertEquals(clazz1, dataset.classValue(0));
        assertEquals(clazz2, dataset.classValue(1));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        Dataset copy = dataset.copy();
        
        assertEquals(2, copy.size());
        assertEquals(instance1, copy.get(0));
        assertEquals(instance2, copy.get(1));
    }
}