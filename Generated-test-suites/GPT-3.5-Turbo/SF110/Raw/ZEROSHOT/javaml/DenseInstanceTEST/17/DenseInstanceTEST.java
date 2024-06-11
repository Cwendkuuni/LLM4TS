import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {
    
    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, instance.attributes, 0.0001);
    }
    
    @Test
    public void testContainsKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
    }
    
    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertNotNull(entrySet);
        assertEquals(3, entrySet.size());
        
        Map<Integer, Double> expectedMap = new HashMap<Integer, Double>();
        expectedMap.put(0, 1.0);
        expectedMap.put(1, 2.0);
        expectedMap.put(2, 3.0);
        
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertTrue(expectedMap.containsKey(entry.getKey()));
            assertEquals(expectedMap.get(entry.getKey()), entry.getValue(), 0.0001);
        }
    }
    
    @Test
    public void testGet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        SortedSet<Integer> keySet = instance.keySet();
        assertNotNull(keySet);
        assertEquals(3, keySet.size());
        
        List<Integer> expectedKeys = new ArrayList<Integer>(Arrays.asList(0, 1, 2));
        
        for (int key : keySet) {
            assertTrue(expectedKeys.contains(key));
        }
    }
    
    @Test
    public void testPut() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, instance.put(0, 0.5), 0.0001);
        assertEquals(2.0, instance.put(1, 1.5), 0.0001);
        assertEquals(3.0, instance.put(2, 2.5), 0.0001);
        
        assertArrayEquals(new double[]{0.5, 1.5, 2.5}, instance.attributes, 0.0001);
    }
    
    @Test
    public void testPutAll() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 0.5);
        map.put(1, 1.5);
        map.put(2, 2.5);
        
        instance.putAll(map);
        
        assertArrayEquals(new double[]{0.5, 1.5, 2.5}, instance.attributes, 0.0001);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testRemove() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Collection<Double> values = instance.values();
        assertNotNull(values);
        assertEquals(3, values.size());
        
        List<Double> expectedValues = new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0));
        
        for (double value : values) {
            assertTrue(expectedValues.contains(value));
        }
    }
    
    @Test
    public void testNoAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.removeAttribute(1);
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0001);
    }
    
    @Test
    public void testHashCode() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[]{3.0, 2.0, 1.0});
        
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[]{3.0, 2.0, 1.0});
        DenseInstance instance4 = new DenseInstance(new double[]{1.0, 2.0});
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(instance4));
    }
    
    @Test
    public void testCopy() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertNotSame(instance, copy);
        assertArrayEquals(instance.attributes, copy.attributes, 0.0001);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> indices = new HashSet<Integer>(Arrays.asList(0, 2));
        instance.removeAttributes(indices);
        assertArrayEquals(new double[]{2.0}, instance.attributes, 0.0001);
    }
}