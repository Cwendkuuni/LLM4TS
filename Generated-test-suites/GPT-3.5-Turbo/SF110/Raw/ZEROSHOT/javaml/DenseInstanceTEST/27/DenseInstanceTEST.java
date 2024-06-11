import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        double expected = 2.0;
        double actual = instance.value(1);
        
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        
        double[] expected = {0.0, 0.0, 0.0};
        double[] actual = instance.attributes;
        
        assertArrayEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testContainsKey_Positive() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.containsKey(1);
        
        assertTrue(result);
    }
    
    @Test
    public void testContainsKey_Negative() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.containsKey(10);
        
        assertFalse(result);
    }
    
    @Test
    public void testContainsKey_NonIntegerObject() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.containsKey("key");
        
        assertFalse(result);
    }
    
    @Test
    public void testContainsValue_Positive() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.containsValue(2.0);
        
        assertTrue(result);
    }
    
    @Test
    public void testContainsValue_Negative() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.containsValue(10.0);
        
        assertFalse(result);
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        
        assertNotNull(entrySet);
        assertEquals(3, entrySet.size());
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Double value = instance.get(1);
        
        assertNotNull(value);
        assertEquals(2.0, value, 0.001);
    }
    
    @Test
    public void testIsEmpty_False() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.isEmpty();
        
        assertFalse(result);
    }
    
    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        SortedSet<Integer> keySet = instance.keySet();
        
        assertNotNull(keySet);
        assertEquals(3, keySet.size());
    }
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Double oldValue = instance.put(1, 5.0);
        
        assertNotNull(oldValue);
        assertEquals(2.0, oldValue, 0.001);
        assertEquals(5.0, instance.value(1), 0.001);
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        
        instance.putAll(map);
        
        assertEquals(4.0, instance.value(1), 0.001);
        assertEquals(5.0, instance.value(2), 0.001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.remove(1);
    }
    
    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        int size = instance.size();
        
        assertEquals(3, size);
    }
    
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Collection<Double> values = instance.values();
        
        assertNotNull(values);
        assertEquals(3, values.size());
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        int noAttributes = instance.noAttributes();
        
        assertEquals(3, noAttributes);
    }
    
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        String str = instance.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("{"));
        assertTrue(str.contains("}"));
        assertTrue(str.contains("["));
        assertTrue(str.contains("]"));
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttribute(1);
        
        double[] expected = {1.0, 3.0};
        double[] actual = instance.attributes;
        
        assertArrayEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);
        
        int hashCode1 = instance1.hashCode();
        int hashCode2 = instance2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals_True() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        
        boolean result = instance1.equals(instance2);
        
        assertTrue(result);
    }
    
    @Test
    public void testEquals_False() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 3.0, 2.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        
        boolean result = instance1.equals(instance2);
        
        assertFalse(result);
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        
        DenseInstance instance2 = (DenseInstance) instance1.copy();
        
        assertNotNull(instance2);
        assertNotSame(instance1, instance2);
        assertArrayEquals(instance1.attributes, instance2.attributes, 0.001);
        assertEquals(instance1.classValue(), instance2.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        
        instance.removeAttributes(indices);
        
        double[] expected = {1.0, 3.0, 5.0};
        double[] actual = instance.attributes;
        
        assertArrayEquals(expected, actual, 0.001);
    }

}