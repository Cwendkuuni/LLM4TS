package net.sf.javaml.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;

import static org.junit.Assert.*;

public class DefaultDatasetTest {

    @Test
    public void testConstructor_withCollection_shouldAddInstances() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        List<Instance> instances = new ArrayList<>(Arrays.asList(instance1, instance2));

        DefaultDataset dataset = new DefaultDataset(instances);

        assertEquals(2, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testConstructor_withoutCollection_shouldCreateEmptyDataset() {
        DefaultDataset dataset = new DefaultDataset();

        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAddAll_shouldAddAllInstances() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        List<Instance> instances = new ArrayList<>(Arrays.asList(instance1, instance2));

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(instances);

        assertEquals(2, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testAddAll_withIndex_shouldAddAllInstancesAtIndex() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        List<Instance> instances = new ArrayList<>(Arrays.asList(instance1, instance2));

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(0, instances);

        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }

    @Test
    public void testClear_shouldClearClassesAndInstances() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        List<Instance> instances = new ArrayList<>(Arrays.asList(instance1, instance2));

        DefaultDataset dataset = new DefaultDataset(instances);
        dataset.clear();

        assertTrue(dataset.isEmpty());
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testAdd_shouldAddInstance() {
        Instance instance = new Instance(new double[]{1.0, 2.0}, "class1");

        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);

        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testAdd_withIndex_shouldAddInstanceAtIndex() {
        Instance instance = new Instance(new double[]{1.0, 2.0}, "class1");

        DefaultDataset dataset = new DefaultDataset();
        dataset.add(0, instance);

        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testAddElement_shouldAddInstance() {
        Instance instance = new Instance(new double[]{1.0, 2.0}, "class1");

        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(instance);

        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testInsertElementAt_shouldInsertInstanceAtGivenIndex() {
        Instance instance = new Instance(new double[]{1.0, 2.0}, "class1");

        DefaultDataset dataset = new DefaultDataset();
        dataset.insertElementAt(instance, 0);

        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testSetElementAt_shouldSetInstanceAtGivenIndex() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");

        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.setElementAt(instance2, 0);

        assertEquals(1, dataset.size());
        assertEquals(instance2, dataset.get(0));
    }

    @Test
    public void testInstance_shouldReturnInstanceAtIndex() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2));

        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }

    @Test
    public void testClasses_shouldReturnSortedSetOfClasses() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2));

        SortedSet<Object> classes = dataset.classes();

        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
    }

    @Test
    public void testKNearest_shouldReturnKNearestInstances() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        Instance instance3 = new Instance(new double[]{5.0, 6.0}, "class3");
        Instance instance4 = new Instance(new double[]{7.0, 8.0}, "class4");

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

        Set<Instance> nearest = dataset.kNearest(2, instance1, new EuclideanDistance());

        assertEquals(2, nearest.size());
        assertTrue(nearest.contains(instance2));
        assertTrue(nearest.contains(instance3));
    }

    @Test
    public void testRemoveFarthest_shouldRemoveFarthestInstanceFromMap() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        Instance instance3 = new Instance(new double[]{5.0, 6.0}, "class3");

        Map<Instance, Double> vector = new HashMap<>();
        vector.put(instance1, 1.0);
        vector.put(instance2, 2.0);
        vector.put(instance3, 3.0);

        DefaultDataset dataset = new DefaultDataset();
        double max = dataset.removeFarthest(vector);

        assertEquals(2.0, max, 0.001);
        assertFalse(vector.containsKey(instance3));
    }

    @Test
    public void testFolds_shouldReturnArrayOfFolds() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        Instance instance3 = new Instance(new double[]{5.0, 6.0}, "class3");
        Instance instance4 = new Instance(new double[]{7.0, 8.0}, "class4");

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

        Random random = new Random(1);
        Dataset[] folds = dataset.folds(2, random);

        assertEquals(2, folds.length);
        assertNotNull(folds[0]);
        assertNotNull(folds[1]);
    }

    @Test
    public void testNoAttributes_withEmptyDataset_shouldReturnZero() {
        DefaultDataset dataset = new DefaultDataset();

        int noAttributes = dataset.noAttributes();

        assertEquals(0, noAttributes);
    }

    @Test
    public void testNoAttributes_withDataset_shouldReturnNumberOfAttributes() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        List<Instance> instances = new ArrayList<>(Arrays.asList(instance1, instance2));

        DefaultDataset dataset = new DefaultDataset(instances);

        int noAttributes = dataset.noAttributes();

        assertEquals(2, noAttributes);
    }

    @Test
    public void testClassIndex_withNullClass_shouldReturnMinusOne() {
        DefaultDataset dataset = new DefaultDataset();

        int classIndex = dataset.classIndex(null);

        assertEquals(-1, classIndex);
    }

    @Test
    public void testClassIndex_withClass_shouldReturnIndexOfClass() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        List<Instance> instances = new ArrayList<>(Arrays.asList(instance1, instance2));

        DefaultDataset dataset = new DefaultDataset(instances);

        int classIndex = dataset.classIndex("class2");

        assertEquals(1, classIndex);
    }

    @Test
    public void testClassValue_withIndex_shouldReturnClassValueAtIndex() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        List<Instance> instances = new ArrayList<>(Arrays.asList(instance1, instance2));

        DefaultDataset dataset = new DefaultDataset(instances);

        Object classValue = dataset.classValue(1);

        assertEquals("class2", classValue);
    }

    @Test
    public void testClassValue_withInvalidIndex_shouldReturnNull() {
        DefaultDataset dataset = new DefaultDataset();

        Object classValue = dataset.classValue(0);

        assertNull(classValue);
    }

    @Test
    public void testCopy_shouldReturnCopyOfDataset() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0}, "class1");
        Instance instance2 = new Instance(new double[]{3.0, 4.0}, "class2");
        List<Instance> instances = new ArrayList<>(Arrays.asList(instance1, instance2));

        DefaultDataset dataset = new DefaultDataset(instances);

        Dataset copy = dataset.copy();

        assertEquals(dataset.size(), copy.size());
        assertNotSame(dataset, copy);

        for (int i = 0; i < dataset.size(); i++) {
            assertNotSame(dataset.instance(i), copy.instance(i));
            assertEquals(dataset.instance(i), copy.instance(i));
        }
    }

}