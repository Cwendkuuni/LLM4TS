import org.junit.*;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testConstructor() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.attributes, 0.0);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testConstructorWithClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        String classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertArrayEquals(att, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.attributes, 0.0);
    }
    
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(instance.entrySet().size(), att.length);
        for (int i = 0; i < att.length; i++) {
            assertTrue(instance.entrySet().contains(new HashMap.SimpleEntry<Integer, Double>(i, att[i])));
        }
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(instance.get(0), att[0]);
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
        assertEquals(instance.keySet().size(), att.length);
        for (int i = 0; i < att.length; i++) {
            assertTrue(instance.keySet().contains(i));
        }
    }
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        int index = 1;
        double value = 4.0;
        DenseInstance instance = new DenseInstance(att);
        double oldValue = instance.put(index, value);
        assertEquals(oldValue, att[index], 0.0);
        assertEquals(instance.get(index), value, 0.0);
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        DenseInstance instance = new DenseInstance(att);
        instance.putAll(map);
        assertEquals(instance.get(0), map.get(0), 0.0);
        assertEquals(instance.get(1), map.get(1), 0.0);
        assertEquals(instance.get(2), map.get(2), 0.0);
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
        assertEquals(instance.size(), att.length);
    }
    
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(instance.values().size(), att.length);
        for (int i = 0; i < att.length; i++) {
            assertTrue(instance.values().contains(att[i]));
        }
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(instance.noAttributes(), att.length);
    }
    
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        String classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        String expected = "{[1.0, 2.0, 3.0];class}";
        assertEquals(instance.toString(), expected);
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        int index = 1;
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(index);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(instance.attributes, expected, 0.0);
    }
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        int expected = Objects.hashCode(att);
        assertEquals(instance.hashCode(), expected);
    }
    
    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copy = instance.copy();
        assertArrayEquals(copy.attributes, instance.attributes, 0.0);
        assertEquals(copy.classValue(), instance.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttributes(indices);
        double[] expected = {2.0, 3.0};
        assertArrayEquals(instance.attributes, expected, 0.0);
    }
}