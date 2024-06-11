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
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }
    
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }
    
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(1, 2.0)));
    }
    
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assertEquals(new Double(2.0), instance.get(1));
        assertEquals(new Double(0.0), instance.get(2));
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(1, 2.0);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        Set<Integer> keySet = instance.keySet();
        assertTrue(keySet.contains(1));
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        assertNull(instance.put(1, 2.0));
        assertEquals(new Double(2.0), instance.put(1, 3.0));
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 2.0);
        map.put(2, 3.0);
        instance.putAll(map);
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assertEquals(new Double(2.0), instance.remove(1));
        assertFalse(instance.containsKey(1));
    }
    
    @Test
    @Deprecated
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assertEquals(1, instance.size());
    }
    
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        Collection<Double> values = instance.values();
        assertTrue(values.contains(2.0));
    }
    
    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        instance.put(3, 4.0);
        assertEquals(4, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        instance.put(3, 4.0);
        instance.removeAttribute(1);
        assertTrue(instance.containsKey(3));
        assertEquals(1, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assertEquals("{1=2.0;null}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 2.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 2.0);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 2.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 2.0);
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(instance, copy);
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Set<Integer> indices = new HashSet<Integer>(Arrays.asList(1, 2));
        instance.removeAttributes(indices);
        assertTrue(instance.isEmpty());
    }
    
    // Additional test cases for potential edge cases and exception handling scenarios
    
    @Test
    public void testDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assertEquals(new Double(1.0), instance.get(0));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableEntrySet() {
        SparseInstance instance = new SparseInstance();
        HashSet<Map.Entry<Integer, Double>> entrySet = new HashSet<Map.Entry<Integer, Double>>(instance.entrySet());
        entrySet.add(new AbstractMap.SimpleEntry<Integer, Double>(1, 2.0));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableKeySet() {
        SparseInstance instance = new SparseInstance();
        HashSet<Integer> keySet = new HashSet<Integer>(instance.keySet());
        keySet.add(1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableValues() {
        SparseInstance instance = new SparseInstance();
        Collection<Double> values = instance.values();
        values.add(2.0);
    }
}


// JUnit 4 test cases for SparseInstance class
// ...

