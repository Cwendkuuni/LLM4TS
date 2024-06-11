package net.sf.javaml.core;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DefaultDatasetTest {
    
    @Test
    public void testConstructor1() {
        List<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        DefaultDataset dataset = new DefaultDataset(instances);
        
        assertEquals(2, dataset.size());       
    }
    
    @Test
    public void testConstructor2() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        
        List<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        dataset.addAll(instances);
        
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        
        List<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        dataset.addAll(0, instances);
        
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        
        dataset.clear();
        
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        
        Instance instance = new Instance();
        
        dataset.add(instance);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        
        Instance instance = new Instance();
        
        dataset.add(0, instance);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        
        Instance instance = new Instance();
        
        dataset.addElement(instance);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        
        Instance instance = new Instance();
        
        dataset.insertElementAt(instance, 0);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        
        Instance instance = new Instance();
        
        dataset.setElementAt(instance, 0);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        
        Instance instance = new Instance();
        
        dataset.add(instance);
        
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        
        Instance instance = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        
        Set<Instance> nearest = dataset.kNearest(3, instance, dm);
        
        assertNotNull(nearest);
    }
    
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        
        Map<Instance, Double> vector = new HashMap<>();
        Instance instance = new Instance();
        double distance = 1.0;
        vector.put(instance, distance);
        
        double farthest = dataset.removeFarthest(vector);
        
        assertEquals(0.0, farthest, 0.001);
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        
        int numFolds = 3;
        Random rg = new Random();
        
        Dataset[] folds = dataset.folds(numFolds, rg);
        
        assertNotNull(folds);
        assertEquals(numFolds, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        
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
        
        Object classValue = dataset.classValue(0);
        
        assertNull(classValue);
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        
        Dataset copy = dataset.copy();
        
        assertNotNull(copy);
    }
}