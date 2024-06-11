import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {
    
    @Test
    public void testConstructor() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertArrayEquals(attributes, instance.attributes, 0.001);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testConstructorWithClassValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        Double classValue = 1.0;
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertArrayEquals(attributes, instance.attributes, 0.001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }
    
    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertArrayEquals(new double[3], instance.attributes, 0.001);
    }
    
    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> expectedSet = new HashSet<>();
        expectedSet.add(new AbstractMap.SimpleEntry<>(0, 1.0));
        expectedSet.add(new AbstractMap.SimpleEntry<>(1, 2.0));
        expectedSet.add(new AbstractMap.SimpleEntry<>(2, 3.0));
        assertEquals(expectedSet, instance.entrySet());
    }
    
    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> expectedSet = new HashSet<>();
        expectedSet.add(0);
        expectedSet.add(1);
        expectedSet.add(2);
        assertEquals(expectedSet, instance.keySet());
    }
    
    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        double oldValue = instance.put(0, 4.0);
        assertEquals(1.0, oldValue, 0.001);
        assertEquals(4.0, instance.get(0), 0.001);
    }
    
    @Test
    public void testPutAll() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        instance.putAll(map);
        assertEquals(4.0, instance.get(0), 0.001);
        assertEquals(5.0, instance.get(1), 0.001);
        assertEquals(6.0, instance.get(2), 0.001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Collection<Double> expectedValues = new ArrayList<>();
        expectedValues.add(1.0);
        expectedValues.add(2.0);
        expectedValues.add(3.0);
        assertEquals(expectedValues, instance.values());
    }
    
    @Test
    public void testNoAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] attributes = {1.0, 2.0, 3.0};
        Double classValue = 1.0;
        DenseInstance instance = new DenseInstance(attributes, classValue);
        String expectedString = "{[1.0, 2.0, 3.0]; 1.0}";
        assertEquals(expectedString, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.001);
    }
    
    @Test
    public void testHashCode() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        DenseInstance instance2 = new DenseInstance(attributes);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        DenseInstance instance2 = new DenseInstance(attributes);
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        Double classValue = 1.0;
        DenseInstance instance = new DenseInstance(attributes, classValue);
        DenseInstance copy = (DenseInstance)instance.copy();
        assertArrayEquals(attributes, copy.attributes, 0.001);
        assertEquals(classValue, copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(0);
        indicesToRemove.add(2);
        instance.removeAttributes(indicesToRemove);
        double[] expectedAttributes = {2.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.001);
    }
}