
package net.sf.javaml.core;

import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {
    @Test
    public void testConstructor1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att, instance.attributes);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testConstructor2() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertEquals(att, instance.attributes);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testConstructor3() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);
        double[] att = new double[size];
        assertEquals(att, instance.attributes);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.value(0));
        assertEquals(2.0, instance.value(1));
        assertEquals(3.0, instance.value(2));
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] clearedAtt = {0.0, 0.0, 0.0};
        assertEquals(clearedAtt, instance.attributes);
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
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        for (int i = 0; i < att.length; ++i) {
            map.put(i, att[i]);
        }
        assertEquals(map.entrySet(), entrySet);
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.get(0));
        assertEquals(2.0, instance.get(1));
        assertEquals(3.0, instance.get(2));
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
        TreeSet<Integer> keys = new TreeSet<Integer>();
        for (int i = 0; i < att.length; ++i) {
            keys.add(i);
        }
        assertEquals(keys, instance.keySet());
    }
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Double oldValue = instance.put(0, 4.0);
        assertEquals(1.0, oldValue);
        assertEquals(4.0, instance.get(0));
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        instance.putAll(map);
        assertEquals(4.0, instance.get(0));
        assertEquals(5.0, instance.get(1));
        assertEquals(6.0, instance.get(2));
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
        Collection<Double> values = new ArrayList<Double>();
        for (double v : att) {
            values.add(v);
        }
        assertEquals(values, instance.values());
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
        String expected = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        double[] removedAtt = {1.0, 3.0};
        assertEquals(removedAtt, instance.attributes);
    }
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        int expected = Objects.hash(att);
        assertEquals(expected, instance.hashCode());
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
        DenseInstance copy = (DenseInstance) instance.copy();
        assertEquals(att, copy.attributes);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        DenseInstance expected = new DenseInstance(new double[] {1.0, 3.0});
        instance.removeAttributes(indices);
        assertEquals(expected.attributes, instance.attributes);
    }
}
