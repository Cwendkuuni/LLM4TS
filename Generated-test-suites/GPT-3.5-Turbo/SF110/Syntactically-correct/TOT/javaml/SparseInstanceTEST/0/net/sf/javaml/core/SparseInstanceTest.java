import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {
    
    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstance() {
        SparseInstance instance1 = new SparseInstance(3);
        SparseInstance instance2 = new SparseInstance();
        
        assertEquals(-1, instance1.noAttributes());
        assertEquals(-1, instance2.noAttributes());
    }
    
    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance1 = new SparseInstance(3, 0.5);
        SparseInstance instance2 = new SparseInstance(3, 0.5, "classA");
        SparseInstance instance3 = new SparseInstance(3, "classA");
        
        assertEquals(0.5, instance1.get(0), 0.0001);
        assertEquals(0.5, instance2.defaultValue, 0.0001);
        assertEquals("classA", instance2.classValue());
        assertEquals(0.5, instance3.defaultValue, 0.0001);
        assertEquals("classA", instance3.classValue());
    }
    
    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {1.2, 0.8, 0.0};
        SparseInstance instance = new SparseInstance(datavector);
        
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
        assertEquals(1.2, instance.get(0), 0.0001);
        assertEquals(0.8, instance.get(1), 0.0001);
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        
        assertEquals(0.5, instance.value(0), 0.0001);
    }
    
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        
        assertFalse(instance.isEmpty());
        
        instance.clear();
        
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }
    
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        
        assertTrue(instance.containsValue(0.5));
        assertFalse(instance.containsValue(0.8));
    }
    
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        
        assertEquals(1, entrySet.size());
        assertEquals(0, entrySet.iterator().next().getKey().intValue());
        assertEquals(0.5, entrySet.iterator().next().getValue(), 0.0001);
    }
    
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        
        assertEquals(0.5, instance.get(0), 0.0001);
        assertEquals(0.0, instance.get(1), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 0.5);
        
        assertTrue(instance1.isEmpty());
        assertFalse(instance2.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        instance.put(2, 0.8);
        
        TreeSet<Integer> keySet = instance.keySet();
        
        assertEquals(2, keySet.size());
        assertEquals(0, keySet.first().intValue());
        assertEquals(2, keySet.last().intValue());
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        instance.put(0, 0.8);
        
        assertEquals(0.8, instance.get(0), 0.0001);
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 0.5);
        
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 0.8);
        map.put(2, 0.6);
        instance1.putAll(map);
        
        assertEquals(0.5, instance1.get(0), 0.0001);
        assertEquals(0.8, instance1.get(1), 0.0001);
        assertEquals(0.6, instance1.get(2), 0.0001);
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        instance.put(1, 0.8);
        
        assertEquals(0.8, instance.remove(1), 0.0001);
        assertNull(instance.get(1));
    }
    
    @Test
    public void testSize() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        
        assertEquals(0, instance1.size());
        assertEquals(0, instance2.size());
        
        instance2.put(0, 0.5);
        
        assertEquals(1, instance2.size());
    }
    
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        instance.put(1, 0.8);
        
        Collection<Double> values = instance.values();
        
        assertEquals(2, values.size());
        assertTrue(values.contains(0.5));
        assertTrue(values.contains(0.8));
    }
    
    @Test
    public void testNoAttributes() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 0.5);
        
        assertEquals(0, instance1.noAttributes());
        assertEquals(1, instance2.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        instance.put(1, 0.8);
        instance.removeAttribute(0);
        
        assertFalse(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertEquals(1, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        instance.put(1, 0.8);
        instance.setClassValue("classA");
        
        assertEquals("{0=0.5;classA}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        
        assertEquals(instance1.hashCode(), instance2.hashCode());
        
        instance1.put(0, 0.5);
        
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        
        assertTrue(instance1.equals(instance1));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
        assertTrue(instance1.equals(instance2));
        
        instance1.put(0, 0.5);
        
        assertFalse(instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 0.5);
        instance1.setClassValue("classA");
        
        SparseInstance instance2 = (SparseInstance) instance1.copy();
        
        assertEquals(instance1.get(0), instance2.get(0), 0.0001);
        assertEquals(instance1.classValue(), instance2.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.5);
        instance.put(1, 0.8);
        instance.put(2, 0.6);
        
        HashSet<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(2);
        
        instance.removeAttributes(indices);
        
        assertFalse(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
        assertEquals(1, instance.noAttributes());
    }
}