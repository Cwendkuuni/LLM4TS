import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

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
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(new Instance());
        collection.add(new Instance());
        assertTrue(dataset.addAll(collection));
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsAll(collection));
    }
    
    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> collection = new ArrayList<Instance>();
        collection.add(new Instance());
        collection.add(new Instance());
        assertTrue(dataset.addAll(0, collection));
        assertEquals(2, dataset.size());
        assertEquals(collection.iterator().next(), dataset.get(0));
    }
    
    @Test
    public void testClear() {
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertEquals(0, dataset.size());
        assertTrue(dataset.isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testSetElementAt() {
        Instance instance = new Instance();
        dataset.add(instance);
        Instance newInstance = new Instance();
        dataset.setElementAt(newInstance, 0);
        assertEquals(1, dataset.size());
        assertEquals(newInstance, dataset.get(0));
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        TreeSet<Object> classes = new TreeSet<Object>();
        classes.add("Class 1");
        classes.add("Class 2");
        dataset.classes = classes;
        assertEquals(classes, dataset.classes());
    }
    
    @Test
    public void testKNearest() {
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        Set<Instance> nearest = dataset.kNearest(2, inst, dm);
        assertEquals(2, nearest.size());
    }
    
    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        vector.put(new Instance(), 0.5);
        vector.put(new Instance(), 0.3);
        vector.put(new Instance(), 0.7);
        double max = dataset.removeFarthest(vector);
        assertEquals(0.7, max, 0.0001);
        assertEquals(2, vector.size());
    }
    
    @Test
    public void testFolds() {
        int numFolds = 3;
        Random rg = new Random();
        Dataset[] folds = dataset.folds(numFolds, rg);
        assertEquals(numFolds, folds.length);
        for (Dataset fold : folds) {
            assertTrue(fold instanceof Fold);
        }
    }
    
    @Test
    public void testNoAttributes() {
        Instance instance = new Instance();
        instance.addAttribute(1);
        dataset.add(instance);
        assertEquals(1, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        dataset.classes.add("Class 1");
        dataset.classes.add("Class 2");
        assertEquals(0, dataset.classIndex("Class 1"));
        assertEquals(1, dataset.classIndex("Class 2"));
        assertEquals(-1, dataset.classIndex("Class 3"));
    }
    
    @Test
    public void testClassValue() {
        dataset.classes.add("Class 1");
        dataset.classes.add("Class 2");
        assertEquals("Class 1", dataset.classValue(0));
        assertEquals("Class 2", dataset.classValue(1));
        assertNull(dataset.classValue(2));
    }
    
    @Test
    public void testCopy() {
        Instance instance = new Instance();
        dataset.add(instance);
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.instance(0), copy.instance(0));
    }
}