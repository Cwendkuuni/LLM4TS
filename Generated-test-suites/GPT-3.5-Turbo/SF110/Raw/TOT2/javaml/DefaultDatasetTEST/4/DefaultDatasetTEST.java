import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

public class DefaultDatasetTest {
    
    @Test
    public void testDefaultDatasetCollectionConstructor() {
        ArrayList<Instance> instances = new ArrayList<Instance>(Arrays.asList(new Instance(), new Instance()));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testDefaultDatasetNoArgConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        assertEquals(2, dataset.kNearest(2, inst, dm).size());
    }
    
    // Add more test cases for other methods based on edge cases and exceptions
    
}