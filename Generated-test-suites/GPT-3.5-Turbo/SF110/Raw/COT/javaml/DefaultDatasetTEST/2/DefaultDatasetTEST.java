import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;

import static org.junit.Assert.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addAll(1, List.of(instance1, instance2));
        assertEquals(2, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.add(instance1);
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.add(instance1);
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
    }

    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.addElement(instance1);
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
    }

    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.addElement(instance1);
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.addElement(instance1);
        assertEquals(1, dataset.size());

        Instance instance2 = new Instance();
        dataset.setElementAt(instance2, 0);
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.add(instance1);
        assertEquals(instance1, dataset.instance(0));
    }

    @Test
    public void testKNarest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);

        Set<Instance> nearestInstances = dataset.kNearest(1, instance1, new DistanceMeasure());
        assertEquals(1, nearestInstances.size());
        assertTrue(nearestInstances.contains(instance2));
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);

        Dataset[] folds = dataset.folds(2, new Random());
        assertEquals(2, folds.length);
        assertEquals(1, folds[0].size());
        assertEquals(1, folds[1].size());
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.add(instance1);
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.add(instance1);

        assertEquals(0, dataset.classIndex(instance1));
        assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.add(instance1);

        assertEquals(instance1.classValue(), dataset.classValue(0));
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.add(instance1);

        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.get(0), copy.get(0));
    }

}
