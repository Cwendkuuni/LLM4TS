package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DefaultDatasetTest {
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addAll(Arrays.asList(instance1, instance2));
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addAll(0, Arrays.asList(instance1, instance2));
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.add(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(0, instance2);
        assertEquals(2, dataset.size());
        assertEquals(instance2, dataset.instance(0));
        assertEquals(instance1, dataset.instance(1));
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
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
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.setElementAt(instance2, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance2, dataset.instance(0));
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        
        Set<Instance> nearest = dataset.kNearest(3, inst, dm);
        assertEquals(0, nearest.size());
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        
        Dataset[] folds = dataset.folds(5, rg);
        assertEquals(5, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.add(instance);
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
        Instance instance = new Instance();
        
        dataset.add(instance);
        Object classValue = dataset.classValue(0);
        assertNull(classValue);
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        
        dataset.add(instance);
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.instance(0), copy.instance(0));
    }
}