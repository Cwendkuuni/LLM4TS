package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.distance.ManhattanDistance;

public class DefaultDatasetTest {

    DefaultDataset dataset;

    @Before
    public void setUp() throws Exception {
        dataset = new DefaultDataset();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testCollectionConstructor() {
        Collection<Instance> coll = new HashSet<Instance>();
        coll.add(new Instance(1));
        coll.add(new Instance(2));
        coll.add(new Instance(3));

        dataset = new DefaultDataset(coll);

        assertEquals(3, dataset.size());
        assertEquals(3, dataset.classes().size());
        assertEquals(1, dataset.noAttributes());
    }

    @Test
    public void testCheckCollection() {
        Collection<Instance> coll = new HashSet<Instance>();
        coll.add(new Instance(1));
        coll.add(new Instance(2));
        coll.add(new Instance(3));

        dataset.check(coll);

        assertEquals(3, dataset.classes().size());
        assertEquals(1, dataset.noAttributes());
    }

    @Test
    public void testCheckInstance() {
        dataset.check(new Instance(1));

        assertEquals(1, dataset.classes().size());
        assertEquals(1, dataset.noAttributes());
    }

    @Test
    public void testAddAll() {
        Collection<Instance> coll = new HashSet<Instance>();
        coll.add(new Instance(1));
        coll.add(new Instance(2));
        coll.add(new Instance(3));

        assertTrue(dataset.addAll(coll));
        assertEquals(3, dataset.size());
        assertEquals(3, dataset.classes().size());
        assertEquals(1, dataset.noAttributes());
    }

    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> coll = new HashSet<Instance>();
        coll.add(new Instance(1));
        coll.add(new Instance(2));
        coll.add(new Instance(3));

        assertTrue(dataset.addAll(0, coll));
        assertEquals(3, dataset.size());
        assertEquals(3, dataset.classes().size());
        assertEquals(1, dataset.noAttributes());
    }

    @Test
    public void testClear() {
        dataset.add(new Instance(1));
        dataset.add(new Instance(2));
        dataset.add(new Instance(3));

        dataset.clear();

        assertEquals(0, dataset.size());
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testAdd() {
        assertTrue(dataset.add(new Instance(1)));
        assertEquals(1, dataset.size());
        assertEquals(1, dataset.classes().size());
        assertEquals(1, dataset.noAttributes());
    }

    @Test
    public void testAddAtIndex() {
        dataset.add(new Instance(1));
        dataset.add(new Instance(2));
        dataset.add(new Instance(3));

        dataset.add(1, new Instance(4));

        assertEquals(4, dataset.size());
        assertEquals(4, dataset.classes().size());
        assertEquals(1, dataset.noAttributes());
        assertEquals(new Instance(4), dataset.get(1));
    }

    @Test
    public void testAddElement() {
        dataset.addElement(new Instance(1));

        assertEquals(1, dataset.size());
        assertEquals(1, dataset.classes().size());
        assertEquals(1, dataset.noAttributes());
    }

    @Test
    public void testInsertElementAt() {
        dataset.addElement(new Instance(1));
        dataset.addElement(new Instance(2));
        dataset.addElement(new Instance(3));

        dataset.insertElementAt(new Instance(4), 1);

        assertEquals(4, dataset.size());
        assertEquals(4, dataset.classes().size());
        assertEquals(1, dataset.noAttributes());
        assertEquals(new Instance(4), dataset.get(1));
    }

    @Test
    public void testSetElementAt() {
        dataset.addElement(new Instance(1));
        dataset.addElement(new Instance(2));
        dataset.addElement(new Instance(3));

        dataset.setElementAt(new Instance(4), 1);

        assertEquals(3, dataset.size());
        assertEquals(3, dataset.classes().size());
        assertEquals(1, dataset.noAttributes());
        assertEquals(new Instance(4), dataset.get(1));
    }

    @Test
    public void testInstance() {
        Instance instance1 = new Instance(1);
        Instance instance2 = new Instance(2);
        Instance instance3 = new Instance(3);

        dataset.addElement(instance1);
        dataset.addElement(instance2);
        dataset.addElement(instance3);

        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
        assertEquals(instance3, dataset.instance(2));
    }

    @Test
    public void testClasses() {
        TreeSet<Object> expectedClasses = new TreeSet<>();
        expectedClasses.add("Class1");
        expectedClasses.add("Class2");

        dataset.addElement(new Instance("Class1"));

        assertEquals(expectedClasses, dataset.classes());
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new Instance(1);
        Instance instance2 = new Instance(2);
        Instance instance3 = new Instance(3);
        Instance instance4 = new Instance(4);

        dataset.addElement(instance1);
        dataset.addElement(instance2);
        dataset.addElement(instance3);
        dataset.addElement(instance4);

        DistanceMeasure distanceMeasure = new ManhattanDistance();

        Set<Instance> nearest = dataset.kNearest(2, instance1, distanceMeasure);

        assertEquals(2, nearest.size());
        assertTrue(nearest.contains(instance2));
        assertTrue(nearest.contains(instance3));
        assertFalse(nearest.contains(instance1));
        assertFalse(nearest.contains(instance4));
    }

    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(new Instance(1), 1.0);
        vector.put(new Instance(2), 2.0);
        vector.put(new Instance(3), 3.0);
        vector.put(new Instance(4), 4.0);

        double farthest = dataset.removeFarthest(vector);

        assertEquals(4.0, farthest, 0.00001);
        assertEquals(3, vector.size());
        assertFalse(vector.containsKey(new Instance(4)));
    }

    @Test
    public void testFolds() {
        dataset.addElement(new Instance(1));
        dataset.addElement(new Instance(2));
        dataset.addElement(new Instance(3));
        dataset.addElement(new Instance(4));
        dataset.addElement(new Instance(5));
        dataset.addElement(new Instance(6));
        dataset.addElement(new Instance(7));
        dataset.addElement(new Instance(8));
        dataset.addElement(new Instance(9));

        int numFolds = 3;
        DefaultDataset[] folds = dataset.folds(numFolds, new Random());

        assertEquals(numFolds, folds.length);
        assertEquals(3, folds[0].size());
        assertEquals(3, folds[1].size());
        assertEquals(3, folds[2].size());
    }

    @Test
    public void testNoAttributes() {
        dataset.addElement(new Instance(1));

        assertEquals(1, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        dataset.addElement(new Instance("Class1"));
        dataset.addElement(new Instance("Class2"));
        dataset.addElement(new Instance("Class3"));

        assertEquals(0, dataset.classIndex("Class1"));
        assertEquals(1, dataset.classIndex("Class2"));
        assertEquals(2, dataset.classIndex("Class3"));
    }

    @Test
    public void testClassValue() {
        dataset.addElement(new Instance("Class1"));
        dataset.addElement(new Instance("Class2"));
        dataset.addElement(new Instance("Class3"));

        assertEquals("Class1", dataset.classValue(0));
        assertEquals("Class2", dataset.classValue(1));
        assertEquals("Class3", dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        dataset.addElement(new Instance(1));
        dataset.addElement(new Instance(2));
        dataset.addElement(new Instance(3));

        DefaultDataset copy = (DefaultDataset) dataset.copy();

        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.classes(), copy.classes());
        assertEquals(dataset.noAttributes(), copy.noAttributes());
        assertEquals(dataset.get(0), copy.get(0));
        assertEquals(dataset.get(1), copy.get(1));
        assertEquals(dataset.get(2), copy.get(2));
    }

}