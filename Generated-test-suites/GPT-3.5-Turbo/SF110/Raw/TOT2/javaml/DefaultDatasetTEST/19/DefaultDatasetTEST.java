import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        assertTrue(dataset.addAll(instances));
    }

    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        assertTrue(dataset.addAll(0, instances));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }

    // Add more test cases for other public methods here

    @Test
    public void testFoldsEdgeCases() {
        DefaultDataset dataset = new DefaultDataset();
        Random random = new Random();
        assertNotNull(dataset.folds(5, random)); // Test with valid number of folds
        assertEquals(0, dataset.folds(-1, random).length); // Test with negative number of folds
    }

    // Add more test cases for edge cases and exceptions here

}