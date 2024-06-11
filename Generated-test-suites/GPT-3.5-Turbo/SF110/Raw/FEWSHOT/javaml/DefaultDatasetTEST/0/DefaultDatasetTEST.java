import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.ManhattanDistance;
import net.sf.javaml.distance.EuclideanDistance;

import org.junit.Test;

public class DefaultDatasetTest {
    
    @Test
    public void testConstructorWithCollection() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        
        DefaultDataset dataset = new DefaultDataset(coll);
        
        assertNotNull(dataset);
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testConstructorWithoutCollection() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(coll);
        
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testAddAllAtIndex() {
        Collection<Instance> coll = new ArrayList<Instance>();
        coll.add(new Instance());
        coll.add(new Instance());
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.addAll(1, coll);
        
        assertEquals(4, dataset.size());
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        dataset.clear();
        
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        dataset.add(1, new Instance());
        
        assertEquals(3, dataset.size());
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(new Instance());
        
        assertEquals(1, dataset.size());
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.insertElementAt(new Instance(), 1);
        
        assertEquals(2, dataset.size());
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.setElementAt(new Instance(), 0);
        
        assertNotNull(dataset.getInstance(0));
    }
    
    @Test
    public void testInstance() {
        Instance instance = new Instance();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance);
        
        assertEquals(instance, dataset.instance(0));
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        assertNotNull(dataset.classes());
    }
    
    @Test
    public void testKNearest() {
        Instance inst = new Instance();
        DistanceMeasure dm = new ManhattanDistance();
        
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        Set<Instance> nearest = dataset.kNearest(1, inst, dm);
        
        assertNotNull(nearest);
        assertEquals(1, nearest.size());
    }
    
    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        vector.put(new Instance(), 1.0);
        vector.put(new Instance(), 0.5);
        
        DefaultDataset dataset = new DefaultDataset();
        double max = dataset.removeFarthest(vector);
        
        assertTrue(max < 1.0);
        assertEquals(1, vector.size());
    }
    
    @Test
    public void testFolds() {
        Random rg = new Random();
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        Dataset[] folds = dataset.folds(2, rg);
        
        assertNotNull(folds);
        assertEquals(2, folds.length);
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        
        assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addClassValue("class1");
        dataset.addClassValue("class2");
        
        int index = dataset.classIndex("class1");
        
        assertEquals(0, index);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addClassValue("class1");
        dataset.addClassValue("class2");
        
        Object classValue = dataset.classValue(1);
        
        assertEquals("class2", classValue);
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        Dataset copy = dataset.copy();
        
        assertNotNull(copy);
        assertEquals(dataset.size(), copy.size());
    }
}