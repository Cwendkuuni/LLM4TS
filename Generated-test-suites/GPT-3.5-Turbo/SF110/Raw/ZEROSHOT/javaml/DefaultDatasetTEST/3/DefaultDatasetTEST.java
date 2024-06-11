import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import org.junit.*;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        
        boolean result = dataset.addAll(Arrays.asList(i1, i2));
        
        Assert.assertTrue(result);
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(i1, dataset.get(0));
        Assert.assertEquals(i2, dataset.get(1));
    }
    
    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        Instance i3 = new Instance();
        
        dataset.add(i1);
        dataset.add(i2);
        
        boolean result = dataset.addAll(1, Arrays.asList(i3));
        
        Assert.assertTrue(result);
        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals(i1, dataset.get(0));
        Assert.assertEquals(i3, dataset.get(1));
        Assert.assertEquals(i2, dataset.get(2));
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        
        dataset.add(i1);
        dataset.add(i2);
        
        dataset.clear();
        
        Assert.assertEquals(0, dataset.size());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        
        boolean result = dataset.add(i1);
        
        Assert.assertTrue(result);
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals(i1, dataset.get(0));
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        Instance i3 = new Instance();
        
        dataset.add(i1);
        dataset.add(i3);
        
        dataset.add(1, i2);
        
        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals(i1, dataset.get(0));
        Assert.assertEquals(i2, dataset.get(1));
        Assert.assertEquals(i3, dataset.get(2));
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        
        dataset.addElement(i1);
        
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals(i1, dataset.get(0));
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        Instance i3 = new Instance();
        
        dataset.add(i1);
        dataset.add(i3);
        
        dataset.insertElementAt(i2, 1);
        
        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals(i1, dataset.get(0));
        Assert.assertEquals(i2, dataset.get(1));
        Assert.assertEquals(i3, dataset.get(2));
    }
    
    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        
        dataset.add(i1);
        
        dataset.setElementAt(i2, 0);
        
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals(i2, dataset.get(0));
    }
    
    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        
        dataset.add(i1);
        dataset.add(i2);
        
        Instance result = dataset.instance(1);
        
        Assert.assertEquals(i2, result);
    }
    
    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new Instance());
        dataset.add(new Instance());
        
        SortedSet<Object> classes = dataset.classes();
        
        Assert.assertEquals(0, classes.size());
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        Instance i3 = new Instance();
        
        dataset.add(i1);
        dataset.add(i2);
        dataset.add(i3);
        
        Set<Instance> result = dataset.kNearest(2, i1, new EuclideanDistance());
        
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(i2));
        Assert.assertTrue(result.contains(i3));
    }
    
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        Instance i3 = new Instance();
        
        dataset.add(i2);
        dataset.add(i1);
        dataset.add(i3);
        
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        vector.put(i1, 1.0);
        vector.put(i2, 2.0);
        vector.put(i3, 3.0);
        
        double result = dataset.removeFarthest(vector);
        
        Assert.assertEquals(3.0, result, 0.001);
        Assert.assertEquals(2, vector.size());
        Assert.assertTrue(vector.containsKey(i1));
        Assert.assertTrue(vector.containsKey(i2));
        Assert.assertFalse(vector.containsKey(i3));
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        Instance i3 = new Instance();
        Instance i4 = new Instance();
        Instance i5 = new Instance();
        
        dataset.addAll(Arrays.asList(i1, i2, i3, i4, i5));
        
        Dataset[] result = dataset.folds(2, new Random());
        
        Assert.assertEquals(2, result.length);
        Assert.assertEquals(3, result[0].size());
        Assert.assertEquals(2, result[1].size());
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        i1.setNoAttributes(5);
        dataset.add(i1);
        
        int result = dataset.noAttributes();
        
        Assert.assertEquals(5, result);
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes().add("Class1");
        dataset.classes().add("Class2");
        
        int result = dataset.classIndex("Class2");
        
        Assert.assertEquals(1, result);
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes().add("Class1");
        dataset.classes().add("Class2");
        
        Object result = dataset.classValue(1);
        
        Assert.assertEquals("Class2", result);
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance i1 = new Instance();
        Instance i2 = new Instance();
        
        dataset.add(i1);
        dataset.add(i2);
        
        Dataset result = dataset.copy();
        
        Assert.assertEquals(2, result.size());
        Assert.assertNotSame(dataset, result);
        Assert.assertEquals(i1, result.get(0));
        Assert.assertEquals(i2, result.get(1));
    }

}