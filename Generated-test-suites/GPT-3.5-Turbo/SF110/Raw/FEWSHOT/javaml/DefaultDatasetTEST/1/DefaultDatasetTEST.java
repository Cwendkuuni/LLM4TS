import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    private Instance instance;
    private DistanceMeasure distanceMeasure;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
        instance = new Instance();
        distanceMeasure = new DistanceMeasure();
    }

    @Test
    public void testConstructor() {
        dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testConstructorWithCollection() {
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(new Instance());
        collection.add(new Instance());
        dataset = new DefaultDataset(collection);
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testCheckWithCollection() {
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(new Instance());
        collection.add(new Instance());
        dataset.check(collection);
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testCheckWithInstance() {
        instance = new Instance();
        dataset.check(instance);
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testAddAll() {
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(new Instance());
        collection.add(new Instance());
        assertTrue(dataset.addAll(collection));
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testAddAllWithIndex() {
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(new Instance());
        collection.add(new Instance());
        assertTrue(dataset.addAll(0, collection));
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testAdd() {
        assertTrue(dataset.add(instance));
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testAddWithIndex() {
        dataset.add(0, instance);
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testAddElement() {
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testInsertElementAt() {
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testSetElementAt() {
        dataset.add(new Instance());
        dataset.setElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNotNull(dataset.classes);
    }

    @Test
    public void testInstance() {
        Instance instance1 = new Instance();
        dataset.add(instance1);
        assertSame(instance1, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        SortedSet<Object> classes = dataset.classes();
        assertEquals(0, classes.size());
        assertTrue(classes instanceof TreeSet);
    }

    @Test
    public void testKNearest() {
        Set<Instance> nearest = dataset.kNearest(3, instance, distanceMeasure);
        assertNotNull(nearest);
        assertTrue(nearest instanceof HashSet);
    }

    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        vector.put(new Instance(), 0.5);
        vector.put(new Instance(), 0.8);
        double max = dataset.removeFarthest(vector);
        assertEquals(0.8, max, 0.001);
        assertEquals(1, vector.size());
    }

    @Test
    public void testFolds() {
        int numFolds = 5;
        Random rg = new Random();
        Dataset[] folds = dataset.folds(numFolds, rg);
        assertNotNull(folds);
        assertEquals(numFolds, folds.length);
        for (Dataset fold : folds) {
            assertTrue(fold instanceof Fold);
        }
    }

    @Test
    public void testNoAttributes() {
        int attributes = dataset.noAttributes();
        assertEquals(0, attributes);
    }

    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        int index = dataset.classIndex(clazz);
        assertEquals(-1, index);
    }

    @Test
    public void testClassValue() {
        assertNull(dataset.classValue(0));
    }

    @Test
    public void testCopy() {
        DefaultDataset copy = dataset.copy();
        assertNotSame(dataset, copy);
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.maxAttributes, copy.maxAttributes);
        assertNotSame(dataset.classes, copy.classes);
    }
}