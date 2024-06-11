import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {
    
    @Test
    public void testValue() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }
    
    @Test
    public void testClear() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] expected = new double[]{0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.attributes, 0.001);
    }
    
    @Test
    public void testContainsKey() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        Map.Entry<Integer, Double> entry1 = new AbstractMap.SimpleEntry<Integer, Double>(0, 1.0);
        Map.Entry<Integer, Double> entry2 = new AbstractMap.SimpleEntry<Integer, Double>(1, 2.0);
        Map.Entry<Integer, Double> entry3 = new AbstractMap.SimpleEntry<Integer, Double>(2, 3.0);
        assertTrue(entrySet.contains(entry1));
        assertTrue(entrySet.contains(entry2));
        assertTrue(entrySet.contains(entry3));
    }
    
    @Test
    public void testGet() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }
    
    @Test
    public void testPut() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Double oldValue = instance.put(0, 0.5);
        assertEquals(1.0, oldValue, 0.001);
        assertEquals(0.5, instance.get(0), 0.001);
    }
    
    @Test
    public void testPutAll() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 0.5);
        map.put(1, 1.5);
        map.put(2, 2.5);
        instance.putAll(map);
        assertEquals(0.5, instance.get(0), 0.001);
        assertEquals(1.5, instance.get(1), 0.001);
        assertEquals(2.5, instance.get(2), 0.001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        String expected = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        double[] expected = new double[]{1.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.001);
    }
    
    @Test
    public void testHashCode() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        int expected = Objects.hash(1.0, 2.0, 3.0);
        assertEquals(expected, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] att1 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
  
        double[] att2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);
        
        double[] att3 = new double[]{2.0, 3.0, 4.0};
        DenseInstance instance3 = new DenseInstance(att3);
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copy = (DenseInstance) instance.copy();
        assertArrayEquals(att, copy.attributes, 0.001);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        double[] expected = new double[]{2.0};
        assertArrayEquals(expected, instance.attributes, 0.001);
    }
}