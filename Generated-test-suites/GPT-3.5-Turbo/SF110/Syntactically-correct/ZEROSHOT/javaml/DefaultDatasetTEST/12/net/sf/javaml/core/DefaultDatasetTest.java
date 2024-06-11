import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import net.sf.javaml.core.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testConstructorWithCollection() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<>();
        collection.add(instance1);
        collection.add(instance2);

        DefaultDataset dataset = new DefaultDataset(collection);
        assertFalse(dataset.isEmpty());
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        dataset.add(instance1);

        assertFalse(dataset.isEmpty());
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> collection = new ArrayList<>();
        collection.add(instance1);
        collection.add(instance2);

        dataset.addAll(collection);
        assertFalse(dataset.isEmpty());
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    // Add tests for the remaining methods in the DefaultDataset class

}