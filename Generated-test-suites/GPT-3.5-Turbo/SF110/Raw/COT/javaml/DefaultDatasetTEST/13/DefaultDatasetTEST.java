import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testAddAll() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        assertTrue(dataset.addAll(instances));
        assertTrue(dataset.containsAll(instances));
    }

    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        assertTrue(dataset.addAll(0, instances));
        assertTrue(dataset.containsAll(instances));
    }

    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testAddElementAtIndex() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testSetElementAt() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        dataset.setElementAt(instance, 0);
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        assertEquals(2, dataset.classes().size());
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        DistanceMeasure measure = new DistanceMeasure();
        Set<Instance> nearest = dataset.kNearest(1, instance1, measure);
        assertEquals(1, nearest.size());
        assertTrue(nearest.contains(instance2));
    }

    @Test
    public void testFolds() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        int numFolds = 2;
        Dataset[] folds = dataset.folds(numFolds, new Random());
        assertEquals(numFolds, folds.length);
    }

    @Test
    public void testNoAttributes() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        dataset.classes().add(clazz);
        assertEquals(0, dataset.classIndex(clazz));
    }

    @Test
    public void testClassValue() {
        Object clazz = new Object();
        dataset.classes().add(clazz);
        assertEquals(clazz, dataset.classValue(0));
    }

    @Test
    public void testCopy() {
        dataset.add(new Instance());
        Dataset copy = dataset.copy();
        assertTrue(copy.containsAll(dataset));
    }

    @Test
    public void testConstructorWithCollection() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        DefaultDataset dataset = new DefaultDataset(instances);
        assertTrue(dataset.containsAll(instances));
    }

    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset);
    }

}