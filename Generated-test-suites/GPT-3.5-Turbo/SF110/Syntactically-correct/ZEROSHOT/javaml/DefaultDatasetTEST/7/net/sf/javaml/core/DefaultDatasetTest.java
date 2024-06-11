package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.distance.DistanceMeasure;
import java.util.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setup() {
        dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testAddAll() {
        Collection<Instance> collection = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        collection.add(instance1);
        collection.add(instance2);
        dataset.addAll(collection);
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> collection = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        collection.add(instance1);
        collection.add(instance2);
        dataset.addAll(0, collection);
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }

    @Test
    public void testClear() {
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testSetElementAt() {
        Instance instance = new Instance();
        dataset.setElementAt(instance, 0);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(3));
    }

    @Test
    public void testClasses() {
        SortedSet<Object> set = dataset.classes();
        assertNotNull(set);
    }

    @Test
    public void testKNearest() {
        Instance instance = new Instance();
        DistanceMeasure measure = new DistanceMeasure();
        Set<Instance> set = dataset.kNearest(2, instance, measure);
        assertNotNull(set);
    }

    @Test
    public void testFolds() {
        int numFolds = 3;
        Random random = new Random();
        Dataset[] folds = dataset.folds(numFolds, random);
        assertNotNull(folds);
        assertEquals(numFolds, folds.length);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Object clazz = "class";
        int index = dataset.classIndex(clazz);
        assertEquals(-1, index);
    }

    @Test
    public void testClassValue() {
        Object value = dataset.classValue(0);
        assertNull(value);
    }

    @Test
    public void testCopy() {
        Dataset copy = dataset.copy();
        assertNotNull(copy);
        assertEquals(dataset.size(), copy.size());
    }
}