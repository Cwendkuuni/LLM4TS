
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import org.junit.*;
import java.util.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testConstructorWithCollection() {
        Collection<Instance> collection = new ArrayList<>();
        collection.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));
        collection.add(new Instance(new double[]{4.0, 5.0, 6.0}, "B"));

        dataset = new DefaultDataset(collection);

        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(2, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("A"));
        Assert.assertTrue(dataset.classes().contains("B"));
    }

    @Test
    public void testConstructorWithoutCollection() {
        Assert.assertEquals(0, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");

        boolean added = dataset.add(instance);

        Assert.assertTrue(added);
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(1, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("A"));
    }

    @Test
    public void testAddAll() {
        Collection<Instance> collection = new ArrayList<>();
        collection.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));
        collection.add(new Instance(new double[]{4.0, 5.0, 6.0}, "B"));

        boolean addedAll = dataset.addAll(collection);

        Assert.assertTrue(addedAll);
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(2, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("A"));
        Assert.assertTrue(dataset.classes().contains("B"));
    }

    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> collection = new ArrayList<>();
        collection.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));
        collection.add(new Instance(new double[]{4.0, 5.0, 6.0}, "B"));
        dataset.add(new Instance(new double[]{7.0, 8.0, 9.0}, "C"));

        boolean addedAll = dataset.addAll(1, collection);

        Assert.assertTrue(addedAll);
        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(3, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("A"));
        Assert.assertTrue(dataset.classes().contains("B"));
        Assert.assertTrue(dataset.classes().contains("C"));
    }

    @Test
    public void testClear() {
        dataset.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));
        dataset.add(new Instance(new double[]{4.0, 5.0, 6.0}, "B"));

        dataset.clear();

        Assert.assertEquals(0, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");

        dataset.addElement(instance);

        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(1, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("A"));
    }

    @Test
    public void testInsertElementAt() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.addElement(new Instance(new double[]{7.0, 8.0, 9.0}, "C"));

        dataset.insertElementAt(instance1, 1);
        dataset.insertElementAt(instance2, 0);

        Assert.assertEquals(4, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(3, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("A"));
        Assert.assertTrue(dataset.classes().contains("B"));
        Assert.assertTrue(dataset.classes().contains("C"));
    }

    @Test
    public void testSetElementAt() {
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        dataset.addElement(new Instance(new double[]{4.0, 5.0, 6.0}, "B"));

        dataset.setElementAt(instance, 0);

        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(1, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("A"));
        Assert.assertFalse(dataset.classes().contains("B"));
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        dataset.add(instance);

        Instance retrievedInstance = dataset.instance(0);

        Assert.assertEquals(instance, retrievedInstance);
    }

    @Test
    public void testClasses() {
        dataset.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));

        SortedSet<Object> classes = dataset.classes();

        Assert.assertEquals(1, classes.size());
        Assert.assertTrue(classes.contains("A"));
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.add(instance1);
        dataset.add(instance2);
        DistanceMeasure dm = new EuclideanDistance();

        Set<Instance> nearestInstances = dataset.kNearest(2, instance1, dm);

        Assert.assertEquals(1, nearestInstances.size());
        Assert.assertTrue(nearestInstances.contains(instance2));
    }

    @Test
    public void testFolds() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.add(instance1);
        dataset.add(instance2);
        Random rg = new Random();

        Dataset[] folds = dataset.folds(2, rg);

        Assert.assertEquals(2, folds.length);
        Assert.assertEquals(1, folds[0].size());
        Assert.assertEquals(1, folds[1].size());
        Assert.assertTrue(folds[0].contains(instance1) || folds[0].contains(instance2));
        Assert.assertTrue(folds[1].contains(instance1) || folds[1].contains(instance2));
    }

    @Test
    public void testNoAttributes() {
        dataset.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));

        int numAttributes = dataset.noAttributes();

        Assert.assertEquals(3, numAttributes);
    }

    @Test
    public void testClassIndexWithExistingClass() {
        dataset.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));

        int classIndex = dataset.classIndex("A");

        Assert.assertEquals(0, classIndex);
    }

    @Test
    public void testClassIndexWithNonExistingClass() {
        dataset.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));

        int classIndex = dataset.classIndex("B");

        Assert.assertEquals(-1, classIndex);
    }

    @Test
    public void testClassValue() {
        dataset.add(new Instance(new double[]{1.0, 2.0, 3.0}, "A"));
        dataset.add(new Instance(new double[]{4.0, 5.0, 6.0}, "B"));

        Object classValue1 = dataset.classValue(0);
        Object classValue2 = dataset.classValue(1);

        Assert.assertEquals("A", classValue1);
        Assert.assertEquals("B", classValue2);
    }

    @Test
    public void testCopy() {
        Instance instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.add(instance1);
        dataset.add(instance2);

        Dataset copy = dataset.copy();

        Assert.assertNotSame(dataset, copy);
        Assert.assertEquals(dataset.size(), copy.size());
        Assert.assertEquals(dataset.noAttributes(), copy.noAttributes());
        Assert.assertEquals(dataset.classes().size(), copy.classes().size());
        Assert.assertTrue(copy.contains(instance1));
        Assert.assertTrue(copy.contains(instance2));
    }

}
