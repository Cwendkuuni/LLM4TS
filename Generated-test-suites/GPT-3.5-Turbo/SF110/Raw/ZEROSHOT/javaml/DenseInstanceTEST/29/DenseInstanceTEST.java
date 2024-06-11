import static org.junit.Assert.*;
import org.junit.Test;

public class DenseInstanceTest {
    
    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.clear();
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, instance.value(i), 0.0001);
        }
    }
    
    @Test
    public void testContainsKey() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertTrue(instance.containsKey(entry.getKey()));
            assertTrue(instance.containsValue(entry.getValue()));
        }
    }
    
    @Test
    public void testGet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertTrue(keySet.contains(i));
        }
    }
    
    @Test
    public void testPut() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        double oldValue = instance.put(0, 10.0);
        assertEquals(1.0, oldValue, 0.0001);
        assertEquals(10.0, instance.value(0), 0.0001);
    }
    
    @Test
    public void testPutAll() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 10.0);
        map.put(2, 30.0);
        instance.putAll(map);
        assertEquals(10.0, instance.value(0), 0.0001);
        assertEquals(30.0, instance.value(2), 0.0001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        for (double value : instance.values()) {
            assertTrue(instance.containsValue(value));
        }
    }
    
    @Test
    public void testNoAttributes() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        String expected = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expected, instance.toString());
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
        DenseInstance instance3 = new DenseInstance(new double[] {1.0, 2.0, 4.0});
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Instance copy = instance.copy();
        assertEquals(instance.noAttributes(), copy.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i), copy.value(i), 0.0001);
        }
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        instance.removeAttributes(indices);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(3.0, instance.value(1), 0.0001);
    }
}