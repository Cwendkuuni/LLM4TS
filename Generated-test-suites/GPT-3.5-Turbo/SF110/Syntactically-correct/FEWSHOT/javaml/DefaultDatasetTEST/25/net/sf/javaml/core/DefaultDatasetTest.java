package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testAddAll() {
        Instance instance = new Instance();
        dataset.addAll(Arrays.asList(instance));
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testAddAllAtIndex() {
        Instance instance = new Instance();
        dataset.addAll(0, Arrays.asList(instance));
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testClear() {
        dataset.addAll(Arrays.asList(new Instance()));
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testAddAtIndex() {
        Instance instance = new Instance();
        dataset.add(0, instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testSetElementAt() {
        Instance instance = new Instance();
        dataset.add(new Instance());
        dataset.setElementAt(instance, 0);
        assertEquals(2, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        dataset.add(new Instance("A"));
        dataset.add(new Instance("B"));
        dataset.add(new Instance("C"));
        assertEquals(3, dataset.classes().size());
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));
        DistanceMeasure dm = new EuclideanDistance();
        assertEquals(2, dataset.kNearest(2, instance1, dm).size());
    }

    @Test
    public void testFolds() {
        dataset.addAll(Arrays.asList(new Instance(), new Instance(), new Instance(), new Instance()));
        Random rg = new Random();
        Dataset[] folds = dataset.folds(2, rg);
        assertEquals(2, folds.length);
        assertEquals(2, folds[0].size());
        assertEquals(2, folds[1].size());
    }

    @Test
    public void testNoAttributes() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        dataset.add(new Instance("A"));
        dataset.add(new Instance("B"));
        dataset.add(new Instance("C"));
        assertEquals(0, dataset.classIndex("A"));
        assertEquals(1, dataset.classIndex("B"));
        assertEquals(2, dataset.classIndex("C"));
        assertEquals(-1, dataset.classIndex("D"));
    }

    @Test
    public void testClassValue() {
        dataset.add(new Instance("A"));
        dataset.add(new Instance("B"));
        dataset.add(new Instance("C"));
        assertEquals("A", dataset.classValue(0));
        assertEquals("B", dataset.classValue(1));
        assertEquals("C", dataset.classValue(2));
        assertNull(dataset.classValue(3));
    }

    @Test
    public void testCopy() {
        Instance instance = new Instance();
        dataset.add(instance);
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertEquals(instance, copy.get(0));
    }

}