
import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {
    
    // Test case for DenseInstance(double[] att)
    @Test
    public void testDenseInstanceWithAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertArrayEquals(att, instance.attributes, 0.0);
    }
    
    // Test case for DenseInstance(double[] att, Object classValue)
    @Test
    public void testDenseInstanceWithAttributesAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class1";
        DenseInstance instance = new DenseInstance(att, classValue);
        
        assertArrayEquals(att, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }
    
    // Test case for DenseInstance(int size)
    @Test
    public void testDenseInstanceWithSize() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);
        
        assertEquals(size, instance.attributes.length);
    }
    
    // Test case for value(int pos)
    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(att[0], instance.value(0), 0.0);
        assertEquals(att[1], instance.value(1), 0.0);
        assertEquals(att[2], instance.value(2), 0.0);
    }
    
    // Test case for clear()
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        
        assertArrayEquals(new double[] {0.0, 0.0, 0.0}, instance.attributes, 0.0);
    }
    
    // Test case for containsKey(Object key)
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }
    
    // Test case for containsValue(Object value)
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    // Test case for entrySet()
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(3, instance.entrySet().size());
        assertTrue(instance.entrySet().contains(new AbstractMap.SimpleEntry<>(0, 1.0)));
        assertTrue(instance.entrySet().contains(new AbstractMap.SimpleEntry<>(1, 2.0)));
        assertTrue(instance.entrySet().contains(new AbstractMap.SimpleEntry<>(2, 3.0)));
    }
    
    // Test case for get(Object key)
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
    }
    
    // Test case for isEmpty()
    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertFalse(instance.isEmpty());
    }
    
    // Test case for keySet()
    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(3, instance.keySet().size());
        assertTrue(instance.keySet().contains(0));
        assertTrue(instance.keySet().contains(1));
        assertTrue(instance.keySet().contains(2));
    }
    
    // Test case for put(Integer key, Double value)
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        double newValue = 4.0;
        Double oldValue = instance.put(1, newValue);
        
        assertEquals(newValue, instance.attributes[1], 0.0);
        assertEquals(2.0, oldValue, 0.0);
    }
    
    // Test case for putAll(Map<? extends Integer, ? extends Double> m)
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        instance.putAll(map);
        
        assertEquals(1.0, instance.attributes[0], 0.0);
        assertEquals(4.0, instance.attributes[1], 0.0);
        assertEquals(5.0, instance.attributes[2], 0.0);
    }
    
    // Test case for remove(Object key)
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.remove(1);
    }
    
    // Test case for size()
    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(3, instance.size());
    }
    
    // Test case for values()
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Collection<Double> values = instance.values();
        
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }
    
    // Test case for noAttributes()
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(3, instance.noAttributes());
    }
    
    // Test case for toString()
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class1";
        DenseInstance instance = new DenseInstance(att, classValue);
        
        assertEquals("{[1.0, 2.0, 3.0]; class1}", instance.toString());
    }
    
    // Test case for removeAttribute(int i)
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttribute(1);
        
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0);
    }
    
    // Test case for hashCode()
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);
        
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    // Test case for equals(Object obj)
    @Test
    public void testEquals() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);
        DenseInstance instance3 = new DenseInstance(new double[]{2.0, 3.0, 4.0});
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    // Test case for copy()
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class1";
        DenseInstance instance = new DenseInstance(att, classValue);
        
        DenseInstance copy = instance.copy();
        
        assertNotSame(instance, copy);
        assertArrayEquals(instance.attributes, copy.attributes, 0.0);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    // Test case for removeAttributes(Set<Integer> indices)
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(0);
        indicesToRemove.add(2);
        
        instance.removeAttributes(indicesToRemove);
        
        assertArrayEquals(new double[]{2.0}, instance.attributes, 0.0);
    }
}
