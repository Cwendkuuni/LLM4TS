import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

import org.junit.Test;

public class DefaultDatasetTest {
    
    // Test case for constructor with collection parameter
    @Test
    public void testConstructorWithCollection() {
        Collection<Instance> instances = new ArrayList<>();
        instances.add(mock(Instance.class));
        DefaultDataset dataset = new DefaultDataset(instances);
        
        assertNotNull(dataset);
        assertEquals(1, dataset.size());
    }
    
    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    // Test case for addAll method
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = mock(Instance.class);
        
        assertFalse(dataset.addAll(Collections.singletonList(instance)));
        assertEquals(1, dataset.size());
    }
    
    // Test case for kNearest method
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = mock(Instance.class);
        Instance instance2 = mock(Instance.class);
        dataset.add(instance1);
        dataset.add(instance2);
        DistanceMeasure dm = mock(DistanceMeasure.class);
        when(dm.measure(any(), any())).thenReturn(1.0);
        
        Set<Instance> nearest = dataset.kNearest(1, instance1, dm);
        
        assertEquals(1, nearest.size());
    }
    
    // Test case for folds method
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Collections.singletonList(mock(Instance.class)));
        Random random = mock(Random.class);
        
        Dataset[] folds = dataset.folds(2, random);
        
        assertEquals(2, folds.length);
    }
    
    // Test case for noAttributes method
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = mock(Instance.class);
        when(instance.noAttributes()).thenReturn(5);
        dataset.add(instance);
        
        assertEquals(5, dataset.noAttributes());
    }
    
    // Test case for classIndex method
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes.add("ClassA");
        dataset.classes.add("ClassB");
        
        assertEquals(1, dataset.classIndex("ClassB"));
    }
    
    // Test case for classValue method
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.classes.add("ClassA");
        dataset.classes.add("ClassB");
        
        assertEquals("ClassB", dataset.classValue(1));
    }
    
    // Test case for copy method
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = mock(Instance.class);
        when(instance.copy()).thenReturn(instance);
        dataset.add(instance);
        
        Dataset copy = dataset.copy();
        assertNotSame(dataset, copy);
        assertEquals(1, copy.size());
    }
}