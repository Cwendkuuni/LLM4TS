import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testConstructorWithCollection() {
        Collection<Instance> coll = new ArrayList<>();
        DefaultDataset dataset = new DefaultDataset(coll);
        assertNotNull(dataset);
    }

    @Test
    public void testConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset);
    }

    // Add more test cases for other methods...

}