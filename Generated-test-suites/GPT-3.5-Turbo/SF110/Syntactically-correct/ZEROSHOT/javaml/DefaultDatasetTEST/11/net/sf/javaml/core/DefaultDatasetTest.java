import java.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {
    
    private DefaultDataset dataset;
    private List<Instance> instances;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
        instances = new ArrayList<Instance>();
        instances.add(new Instance());
        instances.add(new Instance());
    }

    @Test
    public void testAddAll() {
        Assert.assertTrue(dataset.addAll(instances));
        Assert.assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllWithIndex() {
        Assert.assertTrue(dataset.addAll(0, instances));
        Assert.assertEquals(2, dataset.size());
    }

    @Test
    public void testClear() {
        dataset.addAll(instances);
        dataset.clear();
        Assert.assertEquals(0, dataset.size());
    }

    @Test
    public void testAdd() {
        Assert.assertTrue(dataset.add(new Instance()));
        Assert.assertEquals(1, dataset.size());
    }

    @Test
    public void testAddWithIndex() {
        dataset.add(0, new Instance());
        Assert.assertEquals(1, dataset.size());
    }

    @Test
    public void testAddElement() {
        dataset.addElement(new Instance());
        Assert.assertEquals(1, dataset.size());
    }

    @Test
    public void testInsertElementAt() {
        dataset.insertElementAt(new Instance(), 0);
        Assert.assertEquals(1, dataset.size());
    }

    @Test
    public void testSetElementAt() {
        dataset.add(new Instance());
        dataset.setElementAt(new Instance(), 0);
        Assert.assertEquals(1, dataset.size());
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        Assert.assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        Assert.assertNotNull(dataset.classes());
    }

    @Test
    public void testKNearest() {
        Instance instance = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        Set<Instance> nearest = dataset.kNearest(3, instance, dm);
        Assert.assertNotNull(nearest);
    }

    @Test
    public void testFolds() {
        Random rg = new Random();
        Dataset[] folds = dataset.folds(5, rg);
        Assert.assertNotNull(folds);
        Assert.assertEquals(5, folds.length);
    }

    @Test
    public void testNoAttributes() {
        Assert.assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Assert.assertEquals(0, dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        Assert.assertNull(dataset.classValue(0));
    }

    @Test
    public void testCopy() {
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        Assert.assertEquals(dataset.size(), copy.size());
    }
}