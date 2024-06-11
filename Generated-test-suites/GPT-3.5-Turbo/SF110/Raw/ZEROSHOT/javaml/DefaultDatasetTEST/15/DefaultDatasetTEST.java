import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

public class DefaultDatasetTest {

    DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        dataset.add(instance);
        Assert.assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAll() {
        Collection<Instance> instances = new Vector<>();
        instances.add(new Instance());
        instances.add(new Instance());
        dataset.addAll(instances);
        Assert.assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> instances = new Vector<>();
        instances.add(new Instance());
        instances.add(new Instance());
        dataset.add(new Instance());
        dataset.addAll(0, instances);
        Assert.assertEquals(3, dataset.size());
    }

    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        Assert.assertEquals(0, dataset.size());
    }

    @Test
    public void testGetInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        Assert.assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        SortedSet<Object> classes = dataset.classes();
        Assert.assertEquals(2, classes.size());
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        DistanceMeasure dm = new DistanceMeasure();
        Set<Instance> closestInstances = dataset.kNearest(1, instance1, dm);
        Assert.assertEquals(1, closestInstances.size());
        Assert.assertEquals(instance2, closestInstances.iterator().next());
    }

    @Test
    public void testFolds() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        Random rg = new Random();
        int numFolds = 2;
        Dataset[] folds = dataset.folds(numFolds, rg);
        Assert.assertEquals(numFolds, folds.length);
        Assert.assertEquals(instance1, folds[0].instance(0));
        Assert.assertEquals(instance2, folds[1].instance(0));
    }

    @Test
    public void testNoAttributes() {
        Instance instance = new Instance();
        dataset.add(instance);
        Assert.assertEquals(instance.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        Assert.assertEquals(0, dataset.classIndex("class1"));
        Assert.assertEquals(1, dataset.classIndex("class2"));
    }

    @Test
    public void testClassValue() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        Assert.assertEquals("class1", dataset.classValue(0));
        Assert.assertEquals("class2", dataset.classValue(1));
    }

    @Test
    public void testCopy() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        Dataset copy = dataset.copy();
        Assert.assertEquals(dataset.size(), copy.size());
        Assert.assertEquals(instance1, copy.instance(0));
        Assert.assertEquals(instance2, copy.instance(1));
    }

}