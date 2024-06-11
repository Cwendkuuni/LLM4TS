import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testAdd() {
        assertEquals(0, dataset.size());
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testAddAll() {
        assertEquals(0, dataset.size());
        DefaultDataset dataset2 = new DefaultDataset();
        dataset2.add(new Instance());
        dataset2.add(new Instance());
        dataset.addAll(dataset2);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        assertEquals(0, dataset.size());
        DefaultDataset dataset2 = new DefaultDataset();
        dataset2.add(new Instance());
        dataset2.add(new Instance());
        dataset.addAll(0, dataset2);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        assertEquals(2, dataset.size());
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddElement() {
        dataset.addElement(new Instance());
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInsertElementAt() {
        dataset.insertElementAt(new Instance(), 0);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testSetElementAt() {
        dataset.addElement(new Instance());
        dataset.setElementAt(new Instance(), 0);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        assertNotNull(dataset.classes());
    }

    @Test
    public void testKNearest() {
        dataset.add(new Instance());
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.add(new Instance());
        assertNotNull(dataset.kNearest(1, instance, null));
    }

    @Test
    public void testFolds() {
        Dataset[] folds = dataset.folds(5, new Random());
        assertNotNull(folds);
        assertEquals(5, folds.length);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
        Instance instance = new Instance();
        instance.addAttribute(1);
        dataset.add(instance);
        assertEquals(1, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        dataset.classes().add("class1");
        dataset.classes().add("class2");
        assertEquals(0, dataset.classIndex("class1"));
        assertEquals(1, dataset.classIndex("class2"));
    }

    @Test
    public void testClassValue() {
        dataset.classes().add("class1");
        dataset.classes().add("class2");
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
    }

    @Test
    public void testCopy() {
        dataset.add(new Instance());
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        assertEquals(dataset.size(), copy.size());
    }
}