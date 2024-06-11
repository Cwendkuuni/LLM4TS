import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, instance.value(0), 0.00001);
        assertEquals(2.0, instance.value(1), 0.00001);
        assertEquals(3.0, instance.value(2), 0.00001);
    }
    
    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, instance.getAttributes(), 0.00001);
    }
    
    @Test
    public void testContainsKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.entrySet().size());
        assertTrue(instance.entrySet().contains(new AbstractMap.SimpleEntry<>(0, 1.0)));
        assertTrue(instance.entrySet().contains(new AbstractMap.SimpleEntry<>(1, 2.0)));
        assertTrue(instance.entrySet().contains(new AbstractMap.SimpleEntry<>(2, 3.0)));
    }
    
    @Test
    public void testGet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, instance.get(0), 0.00001);
        assertEquals(2.0, instance.get(1), 0.00001);
        assertEquals(3.0, instance.get(2), 0.00001);
    }
    
    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.keySet().size());
        assertTrue(instance.keySet().contains(0));
        assertTrue(instance.keySet().contains(1));
        assertTrue(instance.keySet().contains(2));
    }
    
    @Test
    public void testPut() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, instance.put(0, 4.0), 0.00001);
        assertEquals(4.0, instance.get(0), 0.00001);
        assertEquals(2.0, instance.put(1, 5.0), 0.00001);
        assertEquals(5.0, instance.get(1), 0.00001);
        assertEquals(3.0, instance.put(2, 6.0), 0.00001);
        assertEquals(6.0, instance.get(2), 0.00001);
    }
    
    @Test
    public void testPutAll() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.putAll(Map.of(0, 4.0, 1, 5.0, 2, 6.0));
        assertEquals(4.0, instance.get(0), 0.00001);
        assertEquals(5.0, instance.get(1), 0.00001);
        assertEquals(6.0, instance.get(2), 0.00001);
    }
    
    @Test
    public void testRemove() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertThrows(UnsupportedOperationException.class, () -> instance.remove(0));
    }
    
    @Test
    public void testSize() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.values().size());
        assertTrue(instance.values().contains(1.0));
        assertTrue(instance.values().contains(2.0));
        assertTrue(instance.values().contains(3.0));
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
        assertArrayEquals(new double[]{1.0, 3.0}, instance.getAttributes(), 0.00001);
    }
    
    @Test
    public void testHashCode() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[]{4.0, 5.0, 6.0});

        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[]{4.0, 5.0, 6.0});

        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals("string"));
    }
    
    @Test
    public void testCopy() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance copy = instance.copy();
        assertArrayEquals(instance.getAttributes(), copy.getAttributes(), 0.00001);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0});
        instance.removeAttributes(Set.of(1, 3, 5));
        assertArrayEquals(new double[]{1.0, 3.0, 5.0}, instance.getAttributes(), 0.00001);
    }
}