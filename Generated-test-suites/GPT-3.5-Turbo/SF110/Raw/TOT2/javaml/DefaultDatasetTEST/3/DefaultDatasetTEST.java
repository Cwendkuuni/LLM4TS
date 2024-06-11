import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addAll(Arrays.asList(instance1, instance2));
        assertEquals(2, dataset.size());
    }

    // Add more test cases for other public methods

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);

        Set<Instance> nearest = dataset.kNearest(1, instance1, new EuclideanDistance());
        assertEquals(1, nearest.size());
        assertTrue(nearest.contains(instance2));
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
        assertNotNull(folds[0]);
        assertNotNull(folds[1]);
    }

    // Add more test cases for edge cases and error scenarios

}