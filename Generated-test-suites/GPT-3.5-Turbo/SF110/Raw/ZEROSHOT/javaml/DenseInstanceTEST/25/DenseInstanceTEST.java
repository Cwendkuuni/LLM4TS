package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class DenseInstanceTest {

    private DenseInstance instance;
    private double[] testAttributes;
    private Object testClassValue;
      
    @Before
    public void setUp() {
        testAttributes = new double[]{1.0, 2.0, 3.0};
        testClassValue = new Object();
        instance = new DenseInstance(testAttributes, testClassValue);
    }
    
    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.00001);
        assertEquals(2.0, instance.value(1), 0.00001);
        assertEquals(3.0, instance.value(2), 0.00001);
    }
    
    @Test
    public void testClear() {
        instance.clear();
        double[] expected = new double[]{0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.attributes, 0.00001);
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        HashMap<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 1.0);
        expectedMap.put(1, 2.0);
        expectedMap.put(2, 3.0);
        assertEquals(expectedMap.entrySet(), entrySet);
    }
    
    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.00001);
        assertEquals(2.0, instance.get(1), 0.00001);
        assertEquals(3.0, instance.get(2), 0.00001);
    }
    
    @Test
    public void testIsEmptyFalse() {
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = instance.keySet();
        TreeSet<Integer> expectedSet = new TreeSet<>();
        expectedSet.add(0);
        expectedSet.add(1);
        expectedSet.add(2);
        assertEquals(expectedSet, keySet);
    }
    
    @Test
    public void testPut() {
        Double oldValue = instance.put(0, 0.0);
        assertEquals(1.0, oldValue, 0.00001);
        assertEquals(0.0, instance.attributes[0], 0.00001);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 0.0);
        map.put(1, 4.0);
        map.put(2, 5.0);
        instance.putAll(map);
        double[] expected = new double[]{0.0, 4.0, 5.0};
        assertArrayEquals(expected, instance.attributes, 0.00001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        Collection<Double> values = instance.values();
        List<Double> expected = Arrays.asList(1.0, 2.0, 3.0);
        assertEquals(expected, values);
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0];" + testClassValue + "}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(0);
        double[] expected = new double[]{2.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.00001);
    }
    
    @Test
    public void testHashCode() {
        int expected = 31 * 1 + Arrays.hashCode(instance.attributes);
        assertEquals(expected, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance otherInstance = new DenseInstance(testAttributes, testClassValue);
        assertTrue(instance.equals(otherInstance));
    }
    
    @Test
    public void testCopy() {
        DenseInstance copyInstance = (DenseInstance)instance.copy();
        assertNotSame(instance, copyInstance);
        assertArrayEquals(instance.attributes, copyInstance.attributes, 0.00001);
        assertSame(instance.classValue(), copyInstance.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        instance.removeAttributes(indices);
        double[] expected = new double[]{2.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.00001);
    }
}