package net.sf.javaml.core;

import org.junit.*;
import static org.junit.Assert.*;
import net.sf.javaml.distance.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        // Test addAll method
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        DefaultDataset dataset1 = new DefaultDataset();
        dataset1.addAll(dataset);
        assertEquals(2, dataset1.size());
    }

    @Test
    public void testAdd() {
        // Test add method
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testClear() {
        // Test clear method
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testInstance() {
        // Test instance method
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        Instance result = dataset.instance(0);
        assertEquals(instance, result);
    }

    @Test
    public void testClasses() {
        // Test classes method
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("Class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("Class2");
        dataset.add(instance1);
        dataset.add(instance2);
        SortedSet<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
    }

    @Test
    public void testKNearest() {
        // Test kNearest method
        DefaultDataset dataset = new DefaultDataset();
        Instance inst = new Instance();
        dataset.add(inst);
        DistanceMeasure dm = new EuclideanDistance();
        Set<Instance> result = dataset.kNearest(1, inst, dm);
        assertEquals(0, result.size());
    }

    @Test
    public void testFolds() {
        // Test folds method
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        Dataset[] folds = dataset.folds(2, new Random());
        assertEquals(2, folds.length);
    }

    @Test
    public void testNoAttributes() {
        // Test noAttributes method
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        int result = dataset.noAttributes();
        assertEquals(0, result);
    }

    @Test
    public void testClassIndex() {
        // Test classIndex method
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("Class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("Class2");
        dataset.add(instance1);
        dataset.add(instance2);
        int result = dataset.classIndex("Class2");
        assertEquals(1, result);
    }

    @Test
    public void testClassValue() {
        // Test classValue method
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("Class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("Class2");
        dataset.add(instance1);
        dataset.add(instance2);
        Object result = dataset.classValue(1);
        assertEquals("Class2", result);
    }

    @Test
    public void testCopy() {
        // Test copy method
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        Dataset result = dataset.copy();
        assertEquals(dataset.size(), result.size());
    }

}