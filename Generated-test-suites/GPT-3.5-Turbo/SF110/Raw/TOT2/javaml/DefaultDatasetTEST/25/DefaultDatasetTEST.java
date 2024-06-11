import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import net.sf.javaml.core.*;

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
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    // Add additional test cases for other methods...

}