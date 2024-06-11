package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import net.sf.javaml.distance.DistanceMeasure;

import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset);
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.maxAttributes);
    }

    @Test
    public void testDefaultDatasetCollectionConstructor() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset(coll);
        assertNotNull(dataset);
        assertFalse(dataset.isEmpty());
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.maxAttributes);
    }

    @Test
    public void testCheckCollection() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        dataset.check(coll);
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.maxAttributes);
    }

    @Test
    public void testCheckInstance() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        DefaultDataset dataset = new DefaultDataset();
        dataset.check(instance);
        assertEquals(1, dataset.classes().size());
        assertEquals(5, dataset.maxAttributes);
    }

    @Test
    public void testAddAll() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        boolean addedAll = dataset.addAll(coll);
        assertTrue(addedAll);
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.maxAttributes);
    }

    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        boolean addedAll = dataset.addAll(0, coll);
        assertTrue(addedAll);
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.maxAttributes);
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.clear();
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.maxAttributes);
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        DefaultDataset dataset = new DefaultDataset();
        boolean added = dataset.add(instance);
        assertTrue(added);
        assertEquals(1, dataset.classes().size());
        assertEquals(5, dataset.maxAttributes);
    }

    @Test
    public void testAddAtIndex() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(0, instance);
        assertEquals(1, dataset.classes().size());
        assertEquals(5, dataset.maxAttributes);
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(instance);
        assertEquals(1, dataset.classes().size());
        assertEquals(5, dataset.maxAttributes);
    }

    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        DefaultDataset dataset = new DefaultDataset();
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.classes().size());
        assertEquals(5, dataset.maxAttributes);
    }

    @Test
    public void testSetElementAt() {
        Instance instance = new Instance();
        instance.setClassValue("class1");
        instance.setNoAttributes(5);
        DefaultDataset dataset = new DefaultDataset();
        dataset.setElementAt(instance, 0);
        assertEquals(1, dataset.classes().size());
        assertEquals(5, dataset.maxAttributes);
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        Instance retrievedInstance = dataset.instance(0);
        assertNotNull(retrievedInstance);
        assertEquals(instance, retrievedInstance);
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        SortedSet<Object> classes = dataset.classes();
        assertNotNull(classes);
        assertEquals(0, classes.size());
    }

    @Test
    public void testKNearest() {
        Instance instance = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        DefaultDataset dataset = new DefaultDataset();
        Set<Instance> nearest = dataset.kNearest(5, instance, dm);
        assertNotNull(nearest);
        assertEquals(0, nearest.size());
    }

    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        vector.put(new Instance(), 1.0);
        vector.put(new Instance(), 0.5);
        vector.put(new Instance(), 0.8);
        DefaultDataset dataset = new DefaultDataset();
        double max = dataset.removeFarthest(vector);
        assertEquals(1.0, max, 0.0001);
        assertEquals(2, vector.size());
    }

    @Test
    public void testFolds() {
        Random rg = new Random();
        DefaultDataset dataset = new DefaultDataset();
        Dataset[] folds = dataset.folds(5, rg);
        assertNotNull(folds);
        assertEquals(5, folds.length);
        for (Dataset fold : folds) {
            assertNotNull(fold);
            assertTrue(fold instanceof Fold);
        }
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        int classIndex1 = dataset.classIndex("class1");
        int classIndex2 = dataset.classIndex("class2");
        assertEquals(0, classIndex1);
        assertEquals(1, classIndex2);
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
        dataset.add(new Instance());
        dataset.add(new Instance());
        Dataset copy = dataset.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof DefaultDataset);
        assertEquals(dataset.size(), copy.size());
    }

}