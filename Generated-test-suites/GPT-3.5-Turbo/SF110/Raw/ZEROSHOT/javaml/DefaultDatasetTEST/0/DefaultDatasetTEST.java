import org.junit.*;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDatasetTest {
    
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> coll = new ArrayList<Instance>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        coll.add(instance1);
        coll.add(instance2);
        assertTrue(dataset.addAll(coll));
    }
    
    @Test
    public void testAddAllIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Collection<Instance> coll = new ArrayList<Instance>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        coll.add(instance1);
        coll.add(instance2);
        assertTrue(dataset.addAll(0, coll));
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.clear();
        assertTrue(dataset.size() == 0);
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        assertTrue(dataset.add(instance));
    }
    
    @Test
    public void testAddIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(0, instance);
        assertTrue(dataset.get(0) == instance);
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.addElement(instance);
        assertTrue(dataset.contains(instance));
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.insertElementAt(instance, 0);
        assertTrue(dataset.get(0) == instance);
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(new Instance());
        dataset.setElementAt(instance, 0);
        assertTrue(dataset.get(0) == instance);
    }
    
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        assertTrue(dataset.instance(0) == instance);
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset.classes());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        DistanceMeasure dm = new EuclideanDistance();
        Set<Instance> nearest = dataset.kNearest(5, instance, dm);
        assertNotNull(nearest);
    }
    
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        double d1 = 0.5;
        double d2 = 0.3;
        vector.put(instance1, d1);
        vector.put(instance2, d2);
        
        double max = dataset.removeFarthest(vector);
        assertTrue(max == d1 || max == d2);
        assertTrue(vector.size() == 1);
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Random rg = new Random();
        Dataset[] folds = dataset.folds(5, rg);
        assertNotNull(folds);
        assertTrue(folds.length == 5);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(dataset.noAttributes(), 0);
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        String clazz = "class";
        dataset.classes().add(clazz);
        int index = dataset.classIndex(clazz);
        assertTrue(index == 0);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        String clazz1 = "class1";
        String clazz2 = "class2";
        dataset.classes().add(clazz1);
        dataset.classes().add(clazz2);
        Object classValue1 = dataset.classValue(0);
        Object classValue2 = dataset.classValue(1);
        assertTrue(classValue1.equals(clazz1));
        assertTrue(classValue2.equals(clazz2));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(new Instance());
        Dataset copiedDataset = dataset.copy();
        assertEquals(dataset.size(), copiedDataset.size());
    }
}