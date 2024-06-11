package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.distance.DistanceMeasure;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    private Instance instance;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
        instance = new Instance();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0, dataset.noAttributes());
        assertNull(dataset.classIndex(null));
    }

    @Test
    public void testConstructorWithCollection() {
        List<Instance> instances = new ArrayList<Instance>();
        instances.add(instance);
        dataset = new DefaultDataset(instances);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAll() {
        List<Instance> instances = new ArrayList<Instance>();
        instances.add(instance);
        dataset.addAll(instances);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        List<Instance> instances = new ArrayList<Instance>();
        instances.add(instance);
        dataset.addAll(0, instances);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testClear() {
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAdd() {
        dataset.add(instance);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAtIndex() {
        dataset.add(0, instance);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddElement() {
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInsertElementAt() {
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testSetElementAt() {
        dataset.add(instance);
        dataset.setElementAt(new Instance(), 0);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInstance() {
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        assertEquals(0, dataset.classes().size());
    }

    @Test
    public void testKNearest() {
        DefaultDataset otherDataset = new DefaultDataset();
        Instance otherInstance = new Instance();
        DistanceMeasure distanceMeasure = new DistanceMeasure();

        dataset.add(instance);
        otherDataset.add(otherInstance);

        assertEquals(otherInstance, dataset.kNearest(1, instance, distanceMeasure).toArray()[0]);
    }

    @Test
    public void testFolds() {
        assertEquals(0, dataset.folds(2, new Random()).length);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        assertNull(dataset.classValue(0));
    }

    @Test
    public void testCopy() {
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        assertEquals(0, copy.size());
    }
}