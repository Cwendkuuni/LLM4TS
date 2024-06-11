import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {
    
    private DenseInstance instance;
    
    @Before
    public void setUp() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
    }
    
    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }
    
    @Test
    public void testClear() {
        instance.clear();
        double[] clearedAttributes = {0.0, 0.0, 0.0};
        assertArrayEquals(clearedAttributes, instance.getAttributes(), 0.001);
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        
        Map.Entry<Integer, Double> entry1 = new AbstractMap.SimpleEntry<>(0, 1.0);
        Map.Entry<Integer, Double> entry2 = new AbstractMap.SimpleEntry<>(1, 2.0);
        Map.Entry<Integer, Double> entry3 = new AbstractMap.SimpleEntry<>(2, 3.0);
        
        assertTrue(entrySet.contains(entry1));
        assertTrue(entrySet.contains(entry2));
        assertTrue(entrySet.contains(entry3));
    }
    
    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }
    
    @Test
    public void testPut() {
        assertEquals(1.0, instance.put(0, 4.0), 0.001);
        assertEquals(4.0, instance.get(0), 0.001);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        
        instance.putAll(map);
        
        assertEquals(4.0, instance.get(0), 0.001);
        assertEquals(5.0, instance.get(1), 0.001);
        assertEquals(6.0, instance.get(2), 0.001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(0);
        double[] attributes = {2.0, 3.0};
        assertArrayEquals(attributes, instance.getAttributes(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(attributes);
        
        assertEquals(instance.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(attributes);
        
        assertTrue(instance.equals(instance));
        assertTrue(instance.equals(instance2));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
    }
    
    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertArrayEquals(instance.getAttributes(), copy.getAttributes(), 0.001);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(1);
        
        instance.removeAttributes(indices);
        
        double[] attributes = {3.0};
        assertArrayEquals(attributes, instance.getAttributes(), 0.001);
    }
}