import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import net.sf.javaml.distance.DistanceMeasure;

public class DefaultDatasetTest {
    
    @Test
    public void testDefaultDatasetCollection() {
        Collection<Instance> coll = new ArrayList<Instance>();
        DefaultDataset dataset = new DefaultDataset(coll);
        assertNotNull(dataset);
    }
    
    @Test
    public void testDefaultDataset() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset);
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        boolean result = dataset.addAll(coll);
        assertTrue(result);
        assertEquals(coll.size(), dataset.size());
    }
    
    @Test
    public void testAddAllIndex() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset();
        boolean result = dataset.addAll(0, coll);
        assertTrue(result);
        assertEquals(coll.size(), dataset.size());
        assertEquals(coll.iterator().next(), dataset.get(0));
    }
    
    @Test
    public void testClear() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        DefaultDataset dataset = new DefaultDataset(coll);
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }
    
    @Test
    public void testAdd() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        boolean result = dataset.add(instance);
        assertTrue(result);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testAddIndex() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(0, instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testAddElement() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(instance);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testInsertElementAt() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.insertElementAt(instance, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testSetElementAt() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.setElementAt(instance, 0);
        assertEquals(2, dataset.size());
        assertEquals(instance, dataset.get(0));
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        Instance result = dataset.instance(0);
        assertEquals(instance, result);
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset.classes());
    }
    
    @Test
    public void testKNearest() {
        Instance inst = new Instance();
        DistanceMeasure dm = new DistanceMeasure();
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset.kNearest(1, inst, dm));
    }
    
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        double result = dataset.removeFarthest(vector);
        assertEquals(0.0, result, 0.001);
    }
    
    @Test
    public void testFolds() {
        Random rg = new Random();
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset.folds(2, rg));
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        int result = dataset.noAttributes();
        assertEquals(0, result);
    }
    
    @Test
    public void testClassIndex() {
        Object clazz = new Object();
        DefaultDataset dataset = new DefaultDataset();
        int result = dataset.classIndex(clazz);
        assertEquals(-1, result);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Object result = dataset.classValue(0);
        assertNull(result);
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Dataset result = dataset.copy();
        assertNotNull(result);
    }
}