import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        List<Instance> instances = Arrays.asList(instance1, instance2);

        boolean result = dataset.addAll(instances);

        assertEquals(2, dataset.size());
        assertTrue(result);
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);

        dataset.clear();

        assertEquals(0, dataset.size());
        assertEquals(0, dataset.classes().size());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();

        dataset.add(instance);

        assertEquals(1, dataset.size());
        assertEquals(1, dataset.classes().size());
    }

    // More test cases for other methods can be added here

}