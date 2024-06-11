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
    public void testDefaultConstructor() {
        assertTrue(dataset.size() == 0);
        assertTrue(dataset.classes().isEmpty());
        assertTrue(dataset.maxAttributes == 0);
    }
    
    @Test
    public void testConstructorWithCollection() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3));
        
        assertTrue(dataset.size() == 3);
        assertEquals(3, dataset.maxAttributes);
        assertTrue(dataset.classes().isEmpty());
    }
    
    @Test
    public void testAddAll() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2, instance3)));
        assertTrue(dataset.size() == 3);
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes().isEmpty());
    }
    
    @Test
    public void testAddAllAtIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2, instance3)));
        assertTrue(dataset.addAll(0, Arrays.asList(instance2, instance3)));
        assertTrue(dataset.size() == 5);
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes().isEmpty());
    }
    
    @Test
    public void testClear() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        assertTrue(dataset.addAll(Arrays.asList(instance1, instance2, instance3)));
        dataset.clear();
        
        assertTrue(dataset.size() == 0);
        assertTrue(dataset.classes().isEmpty());
        assertTrue(dataset.maxAttributes == 0);
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        
        assertTrue(dataset.add(instance));
        assertTrue(dataset.size() == 1);
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes().isEmpty());
    }
    
    @Test
    public void testAddAtIndex() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        assertTrue(dataset.add(instance1));
        assertTrue(dataset.add(instance2));
        dataset.add(0, instance3);
        
        assertTrue(dataset.size() == 3);
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes().isEmpty());
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        
        dataset.addElement(instance);
        
        assertTrue(dataset.size() == 1);
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes().isEmpty());
    }
    
    @Test
    public void testInsertElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        dataset.insertElementAt(instance1, 0);
        dataset.insertElementAt(instance2, 1);
        dataset.insertElementAt(instance3, 1);
        
        assertTrue(dataset.size() == 3);
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes().isEmpty());
    }
    
    @Test
    public void testSetElementAt() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        dataset.addElement(instance1);
        dataset.setElementAt(instance2, 0);
        dataset.setElementAt(instance3, 0);
        
        assertTrue(dataset.size() == 1);
        assertEquals(0, dataset.maxAttributes);
        assertTrue(dataset.classes().isEmpty());
    }
    
    @Test
    public void testInstance() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
    }
    
    @Test
    public void testClasses() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        Instance instance3 = new Instance();
        instance3.setClassValue("class1");
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        dataset.addElement(instance3);
        
        assertEquals(2, dataset.classes().size());
        assertTrue(dataset.classes().contains("class1"));
        assertTrue(dataset.classes().contains("class2"));
    }
    
    @Test
    public void testKNearest() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        Instance instance4 = new Instance();
        
        instance1.setClassValue("class1");
        instance2.setClassValue("class2");
        instance3.setClassValue("class1");
        instance4.setClassValue("class2");
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        dataset.addElement(instance3);
        dataset.addElement(instance4);
        
        DistanceMeasure dm = new EuclideanDistance();
        Set<Instance> nearest = dataset.kNearest(2, instance1, dm);
        
        assertEquals(2, nearest.size());
        assertTrue(nearest.contains(instance2));
        assertTrue(nearest.contains(instance4));
    }
    
    @Test
    public void testFolds() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        Instance instance4 = new Instance();
        Instance instance5 = new Instance();
        Instance instance6 = new Instance();
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        dataset.addElement(instance3);
        dataset.addElement(instance4);
        dataset.addElement(instance5);
        dataset.addElement(instance6);
        
        Random rg = new Random();
        Dataset[] folds = dataset.folds(3, rg);
        
        assertEquals(3, folds.length);
        assertTrue(folds[0].size() >= 2);
        assertTrue(folds[1].size() >= 2);
        assertTrue(folds[2].size() >= 2);
    }
    
    @Test
    public void testNoAttributes() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        instance1.addAttribute("attr1", 10);
        instance1.addAttribute("attr2", 20);
        instance2.addAttribute("attr1", 10);
        instance2.addAttribute("attr2", 20);
        instance2.addAttribute("attr3", 30);
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        
        assertTrue(dataset.noAttributes() == 2);
    }
    
    @Test
    public void testClassIndex() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        Instance instance3 = new Instance();
        instance3.setClassValue("class1");
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        dataset.addElement(instance3);
        
        assertEquals(0, dataset.classIndex("class1"));
        assertEquals(1, dataset.classIndex("class2"));
        assertEquals(-1, dataset.classIndex("class3"));
    }
    
    @Test
    public void testClassValue() {
        Instance instance1 = new Instance();
        instance1.setClassValue("class1");
        Instance instance2 = new Instance();
        instance2.setClassValue("class2");
        Instance instance3 = new Instance();
        instance3.setClassValue("class1");
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        dataset.addElement(instance3);
        
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
        assertNull(dataset.classValue(2));
    }
    
    @Test
    public void testCopy() {
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addElement(instance1);
        dataset.addElement(instance2);
        
        Dataset copy = dataset.copy();
        
        assertEquals(dataset.size(), copy.size());
        assertTrue(copy.get(0) != instance1);
        assertTrue(copy.get(1) != instance2);
    }
}