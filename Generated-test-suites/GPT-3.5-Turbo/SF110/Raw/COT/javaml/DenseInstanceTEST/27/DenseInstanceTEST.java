import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testDenseInstance() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertNotNull(instance);
        assertArrayEquals(att, instance.attributes, 0.0);
    }
    
    @Test
    public void testDenseInstanceWithClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class value";
        DenseInstance instance = new DenseInstance(att, classValue);
        
        assertNotNull(instance);
        assertArrayEquaks(att, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        
        assertArrayEquals(new double[att.length], instance.attributes, 0.0);
    }
    
    // Add test cases for remaining methods following the same pattern
    
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
        
        assertEquals(att.length, entrySet.size());
        for (int i = 0; i < att.length; i++) {
            assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(i, att[i])));
        }
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(att[0], instance.get(0), 0.0);
        assertEquals(att[1], instance.get(1), 0.0);
        assertEquals(att[2], instance.get(2), 0.0);
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
        
        double newValue = 10.0;
        double oldValue = instance.put(0, newValue);
        
        assertEquals(att[0], oldValue, 0.0);
        assertEquals(newValue, instance.attributes[0], 0.0);
    }
    
    // Add test cases for remaining methods following the same pattern
    
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
        for (int i = 0; i < att.length; i++) {
            assertTrue(values.contains(att[i]));
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
        Object classValue = "class value";
        DenseInstance instance = new DenseInstance(att, classValue);
        
        assertEquals("{" + Arrays.toString(att) + ";" + classValue + "}", instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttribute(0);
        
        assertEquals(att.length - 1, instance.attributes.length);
        assertEquals(att[1], instance.attributes[0], 0.0);
        assertEquals(att[2], instance.attributes[1], 0.0);
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
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        double[] att3 = {1.0, 2.0, 4.0};
        
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class value";
        DenseInstance instance1 = new DenseInstance(att, classValue);
        DenseInstance instance2 = (DenseInstance) instance1.copy();
        
        assertNotSame(instance1, instance2);
        assertArrayEquals(instance1.attributes, instance2.attributes, 0.0);
        assertEquals(instance1.classValue(), instance2.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(2);
        
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttributes(indices);
        
        assertEquals(att.length - indices.size(), instance.attributes.length);
        assertEquals(att[1], instance.attributes[0], 0.0);
    }

}