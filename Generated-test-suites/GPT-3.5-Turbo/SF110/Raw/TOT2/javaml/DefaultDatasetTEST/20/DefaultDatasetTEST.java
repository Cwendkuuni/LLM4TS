import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        // Typical case
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2)));
        
        // Edge case: empty collection
        DefaultDataset dataset2 = new DefaultDataset();
        assertFalse(dataset2.addAll(Collections.emptyList()));
        
        // Exception handling: null collection
        DefaultDataset dataset3 = new DefaultDataset();
        try {
            dataset3.addAll(null);
            fail("Exception not thrown for null collection");
        } catch (NullPointerException e) {
            assertEquals("Collection cannot be null", e.getMessage());
        }
    }

    @Test
    public void testAdd() {
        // Typical case
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        
        // Edge case: adding null instance
        DefaultDataset dataset2 = new DefaultDataset();
        assertFalse(dataset2.add(null));
    }

    // Additional test cases for other methods can be added here

}