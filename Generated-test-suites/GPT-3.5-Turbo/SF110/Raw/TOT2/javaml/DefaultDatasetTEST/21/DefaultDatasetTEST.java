import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.distance.EuclideanDistance;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstructor() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance(new double[]{1.0, 2.0}, "A"));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(1, dataset.size());
        assertEquals(2, dataset.noAttributes());
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance(new double[]{1.0, 2.0}, "A");
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testClasses() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance(new double[]{1.0, 2.0}, "A"));
        instances.add(new Instance(new double[]{2.0, 3.0}, "B"));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertNotNull(dataset.classes());
        assertTrue(dataset.classes().contains("A"));
        assertTrue(dataset.classes().contains("B"));
    }

    // Additional test cases for edge cases and exception handling would be added here

    @Test
    public void testFolds() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance(new double[]{1.0, 2.0}, "A"));
        instances.add(new Instance(new double[]{2.0, 3.0}, "B"));
        DefaultDataset dataset = new DefaultDataset(instances);
        Random random = new Random();
        int numFolds = 2;
        DefaultDataset[] folds = dataset.folds(numFolds, random);
        assertTrue(folds.length == numFolds);
    }

}