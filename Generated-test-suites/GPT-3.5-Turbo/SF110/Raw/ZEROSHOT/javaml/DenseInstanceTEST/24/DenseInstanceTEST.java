package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class DenseInstanceTest {

    private DenseInstance instance;

    @Before
    public void setUp() {
        double[] att = {1.0, 2.0, 3.0};
        instance = new DenseInstance(att);
    }

    @Test
    public void testValue() {
        Assert.assertEquals(1.0, instance.value(0), 0.0001);
        Assert.assertEquals(2.0, instance.value(1), 0.0001);
        Assert.assertEquals(3.0, instance.value(2), 0.0001);
    }

    @Test
    public void testClear() {
        instance.clear();
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(0.0, instance.value(i), 0.0001);
        }
    }

    @Test
    public void testContainsKey() {
        Assert.assertTrue(instance.containsKey(0));
        Assert.assertTrue(instance.containsKey(1));
        Assert.assertTrue(instance.containsKey(2));
        Assert.assertFalse(instance.containsKey(3));
        Assert.assertFalse(instance.containsKey("test"));
    }

    @Test
    public void testContainsValue() {
        Assert.assertTrue(instance.containsValue(1.0));
        Assert.assertTrue(instance.containsValue(2.0));
        Assert.assertTrue(instance.containsValue(3.0));
        Assert.assertFalse(instance.containsValue(4.0));
        Assert.assertFalse(instance.containsValue("test"));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        Assert.assertEquals(3, entries.size());
        for (Map.Entry<Integer, Double> entry : entries) {
            int key = entry.getKey();
            double value = entry.getValue();
            Assert.assertTrue(key >= 0 && key < 3);
            Assert.assertEquals(instance.value(key), value, 0.0001);
        }
    }
    
    @Test
    public void testGet() {
        Assert.assertEquals(1.0, instance.get(0), 0.0001);
        Assert.assertEquals(2.0, instance.get(1), 0.0001);
        Assert.assertEquals(3.0, instance.get(2), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        Assert.assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SortedSet<Integer> keys = instance.keySet();
        Assert.assertEquals(3, keys.size());
        int i = 0;
        for (int key : keys) {
            Assert.assertEquals(i++, key);
        }
    }
    
    @Test
    public void testPut() {
        Assert.assertEquals(1.0, instance.put(0, 4.0), 0.0001);
        Assert.assertEquals(2.0, instance.put(1, 5.0), 0.0001);
        Assert.assertEquals(3.0, instance.put(2, 6.0), 0.0001);
        Assert.assertEquals(4.0, instance.value(0), 0.0001);
        Assert.assertEquals(5.0, instance.value(1), 0.0001);
        Assert.assertEquals(6.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        instance.putAll(map);
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals((double)i + 4.0, instance.value(i), 0.0001);
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        Assert.assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        Collection<Double> values = instance.values();
        Assert.assertEquals(3, values.size());
        int i = 1;
        for (double value : values) {
            Assert.assertEquals((double)i++, value, 0.0001);
        }
    }
    
    @Test
    public void testNoAttributes() {
        Assert.assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0];null}";
        Assert.assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(1);
        Assert.assertEquals(2, instance.size());
        Assert.assertEquals(1.0, instance.value(0), 0.0001);
        Assert.assertEquals(3.0, instance.value(1), 0.0001);
    }
    
    @Test
    public void testHashCode() {
        int expected = Arrays.hashCode(new double[]{1.0, 2.0, 3.0});
        Assert.assertEquals(expected, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance equalInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Assert.assertTrue(instance.equals(equalInstance));
        
        DenseInstance unequalInstance = new DenseInstance(new double[]{1.0, 2.0, 4.0});
        Assert.assertFalse(instance.equals(unequalInstance));
        
        Assert.assertFalse(instance.equals(null));
        Assert.assertFalse(instance.equals("test"));
    }
    
    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance)instance.copy();
        Assert.assertArrayEquals(instance.attributes, copy.attributes, 0.0001);
        Assert.assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        Assert.assertEquals(2, instance.size());
        Assert.assertEquals(1.0, instance.value(0), 0.0001);
        Assert.assertEquals(3.0, instance.value(1), 0.0001);
    }
}