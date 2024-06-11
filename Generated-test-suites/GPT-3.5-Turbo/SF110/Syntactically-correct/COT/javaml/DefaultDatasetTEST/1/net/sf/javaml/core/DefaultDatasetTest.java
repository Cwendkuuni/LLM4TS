import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import java.util.*;


public class DefaultDatasetTest {

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance e = new Instance();
        assertTrue(dataset.add(e));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> list = new ArrayList<>();
        Instance e1 = new Instance();
        Instance e2 = new Instance();
        list.add(e1);
        list.add(e2);
        assertTrue(dataset.addAll(list));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> list = new ArrayList<>();
        Instance e1 = new Instance();
        Instance e2 = new Instance();
        list.add(e1);
        list.add(e2);
        assertTrue(dataset.addAll(0, list));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance e = new Instance();
        dataset.add(e);
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance e = new Instance();
        dataset.addElement(e);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance e = new Instance();
        dataset.insertElementAt(e, 0);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance e = new Instance();
        dataset.addElement(new Instance());
        dataset.setElementAt(e, 0);
        assertEquals(e, dataset.get(0));
    }

    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance e = new Instance();
        dataset.add(e);
        assertEquals(e, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        Set<Object> classes = dataset.classes();
        assertNotNull(classes);
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        DistanceMeasure dm = new DistanceMeasure();
        Instance inst = new Instance();
        Set<Instance> nearest = dataset.kNearest(1, inst, dm);
        assertNotNull(nearest);
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        Dataset[] folds = dataset.folds(2, rg);
        assertNotNull(folds);
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Object clazz = new Object();
        assertEquals(-1, dataset.classIndex(clazz));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Object classValue = dataset.classValue(0);
        assertNull(classValue);
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Dataset copy = dataset.copy();
        assertNotNull(copy);
    }
}