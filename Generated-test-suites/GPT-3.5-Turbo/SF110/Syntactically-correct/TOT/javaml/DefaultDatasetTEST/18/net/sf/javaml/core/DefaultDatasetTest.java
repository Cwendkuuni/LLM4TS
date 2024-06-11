import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;

import java.util.ArrayList;
import java.util.List;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    private DistanceMeasure distanceMeasure;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
        distanceMeasure = new EuclideanDistance();
    }

    @Test
    public void testDefaultConstructor() {
        Assert.assertEquals(0, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
    }

    @Test
    public void testConstructorWithCollection() {
        List<Instance> list = new ArrayList<>();
        list.add(new Instance(new double[]{1, 2, 3}, "A"));
        list.add(new Instance(new double[]{4, 5, 6}, "B"));
        dataset = new DefaultDataset(list);

        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(2, dataset.classes().size());
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance(new double[]{1, 2, 3}, "A");

        boolean result = dataset.add(instance);

        Assert.assertTrue(result);
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(1, dataset.classes().size());
    }

    @Test
    public void testAddAll() {
        List<Instance> list = new ArrayList<>();
        list.add(new Instance(new double[]{1, 2, 3}, "A"));
        list.add(new Instance(new double[]{4, 5, 6}, "B"));

        boolean result = dataset.addAll(list);

        Assert.assertTrue(result);
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(2, dataset.classes().size());
    }

    @Test
    public void testAddAllWithIndex() {
        List<Instance> list = new ArrayList<>();
        list.add(new Instance(new double[]{1, 2, 3}, "A"));
        list.add(new Instance(new double[]{4, 5, 6}, "B"));
        dataset.add(new Instance(new double[]{7, 8, 9}, "C"));

        boolean result = dataset.addAll(0, list);

        Assert.assertTrue(result);
        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(3, dataset.classes().size());
    }

    @Test
    public void testClear() {
        dataset.add(new Instance(new double[]{1, 2, 3}, "A"));

        dataset.clear();

        Assert.assertEquals(0, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance(new double[]{1, 2, 3}, "A");

        dataset.addElement(instance);

        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(1, dataset.classes().size());
    }

    @Test
    public void testInsertElementAt() {
        dataset.add(new Instance(new double[]{1, 2, 3}, "A"));

        dataset.insertElementAt(new Instance(new double[]{4, 5, 6}, "B"), 1);

        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(2, dataset.classes().size());
    }

    @Test
    public void testSetElementAt() {
        dataset.add(new Instance(new double[]{1, 2, 3}, "A"));

        dataset.setElementAt(new Instance(new double[]{4, 5, 6}, "B"), 0);

        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals(3, dataset.noAttributes());
        Assert.assertEquals(2, dataset.classes().size());
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance(new double[]{1, 2, 3}, "A");
        dataset.add(instance);

        Instance result = dataset.instance(0);

        Assert.assertEquals(instance, result);
    }

    @Test
    public void testClasses() {
        dataset.add(new Instance(new double[]{1, 2, 3}, "A"));
        dataset.add(new Instance(new double[]{4, 5, 6}, "B"));

        Assert.assertEquals(2, dataset.classes().size());
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new Instance(new double[]{1, 2, 3}, "A");
        Instance instance2 = new Instance(new double[]{4, 5, 6}, "B");
        dataset.add(instance1);
        dataset.add(instance2);

        List<Instance> nearest = new ArrayList<>();
        nearest.add(instance1);

        Assert.assertEquals(nearest, dataset.kNearest(1, instance2, distanceMeasure));
    }

    @Test
    public void testFolds() {
        dataset.add(new Instance(new double[]{1, 2, 3}, "A"));
        dataset.add(new Instance(new double[]{4, 5, 6}, "B"));
        dataset.add(new Instance(new double[]{7, 8, 9}, "C"));
        dataset.add(new Instance(new double[]{10, 11, 12}, "D"));

        int numFolds = 2;
        int expectedFoldSize = 2;

        DefaultDataset[] folds = dataset.folds(numFolds, new Random());

        Assert.assertEquals(numFolds, folds.length);

        for (DefaultDataset fold : folds) {
            Assert.assertEquals(expectedFoldSize, fold.size());
        }
    }

    @Test
    public void testNoAttributes() {
        dataset.add(new Instance(new double[]{1, 2, 3}, "A"));

        Assert.assertEquals(3, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        dataset.add(new Instance(new double[]{1, 2, 3}, "A"));
        dataset.add(new Instance(new double[]{4, 5, 6}, "B"));

        Assert.assertEquals(1, dataset.classIndex("B"));
        Assert.assertEquals(-1, dataset.classIndex("C"));
    }

    @Test
    public void testClassValue() {
        dataset.add(new Instance(new double[]{1, 2, 3}, "A"));
        dataset.add(new Instance(new double[]{4, 5, 6}, "B"));

        Assert.assertEquals("A", dataset.classValue(0));
        Assert.assertEquals("B", dataset.classValue(1));
        Assert.assertNull(dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        dataset.add(new Instance(new double[]{1, 2, 3}, "A"));
        dataset.add(new Instance(new double[]{4, 5, 6}, "B"));

        DefaultDataset copy = dataset.copy();

        Assert.assertEquals(dataset.size(), copy.size());
        Assert.assertEquals(dataset.noAttributes(), copy.noAttributes());
        Assert.assertEquals(dataset.classes().size(), copy.classes().size());

        for (int i = 0; i < dataset.size(); i++) {
            Assert.assertEquals(dataset.instance(i), copy.instance(i));
        }
    }

}