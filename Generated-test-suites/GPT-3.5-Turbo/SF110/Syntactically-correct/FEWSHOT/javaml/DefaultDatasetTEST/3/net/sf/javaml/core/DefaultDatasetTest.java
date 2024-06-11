package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.sf.javaml.distance.DistanceMeasure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

public class DefaultDatasetTest {

    @Test
    public void testConstructor() {
        DefaultDataset dataset1 = new DefaultDataset();
        DefaultDataset dataset2 = new DefaultDataset(new Vector<Instance>());
        assertEquals(0, dataset1.size());
        assertEquals(0, dataset1.classes().size());
        assertEquals(0, dataset2.size());
        assertEquals(0, dataset2.classes().size());
    }

    @Test
    public void testAddAll() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> coll = new Vector<Instance>();
        coll.add(instance1);
        coll.add(instance2);
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.addAll(coll));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testClear() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.classes().size());
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.add(instance));
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
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.setElementAt(instance2, 0);
        assertEquals(instance2, dataset.instance(0));
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
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instance1.setClassValue("class1");
        instance2.setClassValue("class2");
        Collection<Instance> coll = new Vector<Instance>();
        coll.add(instance1);
        coll.add(instance2);
        DefaultDataset dataset = new DefaultDataset(coll);
        SortedSet<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
    }

    @Test
    public void testKNearest() {
        Instance inst = new Instance();
        Instance tmp1 = new Instance();
        Instance tmp2 = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(inst);
        dataset.add(tmp1);
        dataset.add(tmp2);
        Set<Instance> nearest = dataset.kNearest(2, inst, dm);
        assertEquals(2, nearest.size());
        assertTrue(nearest.contains(tmp1));
        assertTrue(nearest.contains(tmp2));
    }

    @Test
    public void testFolds() {
        Random rg = new Random();
        DefaultDataset dataset = new DefaultDataset();
        Dataset[] folds = dataset.folds(3, rg);
        assertEquals(3, folds.length);
    }

    @Test
    public void testNoAttributes() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        assertEquals(instance.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Object clazz1 = new Object();
        Object clazz2 = new Object();
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes().add(clazz1);
        dataset.classes().add(clazz2);
        assertEquals(0, dataset.classIndex(clazz1));
        assertEquals(1, dataset.classIndex(clazz2));
        assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        Object clazz1 = new Object();
        Object clazz2 = new Object();
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes().add(clazz1);
        dataset.classes().add(clazz2);
        assertEquals(clazz1, dataset.classValue(0));
        assertEquals(clazz2, dataset.classValue(1));
        assertNull(dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        Dataset copy = dataset.copy();
        assertEquals(1, copy.size());
        assertEquals(instance, copy.instance(0));
    }

}