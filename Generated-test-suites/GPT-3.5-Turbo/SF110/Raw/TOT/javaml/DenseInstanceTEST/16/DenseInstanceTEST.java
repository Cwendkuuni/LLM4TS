import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {
    
    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        double result = instance.value(1);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.clear();
        double[] attributes = instance.getAttributes();
        for (double attr : attributes) {
            assertEquals(0.0, attr, 0.0001);
        }
    }
    
    @Test
    public void testContainsKey() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        boolean result1 = instance.containsKey(1);
        assertTrue(result1);
        boolean result2 = instance.containsKey(3);
        assertFalse(result2);
    }
    
    @Test
    public void testContainsValue() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        boolean result1 = instance.containsValue(2.0);
        assertTrue(result1);
        boolean result2 = instance.containsValue(4.0);
        assertFalse(result2);
    }
    
    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertTrue(instance.containsKey(entry.getKey()));
            assertEquals(instance.value(entry.getKey()), entry.getValue(), 0.0001);
        }
    }
    
    @Test
    public void testGet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Double result = instance.get(1);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        int index = 0;
        for (int key : keySet) {
            assertEquals(index++, key);
        }
    }
    
    @Test
    public void testPut() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Double previousValue = instance.put(1, 4.0);
        assertEquals(2.0, previousValue, 0.0001);
        assertEquals(4.0, instance.value(1), 0.0001);
    }
    
    @Test
    public void testPutAll() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 4.0);
        map.put(2, 5.0);
        instance.putAll(map);
        assertEquals(4.0, instance.value(0), 0.0001);
        assertEquals(5.0, instance.value(2), 0.0001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.remove(1);
    }
    
    @Test
    public void testSize() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        int result = instance.size();
        assertEquals(3, result);
    }
    
    @Test
    public void testValues() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        for (double value : values) {
            assertTrue(instance.containsValue(value));
        }
    }
    
    @Test
    public void testNoAttributes() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        int result = instance.noAttributes();
        assertEquals(3, result);
    }
    
    @Test
    public void testToString() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        String result = instance.toString();
        assertEquals("{[1.0, 2.0, 3.0];null}", result);
    }
    
    @Test
    public void testRemoveAttribute() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(3.0, instance.value(1), 0.0001);
    }
    
    @Test
    public void testHashCode() {
        DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
    }
    
    @Test
    public void testCopy() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance copy = instance.copy();
        assertEquals(instance.noAttributes(), copy.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i), copy.value(i), 0.0001);
        }
    }
    
    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        instance.removeAttributes(indices);
        assertEquals(2, instance.noAttributes());
        assertEquals(2.0, instance.value(0), 0.0001);
        assertEquals(3.0, instance.value(1), 0.0001);
    }
    
}