
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testDefaultDatasetCollectionConstructor() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());

        DefaultDataset dataset = new DefaultDataset(instances);
        assertNotNull(dataset);
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();

        boolean added = dataset.add(instance);

        assertTrue(added);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());

        boolean addedAll = dataset.addAll(instances);

        assertTrue(addedAll);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> instances = new ArrayList<>();
        instances.add(new Instance());
        instances.add(new Instance());

        boolean addedAll = dataset.addAll(0, instances);

        assertTrue(addedAll);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());

        dataset.clear();

        assertEquals(0, dataset.size());
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();

        dataset.addElement(instance);

        assertEquals(1, dataset.size());
    }

    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();

        dataset.insertElementAt(instance, 0);

        assertEquals(1, dataset.size());
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        Instance instance = new Instance();

        dataset.setElementAt(instance, 0);

        assertEquals(1, dataset.size());
    }

    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);

        Instance retrievedInstance = dataset.instance(0);

        assertEquals(instance, retrievedInstance);
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();

        SortedSet<Object> classes = dataset.classes();

        assertNotNull(classes);
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();

        Set<Instance> nearest = dataset.kNearest(3, inst, dm);

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

        int numAttributes = dataset.noAttributes();

        assertEquals(0, numAttributes);
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes().add("A");
        dataset.classes().add("B");

        int classIndex = dataset.classIndex("B");

        assertEquals(1, classIndex);
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes().add("A");
        dataset.classes().add("B");

        Object classValue = dataset.classValue(1);

        assertEquals("B", classValue);
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());

        Dataset copy = dataset.copy();

        assertNotNull(copy);
        assertEquals(2, copy.size());
    }
}
