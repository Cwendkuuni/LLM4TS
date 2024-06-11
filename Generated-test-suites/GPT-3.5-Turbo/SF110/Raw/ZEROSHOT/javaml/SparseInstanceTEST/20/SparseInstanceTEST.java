import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SparseInstanceTest {
    
    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testConstructorNoArguments() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }
    
    @Test
    public void testConstructorWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testConstructorWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.value(0), 0.001);
    }
    
    @Test
    public void testConstructorWithNoAttributesAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testConstructorWithNoAttributesAndDefaultValueAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 0.0, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.value(0), 0.001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testConstructorWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(datavector[0], instance.value(0), 0.001);
        assertEquals(datavector[1], instance.value(1), 0.001);
        assertEquals(datavector[2], instance.value(2), 0.001);
    }
    
    @Test
    public void testConstructorWithDataVectorAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        double defaultValue = 0.0;
        SparseInstance instance = new SparseInstance(datavector, defaultValue);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(datavector[0], instance.value(0), 0.001);
        assertEquals(datavector[1], instance.value(1), 0.001);
        assertEquals(datavector[2], instance.value(2), 0.001);
        assertEquals(defaultValue, instance.value(3), 0.001);
    }
    
    @Test
    public void testConstructorWithDataVectorAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(datavector[0], instance.value(0), 0.001);
        assertEquals(datavector[1], instance.value(1), 0.001);
        assertEquals(datavector[2], instance.value(2), 0.001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testConstructorWithDataVectorAndDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        double defaultValue = 0.0;
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, defaultValue, classValue);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(datavector[0], instance.value(0), 0.001);
        assertEquals(datavector[1], instance.value(1), 0.001);
        assertEquals(datavector[2], instance.value(2), 0.001);
        assertEquals(defaultValue, instance.value(3), 0.001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.clear();
        assertEquals(0, instance.size());
    }
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(2));
    }
    
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(3.0));
    }
    
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(2, entrySet.size());
        assertEquals(1.0, entrySet.iterator().next().getValue(), 0.001);
    }
    
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(0.0, instance.get(2), 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
        assertEquals(0, (int)keySet.iterator().next());
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.001);
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(2, instance.size());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Double removedValue = instance.remove(0);
        assertEquals(1, instance.size());
        assertEquals(1.0, removedValue, 0.001);
    }
    
    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.size());
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
    }
    
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Collection<Double> values = instance.values();
        assertEquals(2, values.size());
        assertEquals(1.0, (double)values.iterator().next(), 0.001);
    }
    
    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(1);
        assertEquals(1, instance.size());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(0, (int)instance.keySet().iterator().next());
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        String expected = "{0=1.0;null}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        instance1.setClassValue(new Object());
        SparseInstance instance2 = (SparseInstance) instance1.copy();
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(1);
        instance.removeAttributes(indices);
        assertEquals(0, instance.size());
    }
}