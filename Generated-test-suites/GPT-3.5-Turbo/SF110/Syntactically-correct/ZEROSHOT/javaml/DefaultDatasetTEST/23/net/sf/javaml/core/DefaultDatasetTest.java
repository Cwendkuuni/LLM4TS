import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2)));
    }

    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        assertTrue(dataset.addAll(0, Arrays.asList(instance1, instance2)));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
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
        assertEquals(instance, dataset.get(dataset.size() - 1));
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
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.setElementAt(instance1, 0);
        dataset.setElementAt(instance2, 1);
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
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
        // create instances and add to dataset
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);

        // create distance measure
        DistanceMeasure dm = new EuclideanDistance();

        // get nearest instances
        Set<Instance> nearest = dataset.kNearest(1, instance1, dm);

        // check result
        assertEquals(1, nearest.size());
        assertFalse(nearest.contains(instance1));
        assertTrue(nearest.contains(instance2));
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        Dataset[] folds = dataset.folds(2, new Random());
        assertEquals(2, folds.length);
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        // add classes to the dataset
        dataset.classes.add("class1");
        dataset.classes.add("class2");
        dataset.classes.add("class3");

        // test class indexes
        assertEquals(0, dataset.classIndex("class1"));
        assertEquals(1, dataset.classIndex("class2"));
        assertEquals(2, dataset.classIndex("class3"));
        assertEquals(-1, dataset.classIndex("class4"));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        // add classes to the dataset
        dataset.classes.add("class1");
        dataset.classes.add("class2");
        dataset.classes.add("class3");

        // test class values
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
        assertEquals("class3", dataset.classValue(2));
        assertNull(dataset.classValue(3));
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.get(0), copy.get(0));
    }
}