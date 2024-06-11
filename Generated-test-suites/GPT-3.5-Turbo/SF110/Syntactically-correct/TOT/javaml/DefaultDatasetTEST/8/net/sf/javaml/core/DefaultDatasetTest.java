import org.junit.*;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    private Instance instance1;
    private Instance instance2;
    private Instance instance3;
    private Instance instance4;
    private DistanceMeasure dm;
    
    @Before
    public void setUp() {
        dataset = new DefaultDataset();
        instance1 = new Instance(new double[]{1.0, 2.0, 3.0}, "A");
        instance2 = new Instance(new double[]{4.0, 5.0, 6.0}, "B");
        instance3 = new Instance(new double[]{7.0, 8.0, 9.0}, "C");
        instance4 = new Instance(new double[]{10.0, 11.0, 12.0}, "D");
        dm = new EuclideanDistance();
    }
    
    @Test
    public void testDefaultDataset_WithCollection() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        assertEquals(3, dataset.size());
        assertEquals(3, dataset.noAttributes());
        assertEquals(3, dataset.classes().size());
        assertEquals(true, dataset.classes().contains("A"));
        assertEquals(true, dataset.classes().contains("B"));
        assertEquals(true, dataset.classes().contains("C"));
    }
    
    @Test
    public void testDefaultDataset_WithoutCollection() {
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testAddAll_Collection() {
        dataset = new DefaultDataset();
        assertEquals(true, dataset.addAll(Arrays.asList(instance1, instance2, instance3)));
        assertEquals(3, dataset.size());
        assertEquals(3, dataset.noAttributes());
        assertEquals(3, dataset.classes().size());
        assertEquals(true, dataset.classes().contains("A"));
        assertEquals(true, dataset.classes().contains("B"));
        assertEquals(true, dataset.classes().contains("C"));
    }
    
    @Test
    public void testAddAll_Index_Collection() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        assertEquals(true, dataset.addAll(1, Arrays.asList(instance4)));
        assertEquals(4, dataset.size());
        assertEquals(3, dataset.noAttributes());
        assertEquals(4, dataset.classes().size());
        assertEquals(true, dataset.classes().contains("A"));
        assertEquals(true, dataset.classes().contains("D"));
    }
    
    @Test
    public void testClear() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        dataset.clear();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertEquals(0, dataset.classes().size());
        assertEquals(false, dataset.classes().contains("A"));
        assertEquals(false, dataset.classes().contains("B"));
        assertEquals(false, dataset.classes().contains("C"));
    }
    
    @Test
    public void testAdd() {
        dataset = new DefaultDataset();
        assertEquals(true, dataset.add(instance1));
        assertEquals(1, dataset.size());
        assertEquals(3, dataset.noAttributes());
        assertEquals(1, dataset.classes().size());
        assertEquals(true, dataset.classes().contains("A"));
    }
    
    @Test
    public void testAdd_Index() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
        dataset.add(1, instance4);
        assertEquals(3, dataset.size());
        assertEquals(3, dataset.noAttributes());
        assertEquals(2, dataset.classes().size());
        assertEquals(true, dataset.classes().contains("A"));
        assertEquals(true, dataset.classes().contains("D"));
    }
    
    @Test
    public void testAddElement() {
        dataset = new DefaultDataset();
        dataset.addElement(instance1);
        assertEquals(1, dataset.size());
        assertEquals(3, dataset.noAttributes());
        assertEquals(1, dataset.classes().size());
        assertEquals(true, dataset.classes().contains("A"));
    }
    
    @Test
    public void testInsertElementAt() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
        dataset.insertElementAt(instance4, 1);
        assertEquals(3, dataset.size());
        assertEquals(3, dataset.noAttributes());
        assertEquals(2, dataset.classes().size());
        assertEquals(true, dataset.classes().contains("A"));
        assertEquals(true, dataset.classes().contains("D"));
    }
    
    @Test
    public void testSetElementAt() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        dataset.setElementAt(instance4, 1);
        assertEquals(3, dataset.size());
        assertEquals(3, dataset.noAttributes());
        assertEquals(3, dataset.classes().size());
        assertEquals(true, dataset.classes().contains("A"));
        assertEquals(true, dataset.classes().contains("D"));
        assertEquals(false, dataset.classes().contains("B"));
    }
    
    @Test
    public void testInstance() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
        assertEquals(instance3, dataset.instance(2));
    }
    
    @Test
    public void testClasses() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        assertEquals(true, dataset.classes().contains("A"));
        assertEquals(true, dataset.classes().contains("B"));
        assertEquals(true, dataset.classes().contains("C"));
    }
    
    @Test
    public void testKNearest() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        assertEquals(2, dataset.kNearest(2, instance4, dm).size());
        assertEquals(true, dataset.kNearest(2, instance4, dm).contains(instance1));
        assertEquals(true, dataset.kNearest(2, instance4, dm).contains(instance2));
    }
    
    @Test
    public void testFold() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3, instance4));
        Dataset[] folds = dataset.folds(2, new Random());
        assertEquals(2, folds.length);
        assertEquals(2, folds[0].size());
        assertEquals(2, folds[1].size());
        assertEquals(true, folds[0].classes().contains("A"));
        assertEquals(true, folds[0].classes().contains("D"));
        assertEquals(true, folds[1].classes().contains("B"));
        assertEquals(true, folds[1].classes().contains("C"));
    }
    
    @Test
    public void testNoAttributes() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        assertEquals(3, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        assertEquals(0, dataset.classIndex("A"));
        assertEquals(1, dataset.classIndex("B"));
        assertEquals(2, dataset.classIndex("C"));
        assertEquals(-1, dataset.classIndex("D"));
    }
    
    @Test
    public void testClassValue() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        assertEquals("A", dataset.classValue(0));
        assertEquals("B", dataset.classValue(1));
        assertEquals("C", dataset.classValue(2));
        assertNull(dataset.classValue(3));
    }
    
    @Test
    public void testCopy() {
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        Dataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.noAttributes(), copy.noAttributes());
        assertEquals(dataset.classes().size(), copy.classes().size());
        assertEquals(true, copy.classes().contains("A"));
        assertEquals(true, copy.classes().contains("B"));
        assertEquals(true, copy.classes().contains("C"));
        assertNotSame(dataset, copy);
        assertNotSame(dataset.instance(0), copy.instance(0));
    }
}