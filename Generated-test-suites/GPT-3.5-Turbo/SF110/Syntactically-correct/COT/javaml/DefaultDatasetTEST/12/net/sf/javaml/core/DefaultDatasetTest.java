import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DefaultDatasetTest {
    DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAll() {
        Collection<Instance> collection = new ArrayList<>();
        Instance instance = new Instance();
        collection.add(instance);
        assertTrue(dataset.addAll(collection));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> collection = new ArrayList<>();
        Instance instance = new Instance();
        collection.add(instance);
        dataset.add(new Instance());
        assertTrue(dataset.addAll(1, collection));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testSetElementAt() {
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.setElementAt(new Instance(), 0);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        SortedSet<Object> classes = dataset.classes();
        assertNotNull(classes);
    }

    @Test
    public void testKNearest() {
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        Set<Instance> nearest = dataset.kNearest(3, inst, dm);
        assertNotNull(nearest);
    }

    @Test
    public void testFolds() {
        Random rg = new Random();
        Dataset[] folds = dataset.folds(5, rg);
        assertNotNull(folds);
        assertEquals(5, folds.length);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        assertEquals(-1, dataset.classIndex(clazz));
    }

    @Test
    public void testClassValue() {
        Object clazz = new Object();
        assertNull(dataset.classValue(0));
    }

    @Test
    public void testCopy() {
        DefaultDataset copy = dataset.copy();
        assertNotNull(copy);
        assertEquals(0, copy.size());
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test(expected = NullPointerException.class)
    public void testAddNull() {
        dataset.add(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllNull() {
        dataset.addAll(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAllAtIndexInvalidIndex() {
        Collection<Instance> collection = new ArrayList<>();
        collection.add(new Instance());
        dataset.addAll(1, collection);
    }

    @Test(expected = NullPointerException.class)
    public void testInsertElementAtNull() {
        dataset.insertElementAt(null, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetElementAtInvalidIndex() {
        dataset.setElementAt(new Instance(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testKNearestInvalidK() {
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        dataset.kNearest(0, inst, dm);
    }

    @Test(expected = NullPointerException.class)
    public void testKNearestNullInstance() {
        DistanceMeasure dm = new DistanceMeasure();
        dataset.kNearest(3, null, dm);
    }

    @Test(expected = NullPointerException.class)
    public void testKNearestNullDistanceMeasure() {
        Instance inst = new Instance();
        dataset.kNearest(3, inst, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFoldsInvalidNumFolds() {
        Random rg = new Random();
        dataset.folds(0, rg);
    }

    @Test(expected = NullPointerException.class)
    public void testFoldsNullRandom() {
        dataset.folds(5, null);
    }
    
    @Test
    public void testClassIndexNull() {
        assertEquals(-1, dataset.classIndex(null));
    }

    @Test(expected = NullPointerException.class)
    public void testClassValueNegativeIndex() {
        dataset.classValue(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testClassValueInvalidIndex() {
        dataset.classValue(0);
    }
}

