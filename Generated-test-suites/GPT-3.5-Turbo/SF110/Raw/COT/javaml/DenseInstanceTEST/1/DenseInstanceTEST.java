import static org.junit.Assert.*;
import org.junit.Test;

public class DenseInstanceTest {
    
    @Test
    public void testConstructorWithAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.attributes, 0.00001);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testConstructorWithAttributesAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertArrayEquals(att, instance.attributes, 0.00001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);
        assertEquals(size, instance.attributes.length);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att[0], instance.value(0), 0.00001);
        assertEquals(att[1], instance.value(1), 0.00001);
        assertEquals(att[2], instance.value(2), 0.00001);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] expected = new double[att.length];
        assertArrayEquals(expected, instance.attributes, 0.00001);
    }
    
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
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        for (int i = 0; i < att.length; i++) {
            Map.Entry<Integer, Double> entry = entrySet.stream().filter(e -> e.getKey() == i).findFirst().orElse(null);
            assertNotNull(entry);
            assertEquals(i, (int)entry.getKey());
            assertEquals(att[i], entry.getValue(), 0.00001);
        }
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertNull(instance.get(-1));
        assertEquals(att[0], instance.get(0), 0.00001);
        assertEquals(att[1], instance.get(1), 0.00001);
        assertEquals(att[2], instance.get(2), 0.00001);
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
        Double oldValue = instance.put(0, 0.0);
        assertEquals(att[0], oldValue, 0.00001);
        assertEquals(0.0, instance.attributes[0], 0.00001);
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 0.0);
        map.put(1, 1.0);
        map.put(2, 2.0);
        instance.putAll(map);
        assertArrayEquals(new double[]{0.0, 1.0, 2.0}, instance.attributes, 0.00001);
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
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.00001);
    }
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);
        DenseInstance instance3 = new DenseInstance(new double[]{3.0, 2.0, 1.0});
        assertEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copy = (DenseInstance)instance.copy();
        assertNotSame(instance, copy);
        assertArrayEquals(instance.attributes, copy.attributes, 0.00001);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        HashSet<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        assertArrayEquals(new double[]{2.0}, instance.attributes, 0.00001);
    }
}