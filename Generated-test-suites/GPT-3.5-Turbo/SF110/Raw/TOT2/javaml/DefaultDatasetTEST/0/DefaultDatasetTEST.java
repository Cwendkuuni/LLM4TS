import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultDatasetTest {
    
    private DefaultDataset dataset;
    
    @Before
    public void setUp() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance(new double[] {1.0, 2.0}, "A"));
        instances.add(new Instance(new double[] {3.0, 4.0}, "B"));
        dataset = new DefaultDataset(instances);
    }
    
    @Test
    public void testAddAll() {
        Instance newInstance = new Instance(new double[] {5.0, 6.0}, "C");
        assertTrue(dataset.addAll(Arrays.asList(newInstance)));
    }

    @Test
    public void testAdd() {
        Instance newInstance = new Instance(new double[] {7.0, 8.0}, "D");
        assertTrue(dataset.add(newInstance));
    }
    
    @Test
    public void testClear() {
        dataset.clear();
        assertEquals(0, dataset.size());
    }
    
    // Add more test cases for other methods in DefaultDataset class
    
}