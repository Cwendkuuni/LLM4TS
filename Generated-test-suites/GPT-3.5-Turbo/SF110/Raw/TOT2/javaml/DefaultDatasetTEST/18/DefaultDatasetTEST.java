import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll(){
        DefaultDataset dataset = new DefaultDataset();
        
        ArrayList<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        
        assertTrue(dataset.addAll(instances));
    }

    @Test
    public void testClear(){
        DefaultDataset dataset = new DefaultDataset();
        
        dataset.add(new Instance());
        dataset.clear();
        
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAdd(){
        DefaultDataset dataset = new DefaultDataset();
        
        Instance instance = new Instance();
        
        assertTrue(dataset.add(instance));
    }

    @Test
    public void testClasses(){
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.classes().size());
        
        dataset.add(new Instance());
        
        assertEquals(1, dataset.classes().size());
    }

    // Additional tests for other methods can be added here

}