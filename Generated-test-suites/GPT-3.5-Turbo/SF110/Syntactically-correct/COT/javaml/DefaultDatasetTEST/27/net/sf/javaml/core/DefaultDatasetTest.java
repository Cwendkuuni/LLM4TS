import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Dataset;

public class DefaultDatasetTest {

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        boolean result = dataset.add(instance);
        assertTrue(result);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        boolean result = dataset.addAll(Arrays.asList(instance1, instance2));
        assertTrue(result);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        boolean result = dataset.addAll(0, Arrays.asList(instance1, instance2));
        assertTrue(result);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(0, instance);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.setElementAt(instance, 0);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset.classes());
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        Set<Instance> nearest = dataset.kNearest(1, instance, dm);
        assertNotNull(nearest);
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        Dataset[] folds = dataset.folds(5, rg);
        assertNotNull(folds);
        assertEquals(5, folds.length);
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        int result = dataset.noAttributes();
        assertEquals(0, result);
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Object clazz = new Object();
        int result = dataset.classIndex(clazz);
        assertEquals(-1, result);
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Object classValue = new Object();
        dataset.classes().add(classValue);
        Object result = dataset.classValue(0);
        assertEquals(classValue, result);
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        Dataset copy = dataset.copy();
        assertNotNull(copy);
        assertEquals(dataset.size(), copy.size());
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testNoAttributesEmptyDataset() {
        DefaultDataset dataset = new DefaultDataset();
        int result = dataset.noAttributes();
        assertEquals(0, result);
    }
    
    @Test
    public void testClassIndexNullClass() {
        DefaultDataset dataset = new DefaultDataset();
        int result = dataset.classIndex(null);
        assertEquals(-1, result);
    }

    @Test
    public void testClassValueOutOfBounds() {
        DefaultDataset dataset = new DefaultDataset();
        Object result = dataset.classValue(0);
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.remove(); // Unsupported operation on dataset
    }
}