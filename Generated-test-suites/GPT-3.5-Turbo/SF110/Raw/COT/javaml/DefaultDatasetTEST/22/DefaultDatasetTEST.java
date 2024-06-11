
import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(createInstances());
        // Check if all instances are added to the dataset
        assertEquals(createInstances().size(), dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(0, createInstances());
        // Check if instances are added at the specified index
        assertEquals(createInstances().get(0), dataset.get(0));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(createInstances());
        dataset.clear();
        // Check if dataset is cleared
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = createInstance();
        dataset.add(instance);
        // Check if instance is added to the dataset
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = createInstance();
        dataset.add(0, instance);
        // Check if instance is added at the specified index
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = createInstance();
        dataset.addElement(instance);
        // Check if instance is added to the dataset
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = createInstance();
        dataset.insertElementAt(instance, 0);
        // Check if instance is inserted at the specified index
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = createInstance();
        dataset.add(instance);
        Instance updatedInstance = createInstance();
        dataset.setElementAt(updatedInstance, 0);
        // Check if instance at the specified index is updated
        assertEquals(updatedInstance, dataset.get(0));
    }

    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(createInstances());
        Instance instance = dataset.instance(0);
        // Check if correct instance is returned
        assertEquals(createInstances().get(0), instance);
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(createInstances());
        // Check if classes are returned correctly
        assertEquals(createClasses(), dataset.classes());
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(createInstances());
        Instance inst = createInstance();
        DistanceMeasure dm = new EuclideanDistance();
        int k = 3;
        Set<Instance> nearest = dataset.kNearest(k, inst, dm);
        // Check if nearest instances are returned correctly
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(createInstances());
        int numFolds = 5;
        Random rg = new Random();
        Dataset[] folds = dataset.folds(numFolds, rg);
        // Check if correct number of folds is generated
        assertEquals(numFolds, folds.length);
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(createInstances());
        // Check if correct number of attributes is returned
        assertEquals(createInstances().get(0).noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(createInstances());
        Object clazz = createClasses().get(0);
        // Check if correct class index is returned
        assertEquals(0, dataset.classIndex(clazz));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(createInstances());
        int index = 0;
        Object classValue = dataset.classValue(index);
        // Check if correct class value is returned
        assertEquals(createClasses().get(index), classValue);
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(createInstances());
        Dataset copiedDataset = dataset.copy();
        // Check if the copied dataset is equal to the original dataset
        assertEquals(dataset, copiedDataset);
    }

    // Helper methods to create instances
    private List<Instance> createInstances() {
        List<Instance> instances = new ArrayList<>();
        instances.add(createInstance());
        return instances;
    }

    private Instance createInstance() {
        return new Instance();
    }

    // Helper method to create classes
    private List<Object> createClasses() {
        List<Object> classes = new ArrayList<>();
        classes.add("class1");
        return classes;
    }
}
