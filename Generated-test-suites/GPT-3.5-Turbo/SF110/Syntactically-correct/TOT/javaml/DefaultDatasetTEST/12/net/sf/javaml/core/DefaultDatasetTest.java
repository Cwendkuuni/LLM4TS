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
        dataset.add(instance);
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testAddAll() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addAll(Arrays.asList(instance1, instance2));
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testAddAllWithIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.addAll(0, Arrays.asList(instance2));
        assertTrue(dataset.indexOf(instance2) == 0);
    }

    @Test
    public void testClear() {
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.clear();
        assertFalse(dataset.contains(instance));
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertTrue(dataset.contains(instance));
    }

    @Test
    public void testInsertElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.insertElementAt(instance2, 0);
        assertTrue(dataset.indexOf(instance2) == 0);
    }

    @Test
    public void testSetElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.setElementAt(instance2, 0);
        assertTrue(dataset.get(0) == instance2);
    }

    @Test
    public void testInstance() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        assertTrue(dataset.instance(0) == instance1);
        assertTrue(dataset.instance(1) == instance2);
    }

    @Test
    public void testClasses() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instance1.setClassValue("class1");
        instance2.setClassValue("class2");
        dataset.addAll(Arrays.asList(instance1, instance2));
        assertTrue(dataset.classes().contains("class1"));
        assertTrue(dataset.classes().contains("class2"));
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Dataset otherDataset = new DefaultDataset();
        otherDataset.add(instance1);
        otherDataset.add(instance2);
        DistanceMeasure dm = new EuclideanDistance();
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(dataset.kNearest(1, instance1, dm).size(), 1);
        assertEquals(dataset.kNearest(1, instance1, dm).iterator().next(), instance2);
    }

    @Test
    public void testFolds() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Random rg = new Random();
        dataset.add(instance1);
        dataset.add(instance2);
        Dataset[] folds = dataset.folds(2, rg);
        assertEquals(folds.length, 2);
        assertTrue(folds[0].size() == 1);
        assertTrue(folds[1].size() == 1);
    }

    @Test
    public void testNoAttributes() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(dataset.noAttributes(), instance.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instance1.setClassValue("class1");
        instance2.setClassValue("class2");
        dataset.addAll(Arrays.asList(instance1, instance2));
        assertEquals(dataset.classIndex("class1"), 0);
        assertEquals(dataset.classIndex("class2"), 1);
    }

    @Test
    public void testClassValue() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instance1.setClassValue("class1");
        instance2.setClassValue("class2");
        dataset.addAll(Arrays.asList(instance1, instance2));
        assertEquals(dataset.classValue(0), "class1");
        assertEquals(dataset.classValue(1), "class2");
    }

    @Test
    public void testCopy() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.addAll(Arrays.asList(instance1, instance2));
        Dataset copy = dataset.copy();
        assertTrue(copy.contains(instance1));
        assertTrue(copy.contains(instance2));
    }

}