import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.util.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    private Collection<Instance> instances;
    private Instance instance1, instance2, instance3;
    
    @Before
    public void setUp(){
        instances = new ArrayList<>();
        instance1 = Mockito.mock(Instance.class);
        instance2 = Mockito.mock(Instance.class);
        instance3 = Mockito.mock(Instance.class);
        instances.add(instance1);
        instances.add(instance2);
        instances.add(instance3);
        dataset = new DefaultDataset(instances);
    }
    
    @Test
    public void testDefaultConstructor() {
        DefaultDataset ds = new DefaultDataset();
        Assert.assertEquals(0, ds.size());
        Assert.assertEquals(0, ds.noAttributes());
        Assert.assertEquals(0, ds.classes().size());
    }
    
    @Test
    public void testConstructorWithCollection() {
        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testConstructorWithCollection_NullClassValue() {
        when(instance1.classValue()).thenReturn(null);
        when(instance2.classValue()).thenReturn(null);
        when(instance3.classValue()).thenReturn(null);
        dataset = new DefaultDataset(instances);
        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testAddAll() {
        Collection<Instance> newInstances = new ArrayList<>();
        newInstances.add(Mockito.mock(Instance.class));
        newInstances.add(Mockito.mock(Instance.class));
        dataset.addAll(newInstances);
        Assert.assertEquals(5, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testAddAll_withIndex() {
        int index = 1;
        Collection<Instance> newInstances = new ArrayList<>();
        newInstances.add(Mockito.mock(Instance.class));
        newInstances.add(Mockito.mock(Instance.class));
        dataset.addAll(index, newInstances);
        Assert.assertEquals(5, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
        Assert.assertEquals(instance2, dataset.get(index));
    }
    
    @Test
    public void testAdd_elementWithClassValue() {
        when(instance1.classValue()).thenReturn("class1");
        dataset.add(instance1);
        Assert.assertEquals(4, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(1, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("class1"));
    }
    
    @Test
    public void testAdd_elementWithNoAttributes() {
        when(instance1.noAttributes()).thenReturn(5);
        dataset.add(instance1);
        Assert.assertEquals(4, dataset.size());
        Assert.assertEquals(5, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testClear() {
        dataset.clear();
        Assert.assertEquals(0, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testAddElement_elementWithClassValue() {
        when(instance1.classValue()).thenReturn("class1");
        dataset.addElement(instance1);
        Assert.assertEquals(4, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(1, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("class1"));
    }
    
    @Test
    public void testAddElement_elementWithNoAttributes() {
        when(instance1.noAttributes()).thenReturn(5);
        dataset.addElement(instance1);
        Assert.assertEquals(4, dataset.size());
        Assert.assertEquals(5, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testInsertElementAt_elementWithClassValue() {
        when(instance1.classValue()).thenReturn("class1");
        int index = 1;
        dataset.insertElementAt(instance1, index);
        Assert.assertEquals(4, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(1, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("class1"));
        Assert.assertEquals(instance1, dataset.get(index));
    }
    
    @Test
    public void testInsertElementAt_elementWithNoAttributes() {
        when(instance1.noAttributes()).thenReturn(5);
        int index = 1;
        dataset.insertElementAt(instance1, index);
        Assert.assertEquals(4, dataset.size());
        Assert.assertEquals(5, dataset.noAttributes());
        Assert.assertEquals(0, dataset.classes().size());
        Assert.assertEquals(instance1, dataset.get(index));
    }
    
    @Test
    public void testSetElementAt() {
        when(instance1.classValue()).thenReturn("class1");
        when(instance2.classValue()).thenReturn("class2");
        when(instance3.classValue()).thenReturn("class3");
        int index = 1;
        dataset.setElementAt(instance1, index);
        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals(0, dataset.noAttributes());
        Assert.assertEquals(3, dataset.classes().size());
        Assert.assertTrue(dataset.classes().contains("class1"));
        Assert.assertEquals(instance1, dataset.get(index));
    }
    
    @Test
    public void testInstance() {
        int index = 1;
        Instance result = dataset.instance(index);
        Assert.assertEquals(instance2, result);
    }
    
    @Test
    public void testClasses() {
        Assert.assertEquals(0, dataset.classes().size());
    }
    
    @Test
    public void testKNearest() {
        Instance instance4 = Mockito.mock(Instance.class);
        DistanceMeasure distanceMeasure = Mockito.mock(DistanceMeasure.class);
        Map<Instance, Double> closest = new HashMap<>();
        closest.put(instance2, 2.0);
        closest.put(instance3, 1.0);
        when(distanceMeasure.compare(2.0, Double.POSITIVE_INFINITY)).thenReturn(true);
        when(distanceMeasure.measure(instance1, instance2)).thenReturn(2.0);
        when(distanceMeasure.measure(instance1, instance3)).thenReturn(1.0);
        when(instance1.equals(instance2)).thenReturn(false);
        when(instance1.equals(instance3)).thenReturn(false);
        when(instance2.equals(instance3)).thenReturn(false);
        when(dataset.removeFarthest(closest)).thenReturn(2.0);
        when(dataset.kNearest(2, instance1, distanceMeasure)).thenReturn(closest.keySet());
        
        Set<Instance> result = dataset.kNearest(2, instance1, distanceMeasure);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(instance2));
        Assert.assertTrue(result.contains(instance3));
    }
    
    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<>();
        vector.put(instance2, 2.0);
        vector.put(instance3, 1.0);
        
        double result = dataset.removeFarthest(vector);
        Assert.assertEquals(2.0, result, 0.001);
        Assert.assertNull(vector.get(instance2));
        Assert.assertEquals(1.0, vector.get(instance3), 0.001);
    }
    
    @Test
    public void testFolds() {
        int numFolds = 2;
        Random rg = new Random();
        int size1 = dataset.size() / numFolds + 1;
        int size2 = dataset.size() / numFolds;
        int[] indi1 = new int[size1];
        int[] indi2 = new int[size2];
        
        for (int i=0; i<size1; i++){
            indi1[i] = i;
        }
        
        for (int i=0; i<size2; i++){
            indi2[i] = i;
        }
        
        Dataset[] out = dataset.folds(numFolds, rg);
        Assert.assertEquals(numFolds, out.length);
        Assert.assertEquals(size1, out[0].size());
        Assert.assertEquals(size2, out[1].size());
        Assert.assertArrayEquals(indi1, ((Fold)out[0]).getIndices());
        Assert.assertArrayEquals(indi2, ((Fold)out[1]).getIndices());
    }
    
    @Test
    public void testNoAttributes_EmptyDataset() {
        dataset.clear();
        Assert.assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testNoAttributes() {
        int expected = Mockito.mock(Instance.class).noAttributes();
        Assert.assertEquals(expected, dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        String clazz = "class3";
        dataset.classes().addAll(Arrays.asList("class1", "class2", "class3"));
        Assert.assertEquals(2, dataset.classIndex(clazz));
    }
    
    @Test
    public void testClassIndex_NullClass() {
        Assert.assertEquals(-1, dataset.classIndex(null));
    }
    
    @Test
    public void testClassValue() {
        Object clazz = "class2";
        dataset.classes().addAll(Arrays.asList("class1", "class2", "class3"));
        Assert.assertEquals(clazz, dataset.classValue(1));
    }
    
    @Test
    public void testClassValue_OutOfBounds() {
        Assert.assertNull(dataset.classValue(10));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset copy = (DefaultDataset) dataset.copy();
        Assert.assertEquals(dataset.size(), copy.size());
        for (int i=0; i<dataset.size(); i++){
            Assert.assertNotSame(dataset.instance(i), copy.instance(i));
            Assert.assertEquals(dataset.instance(i).toString(), copy.instance(i).toString());
        }
    }
}