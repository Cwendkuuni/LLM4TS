import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        assertEquals(2, dataset.size());

        DefaultDataset anotherDataset = new DefaultDataset();
        anotherDataset.addAll(dataset);
        assertEquals(4, anotherDataset.size());
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
        dataset.add(new Instance());
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.insertElementAt(new Instance(), 0);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.setElementAt(new Instance(), 0);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(1, "class1"));
        dataset.add(new Instance(2, "class2"));
        assertEquals(2, dataset.classes().size());
        assertTrue(dataset.classes().contains("class1"));
        assertTrue(dataset.classes().contains("class2"));
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        dataset.add(new Instance(1, 1, 1));
        dataset.add(new Instance(2, 2, 2));
        dataset.add(new Instance(3, 3, 3));

        assertEquals(2, dataset.kNearest(2, inst, dm).size());
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        Dataset[] folds = dataset.folds(5, rg);
        assertEquals(5, folds.length);
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(1, 1, 1));
        assertEquals(3, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(1, "class1"));
        dataset.add(new Instance(2, "class2"));
        assertEquals(0, dataset.classIndex("class1"));
        assertEquals(1, dataset.classIndex("class2"));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(1, "class1"));
        dataset.add(new Instance(2, "class2"));
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance(1, "class1"));
        dataset.add(new Instance(2, "class2"));
        Dataset copy = dataset.copy();
        assertEquals(2, copy.size());
        assertEquals("class1", copy.instance(0).classValue());
        assertEquals("class2", copy.instance(1).classValue());
    }

}