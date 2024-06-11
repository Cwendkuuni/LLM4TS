import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setup() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testAddInstance() {
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        assertTrue(dataset.add(instance));
        assertTrue(dataset.contains(instance));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddInstanceAtIndex() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.add(instance1);
        dataset.add(0, instance2);
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(instance2, dataset.get(0));
        assertEquals(instance1, dataset.get(1));
    }

    @Test
    public void testAddAllInstances() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        List<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);
        assertTrue(dataset.addAll(instances));
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllInstancesAtIndex() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.add(instance1);
        List<Instance> instances = new ArrayList<>();
        instances.add(instance2);
        assertTrue(dataset.addAll(0, instances));
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(instance2, dataset.get(0));
        assertEquals(instance1, dataset.get(1));
    }

    @Test
    public void testClear() {
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        dataset.add(instance);
        dataset.clear();
        assertFalse(dataset.contains(instance));
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        dataset.addElement(instance);
        assertTrue(dataset.contains(instance));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInsertElementAt() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.insertElementAt(instance1, 0);
        dataset.insertElementAt(instance2, 0);
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(instance2, dataset.get(0));
        assertEquals(instance1, dataset.get(1));
    }

    @Test
    public void testSetElementAt() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.add(instance1);
        dataset.setElementAt(instance2, 0);
        assertFalse(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(instance2, dataset.get(0));
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "A");
        dataset.add(instance1);
        dataset.add(instance2);
        SortedSet<Object> classes = dataset.classes();
        assertEquals(1, classes.size());
        assertTrue(classes.contains("A"));
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.add(instance1);
        dataset.add(instance2);
        DistanceMeasure dm = new EuclideanDistance();
        Set<Instance> nearest = dataset.kNearest(1, instance1, dm);
        assertEquals(1, nearest.size());
        assertTrue(nearest.contains(instance2));
    }

    @Test
    public void testFolds() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.add(instance1);
        dataset.add(instance2);
        Random rg = new Random();
        Dataset[] folds = dataset.folds(2, rg);
        assertEquals(2, folds.length);
        assertTrue(folds[0].contains(instance1) || folds[0].contains(instance2));
        assertTrue(folds[1].contains(instance1) || folds[1].contains(instance2));
        assertNotEquals(folds[0], folds[1]);
    }

    @Test
    public void testNoAttributesEmptyDataset() {
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testNoAttributesNonEmptyDataset() {
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        dataset.add(instance);
        assertEquals(3, dataset.noAttributes());
    }

    @Test
    public void testClassIndexWithValidClass() {
        dataset.classes().add("A");
        dataset.classes().add("B");
        assertEquals(0, dataset.classIndex("A"));
        assertEquals(1, dataset.classIndex("B"));
    }

    @Test
    public void testClassIndexWithInvalidClass() {
        dataset.classes().add("A");
        dataset.classes().add("B");
        assertEquals(-1, dataset.classIndex("C"));
    }

    @Test
    public void testClassValue() {
        dataset.classes().add("A");
        dataset.classes().add("B");
        assertEquals("A", dataset.classValue(0));
        assertEquals("B", dataset.classValue(1));
        assertNull(dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.add(instance1);
        dataset.add(instance2);
        Dataset copy = dataset.copy();
        assertNotSame(dataset, copy);
        assertEquals(dataset.size(), copy.size());
        assertTrue(copy.contains(instance1));
        assertTrue(copy.contains(instance2));
    }

}