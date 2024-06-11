import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {

    DefaultDataset dataset;

    @Test
    public void testAddAll() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        assertTrue(dataset.addAll(instances));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllIndex() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        assertTrue(dataset.addAll(0, instances));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    // Add more test cases for other methods...

}