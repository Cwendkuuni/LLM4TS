import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(null, instance.classValue());
        assertTrue(instance.data.isEmpty());
    }
    
    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(null, instance.classValue());
        assertTrue(instance.data.isEmpty());
    }
    
    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertEquals(null, instance.classValue());
        assertTrue(instance.data.isEmpty());
    }
    
    @Test
    public void testSparseInstanceWithClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
        assertTrue(instance.data.isEmpty());
    }
    
    @Test
    public void testSparseInstanceWithDefaultValueAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 1.0, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
        assertTrue(instance.data.isEmpty());
    }
    
    @Test
    public void testSparseInstanceWithDatavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(null, instance.classValue());
        assertEquals(3, instance.data.size());
    }
    
    @Test
    public void testSparseInstanceWithDatavectorAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.5);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.5, instance.defaultValue, 0.001);
        assertEquals(null, instance.classValue());
        assertEquals(3, instance.data.size());
    }
    
    @Test
    public void testSparseInstanceWithDatavectorAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
        assertEquals(3, instance.data.size());
    }
    
    @Test
    public void testSparseInstanceWithDatavectorAndDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.5, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.5, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
        assertEquals(3, instance.data.size());
    }
    
    @Test
    public void testInitiateWithNonDefaultValue() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 0.0, 3.0};
        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(null, instance.classValue());
        assertEquals(2, instance.data.size());
    }
    
    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        assertEquals(2.5, instance.value(1), 0.001);
    }
    
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        instance.clear();
        assertEquals(true, instance.data.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        assertEquals(true, instance.containsKey(1));
        assertEquals(false, instance.containsKey(2));
    }
    
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        assertEquals(true, instance.containsValue(2.5));
        assertEquals(false, instance.containsValue(3.0));
    }
    
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(1, entrySet.size());
        Map.Entry<Integer, Double> entry = entrySet.iterator().next();
        assertEquals(1, (int) entry.getKey());
        assertEquals(2.5, entry.getValue(), 0.001);
    }
    
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        assertEquals((Double) 2.5, instance.get(1));
        assertEquals((Double) 0.0, instance.get(2));
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertEquals(true, instance.isEmpty());
        instance.put(1, 2.5);
        assertEquals(false, instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(1, keySet.size());
        assertEquals(1, (int) keySet.first());
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        Double oldValue = instance.put(1, 2.5);
        assertEquals(null, oldValue);
        assertEquals((Double) 2.5, instance.get(1));
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 2.5);
        map.put(2, 3.0);
        instance.putAll(map);
        assertEquals((Double) 2.5, instance.get(1));
        assertEquals((Double) 3.0, instance.get(2));
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        Double removedValue = instance.remove(1);
        assertEquals((Double) 2.5, removedValue);
        assertEquals(true, instance.data.isEmpty());
    }
    
    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.size());
        instance.put(1, 2.5);
        assertEquals(1, instance.size());
    }
    
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        Collection<Double> values = instance.values();
        assertEquals(1, values.size());
        assertEquals((Double) 2.5, values.iterator().next());
    }
    
    @Test
    public void testNoAttributesWithNoAttributesSet() {
        SparseInstance instance = new SparseInstance();
        instance.removeAttribute(1);
        assertEquals(0, instance.noAttributes());
    }
    
    @Test
    public void testNoAttributesWithEmptyData() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
    }
    
    @Test
    public void testNoAttributesWithData() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        assertEquals(2, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertEquals((Double) 3.0, instance.get(1));
        assertEquals(1, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        instance.setClassValue(3.0);
        assertEquals("{1=2.5;3.0}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 2.5);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 2.5);
        SparseInstance instance3 = new SparseInstance();
        instance3.put(1, 3.0);
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 2.5);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 2.5);
        SparseInstance instance3 = new SparseInstance();
        instance3.put(1, 3.0);
        assertEquals(true, instance1.equals(instance2));
        assertEquals(false, instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 2.5);
        instance1.setClassValue(3.0);
        SparseInstance instance2 = instance1.copy();
        assertEquals(instance1.toString(), instance2.toString());
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.5);
        instance.put(2, 3.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        assertEquals(true, instance.containsKey(1));
        assertEquals(false, instance.containsKey(2));
        assertEquals(1, instance.noAttributes());
    }
}