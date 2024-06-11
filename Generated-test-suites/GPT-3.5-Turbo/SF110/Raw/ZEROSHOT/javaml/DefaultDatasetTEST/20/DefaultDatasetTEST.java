import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        boolean result = dataset.add(instance);
        assertTrue(result);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testAddAll() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        boolean result = dataset.addAll(instances);
        assertTrue(result);
        assertEquals(2, dataset.size());
        assertEquals(instances.get(0), dataset.get(0));
        assertEquals(instances.get(1), dataset.get(1));
    }

    @Test
    public void testAddAllWithIndex() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());
        dataset.add(new Instance());
        boolean result = dataset.addAll(0, instances);
        assertTrue(result);
        assertEquals(3, dataset.size());
        assertEquals(instances.get(0), dataset.get(0));
        assertEquals(instances.get(1), dataset.get(1));
        assertEquals(dataset.get(2), new Instance());
    }

    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testSetElementAt() {
        dataset.add(new Instance());
        Instance instance = new Instance();
        dataset.setElementAt(instance, 0);
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        Instance result = dataset.instance(0);
        assertEquals(instance, result);
    }

    @Test
    public void testClasses() {
        SortedSet<Object> classes = dataset.classes();
        assertEquals(new TreeSet<Object>(), classes);
    }

    @Test
    public void testKNearest() {
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        Set<Instance> result = dataset.kNearest(1, inst, dm);
        assertNotNull(result);
    }

    @Test
    public void testFolds() {
        Random rg = new Random();
        Dataset[] result = dataset.folds(5, rg);
        assertNotNull(result);
        assertEquals(5, result.length);
    }

    @Test
    public void testNoAttributes() {
        int result = dataset.noAttributes();
        assertEquals(0, result);
    }

    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        int result = dataset.classIndex(clazz);
        assertEquals(-1, result);
    }

    @Test
    public void testClassValue() {
        int index = 0;
        Object result = dataset.classValue(index);
        assertNull(result);
    }

    @Test
    public void testCopy() {
        Dataset result = dataset.copy();
        assertNotNull(result);
        assertNotSame(dataset, result);
    }
}