import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    // Test for DefaultDataset(Collection<Instance> coll)
    @Test
    public void testDefaultDataset_Collection() {
        Collection<Instance> coll = new ArrayList<>();
        coll.add(new Instance());
        coll.add(new Instance());
        dataset = new DefaultDataset(coll);
        assertEquals(coll.size(), dataset.size());
        assertEquals(0, dataset.classes().size());
    }

    // Test for DefaultDataset()
    @Test
    public void testDefaultDataset() {
        assertEquals(0, dataset.size());
    }

    // Test for addAll(Collection<? extends Instance> c) when c is not null
    @Test
    public void testAddAll_CollectionNotNull() {
        Collection<Instance> c = new ArrayList<>();
        c.add(new Instance());
        c.add(new Instance());
        assertTrue(dataset.addAll(c));
        assertEquals(c.size(), dataset.size());
    }

    // Test for addAll(Collection<? extends Instance> c) when c is null
    @Test
    public void testAddAll_CollectionNull() {
        assertFalse(dataset.addAll(null));
        assertEquals(0, dataset.size());
    }

    // Test for addAll(int index, Collection<? extends Instance> c) when index is valid and c is not null
    @Test
    public void testAddAll_IndexValid_CollectionNotNull() {
        dataset.add(new Instance());
        Collection<Instance> c = new ArrayList<>();
        c.add(new Instance());
        c.add(new Instance());
        assertTrue(dataset.addAll(0, c));
        assertEquals(c.size() + 1, dataset.size());
    }

    // Test for addAll(int index, Collection<? extends Instance> c) when index is valid and c is null
    @Test
    public void testAddAll_IndexValid_CollectionNull() {
        dataset.add(new Instance());
        assertFalse(dataset.addAll(0, null));
        assertEquals(1, dataset.size());
    }

    // Test for addAll(int index, Collection<? extends Instance> c) when index is invalid and c is not null
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAll_IndexInvalid_CollectionNotNull() {
        Collection<Instance> c = new ArrayList<>();
        c.add(new Instance());
        c.add(new Instance());
        dataset.addAll(1, c);
    }

    // Test for addAll(int index, Collection<? extends Instance> c) when index is invalid and c is null
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAll_IndexInvalid_CollectionNull() {
        dataset.addAll(1, null);
    }

    // Test for clear()
    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.classes().size());
    }

    // Test for add(Instance e)
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
        assertTrue(dataset.classes().isEmpty());
    }

    // Test for add(int index, Instance e)
    @Test
    public void testAdd_IndexValid() {
        Instance instance = new Instance();
        assertTrue(dataset.add(0, instance));
        assertEquals(1, dataset.size());
        assertTrue(dataset.classes().isEmpty());
    }

    // Test for addElement(Instance e)
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertTrue(dataset.classes().isEmpty());
    }

    // Test for insertElementAt(Instance e, int index)
    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertTrue(dataset.classes().isEmpty());
    }

    // Test for setElementAt(Instance e, int index)
    @Test
    public void testSetElementAt() {
        dataset.add(new Instance());
        Instance instance = new Instance();
        dataset.setElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertTrue(dataset.classes().isEmpty());
    }

    // Test for instance(int index)
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertSame(instance, dataset.instance(0));
    }

    // Test for classes()
    @Test
    public void testClasses() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        assertEquals(0, dataset.classes().size());
        dataset.get(0).setClassValue("class1");
        assertEquals(1, dataset.classes().size());
        dataset.get(1).setClassValue("class2");
        assertEquals(2, dataset.classes().size());
    }

    // Test for kNearest(int k, Instance inst, DistanceMeasure dm)
    @Test
    public void testKNearest() {
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        dataset.add(new Instance());
        dataset.add(new Instance());
        Set<Instance> nearest = dataset.kNearest(1, inst, dm);
        assertEquals(1, nearest.size());
    }

    // Test for kNearest(int k, Instance inst, DistanceMeasure dm) when k is greater than dataset size
    @Test
    public void testKNearest_KGreaterThanSize() {
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        dataset.add(new Instance());
        Set<Instance> nearest = dataset.kNearest(dataset.size() + 1, inst, dm);
        assertEquals(dataset.size(), nearest.size());
    }

    // Test for removeFarthest(Map<Instance, Double> vector)
    @Test
    public void testRemoveFarthest() {
        Instance inst1 = new Instance();
        Instance inst2 = new Instance();
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(inst1, 1.0);
        vector.put(inst2, 2.0);
        double maxDistance = dataset.removeFarthest(vector);
        assertEquals(2.0, maxDistance, 0.001);
        assertEquals(1, vector.size());
        assertFalse(vector.containsKey(inst2));
    }

    // Test for folds(int numFolds, Random rg)
    @Test
    public void testFolds() {
        int numFolds = 3;
        Random rg = new Random();
        dataset.add(new Instance());
        dataset.add(new Instance());
        Dataset[] folds = dataset.folds(numFolds, rg);
        assertEquals(numFolds, folds.length);
        assertEquals(2, folds[0].size() + folds[1].size() + folds[2].size());
    }

    // Test for noAttributes()
    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
        dataset.add(new Instance("value1"));
        assertEquals(1, dataset.noAttributes());
    }

    // Test for classIndex(Object clazz) when clazz is not null
    @Test
    public void testClassIndex_ClazzNotNull() {
        dataset.get(0).setClassValue("class1");
        dataset.get(1).setClassValue("class2");
        int index = dataset.classIndex("class2");
        assertEquals(1, index);
    }

    // Test for classIndex(Object clazz) when clazz is null
    @Test
    public void testClassIndex_ClazzNull() {
        dataset.get(0).setClassValue("class1");
        int index = dataset.classIndex(null);
        assertEquals(-1, index);
    }

    // Test for classValue(int index)
    @Test
    public void testClassValue() {
        dataset.get(0).setClassValue("class1");
        dataset.get(1).setClassValue("class2");
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
    }

    // Test for copy()
    @Test
    public void testCopy() {
        dataset.add(new Instance());
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertNotSame(dataset.get(0), copy.get(0));
    }
}