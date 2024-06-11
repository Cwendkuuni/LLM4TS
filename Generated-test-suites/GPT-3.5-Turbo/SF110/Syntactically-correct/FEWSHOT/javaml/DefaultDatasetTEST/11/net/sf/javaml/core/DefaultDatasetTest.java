package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class DefaultDatasetTest {
    private DefaultDataset dataset;
    private Collection<Instance> instances;

    @Before
    public void setup() {
        instances = new ArrayList<>();
        dataset = new DefaultDataset(instances);
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(dataset.classes());
        assertEquals(0, dataset.maxAttributes);
    }

    @Test
    public void testCollectionConstructor() {
        instances.add(new Instance());
        dataset = new DefaultDataset(instances);
        assertNotNull(dataset.classes());
        assertEquals(0, dataset.maxAttributes);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testCheck() {
        dataset.check(new Instance());
        assertEquals(1, dataset.classes().size());
        assertEquals(1, dataset.maxAttributes);
    }

    @Test
    public void testAddAll() {
        instances.add(new Instance());
        instances.add(new Instance());
        dataset.addAll(instances);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        instances.add(new Instance());
        instances.add(new Instance());
        dataset.addAll(0, instances);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testClear() {
        instances.add(new Instance());
        instances.add(new Instance());
        dataset.addAll(instances);
        dataset.clear();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.classes().size());
    }

    @Test
    public void testAdd() {
        dataset.add(new Instance());
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAtIndex() {
        instances.add(new Instance());
        dataset.add(0, new Instance());
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddElement() {
        dataset.addElement(new Instance());
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInsertElementAt() {
        instances.add(new Instance());
        dataset.insertElementAt(new Instance(), 0);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testSetElementAt() {
        instances.add(new Instance());
        dataset.setElementAt(new Instance(), 0);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        instances.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        assertNotNull(dataset.classes());
    }

    @Test
    public void testKNearest() {
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        Set<Instance> nearest = dataset.kNearest(1, inst, dm);
        assertEquals(0, nearest.size());
    }

    @Test
    public void testRemoveFarthest() {
        instances.add(new Instance());
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(new Instance(), 0.0);
        double farthest = dataset.removeFarthest(vector);
        assertEquals(0.0, farthest, 0.0001);
        assertEquals(0, vector.size());
    }

    @Test
    public void testFolds() {
        int numFolds = 2;
        Random rg = new Random();
        Dataset[] folds = dataset.folds(numFolds, rg);
        assertNotNull(folds);
        assertEquals(numFolds, folds.length);
    }

    @Test
    public void testNoAttributesWithEmptyDataset() {
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testNoAttributesWithNonEmptyDataset() {
        instances.add(new Instance());
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndexWithNullClass() {
        assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testClassIndexWithNonNullClass() {
        Object testClass = new Object();
        dataset.classes().add(testClass);
        assertEquals(0, dataset.classIndex(testClass));
    }

    @Test
    public void testClassValue() {
        Object testClass = new Object();
        dataset.classes().add(testClass);
        assertEquals(testClass, dataset.classValue(0));
    }

    @Test
    public void testCopy() {
        Instance instance = new Instance();
        instance.setAttribute("attribute", 1);
        instances.add(instance);
        Dataset copy = dataset.copy();
        assertNotEquals(dataset, copy);
        assertEquals(dataset.size(), copy.size());
    }
}