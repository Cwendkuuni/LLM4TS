import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

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
    public void testAdd() {
        assertTrue(dataset.add(instance1));
        assertTrue(dataset.contains(instance1));
        assertFalse(dataset.isEmpty());
    }

    @Test
    public void testAddAll() {
        List<Instance> list = new ArrayList<>();
        list.add(instance2);
        list.add(instance3);

        assertTrue(dataset.addAll(list));
        assertTrue(dataset.containsAll(list));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        List<Instance> list = new ArrayList<>();
        list.add(instance2);
        list.add(instance3);

        assertTrue(dataset.add(instance1));
        assertTrue(dataset.addAll(0, list));

        assertEquals(instance2, dataset.get(0));
        assertEquals(instance3, dataset.get(1));
        assertEquals(instance1, dataset.get(2));
    }

    @Test
    public void testClear() {
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        dataset.clear();

        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddElement() {
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        dataset.addElement(instance3);

        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
        assertEquals(instance3, dataset.get(2));
    }

    @Test
    public void testInsertElementAt() {
        dataset.insertElementAt(instance1, 0);
        dataset.insertElementAt(instance2, 0);
        dataset.insertElementAt(instance3, 1);

        assertEquals(instance2, dataset.get(0));
        assertEquals(instance3, dataset.get(1));
        assertEquals(instance1, dataset.get(2));
    }

    @Test
    public void testSetElementAt() {
        dataset.setElementAt(instance1, 0);
        dataset.setElementAt(instance2, 1);
        dataset.setElementAt(instance3, 2);

        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
        assertEquals(instance3, dataset.get(2));
    }

    @Test
    public void testInstance() {
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
        assertEquals(instance3, dataset.instance(2));
    }

    @Test
    public void testClasses() {
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        assertNotNull(dataset.classes());
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testKNearest() {
        DistanceMeasure distanceMeasure = new DistanceMeasure();

        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        Set<Instance> nearest = dataset.kNearest(2, instance1, distanceMeasure);

        assertNotNull(nearest);
        assertTrue(nearest.isEmpty());
    }

    @Test
    public void testFolds() {
        Random random = new Random();

        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        Dataset[] folds = dataset.folds(3, random);

        assertNotNull(folds);
        assertEquals(3, folds.length);
    }

    @Test
    public void testNoAttributes() {
        instance1.addAttribute("attribute1", 1);
        instance2.addAttribute("attribute2", 2);
        instance3.addAttribute("attribute3", 3);

        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        int attributes = dataset.noAttributes();

        assertEquals(1, attributes);
    }

    @Test
    public void testClassIndex() {
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        int classIndex = dataset.classIndex("class1");

        assertEquals(-1, classIndex);
    }

    @Test
    public void testClassValue() {
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        Object classValue = dataset.classValue(0);

        assertNull(classValue);
    }

    @Test
    public void testCopy() {
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        Dataset copy = dataset.copy();

        assertNotNull(copy);
        assertEquals(dataset.size(), copy.size());
    }
}