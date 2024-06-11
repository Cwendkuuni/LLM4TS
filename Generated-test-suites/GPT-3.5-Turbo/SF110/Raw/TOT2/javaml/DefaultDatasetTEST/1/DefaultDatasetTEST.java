import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testAddInstance() {
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testClearDataset() {
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testMaxAttributes() {
        Instance instance1 = new Instance();
        instance1.setAttributes(5);
        Instance instance2 = new Instance();
        instance2.setAttributes(10);
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(10, dataset.noAttributes());
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        Set<Instance> nearest = dataset.kNearest(1, instance1, new EuclideanDistance());
        assertEquals(1, nearest.size());
    }

    @Test
    public void testClassIndex() {
        Instance instance = new Instance();
        dataset.add(instance);
        Object clazz = instance.classValue();
        assertEquals(0, dataset.classIndex(clazz));
    }

    @Test
    public void testCopyDataset() {
        Instance instance = new Instance();
        dataset.add(instance);
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
    }

}