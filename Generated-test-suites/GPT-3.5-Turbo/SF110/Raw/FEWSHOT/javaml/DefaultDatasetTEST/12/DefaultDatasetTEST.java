package net.sf.javaml.core;

import org.junit.Test;

import static org.junit.Assert.*;

import net.sf.javaml.distance.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class DefaultDatasetTest {
    
    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
        assertTrue(dataset.classes().isEmpty());
    }
    
    @Test
    public void testConstructorWithCollection() {
        Collection<Instance> instances = new ArrayList<>();
        
        DefaultDataset dataset = new DefaultDataset(instances);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
        assertTrue(dataset.classes().isEmpty());
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(instances);
        
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(0, instances);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testClear() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        
        DefaultDataset dataset = new DefaultDataset(instances);
        dataset.clear();
        
        assertEquals(0, dataset.size());
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddAtIndex() {
        Instance instance = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(0, instance);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(instance);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.insertElementAt(instance, 0);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testSetElementAt() {
        Instance instance = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.setElementAt(instance, 0);
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        
        SortedSet<Object> classes = dataset.classes();
        
        assertNotNull(classes);
        assertEquals(0, classes.size());
    }
    
    @Test
    public void testKNearest() {
        int k = 3;
        Instance instance = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        Set<Instance> nearest = dataset.kNearest(k, instance, dm);
        
        assertNotNull(nearest);
        assertEquals(k, nearest.size());
    }
    
    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(new Instance(), 1.0);
        vector.put(new Instance(), 2.0);
        vector.put(new Instance(), 3.0);
        
        DefaultDataset dataset = new DefaultDataset();
        
        double max = dataset.removeFarthest(vector);
        
        assertEquals(3.0, max, 0.001);
        assertEquals(2, vector.size());
    }
    
    @Test
    public void testFolds() {
        int numFolds = 3;
        Random rg = new Random();
        
        DefaultDataset dataset = new DefaultDataset();
        
        Dataset[] folds = dataset.folds(numFolds, rg);
        
        assertNotNull(folds);
        assertEquals(numFolds, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        
        int noAttributes = dataset.noAttributes();
        
        assertEquals(0, noAttributes);
    }
    
    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        
        DefaultDataset dataset = new DefaultDataset();
        
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
        dataset.add(new Instance());
        
        Dataset copy = dataset.copy();
        
        assertNotNull(copy);
        assertNotSame(dataset, copy);
        assertEquals(dataset.size(), copy.size());
    }
    
}