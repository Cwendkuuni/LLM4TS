import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    private double[] attributes = {1.0, 2.0, 3.0};
    private DenseInstance instance = new DenseInstance(attributes);
    
    @Test
    public void testValue() {
        assertEquals(2.0, instance.value(1), 0.0);
    }
    
    @Test
    public void testClear() {
        instance.clear();
        assertArrayEquals(new double[3], instance.attributes, 0.0);
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
    }
    
    @Test
    public void testGet() {
        assertEquals(2.0, instance.get(1), 0.0);
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
    }
    
    @Test
    public void testPut() {
        assertEquals(2.0, instance.put(1, 4.0), 0.0);
        assertEquals(4.0, instance.get(1), 0.0);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 5.0);
        map.put(2, 6.0);
        instance.putAll(map);
        assertEquals(5.0, instance.get(0), 0.0);
        assertEquals(6.0, instance.get(2), 0.0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(1);
    }
    
    @Test
    public void testSize() {
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(1);
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0);
    }
    
    @Test
    public void testHashCode() {
        assertEquals(442674660, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance other = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.equals(other));
        
        DenseInstance diff = new DenseInstance(new double[]{1.0, 2.0, 4.0});
        assertFalse(instance.equals(diff));
    }
    
    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance)instance.copy();
        assertArrayEquals(instance.attributes, copy.attributes, 0.0);
        assertEquals(null, copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        instance.removeAttributes(indices);
        assertEquals(2, instance.noAttributes());
    }
}