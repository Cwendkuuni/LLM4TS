import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.*;

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
    }
    
    @Test
    public void testAddAll() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2)));
    }
    
    @Test
    public void testAddAllAtIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        assertTrue(dataset.addAll(0, Arrays.asList(instance1, instance2)));
    }
    
    @Test
    public void testClear() {
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertTrue(dataset.contains(instance));
    }
    
    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertTrue(dataset.elementAt(0).equals(instance));
    }
    
    @Test
    public void testSetElementAt() {
        Instance instance = new Instance();
        dataset.add(instance);
        dataset.setElementAt(instance, 0);
        assertTrue(dataset.elementAt(0).equals(instance));
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        dataset.add(instance);
        assertTrue(dataset.instance(0).equals(instance));
    }
    
    @Test
    public void testClasses() {
        assertNotNull(dataset.classes());
    }
    
    @Test
    public void testKNearest() {
        Instance instance = new Instance();
        DistanceMeasure measure = new DistanceMeasure();
        assertNotNull(dataset.kNearest(1, instance, measure));
    }
    
    @Test
    public void testFolds() {
        int numFolds = 5;
        Random rg = new Random();
        Dataset[] folds = dataset.folds(numFolds, rg);
        assertEquals(numFolds, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        assertEquals(-1, dataset.classIndex(clazz));
    }
    
    @Test
    public void testClassValue() {
        assertNull(dataset.classValue(0));
    }
    
    @Test
    public void testCopy() {
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
    }
}