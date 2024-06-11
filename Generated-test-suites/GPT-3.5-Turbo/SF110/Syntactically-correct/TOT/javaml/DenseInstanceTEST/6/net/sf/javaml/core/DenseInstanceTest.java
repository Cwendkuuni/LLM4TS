import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.DenseInstance;
import java.util.*;

public class DenseInstanceTest {
    
    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.001);
    }
    
    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        double[] clearedAttributes = {0.0, 0.0, 0.0};
        assertArrayEquals(clearedAttributes, instance.getAttributes(), 0.001);
    }
    
    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(4));
    }
    
    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        HashMap<Integer, Double> expectedMap = new HashMap<Integer, Double>();
        expectedMap.put(0, 1.0);
        expectedMap.put(1, 2.0);
        expectedMap.put(2, 3.0);
        assertEquals(expectedMap.entrySet(), entrySet);
    }
    
    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(Double.valueOf(2.0), instance.get(1));
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
        SortedSet<Integer> keySet = instance.keySet();
        TreeSet<Integer> expectedSet = new TreeSet<Integer>();
        expectedSet.add(0);
        expectedSet.add(1);
        expectedSet.add(2);
        assertEquals(expectedSet, keySet);
    }
    
    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(Double.valueOf(2.0), instance.put(1, 4.0));
        assertEquals(Double.valueOf(4.0), instance.get(1));
    }
    
    @Test
    public void testPutAll() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        instance.putAll(map);
        assertEquals(Double.valueOf(4.0), instance.get(1));
        assertEquals(Double.valueOf(5.0), instance.get(2));
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
        Collection<Double> values = instance.values();
        Collection<Double> expectedValues = new ArrayList<Double>();
        expectedValues.add(1.0);
        expectedValues.add(2.0);
        expectedValues.add(3.0);
        assertEquals(expectedValues, values);
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
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.getAttributes(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance copy = instance.copy();
        assertArrayEquals(instance.getAttributes(), copy.getAttributes(), 0.001);
        assertEquals(instance.getClassValue(), copy.getClassValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        instance.removeAttributes(indices);
        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.getAttributes(), 0.001);
    }
}