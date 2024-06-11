import org.junit.Test;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testConstructorWithCollection() {
        // Create instances for testing
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // Create DefaultDataset using the constructor
        DefaultDataset dataset = new DefaultDataset(instances);

        // Assert that the dataset contains the instances
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testConstructorWithoutCollection() {
        // Create DefaultDataset using the default constructor
        DefaultDataset dataset = new DefaultDataset();

        // Assert that the dataset is empty
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAddAll() {
        // Create instances for testing
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset();

        // Add instances to the dataset
        dataset.addAll(instances);

        // Assert that the dataset contains the instances
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testAddAllAtIndex() {
        // Create instances for testing
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset();

        // Add instances to the dataset at a specific index
        dataset.addAll(0, instances);

        // Assert that the dataset contains the instances in the correct order
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }

    @Test
    public void testClear() {
        // Create instances for testing
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset(instances);

        // Clear the dataset
        dataset.clear();

        // Assert that the dataset is empty
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAdd() {
        // Create an instance for testing
        Instance instance = new Instance();

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset();

        // Add the instance to the dataset
        dataset.add(instance);

        // Assert that the dataset contains the instance
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testAddAtIndex() {
        // Create an instance for testing
        Instance instance = new Instance();

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset();

        // Add the instance to the dataset at a specific index
        dataset.add(0, instance);

        // Assert that the dataset contains the instance at the correct index
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testAddElement() {
        // Create an instance for testing
        Instance instance = new Instance();

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset();

        // Add the instance to the dataset
        dataset.addElement(instance);

        // Assert that the dataset contains the instance
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testInsertElementAt() {
        // Create an instance for testing
        Instance instance = new Instance();

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset();

        // Insert the instance at a specific index in the dataset
        dataset.insertElementAt(instance, 0);

        // Assert that the dataset contains the instance at the correct index
        assertEquals(instance, dataset.get(0));
    }

    @Test
    public void testSetElementAt() {
        // Create instances for testing
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);

        // Create an instance for testing
        Instance newInstance = new Instance();

        // Set the instance at a specific index in the dataset
        dataset.setElementAt(newInstance, 1);

        // Assert that the dataset contains the new instance at the correct index
        assertEquals(newInstance, dataset.get(1));
    }

    @Test
    public void testInstance() {
        // Create instances for testing
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);

        // Get an instance from the dataset
        Instance retrievedInstance = dataset.instance(0);

        // Assert that the retrieved instance is the correct instance
        assertEquals(instance1, retrievedInstance);
    }

    @Test
    public void testClasses() {
        // Create instances for testing
        Instance instance1 = new Instance();
        instance1.setClassValue("A");
        Instance instance2 = new Instance();
        instance2.setClassValue("B");
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset(instances);

        // Get the classes from the dataset
        SortedSet<Object> classes = dataset.classes();

        // Assert that the classes set contains the correct class values
        assertTrue(classes.contains("A"));
        assertTrue(classes.contains("B"));
    }

    @Test
    public void testKNearest() {
        // Create instances for testing
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instance2.setClassValue("A");
        Instance instance3 = new Instance();
        instance3.setClassValue("B");
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);
        instances.add(instance3);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset(instances);

        // Create an instance for testing
        Instance queryInstance = new Instance();

        // Create a distance measure for testing
        DistanceMeasure distanceMeasure = new EuclideanDistance();

        // Get the k nearest instances to the query instance
        Set<Instance> nearestInstances = dataset.kNearest(2, queryInstance, distanceMeasure);

        // Assert that the nearest instances set contains the correct instances
        assertTrue(nearestInstances.contains(instance1));
        assertTrue(nearestInstances.contains(instance2));
        assertFalse(nearestInstances.contains(instance3));
    }

    @Test
    public void removeFarthest() {
        // Create instances for testing
        Instance instance1 = new Instance();
        instance1.setClassValue("A");
        Instance instance2 = new Instance();
        instance2.setClassValue("B");
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset(instances);

        // Create a map of instances and distances for testing
        Map<Instance, Double> instancesMap = new HashMap<>();
        instancesMap.put(instance1, 5.0);
        instancesMap.put(instance2, 10.0);

        // Remove the farthest instance from the map
        double maxDistance = dataset.removeFarthest(instancesMap);

        // Assert that the farthest instance was removed and the maximum distance is correct
        assertFalse(instancesMap.containsKey(instance2));
        assertEquals(10.0, maxDistance, 0.001);
    }

    @Test
    public void testFolds() {
        // Create instances for testing
        Instance instance1 = new Instance();
        instance1.setClassValue("A");
        Instance instance2 = new Instance();
        instance2.setClassValue("B");
        Instance instance3 = new Instance();
        instance3.setClassValue("C");
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);
        instances.add(instance3);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset(instances);

        // Create a random number generator for testing
        Random random = new Random();

        // Get the folds of the dataset
        Dataset[] folds = dataset.folds(2, random);

        // Assert that the folds array is not null and has the correct length
        assertNotNull(folds);
        assertEquals(2, folds.length);

        // Assert that each fold is a Fold instance and contains the correct instances
        assertTrue(folds[0] instanceof Fold);
        assertTrue(folds[1] instanceof Fold);
        assertTrue(folds[0].contains(instance1) || folds[0].contains(instance2) || folds[0].contains(instance3));
        assertTrue(folds[1].contains(instance1) || folds[1].contains(instance2) || folds[1].contains(instance3));
    }

    @Test
    public void testNoAttributes() {
        // Create instances for testing
        Instance instance1 = new Instance();
        instance1.setNoAttributes(5);
        Instance instance2 = new Instance();
        instance2.setNoAttributes(10);
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset(instances);

        // Get the number of attributes in the dataset
        int numAttributes = dataset.noAttributes();

        // Assert that the number of attributes is correct
        assertEquals(5, numAttributes);
    }

    @Test
    public void testClassIndex() {
        // Create instances for testing
        Instance instance1 = new Instance();
        instance1.setClassValue("A");
        Instance instance2 = new Instance();
        instance2.setClassValue("B");
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset(instances);

        // Get the index of a class value in the dataset
        int classIndexA = dataset.classIndex("A");
        int classIndexB = dataset.classIndex("B");

        // Assert that the class indexes are correct
        assertEquals(0, classIndexA);
        assertEquals(1, classIndexB);
    }

    @Test
    public void testClassValue() {
        // Create instances for testing
        Instance instance1 = new Instance();
        instance1.setClassValue("A");
        Instance instance2 = new Instance();
        instance2.setClassValue("B");
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset(instances);

        // Get a class value from the dataset
        Object classValueA = dataset.classValue(0);
        Object classValueB = dataset.classValue(1);

        // Assert that the class values are correct
        assertEquals("A", classValueA);
        assertEquals("B", classValueB);
    }

    @Test
    public void testCopy() {
        // Create instances for testing
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        // Create DefaultDataset
        DefaultDataset dataset = new DefaultDataset(instances);

        // Create a copy of the dataset
        Dataset copyDataset = dataset.copy();

        // Assert that the copy dataset is a DefaultDataset instance
        assertTrue(copyDataset instanceof DefaultDataset);

        // Assert that the copy dataset contains the same instances
        assertTrue(copyDataset.contains(instance1));
        assertTrue(copyDataset.contains(instance2));
    }
}