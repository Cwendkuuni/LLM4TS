import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.DistanceMeasure;
import java.util.*;

public class DefaultDatasetTest {

    private DefaultDataset defaultDataset;
    private DistanceMeasure distanceMeasure;
    
    @Before
    public void setUp() {
        defaultDataset = new DefaultDataset();
        distanceMeasure = new EuclideanDistance();
    }
    
    @Test
    public void testAddAll() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance(null));
        instances.add(new Instance(new double[] {1.0, 2.0}));
        assertTrue(defaultDataset.addAll(instances));
        
        assertEquals(2, defaultDataset.size());
        
        instances.clear();
        assertFalse(defaultDataset.addAll(instances));
    }
    
    @Test
    public void testAddAllAtIndex() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new Instance(null));
        instances.add(new Instance(new double[] {1.0, 2.0}));
        assertTrue(defaultDataset.addAll(0, instances));
        
        assertEquals(2, defaultDataset.size());
        
        instances.clear();
        assertFalse(defaultDataset.addAll(1, instances));
    }
    
    @Test
    public void testClear() {
        defaultDataset.add(new Instance(new double[] {1.0, 2.0}));
        defaultDataset.clear();
        
        assertEquals(0, defaultDataset.size());
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance(null);
        assertTrue(defaultDataset.add(instance));
        
        assertEquals(1, defaultDataset.size());
        
        instance = null;
        assertFalse(defaultDataset.add(instance));
    }
    
    @Test
    public void testAddAtIndex() {
        Instance instance = new Instance(null);
        defaultDataset.add(new Instance(new double[] {1.0, 2.0}));
        defaultDataset.add(0, instance);
        
        assertEquals(instance, defaultDataset.get(0));
        
        instance = null;
        try {
            defaultDataset.add(1, instance);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance(null);
        defaultDataset.addElement(instance);
        
        assertEquals(instance, defaultDataset.lastElement());
        
        instance = null;
        try {
            defaultDataset.addElement(instance);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance(null);
        defaultDataset.add(new Instance(new double[] {1.0, 2.0}));
        defaultDataset.insertElementAt(instance, 0);
        
        assertEquals(instance, defaultDataset.get(0));
        
        instance = null;
        try {
            defaultDataset.insertElementAt(instance, 1);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testSetElementAt() {
        Instance instance1 = new Instance(null);
        Instance instance2 = new Instance(new double[] {1.0, 2.0});
        defaultDataset.add(instance2);
        defaultDataset.setElementAt(instance1, 0);
        
        assertEquals(instance1, defaultDataset.get(0));
        
        try {
            defaultDataset.setElementAt(null, 0);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance(null);
        defaultDataset.add(instance);
        
        assertEquals(instance, defaultDataset.instance(0));
        
        try {
            defaultDataset.instance(1);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testClasses() {
        SortedSet<Object> classes = defaultDataset.classes();
        
        assertTrue(classes.isEmpty());
    }
    
    @Test
    public void testKNearest() {
        Instance instance1 = new Instance(null);
        Instance instance2 = new Instance(new double[] {1.0, 2.0});
        defaultDataset.add(instance2);
        defaultDataset.add(new Instance(new double[] {2.0, 3.0}));
        
        Set<Instance> nearest = defaultDataset.kNearest(1, instance1, distanceMeasure);
        
        assertTrue(nearest.contains(instance2));
        
        try {
            defaultDataset.kNearest(0, instance1, distanceMeasure);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        try {
            defaultDataset.kNearest(2, instance1, distanceMeasure);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        try {
            defaultDataset.kNearest(1, null, distanceMeasure);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testFolds() {
        Dataset[] folds = defaultDataset.folds(2, new Random());
        
        assertEquals(2, folds.length);
        
        defaultDataset.add(new Instance(new double[] {1.0, 2.0}));
        defaultDataset.add(new Instance(new double[] {2.0, 3.0}));
        
        folds = defaultDataset.folds(2, new Random());
        
        assertEquals(2, folds.length);
        
        try {
            defaultDataset.folds(0, new Random());
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        try {
            defaultDataset.folds(2, null);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testNoAttributes() {
        defaultDataset.add(new Instance(null));
        
        assertEquals(0, defaultDataset.noAttributes());
        
        defaultDataset.add(new Instance(new double[] {1.0, 2.0}));
        
        assertEquals(2, defaultDataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        defaultDataset.classes().add("A");
        defaultDataset.classes().add("B");
        
        assertEquals(0, defaultDataset.classIndex("A"));
        assertEquals(1, defaultDataset.classIndex("B"));
        
        assertEquals(-1, defaultDataset.classIndex(null));
    }
    
    @Test
    public void testClassValue() {
        defaultDataset.classes.add("A");
        defaultDataset.classes.add("B");
        
        assertEquals("A", defaultDataset.classValue(0));
        assertEquals("B", defaultDataset.classValue(1));
        
        assertNull(defaultDataset.classValue(2));
    }
    
    @Test
    public void testCopy() {
        Instance instance1 = new Instance(null);
        Instance instance2 = new Instance(new double[] {1.0, 2.0});
        defaultDataset.add(instance1);
        defaultDataset.add(instance2);
        
        Dataset copy = defaultDataset.copy();
        
        assertEquals(2, copy.size());
        assertNotSame(defaultDataset, copy);
        assertNotSame(instance1, copy.get(0));
        assertNotSame(instance2, copy.get(1));
    }
}