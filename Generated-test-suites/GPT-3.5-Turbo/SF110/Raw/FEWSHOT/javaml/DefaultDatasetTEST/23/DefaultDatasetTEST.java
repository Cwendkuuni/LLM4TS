
package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testConstructor1() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.classes().isEmpty());
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testConstructor2() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        
        assertFalse(dataset.classes().isEmpty());
        assertEquals(3, dataset.size());
        assertEquals(0, dataset.classIndex(null));
        assertNotNull(dataset.classValue(0));
    }
    
    @Test
    public void testCheckCollection() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        
        dataset.check(Arrays.asList(instance1, instance2, instance3));
        assertFalse(dataset.classes().isEmpty());
        assertEquals(3, dataset.noAttributes());
    }
    
    @Test
    public void testCheckInstance() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        
        dataset.check(instance);
        assertTrue(dataset.classes().isEmpty());
        assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testAddAll1() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2, instance3)));
        assertFalse(dataset.classes().isEmpty());
        assertEquals(3, dataset.size());
        assertEquals(0, dataset.classIndex(null));
        assertNotNull(dataset.classValue(0));
    }
    
    @Test
    public void testAddAll2() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
        
        assertTrue(dataset.addAll(1, Arrays.asList(instance3)));
        assertFalse(dataset.classes().isEmpty());
        assertEquals(3, dataset.size());
        assertEquals(0, dataset.classIndex(null));
        assertNotNull(dataset.classValue(0));
    }
    
    @Test
    public void testClear() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance));
        
        dataset.clear();
        assertTrue(dataset.classes().isEmpty());
        assertTrue(dataset.isEmpty());
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        
        assertTrue(dataset.add(instance));
        assertTrue(dataset.classes().isEmpty());
        assertEquals(1, dataset.noAttributes());
    }
    
    @Test
    public void testAddWithIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
        
        dataset.add(1, new Instance());
        assertFalse(dataset.classes().isEmpty());
        assertEquals(3, dataset.size());
        assertEquals(0, dataset.classIndex(null));
        assertNotNull(dataset.classValue(0));
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        
        dataset.addElement(instance);
        assertTrue(dataset.classes().isEmpty());
        assertEquals(1, dataset.noAttributes());
    }
    
    @Test
    public void testInsertElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
        
        dataset.insertElementAt(new Instance(), 1);
        assertFalse(dataset.classes().isEmpty());
        assertEquals(3, dataset.size());
        assertEquals(0, dataset.classIndex(null));
        assertNotNull(dataset.classValue(0));
    }
    
    @Test
    public void testSetElementAt() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(new Instance()));
        
        dataset.setElementAt(instance, 0);
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance));
        
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        
        Set<Instance> nearest = dataset.kNearest(2, instance1, new EuclideanDistance());
        assertEquals(2, nearest.size());
        assertFalse(nearest.contains(instance1));
    }
    
    @Test
    public void testRemoveFarthest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        
        Map<Instance, Double> closest = new HashMap<>();
        closest.put(instance1, 1.0);
        closest.put(instance2, 2.0);
        closest.put(instance3, 3.0);
        
        double max = dataset.removeFarthest(closest);
        assertEquals(3.0, max, 0.0);
        assertFalse(closest.containsKey(instance3));
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(new Instance(), new Instance(), new Instance()));
        
        Dataset[] folds = dataset.folds(2, new Random());
        assertEquals(2, folds.length);
        assertFalse(folds[0].isEmpty());
        assertFalse(folds[1].isEmpty());
    }
    
    @Test
    public void testCopy() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
        
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertNotSame(dataset, copy);
        assertNotSame(dataset.get(0), copy.get(0));
    }

}

