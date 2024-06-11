
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import org.junit.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        DefaultDataset addedDataset = new DefaultDataset();
        addedDataset.add(new Instance());
        addedDataset.add(new Instance());
        addedDataset.add(new Instance());

        boolean result = dataset.addAll(addedDataset);

        Assert.assertTrue(result);
        Assert.assertEquals(3, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        DefaultDataset addedDataset = new DefaultDataset();
        addedDataset.add(new Instance());
        addedDataset.add(new Instance());
        addedDataset.add(new Instance());

        boolean result = dataset.addAll(1, addedDataset);

        Assert.assertTrue(result);
        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals(addedDataset.get(0), dataset.get(1));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());

        dataset.clear();

        Assert.assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();

        boolean result = dataset.add(instance);

        Assert.assertTrue(result);
        Assert.assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();

        dataset.add(1, instance);

        Assert.assertEquals(instance, dataset.get(1));
    }

    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();

        dataset.addElement(instance);

        Assert.assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();

        dataset.insertElementAt(instance, 1);

        Assert.assertEquals(instance, dataset.get(1));
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();

        dataset.add(new Instance());
        dataset.setElementAt(instance, 0);

        Assert.assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);

        Instance result = dataset.instance(0);

        Assert.assertEquals(instance, result);
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());

        SortedSet<Object> classes = dataset.classes();

        Assert.assertEquals(3, classes.size());
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());

        Set<Instance> nearest = dataset.kNearest(2, inst, dm);

        Assert.assertEquals(2, nearest.size());
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());

        Dataset[] folds = dataset.folds(3, rg);

        Assert.assertEquals(3, folds.length);
        Assert.assertEquals(1, folds[0].size());
        Assert.assertEquals(1, folds[1].size());
        Assert.assertEquals(1, folds[2].size());
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());

        int result = dataset.noAttributes();

        Assert.assertEquals(0, result); // Should be 0 because no attribute is added
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());

        int result = dataset.classIndex("class");

        Assert.assertEquals(-1, result); // Should be -1 because "class" is not in the classes set
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());

        Object result = dataset.classValue(0);

        Assert.assertNull(result); // Should be null because 0 index is out of the range of classes set
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());

        Dataset result = dataset.copy();

        Assert.assertEquals(dataset.size(), result.size());
    }
}
