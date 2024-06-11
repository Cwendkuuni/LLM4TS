package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.javaml.distance.DistanceMeasure;
import java.util.*;

public class DefaultDatasetTest {
    
    @Test
    public void testConstructor() {
        Collection<Instance> coll = new ArrayList<Instance>();
        DefaultDataset dataset = new DefaultDataset(coll);
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
        
        DefaultDataset dataset1 = new DefaultDataset();
        assertEquals(0, dataset1.maxAttributes);
        assertNotNull(dataset1.classes);
    }
    
    @Test
    public void testCheck() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        dataset.check(coll);
        assertEquals(0, dataset.maxAttributes);
        assertEquals(0, dataset.classes.size());
    }
    
    @Test
    public void testCheckInstance() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.check(instance);
        
        assertEquals(1, dataset.classes.size());
        assertTrue(dataset.classes.contains("class1"));
        assertEquals(5, dataset.maxAttributes);
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        coll.add(new Instance());
        
        DefaultDataset dataset = new DefaultDataset();
        boolean result = dataset.addAll(coll);
        
        assertTrue(result);
        assertEquals(0, dataset.maxAttributes);
        assertEquals(0, dataset.classes.size());
        assertEquals(3, dataset.size());
    }
    
    @Test
    public void testAddAllIndex() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        coll.add(new Instance());
        
        DefaultDataset dataset = new DefaultDataset();
        boolean result = dataset.addAll(0, coll);
        
        assertTrue(result);
        assertEquals(0, dataset.maxAttributes);
        assertEquals(0, dataset.classes.size());
        assertEquals(3, dataset.size());
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset  = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        dataset.clear();
        
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.classes.size());
        assertEquals(0, dataset.maxAttributes);
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        
        DefaultDataset dataset = new DefaultDataset();
        boolean result = dataset.add(instance);
        
        assertTrue(result);
        assertTrue(dataset.contains(instance));
        assertEquals(1, dataset.classes.size());
        assertEquals(5, dataset.maxAttributes);
    }
    
    @Test
    public void testAddAtIndex() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(0, instance);
        
        assertEquals(instance, dataset.instance(0));
        assertEquals(1, dataset.classes.size());
        assertEquals(5, dataset.maxAttributes);
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(instance);
        
        assertTrue(dataset.contains(instance));
        assertEquals(1, dataset.classes.size());
        assertEquals(5, dataset.maxAttributes);
    }
    
    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.insertElementAt(instance, 0);
        
        assertEquals(instance, dataset.instance(0));
        assertEquals(1, dataset.classes.size());
        assertEquals(5, dataset.maxAttributes);
    }
    
    @Test
    public void testSetElementAt() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        instance1.setNoAttributes(5);
        
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        instance2.setNoAttributes(3);
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        
        dataset.setElementAt(instance2, 0);
        
        assertEquals(instance2, dataset.instance(0));
        assertEquals(2, dataset.classes.size());
        assertEquals(5, dataset.maxAttributes);
    }
    
    @Test
    public void testInstance() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        instance1.setNoAttributes(5);
        
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        instance2.setNoAttributes(3);
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes.add("class1");
        dataset.classes.add("class2");
        dataset.classes.add("class3");
        
        SortedSet<Object> classes = dataset.classes();
        
        assertEquals(3, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
        assertTrue(classes.contains("class3"));
    }
    
    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        instance1.setNoAttributes(5);
        
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        instance2.setNoAttributes(3);
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        
        DistanceMeasure dm = new DistanceMeasure();
        Set<Instance> neighbors = dataset.kNearest(1, instance1, dm);
        
        assertEquals(1, neighbors.size());
        assertTrue(neighbors.contains(instance2));
    }
    
    @Test
    public void testRemoveFarthest() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        instance1.setNoAttributes(5);
        
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        instance2.setNoAttributes(3);
        
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        vector.put(instance1, 0.5);
        vector.put(instance2, 0.7);
        
        DefaultDataset dataset = new DefaultDataset();
        double max = dataset.removeFarthest(vector);
        
        assertEquals(0.7, max, 0.001);
        assertEquals(1, vector.size());
        assertTrue(vector.containsKey(instance1));
        assertFalse(vector.containsKey(instance2));
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        Random rg = new Random();
        Dataset[] folds = dataset.folds(2, rg);
        
        assertNotNull(folds);
        assertEquals(2, folds.length);
        assertNotNull(folds[0]);
        assertNotNull(folds[1]);
        assertTrue(folds[0] instanceof Fold);
        assertTrue(folds[1] instanceof Fold);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.noAttributes());
        
        Instance instance = new Instance();
        instance.setNoAttributes(5);
        dataset.add(instance);
        
        assertEquals(5, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes.add("class1");
        dataset.classes.add("class2");
        dataset.classes.add("class3");
        
        int index1 = dataset.classIndex("class1");
        int index2 = dataset.classIndex("class2");
        int index3 = dataset.classIndex("class3");
        int index4 = dataset.classIndex("class4");
        
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
        assertEquals(-1, index4);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes.add("class1");
        dataset.classes.add("class2");
        dataset.classes.add("class3");
        
        Object class1 = dataset.classValue(0);
        Object class2 = dataset.classValue(1);
        Object class3 = dataset.classValue(2);
        Object class4 = dataset.classValue(3);
        
        assertEquals("class1", class1);
        assertEquals("class2", class2);
        assertEquals("class3", class3);
        assertNull(class4);
    }
    
    @Test
    public void testCopy() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        instance1.setNoAttributes(5);
        
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        instance2.setNoAttributes(3);
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.classes.size(), copy.classes.size());
        assertEquals(dataset.maxAttributes, copy.maxAttributes);
        assertTrue(copy.contains(instance1));
        assertTrue(copy.contains(instance2));
    }
}