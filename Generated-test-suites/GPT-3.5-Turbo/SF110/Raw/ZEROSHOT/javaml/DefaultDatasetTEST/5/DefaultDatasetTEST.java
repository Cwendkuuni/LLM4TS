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
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testAddAll() {
        DefaultDataset otherDataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        otherDataset.add(instance1);
        otherDataset.add(instance2);
        assertTrue(dataset.addAll(otherDataset));
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }

    @Test
    public void testAddAllAtIndex() {
        DefaultDataset otherDataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        otherDataset.add(instance1);
        otherDataset.add(instance2);
        assertTrue(dataset.addAll(0, otherDataset));
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }

    @Test
    public void testClear() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testInsertElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.insertElementAt(instance2, 1);
        
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }

    @Test
    public void testSetElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        dataset.add(instance1);
        dataset.setElementAt(instance2, 0);
        
        assertEquals(1, dataset.size());
        assertEquals(instance2, dataset.instance(0));
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        assertTrue(dataset.classes().isEmpty());
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(2, dataset.classes().size());
        assertTrue(dataset.classes().contains("class1"));
        assertTrue(dataset.classes().contains("class2"));
    }

    @Test
    public void testKNearest() {
        DefaultDataset otherDataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setNoAttributes(1);
        Instance instance2 = new Instance();
        instance2.setNoAttributes(2);
        otherDataset.add(instance1);
        otherDataset.add(instance2);
        DistanceMeasure measure = new EuclideanDistance();
        assertEquals(0, dataset.kNearest(1, instance1, measure).size());
        dataset.addAll(otherDataset);
        assertEquals(1, dataset.kNearest(1, instance1, measure).size());
        assertTrue(dataset.kNearest(1, instance1, measure).contains(instance2));
    }

    @Test
    public void testFolds() {
        Random rg = new Random();
        int numFolds = 5;
        int size = 10;
        dataset = new DefaultDataset(size);
        Dataset[] folds = dataset.folds(numFolds, rg);
        assertEquals(numFolds, folds.length);
        for (int i = 0; i < numFolds; i++) {
            assertEquals(size / numFolds + 1, folds[i].size());
        }
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
        Instance instance = new Instance();
        instance.setNoAttributes(5);
        dataset.add(instance);
        assertEquals(5, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(0, dataset.classIndex("class1"));
        assertEquals(1, dataset.classIndex("class2"));
    }

    @Test
    public void testClassValue() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
    }

    @Test
    public void testCopy() {
        DefaultDataset otherDataset = new DefaultDataset();
        Instance instance1 = new Instance();
        instance1.setNoAttributes(1);
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setNoAttributes(2);
        instance2.setClassValue("class2");
        otherDataset.add(instance1);
        otherDataset.add(instance2);
        dataset = otherDataset.copy();
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
        assertEquals("class1", dataset.instance(0).getClassValue());
        assertEquals("class2", dataset.instance(1).getClassValue());
    }

}