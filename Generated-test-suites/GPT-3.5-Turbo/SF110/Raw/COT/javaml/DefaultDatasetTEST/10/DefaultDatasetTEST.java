import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    private DistanceMeasure distanceMeasure;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
        distanceMeasure = new EuclideanDistance();
    }

    @Test
    public void testAdd() {
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
        assertSame(instance, dataset.get(0));
    }

    @Test
    public void testAddAll() {
        DefaultDataset coll = new DefaultDataset();
        assertTrue(dataset.addAll(coll));
        assertEquals(coll.size(), dataset.size());
    }

    @Test
    public void testAddAllAtIndex() {
        DefaultDataset coll = new DefaultDataset();
        assertTrue(dataset.addAll(0, coll));
        assertEquals(coll.size(), dataset.size());
    }

    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAddElement() {
        Instance element = new Instance();
        dataset.addElement(element);
        assertEquals(1, dataset.size());
        assertSame(element, dataset.get(0));
    }

    @Test
    public void testInsertElementAt() {
        Instance element = new Instance();
        dataset.insertElementAt(element, 0);
        assertEquals(1, dataset.size());
        assertSame(element, dataset.get(0));
    }

    @Test
    public void testSetElementAt() {
        Instance element = new Instance();
        dataset.setElementAt(element, 0);
        assertEquals(1, dataset.size());
        assertSame(element, dataset.get(0));
    }

    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertSame(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        assertNotNull(dataset.classes());
    }

    @Test
    public void testKNearest() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertNotNull(dataset.kNearest(1, instance, distanceMeasure));
    }

    @Test
    public void testFolds() {
        assertNotNull(dataset.folds(2, new Random()));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        assertNull(dataset.classValue(0));
    }

    @Test
    public void testCopy() {
        assertNotNull(dataset.copy());
    }

}
