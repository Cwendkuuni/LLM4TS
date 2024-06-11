import static org.junit.Assert.*;
import org.junit.*;

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
    public void testAddInstanceAtIndex() {
        Instance instance = new Instance();
        dataset.add(0, instance);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testCopyDataset() {
        DefaultDataset copyDataset = (DefaultDataset) dataset.copy();
        assertEquals(dataset.size(), copyDataset.size());
        for (int i = 0; i < dataset.size(); i++) {
            assertEquals(dataset.get(i), copyDataset.get(i));
        }
    }

    // Add more test cases for other methods as per requirements

}