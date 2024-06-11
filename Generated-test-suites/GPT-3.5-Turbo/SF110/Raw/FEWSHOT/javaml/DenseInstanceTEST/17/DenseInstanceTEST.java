
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {
    @Test
    public void testConstructorWithDoubleArray() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.attributes, 0.0001);
        assertNull(instance.getClassValue());
    }
    
    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertArrayEquals(att, instance.attributes, 0.0001);
        assertEquals(classValue, instance.getClassValue());
    }
    
    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.attributes, 0.0001);
        assertNull(instance.getClassValue());
    }
    
    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.attributes, 0.0001);
    }
    
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
        assertFalse(instance.containsKey("key"));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
        assertFalse(instance.containsValue("value"));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(att.length, entrySet.size());
        
        for (int i = 0; i < att.length; i++) {
            assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(i, att[i])));
        }
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
        assertNull(instance.get(3));
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
        
        assertNull(instance.put(0, 0.5));
        assertNull(instance.put(1, 1.5));
        assertNull(instance.put(2, 2.5));
        
        assertEquals(0.5, instance.attributes[0], 0.0001);
        assertEquals(1.5, instance.attributes[1], 0.0001);
        assertEquals(2.5, instance.attributes[2], 0.0001);
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 0.5);
        map.put(1, 1.5);
        map.put(2, 2.5);
        
        instance.putAll(map);
        
        assertEquals(0.5, instance.attributes[0], 0.0001);
        assertEquals(1.5, instance.attributes[1], 0.0001);
        assertEquals(2.5, instance.attributes[2], 0.0001);
    }
    
    @Test
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertThrows(UnsupportedOperationException.class, () -> instance.remove(0));
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
        
        for (double v : att) {
            assertTrue(values.contains(v));
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
        
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttribute(0);
        
        double[] expected = {2.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.0001);
    }
    
    @Test
    public void testHashCode() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);
        
        double[] att3 = {1.0, 2.0, 4.0};
        DenseInstance instance3 = new DenseInstance(att3);
        
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);
        
        double[] att3 = {1.0, 2.0, 4.0};
        DenseInstance instance3 = new DenseInstance(att3);
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertNotSame(instance, copy);
        assertArrayEquals(instance.attributes, copy.attributes, 0.0001);
        assertEquals(instance.getClassValue(), copy.getClassValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0, 4.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        
        instance.removeAttributes(indices);
        
        double[] expected = {2.0, 4.0};
        assertArrayEquals(expected, instance.attributes, 0.0001);
    }
}
