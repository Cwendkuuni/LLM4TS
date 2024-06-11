package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import net.sf.javaml.distance.DistanceMeasure;
import org.junit.Test;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDataset() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
        assertTrue(dataset.isEmpty());
    }
    
    @Test
    public void testDefaultDatasetWithCollection() {
        Collection<Instance> collection = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        collection.add(instance1);
        collection.add(instance2);
        
        DefaultDataset dataset = new DefaultDataset(collection);
        
        assertEquals(collection.size(), dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }
    
    @Test
    public void testCheck() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.check(instance1);
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
        
        instance1.setClassValue(1);
        dataset.check(instance1);
        assertEquals(1, dataset.maxAttributes);
        assertFalse(dataset.classes.isEmpty());
        
        dataset.check(instance2);
        assertEquals(1, dataset.maxAttributes);
        assertFalse(dataset.classes.isEmpty());
    }
    
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> collection = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        collection.add(instance1);
        collection.add(instance2);
        
        dataset.addAll(collection);
        
        assertEquals(collection.size(), dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }
    
    @Test
    public void testAddAllWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> collection = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        collection.add(instance1);
        collection.add(instance2);
        
        dataset.addAll(0, collection);
        
        assertEquals(collection.size(), dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue(1);
        dataset.add(instance1);
        
        dataset.clear();
        
        assertTrue(dataset.isEmpty());
        assertTrue(dataset.classes.isEmpty());
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        
        dataset.add(instance1);
        
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }
    
    @Test
    public void testAddWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        
        dataset.add(0, instance1);
        
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        
        dataset.addElement(instance1);
        
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        
        dataset.insertElementAt(instance1, 0);
        
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        
        dataset.setElementAt(instance1, 0);
        
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }
    
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.add(instance1);
        
        assertEquals(instance1, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        TreeSet<Object> classes = new TreeSet<>();
        classes.add(1);
        classes.add(2);
        dataset.classes = classes;
        
        assertEquals(classes, dataset.classes());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        DistanceMeasure dm = new DistanceMeasure();
        
        Set<Instance> nearest = dataset.kNearest(1, instance1, dm);
        
        assertTrue(nearest.contains(instance2));
    }
    
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Map<Instance, Double> vector = new HashMap<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        vector.put(instance1, 1.0);
        vector.put(instance2, 2.0);
        
        double farthest = dataset.removeFarthest(vector);
        
        assertEquals(2.0, farthest, 0.001);
        assertFalse(vector.containsValue(farthest));
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        int numFolds = 3;
        
        Dataset[] folds = dataset.folds(numFolds, rg);
        
        assertEquals(numFolds, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.addAttribute("attribute1");
        dataset.add(instance1);
        
        int noAttributes = dataset.noAttributes();
        
        assertEquals(1, noAttributes);
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        TreeSet<Object> classes = new TreeSet<>();
        classes.add(1);
        classes.add(2);
        dataset.classes = classes;
        
        int classIndex1 = dataset.classIndex(1);
        int classIndex2 = dataset.classIndex(2);
        
        assertEquals(0, classIndex1);
        assertEquals(1, classIndex2);
        assertEquals(-1, dataset.classIndex(null));
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue(1);
        Instance instance2 = new Instance();
        instance2.setClassValue(2);
        dataset.add(instance1);
        dataset.add(instance2);
        
        Object classValue1 = dataset.classValue(0);
        Object classValue2 = dataset.classValue(1);
        
        assertEquals(1, classValue1);
        assertEquals(2, classValue2);
        assertNull(dataset.classValue(2));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue(1);
        dataset.add(instance1);
        
        Dataset copy = dataset.copy();
        
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.classes, copy.classes);
        assertEquals(dataset.maxAttributes, copy.maxAttributes);
    }
}