import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class SparseInstanceTest {
    private SparseInstance instance; 
    
    public SparseInstanceTest() {
        instance = new SparseInstance();
    }
    
    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstance1() {
        SparseInstance instance1 = new SparseInstance();
        assertEquals(-1, instance1.noAttributes());
    }
    
    @Test
    public void testSparseInstance2() {
        SparseInstance instance2 = new SparseInstance(5);
        assertEquals(5, instance2.noAttributes());
        assertEquals(5, instance2.size());
    }
    
    @Test
    public void testSparseInstance3() {
        SparseInstance instance3 = new SparseInstance(5, 9.5);
        assertEquals(5, instance3.noAttributes());
        assertEquals(5, instance3.size());
        assertEquals(9.5, instance3.defaultValue, 0);
    }
    
    @Test
    public void testSparseInstance4() {
        Object classValue = "A";
        SparseInstance instance4 = new SparseInstance(5, classValue);
        assertEquals(5, instance4.noAttributes());
        assertEquals(5, instance4.size());
        assertEquals(0.0, instance4.defaultValue, 0);
        assertEquals("A", instance4.classValue());
    }
    
    @Test
    public void testSparseInstance5() {
        Object classValue = "A";
        SparseInstance instance5 = new SparseInstance(5, 9.5, classValue);
        assertEquals(5, instance5.noAttributes());
        assertEquals(5, instance5.size());
        assertEquals(9.5, instance5.defaultValue, 0);
        assertEquals("A", instance5.classValue());
    }
    
    @Test
    public void testValue() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        assertEquals(2.5, instance.value(1), 0);
        assertEquals(3.5, instance.value(2), 0);
    }
    
    @Test
    public void testClear() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        assertTrue(instance.containsValue(2.5));
        assertFalse(instance.containsValue(4.5));
    }
    
    @Test
    public void testEntrySet() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(2, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertTrue(entry.getKey() instanceof Integer);
            assertTrue(entry.getValue() instanceof Double);
        }
    }
    
    @Test
    public void testGet() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        assertEquals(2.5, instance.get(1), 0);
        assertEquals(instance.defaultValue, instance.get(3), 0);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        instance.put(1, 2.5);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }
    
    @Test
    public void testPut() {
        Double previousValue = instance.put(1, 2.5);
        assertNull(previousValue);
        previousValue = instance.put(1, 3.5);
        assertEquals(2.5, previousValue, 0);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 2.5);
        map.put(2, 3.5);
        instance.putAll(map);
        assertEquals(2.5, instance.get(1), 0);
        assertEquals(3.5, instance.get(2), 0);
    }
    
    @Test
    public void testRemove() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        Double removedValue = instance.remove(1);
        assertEquals(2.5, removedValue, 0);
        assertFalse(instance.containsKey(1));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, instance.size());
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        assertEquals(2, instance.size());
    }
    
    @Test
    public void testValues() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        Collection<Double> values = instance.values();
        assertEquals(2, values.size());
        for (Double value : values) {
            assertTrue(value instanceof Double);
        }
    }
    
    @Test
    public void testNoAttributes1() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testNoAttributes2() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.5);
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        instance.removeAttribute(1);
        assertEquals(3, instance.size());
        assertFalse(instance.containsKey(1));
        assertEquals(2.5, instance.get(2), 0);
    }
    
    @Test
    public void testToString() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        assertEquals("{1=2.5;null}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        int hashCode = instance.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance2 = new SparseInstance();
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(instance2));
        
        instance2.put(1, 2.5);
        instance.put(1, 2.5);
        assertTrue(instance.equals(instance2));
        
        instance2.put(1, 3.5);
        assertFalse(instance.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        SparseInstance copy = (SparseInstance) instance.copy();
        
        assertTrue(instance.equals(copy));
        assertNotSame(instance, copy);
        assertSame(instance.data, copy.data);
    }
    
    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.5);
        instance.put(1, 2.5);
        instance.put(2, 3.5);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(1, instance.size());
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
        assertEquals(1.5, instance.get(0), 0);
    }
}