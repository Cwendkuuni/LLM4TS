import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import java.util.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setup() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testAddInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testKNearest() {
        Instance inst1 = new Instance();
        Instance inst2 = new Instance();
        // Add instances to dataset
        dataset.add(inst1);
        dataset.add(inst2);
        DistanceMeasure dm = new EuclideanDistance();
        Set<Instance> result = dataset.kNearest(1, inst1, dm);
        assertEquals(1, result.size());
    }

    @Test
    public void testNoAttributesEmptyDataset() {
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testNoAttributesNonEmptyDataset() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Object clazz = "class1";
        dataset.classes.add(clazz);
        assertEquals(0, dataset.classIndex(clazz));
    }

    @Test
    public void testClassValue() {
        Object clazz = "class2";
        dataset.classes.add("class1");
        dataset.classes.add(clazz);
        assertEquals(clazz, dataset.classValue(1));
    }

    @Test
    public void testCopy() {
        Instance inst1 = new Instance();
        Instance inst2 = new Instance();
        dataset.add(inst1);
        dataset.add(inst2);
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        assertEquals(dataset.size(), copy.size());
    }

    // Include additional test cases for edge cases and error scenarios


}