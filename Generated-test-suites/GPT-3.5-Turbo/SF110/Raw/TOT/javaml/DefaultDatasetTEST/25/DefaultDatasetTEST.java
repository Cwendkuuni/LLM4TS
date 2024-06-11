import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.distance.EuclideanDistance;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class DefaultDatasetTest {
    
    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testConstructorWithCollection() {
        List<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.classes().size());
        assertEquals(instance1.noAttributes(), dataset.noAttributes());
    }
    
    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        dataset.addAll(instances);
        assertEquals(2, dataset.size());
        assertEquals(0, dataset.classes().size());
        assertEquals(instance1.noAttributes(), dataset.noAttributes());
    }
    
    @Test
    public void testAddAllAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        dataset.add(instance1);
        dataset.addAll(0, instances);
        assertEquals(3, dataset.size());
        assertEquals(0, dataset.classes().size());
        assertEquals(instance1.noAttributes(), dataset.noAttributes());
    }
    
    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        instances.add(instance1);
        instances.add(instance2);
        
        dataset.addAll(instances);
        dataset.clear();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.classes().size());
        assertEquals(0, dataset.noAttributes());
    }
    
    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        
        dataset.add(instance1);
        assertEquals(1, dataset.size());
        assertEquals(1, dataset.classes().size());
        assertEquals(instance1.noAttributes(), dataset.noAttributes());
    }
    
    @Test
    public void testAddAtIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.add(instance1);
        dataset.add(0, instance2);
        assertEquals(2, dataset.size());
        assertEquals(2, dataset.classes().size());
        assertEquals(instance1.noAttributes(), dataset.noAttributes());
    }
    
    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        
        dataset.addElement(instance1);
        assertEquals(1, dataset.size());
        assertEquals(1, dataset.classes().size());
        assertEquals(instance1.noAttributes(), dataset.noAttributes());
    }
    
    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addElement(instance1);
        dataset.insertElementAt(instance2, 0);
        assertEquals(2, dataset.size());
        assertEquals(2, dataset.classes().size());
        assertEquals(instance1.noAttributes(), dataset.noAttributes());
    }
    
    @Test
    public void testSetlementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        
        dataset.addElement(instance1);
        dataset.insertElementAt(instance2, 0);
        dataset.setElementAt(instance1, 0);
        assertEquals(instance1, dataset.instance(0));
    }
    
    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        
        Set<Instance> nearestInstances = dataset.kNearest(2, instance1, new EuclideanDistance());
        assertEquals(2, nearestInstances.size());
        assertTrue(nearestInstances.contains(instance2));
        assertTrue(nearestInstances.contains(instance3));
    }
    
    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        
        Map<Instance, Double> instancesMap = new HashMap<>();
        instancesMap.put(instance1, 0.5);
        instancesMap.put(instance2, 0.3);
        instancesMap.put(instance3, 0.8);
        
        double removedDistance = dataset.removeFarthest(instancesMap);
        assertEquals(0.8, removedDistance, 0);
        assertEquals(2, instancesMap.size());
        assertFalse(instancesMap.containsKey(instance3));
    }
    
    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance();
        Instance instance2 = new Instance();
        Instance instance3 = new Instance();
        Instance instance4 = new Instance();
        Instance instance5 = new Instance();
        
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        dataset.add(instance4);
        dataset.add(instance5);
        
        Dataset[] folds = dataset.folds(3, new Random());
        assertEquals(3, folds.length);
        
        Set<Instance> instancesInFolds = new HashSet<>();
        for (Dataset fold : folds) {
            instancesInFolds.addAll(fold);
        }
        
        assertTrue(instancesInFolds.containsAll(dataset));
    }
    
    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new Instance();
        dataset.add(instance);
        
        assertEquals(instance.noAttributes(), dataset.noAttributes());
    }
    
    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance("class1");
        Instance instance2 = new Instance("class1");
        Instance instance3 = new Instance("class2");
        
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        
        assertEquals(0, dataset.classIndex("class1"));
        assertEquals(1, dataset.classIndex("class2"));
        assertEquals(-1, dataset.classIndex("class3"));
    }
    
    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance("class1");
        Instance instance2 = new Instance("class2");
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
    }
    
    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new Instance("class1");
        Instance instance2 = new Instance("class2");
        
        dataset.add(instance1);
        dataset.add(instance2);
        
        Dataset copyDataset = dataset.copy();
        
        assertEquals(dataset.size(), copyDataset.size());
        assertEquals(dataset.classes().size(), copyDataset.classes().size());
        assertEquals(dataset.noAttributes(), copyDataset.noAttributes());
        
        for (int i = 0; i < dataset.size(); i++) {
            assertEquals(dataset.instance(i), copyDataset.instance(i));
        }
    }
}