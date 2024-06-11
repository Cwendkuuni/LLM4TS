
package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructor() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.attributes, 0.0);
        assertNull(instance.classValue());
        
        double[] att2 = {4.0, 5.0, 6.0};
        Object classValue = new Object();
        DenseInstance instance2 = new DenseInstance(att2, classValue);
        assertArrayEquals(att2, instance2.attributes, 0.0);
        assertEquals(classValue, instance2.classValue());
        
        double[] emptyAtt = {};
        DenseInstance instance3 = new DenseInstance(emptyAtt);
        assertArrayEquals(emptyAtt, instance3.attributes, 0.0);
        assertNull(instance3.classValue());
    }
    
    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        
        double[] expectedAtt = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedAtt, instance.attributes, 0.0);
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
        
        HashMap<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 1.0);
        expectedMap.put(1, 2.0);
        expectedMap.put(2, 3.0);
        
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertTrue(expectedMap.containsKey(entry.getKey()));
            assertEquals(expectedMap.get(entry.getKey()), entry.getValue(), 0.0);
        }
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
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
        assertEquals(3, keySet.size());
        
        TreeSet<Integer> expectedSet = new TreeSet<>();
        expectedSet.add(0);
        expectedSet.add(1);
        expectedSet.add(2);
        
        for (int key : keySet) {
            assertTrue(expectedSet.contains(key));
        }
    }
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Double oldValue = instance.put(0, 4.0);
        assertEquals(1.0, oldValue, 0.0);
        assertEquals(4.0, instance.get(0), 0.0);
        
        Double oldValue2 = instance.put(3, 5.0);
        assertNull(oldValue2);
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(3, 5.0);
        
        instance.putAll(map);
        
        assertEquals(4.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        assertFalse(instance.containsKey(3));
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
        
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        
        ArrayList<Double> expectedValues = new ArrayList<>();
        expectedValues.add(1.0);
        expectedValues.add(2.0);
        expectedValues.add(3.0);
        
        for (double value : values) {
            assertTrue(expectedValues.contains(value));
        }
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttribute(0);
        
        double[] expectedAtt = {2.0, 3.0};
        assertArrayEquals(expectedAtt, instance.attributes, 0.0);
    }
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        int hashCode = instance.hashCode();
        
        assertEquals(Arrays.hashCode(att), hashCode);
    }
    
    @Test
    public void testEquals() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        DenseInstance instance2 = new DenseInstance(att);
        DenseInstance instance3 = new DenseInstance(new double[] {4.0, 5.0, 6.0});
        
        assertTrue(instance.equals(instance));
        assertTrue(instance.equals(instance2));
        assertFalse(instance.equals(instance3));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertNotSame(instance, copy);
        assertArrayEquals(instance.attributes, copy.attributes, 0.0);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Integer> indices = new TreeSet<>();
        indices.add(0);
        indices.add(2);
        
        instance.removeAttributes(indices);
        
        double[] expectedAtt = {2.0};
        assertArrayEquals(expectedAtt, instance.attributes, 0.0);
    }
}
