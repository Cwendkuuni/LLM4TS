import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.distance.DistanceMeasure;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        // Test case 1: empty collection
        Collection<Instance> c1 = new Vector<>();
        DefaultDataset dataset1 = new DefaultDataset();
        boolean result1 = dataset1.addAll(c1);
        assertTrue(result1);
        assertEquals(0, dataset1.classes().size());
        assertEquals(0, dataset1.noAttributes());

        // Test case 2: non-empty collection
        Instance instance1 = new Instance();
        Collection<Instance> c2 = new Vector<>();
        c2.add(instance1);
        DefaultDataset dataset2 = new DefaultDataset();
        boolean result2 = dataset2.addAll(c2);
        assertTrue(result2);
        assertEquals(1, dataset2.classes().size());
        assertEquals(0, dataset2.noAttributes());
        assertTrue(dataset2.contains(instance1));
    }

    @Test
    public void testAddAllAtIndex() {
        // Test case 1: empty collection
        Collection<Instance> c1 = new Vector<>();
        DefaultDataset dataset1 = new DefaultDataset();
        boolean result1 = dataset1.addAll(0, c1);
        assertTrue(result1);
        assertEquals(0, dataset1.classes().size());
        assertEquals(0, dataset1.noAttributes());

        // Test case 2: non-empty collection
        Instance instance1 = new Instance();
        Collection<Instance> c2 = new Vector<>();
        c2.add(instance1);
        DefaultDataset dataset2 = new DefaultDataset();
        boolean result2 = dataset2.addAll(0, c2);
        assertTrue(result2);
        assertEquals(1, dataset2.classes().size());
        assertEquals(0, dataset2.noAttributes());
        assertTrue(dataset2.contains(instance1));
    }

    @Test
    public void testClear() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);

        dataset.clear();

        assertEquals(0, dataset.size());
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testAdd() {
        // Test case 1: adding an instance with class value
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        DefaultDataset dataset1 = new DefaultDataset();
        boolean result1 = dataset1.add(instance1);
        assertTrue(result1);
        assertEquals(1, dataset1.classes().size());
        assertEquals(0, dataset1.noAttributes());
        assertTrue(dataset1.contains(instance1));

        // Test case 2: adding an instance without class value
        Instance instance2 = new Instance();
        DefaultDataset dataset2 = new DefaultDataset();
        boolean result2 = dataset2.add(instance2);
        assertTrue(result2);
        assertEquals(0, dataset2.classes().size());
        assertEquals(0, dataset2.noAttributes());
        assertTrue(dataset2.contains(instance2));
    }

    @Test
    public void testAddAtIndex() {
        // Test case 1: adding an instance with class value at index 0
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        DefaultDataset dataset1 = new DefaultDataset();
        dataset1.add(0, instance1);
        assertEquals(1, dataset1.classes().size());
        assertEquals(0, dataset1.noAttributes());
        assertTrue(dataset1.contains(instance1));

        // Test case 2: adding an instance without class value at index 1
        Instance instance2 = new Instance();
        DefaultDataset dataset2 = new DefaultDataset();
        dataset2.add(1, instance2);
        assertEquals(0, dataset2.classes().size());
        assertEquals(0, dataset2.noAttributes());
        assertTrue(dataset2.contains(instance2));
    }

    @Test
    public void testAddElement() {
        // Test case 1: adding an instance with class value
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        DefaultDataset dataset1 = new DefaultDataset();
        dataset1.addElement(instance1);
        assertEquals(1, dataset1.classes().size());
        assertEquals(0, dataset1.noAttributes());
        assertTrue(dataset1.contains(instance1));

        // Test case 2: adding an instance without class value
        Instance instance2 = new Instance();
        DefaultDataset dataset2 = new DefaultDataset();
        dataset2.addElement(instance2);
        assertEquals(0, dataset2.classes().size());
        assertEquals(0, dataset2.noAttributes());
        assertTrue(dataset2.contains(instance2));
    }

    @Test
    public void testInsertElementAt() {
        // Test case 1: adding an instance with class value at index 0
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        DefaultDataset dataset1 = new DefaultDataset();
        dataset1.insertElementAt(instance1, 0);
        assertEquals(1, dataset1.classes().size());
        assertEquals(0, dataset1.noAttributes());
        assertTrue(dataset1.contains(instance1));

        // Test case 2: adding an instance without class value at index 1
        Instance instance2 = new Instance();
        DefaultDataset dataset2 = new DefaultDataset();
        dataset2.insertElementAt(instance2, 1);
        assertEquals(0, dataset2.classes().size());
        assertEquals(0, dataset2.noAttributes());
        assertTrue(dataset2.contains(instance2));
    }

    @Test
    public void testSetElementAt() {
        // Test case 1: setting an instance with class value at index 0
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.setElementAt(instance2, 0);
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.noAttributes());
        assertFalse(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testInstance() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);

        Instance result1 = dataset.instance(0);
        assertEquals(instance1, result1);

        Instance result2 = dataset.instance(1);
        assertEquals(instance2, result2);
    }

    @Test
    public void testClasses() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);

        SortedSet<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setAttribute(0, 1.0);
        Instance instance2 = new Instance();
        instance2.setAttribute(0, 2.0);
        dataset.add(instance1);
        dataset.add(instance2);

        DistanceMeasure dm = new DistanceMeasure();

        Set<Instance> kNearest = dataset.kNearest(1, instance1, dm);
        assertEquals(1, kNearest.size());
        assertTrue(kNearest.contains(instance2));
    }

    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Map<Instance, Double> vector = new HashMap<>();
        Instance instance1 = new Instance();
        instance1.setAttribute(0, 1.0);
        Instance instance2 = new Instance();
        instance2.setAttribute(0, 2.0);
        vector.put(instance1, 1.0);
        vector.put(instance2, 2.0);

        double result = dataset.removeFarthest(vector);
        assertEquals(2.0, result, 0.001);
        assertEquals(1, vector.size());
        assertFalse(vector.containsKey(instance2));
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);

        int numFolds = 2;
        Random rg = new Random();

        Dataset[] folds = dataset.folds(numFolds, rg);
        assertEquals(2, folds.length);
        assertEquals(dataset, folds[0]);
        assertEquals(dataset, folds[1]);
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setAttribute(0, 1.0);
        dataset.add(instance1);

        int result = dataset.noAttributes();
        assertEquals(1, result);
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);

        int result1 = dataset.classIndex("class1");
        assertEquals(0, result1);

        int result2 = dataset.classIndex("class2");
        assertEquals(1, result2);

        int result3 = dataset.classIndex("class3");
        assertEquals(-1, result3);
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);

        Object result1 = dataset.classValue(0);
        assertEquals("class1", result1);

        Object result2 = dataset.classValue(1);
        assertEquals("class2", result2);

        Object result3 = dataset.classValue(2);
        assertNull(result3);
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setAttribute(0, 1.0);
        Instance instance2 = new Instance();
        instance2.setAttribute(0, 2.0);
        dataset.add(instance1);
        dataset.add(instance2);

        Dataset result = dataset.copy();
        assertEquals(dataset, result);
        assertNotSame(dataset, result);
    }
}