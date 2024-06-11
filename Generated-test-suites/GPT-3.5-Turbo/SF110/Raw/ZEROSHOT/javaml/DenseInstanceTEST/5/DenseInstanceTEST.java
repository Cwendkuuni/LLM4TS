import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.value(0), 1e-8);
        assertEquals(2.0, instance.value(1), 1e-8);
        assertEquals(3.0, instance.value(2), 1e-8);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.getAttributes(), 1e-8);
    }
    
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        
        assertEquals(att.length, entrySet.size());
        
        for (int i = 0; i < att.length; i++) {
            assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(i, att[i])));
        }
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.get(0), 1e-8);
        assertEquals(2.0, instance.get(1), 1e-8);
        assertEquals(3.0, instance.get(2), 1e-8);
    }
    
    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        SortedSet<Integer> keySet = instance.keySet();
        
        assertEquals(att.length, keySet.size());
        
        for (int i = 0; i < att.length; i++) {
            assertTrue(keySet.contains(i));
        }
    }
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Double oldValue = instance.put(0, 4.0);
        assertEquals(1.0, oldValue, 1e-8);
        assertEquals(4.0, instance.get(0), 1e-8);
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        
        instance.putAll(map);
        
        assertEquals(4.0, instance.get(0), 1e-8);
        assertEquals(5.0, instance.get(1), 1e-8);
        assertEquals(3.0, instance.get(2), 1e-8);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(att.length, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Collection<Double> values = instance.values();
        
        assertEquals(att.length, values.size());
        
        for (double value : att) {
            assertTrue(values.contains(value));
        }
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(att.length, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        String expected = "{[1.0, 2.0, 3.0];null}";
        
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttribute(1);
        
        double[] expected = {1.0, 3.0};
        
        assertArrayEquals(expected, instance.getAttributes(), 1e-8);
    }
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        int expected = 961;
        
        assertEquals(expected, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);
        
        double[] att3 = {2.0, 3.0, 4.0};
        DenseInstance instance3 = new DenseInstance(att3);
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertArrayEquals(instance.getAttributes(), copy.getAttributes(), 1e-8);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        
        instance.removeAttributes(indices);
        
        double[] expected = {1.0, 3.0};
        
        assertArrayEquals(expected, instance.getAttributes(), 1e-8);
    }
}