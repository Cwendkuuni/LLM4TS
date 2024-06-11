import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<Instance>();
        instances.add(new Instance());
        instances.add(new Instance());
        boolean result = dataset.addAll(instances);
        assertTrue(result);
    }

    @Test
    public void testAddAllWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<Instance>();
        instances.add(new Instance());
        instances.add(new Instance());
        boolean result = dataset.addAll(0, instances);
        assertTrue(result);
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        boolean result = dataset.add(instance);
        assertTrue(result);
    }

    @Test
    public void testAddWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(0, instance);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.setElementAt(new Instance(), 0);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        Instance result = dataset.instance(0);
        assertEquals(instance, result);
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        SortedSet<Object> classes = dataset.classes();
        assertEquals(new TreeSet<Object>(), classes);
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        Set<Instance> nearest = dataset.kNearest(3, instance, dm);
        assertNotNull(nearest);
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        int numFolds = 10;
        Random rg = new Random();
        Dataset[] folds = dataset.folds(numFolds, rg);
        assertNotNull(folds);
        assertEquals(numFolds, folds.length);
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        int result = dataset.noAttributes();
        assertEquals(0, result);
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Object clazz = new Object();
        int result = dataset.classIndex(clazz);
        assertEquals(-1, result);
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes.add(new Object());
        Object result = dataset.classValue(0);
        assertNotNull(result);
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        for (int i = 0; i < dataset.size(); i++) {
            assertEquals(dataset.get(i), copy.get(i));
        }
    }
}