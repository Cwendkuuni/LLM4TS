import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class DefaultDatasetTest {

    private List<Instance> instances;
    private DefaultDataset dataset;

    @Before
    public void setUp() {
        instances = new ArrayList<>();
        dataset = new DefaultDataset(instances);
    }

    // Test addAll(Collection<? extends Instance> c)
    @Test
    public void addAllTest() {
        assertTrue(dataset.addAll(instances));
    }

    // Test addAll(int index, Collection<? extends Instance> c)
    @Test
    public void addAllAtIndexTest() {
        int index = 0;
        assertTrue(dataset.addAll(index, instances));
    }

    // Test clear()
    @Test
    public void clearTest() {
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    // Test add(Instance e)
    @Test
    public void addTest() {
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
    }

    // Test add(int index, Instance e)
    @Test
    public void addAtIndexTest() {
        Instance instance = new Instance();
        int index = 0;
        dataset.add(index, instance);
        assertEquals(instance, dataset.get(index));
    }

    // Test addElement(Instance e)
    @Test
    public void addElementTest() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertTrue(dataset.contains(instance));
    }

    // Test insertElementAt(Instance e, int index)
    @Test
    public void insertElementAtTest() {
        Instance instance = new Instance();
        int index = 0;
        dataset.insertElementAt(instance, index);
        assertEquals(instance, dataset.get(index));
    }

    // Test setElementAt(Instance e, int index)
    @Test
    public void setElementAtTest() {
        Instance instance = new Instance();
        int index = 0;
        dataset.setElementAt(instance, index);
        assertEquals(instance, dataset.get(index));
    }

    // Test instance(int index)
    @Test
    public void instanceTest() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    // Test classes()
    @Test
    public void classesTest() {
        SortedSet<Object> classes = dataset.classes();
        assertNotNull(classes);
        assertTrue(classes.isEmpty());
    }

    // Test kNearest(int k, Instance inst, DistanceMeasure dm)
    @Test
    public void kNearestTest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        DistanceMeasure dm = new DistanceMeasure();
        Set<Instance> nearest = dataset.kNearest(1, instance1, dm);
        assertNotNull(nearest);
        assertTrue(nearest.contains(instance2));
    }

    // Test removeFarthest(Map<Instance, Double> vector)
    @Test
    public void removeFarthestTest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(instance1, 1.0);
        vector.put(instance2, 2.0);
        double max = dataset.removeFarthest(vector);
        assertEquals(2.0, max, 0.01);
        assertFalse(vector.containsKey(instance2));
    }

    // Test folds(int numFolds, Random rg)
    @Test
    public void foldsTest() {
        int numFolds = 2;
        Random rg = new Random();
        DefaultDataset[] folds = dataset.folds(numFolds, rg);
        assertNotNull(folds);
        assertEquals(numFolds, folds.length);
    }

    // Test noAttributes()
    @Test
    public void noAttributesTest() {
        Instance instance = new Instance();
        dataset.add(instance);
        int noAttributes = dataset.noAttributes();
        assertEquals(instance.noAttributes(), noAttributes);
    }

    // Test classIndex(Object clazz)
    @Test
    public void classIndexTest() {
        Object clazz = "class";
        int classIndex = dataset.classIndex(clazz);
        assertEquals(-1, classIndex);
    }

    // Test classValue(int index)
    @Test
    public void classValueTest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        Object classValue = dataset.classValue(0);
        assertEquals(instance1.classValue(), classValue);
    }

    // Test copy()
    @Test
    public void copyTest() {
        DefaultDataset copy = dataset.copy();
        assertNotNull(copy);
        assertEquals(dataset.size(), copy.size());
    }

}