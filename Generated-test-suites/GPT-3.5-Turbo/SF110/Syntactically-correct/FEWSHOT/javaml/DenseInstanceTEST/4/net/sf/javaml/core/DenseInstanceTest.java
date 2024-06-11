
package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {
    
    @Test
    public void testConstructorWithAttributes() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertArrayEquals(att, instance.attributes, 0.0);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testConstructorWithAttributesAndClassValue() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        String classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        
        assertArrayEquals(att, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testClear() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        
        assertArrayEquals(new double[att.length], instance.attributes, 0.0);
    }
    
    @Test
    public void testContainsKey() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
        assertFalse(instance.containsKey("key"));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
        assertFalse(instance.containsValue("value"));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        
        assertEquals(att.length, entries.size());
        for (int i = 0; i < att.length; i++) {
            assertTrue(entries.contains(new HashMap.SimpleEntry<>(i, att[i])));
        }
    }
    
    @Test
    public void testGet() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        assertNull(instance.get(3));
        assertNull(instance.get("key"));
    }
    
    @Test
    public void testIsEmpty() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Integer> keys = instance.keySet();
        
        assertEquals(att.length, keys.size());
        for (int i = 0; i < att.length; i++) {
            assertTrue(keys.contains(i));
        }
    }
    
    @Test
    public void testPut() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.put(0, 10.0), 0.0);
        assertEquals(2.0, instance.put(1, 20.0), 0.0);
        assertEquals(3.0, instance.put(2, 30.0), 0.0);
        assertArrayEquals(new double[] {10.0, 20.0, 30.0}, instance.attributes, 0.0);
    }
    
    @Test
    public void testPutAll() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> paramMap = new HashMap<>();
        paramMap.put(0, 10.0);
        paramMap.put(1, 20.0);
        
        instance.putAll(paramMap);
        
        assertArrayEquals(new double[] {10.0, 20.0, 3.0}, instance.attributes, 0.0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(att.length, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(Arrays.asList(1.0, 2.0, 3.0), instance.values());
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(att.length, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        String classValue = "class";
        DenseInstance instance = new DenseInstance(att);
        instance.setClassValue(classValue);
        
        assertEquals("{" + Arrays.toString(att) + ";" + classValue + "}", instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttribute(1);
        
        assertArrayEquals(new double[] {1.0, 3.0}, instance.attributes, 0.0);
    }
    
    @Test
    public void testHashCode() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        double[] attCopy = new double[] {1.0, 2.0, 3.0};
        
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(attCopy);
        
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        double[] attCopy = new double[] {1.0, 2.0, 3.0};
        
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(attCopy);
        DenseInstance instance3 = new DenseInstance(new double[] {1.0, 2.0, 4.0});
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        String classValue = "class";
        DenseInstance instance = new DenseInstance(att);
        instance.setClassValue(classValue);
        
        DenseInstance copy = instance.copy();
        
        assertArrayEquals(att, copy.attributes, 0.0);
        assertEquals(classValue, copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttributes(Set.of(1));
        
        assertArrayEquals(new double[] {1.0, 3.0}, instance.attributes, 0.0);
    }
}
