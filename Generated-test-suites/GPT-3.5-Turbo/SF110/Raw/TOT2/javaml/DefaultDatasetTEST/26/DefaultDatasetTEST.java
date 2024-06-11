import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
        assertFalse(dataset.contains(instance));
    }

    // Add more test cases for the remaining methods here

}