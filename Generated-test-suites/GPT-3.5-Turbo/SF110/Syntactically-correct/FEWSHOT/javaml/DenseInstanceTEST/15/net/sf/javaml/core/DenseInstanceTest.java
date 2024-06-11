package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class DenseInstanceTest {
    
    private final double[] attributes = {1.0, 2.0, 3.0};
    private final Object classValue = "class";

    @Test
    public void testConstructorWithAttributes() {
        DenseInstance instance = new DenseInstance(attributes);
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testConstructorWithAttributesAndClassValue() {
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, instance.attributes, 0.0);
    }
    
    @Test
    public void testContainsKeyWithValidKey() {
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
    }
    
    @Test
    public void testContainsKeyWithInvalidKey() {
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsKeyWithNonIntegerObject() {
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.containsKey("key"));
    }
    
    @Test
    public void testContainsValueWithValidValue() {
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(2.0));
    }
    
    @Test
    public void testContainsValueWithInvalidValue() {
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testContainsValueWithNonNumberObject() {
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.containsValue("value"));
    }
    
    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        assertEquals(0, entrySet.iterator().next().getKey().intValue());
        assertEquals(attributes[0], entrySet.iterator().next().getValue(), 0.0);
    }
    
    @Test
    public void testGetWithValidKey() {
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(attributes[0], instance.get(0), 0.0);
    }
    
    @Test
    public void testGetWithInvalidKey() {
        DenseInstance instance = new DenseInstance(attributes);
        assertNull(instance.get(3));
    }
    
    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        assertEquals(0, keySet.first().intValue());
        assertEquals(2, keySet.last().intValue());
    }
    
    @Test
    public void testPut() {
        DenseInstance instance = new DenseInstance(attributes);
        Double previousValue = instance.put(0, 5.0);
        assertEquals(attributes[0], previousValue, 0.0);
        assertEquals(5.0, instance.value(0), 0.0);
    }
    
    @Test
    public void testPutAll() {
        DenseInstance instance = new DenseInstance(attributes);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 5.0);
        map.put(1, 6.0);
        instance.putAll(map);
        assertEquals(5.0, instance.value(0), 0.0);
        assertEquals(6.0, instance.value(1), 0.0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(0);
    }
    
    @Test
    public void testNoAttributes() {
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        DenseInstance instance = new DenseInstance(attributes);
        String expectedString = "{" + Arrays.toString(attributes) + ";" + instance.classValue() + "}";
        assertEquals(expectedString, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0);
    }
    
    @Test
    public void testHashCode() {
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance instanceSameAttributes = new DenseInstance(attributes);
        assertEquals(instance.hashCode(), instanceSameAttributes.hashCode());
    }
    
    @Test
    public void testEqualsWithSameInstance() {
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.equals(instance));
    }
    
    @Test
    public void testEqualsWithNull() {
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.equals(new Object()));
    }
    
    @Test
    public void testEqualsWithDifferentAttributes() {
        DenseInstance instance = new DenseInstance(attributes);
        double[] differentAttributes = {4.0, 5.0, 6.0};
        DenseInstance instanceDifferentAttributes = new DenseInstance(differentAttributes);
        assertFalse(instance.equals(instanceDifferentAttributes));
    }
    
    @Test
    public void testCopy() {
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance copy = (DenseInstance) instance.copy();
        assertArrayEquals(attributes, copy.attributes, 0.0);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indices = new TreeSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        assertArrayEquals(new double[]{2.0}, instance.attributes, 0.0);
    }
}