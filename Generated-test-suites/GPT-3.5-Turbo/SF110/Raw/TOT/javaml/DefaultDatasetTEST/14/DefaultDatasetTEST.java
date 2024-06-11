import org.junit.Before;
import org.junit.Test;
import java.util.*;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.*;
import java.util.SortedSet;
import java.util.Set;
import static org.junit.Assert.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    private Instance instance1;
    private Instance instance2;
    private Instance instance3;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
        instance1 = new Instance();
        instance2 = new Instance();
        instance3 = new Instance();
    }

    @Test
    public void testDefaultDatasetConstructor() {
        assertEquals(0, dataset.size());
    }

    @Test
    public void testDefaultDatasetCollectionConstructor() {
        List<Instance> list = new ArrayList<>();
        list.add(instance1);
        list.add(instance2);
        dataset = new DefaultDataset(list);
        assertEquals(2, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testCheckSingleInstance() {
        dataset.add(instance1);
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testCheckMultipleInstances() {
        List<Instance> list = new ArrayList<>();
        list.add(instance1);
        list.add(instance2);
        list.add(instance3);
        dataset.addAll(list);
        assertEquals(3, dataset.size());
        assertTrue(dataset.containsAll(list));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testAddAllCollection() {
        List<Instance> list = new ArrayList<>();
        list.add(instance1);
        list.add(instance2);
        list.add(instance3);
        assertTrue(dataset.addAll(list));
        assertEquals(3, dataset.size());
        assertTrue(dataset.containsAll(list));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testAddAllIndexCollection() {
        List<Instance> list = new ArrayList<>();
        list.add(instance1);
        list.add(instance2);
        assertTrue(dataset.add(instance3));
        assertTrue(dataset.addAll(0, list));
        assertEquals(3, dataset.size());
        assertTrue(dataset.containsAll(list));
        assertTrue(dataset.contains(instance3));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testAddInstance() {
        assertTrue(dataset.add(instance1));
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testAddIndexInstance() {
        assertTrue(dataset.add(instance1));
        dataset.add(0, instance2);
        assertEquals(2, dataset.size());
        assertEquals(instance2, dataset.get(0));
        assertEquals(instance1, dataset.get(1));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testAddElement() {
        dataset.addElement(instance1);
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testInsertElementAt() {
        dataset.insertElementAt(instance1, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testSetElementAt() {
        assertTrue(dataset.add(instance1));
        dataset.setElementAt(instance2, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance2, dataset.get(0));
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testClear() {
        assertTrue(dataset.add(instance1));
        assertTrue(dataset.add(instance2));
        assertTrue(dataset.add(instance3));
        dataset.clear();
        assertEquals(0, dataset.size());
        assertFalse(dataset.contains(instance1));
        assertFalse(dataset.contains(instance2));
        assertFalse(dataset.contains(instance3));
        assertTrue(dataset.classes.isEmpty());
    }

    @Test
    public void testInstance() {
        assertTrue(dataset.add(instance1));
        assertTrue(dataset.add(instance2));
        assertTrue(dataset.add(instance3));
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
        assertEquals(instance3, dataset.instance(2));
    }

    @Test
    public void testClasses() {
        SortedSet<Object> classes = dataset.classes();
        assertTrue(classes.isEmpty());

        instance1.setClassValue("Class1");
        dataset.add(instance1);
        classes = dataset.classes();
        assertEquals(1, classes.size());
        assertTrue(classes.contains("Class1"));
        assertFalse(classes.contains("Class2"));
    }

    @Test
    public void testKNearest() {
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        Set<Instance> nearest = dataset.kNearest(2, instance1, distanceMeasure);
        assertEquals(2, nearest.size());
        assertTrue(nearest.contains(instance2));
        assertTrue(nearest.contains(instance3));
    }

    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(instance1, 1.0);
        vector.put(instance2, 2.0);
        vector.put(instance3, 3.0);
        double max = dataset.removeFarthest(vector);
        assertEquals(3.0, max, 0.001);
        assertFalse(vector.containsValue(3.0));
    }

    @Test
    public void testFolds() {
        Random random = new Random();
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        Dataset[] folds = dataset.folds(2, random);
        assertEquals(2, folds.length);
        assertNotSame(dataset, folds[0]);
        assertNotSame(dataset, folds[1]);
        assertNotSame(folds[0], folds[1]);
        assertEquals(2, folds[0].size());
        assertEquals(1, folds[1].size());
        assertTrue(folds[0].contains(instance1));
        assertTrue(folds[0].contains(instance2));
        assertTrue(folds[1].contains(instance3));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
        dataset.add(instance1);
        assertEquals(0, dataset.noAttributes());
        instance1.addAttribute("Attribute1");
        assertEquals(1, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        instance1.setClassValue("Class1");
        instance2.setClassValue("Class2");
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(0, dataset.classIndex("Class1"));
        assertEquals(1, dataset.classIndex("Class2"));
        assertEquals(-1, dataset.classIndex("Class3"));
    }

    @Test
    public void testClassValue() {
        instance1.setClassValue("Class1");
        instance2.setClassValue("Class2");
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals("Class1", dataset.classValue(0));
        assertEquals("Class2", dataset.classValue(1));
        assertNull(dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        dataset.add(instance1);
        dataset.add(instance2);
        Dataset copy = dataset.copy();
        assertNotSame(dataset, copy);
        assertEquals(dataset.size(), copy.size());
        assertTrue(copy.contains(instance1));
        assertTrue(copy.contains(instance2));
    }

}