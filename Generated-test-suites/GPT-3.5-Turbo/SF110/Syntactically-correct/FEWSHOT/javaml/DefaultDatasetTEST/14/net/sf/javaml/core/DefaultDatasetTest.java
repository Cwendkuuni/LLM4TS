package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.Instance;

import org.junit.Test;

public class DefaultDatasetTest {
    
    @Test
    public void testDefaultDatasetWithCollection() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset(coll);
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes instanceof TreeSet<?>);
    }
    
    @Test
    public void testDefaultDataset() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes instanceof TreeSet<?>);
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        boolean result = dataset.addAll(coll);
        assertTrue(result);
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        boolean result = dataset.addAll(0, coll);
        assertTrue(result);
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertEquals(0, dataset.size());
        assertTrue(dataset.classes.isEmpty());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        boolean result = dataset.add(new Instance());
        assertTrue(result);
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(0, new Instance());
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.insertElementAt(new Instance(), 0);
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());
        dataset.setElementAt(new Instance(), 0);
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        Instance returnedInstance = dataset.instance(0);
        assertEquals(instance, returnedInstance);
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        SortedSet<Object> classes = dataset.classes();
        dataset.add(new Instance("class1"));
        dataset.add(new Instance("class2"));
        assertEquals(2, classes.size());
        assertEquals("class1", classes.first());
        assertEquals("class2", classes.last());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        Set<Instance> nearest = dataset.kNearest(2, inst, dm);
        assertEquals(2, nearest.size());
    }
    
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        vector.put(instance1, 0.5);
        vector.put(instance2, 0.8);
        double max = dataset.removeFarthest(vector);
        assertEquals(0.8, max, 0.0001);
        assertEquals(1, vector.size());
        assertTrue(vector.containsKey(instance1));
        assertFalse(vector.containsKey(instance2));
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        Dataset[] folds = dataset.folds(4, rg);
        assertEquals(4, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        int result = dataset.noAttributes();
        assertEquals(0, result);
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes.add("class1");
        dataset.classes.add("class2");
        int result = dataset.classIndex("class2");
        assertEquals(1, result);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes.add("class1");
        dataset.classes.add("class2");
        Object result = dataset.classValue(1);
        assertEquals("class2", result);
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        Dataset copy = dataset.copy();
        assertEquals(2, copy.size());
    }

}