
package net.sf.javaml.core;

import org.junit.Test;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DenseInstanceTest {
    
    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertThat(instance.value(0), is(equalTo(1.0)));
        assertThat(instance.value(1), is(equalTo(2.0)));
        assertThat(instance.value(2), is(equalTo(3.0)));
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        assertThat(instance.value(0), is(equalTo(0.0)));
        assertThat(instance.value(1), is(equalTo(0.0)));
        assertThat(instance.value(2), is(equalTo(0.0)));
    }
    
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertThat(instance.containsKey(0), is(equalTo(true)));
        assertThat(instance.containsKey(1), is(equalTo(true)));
        assertThat(instance.containsKey(2), is(equalTo(true)));
        assertThat(instance.containsKey(3), is(equalTo(false)));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertThat(instance.containsValue(1.0), is(equalTo(true)));
        assertThat(instance.containsValue(2.0), is(equalTo(true)));
        assertThat(instance.containsValue(3.0), is(equalTo(true)));
        assertThat(instance.containsValue(4.0), is(equalTo(false)));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertThat(entries.size(), is(equalTo(3)));
        for (Map.Entry<Integer, Double> entry : entries) {
            int key = entry.getKey();
            assertThat(att[key], is(equalTo(entry.getValue())));
        }
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertThat(instance.get(0), is(equalTo(1.0)));
        assertThat(instance.get(1), is(equalTo(2.0)));
        assertThat(instance.get(2), is(equalTo(3.0)));
    }
    
    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertThat(instance.isEmpty(), is(equalTo(false)));
        instance.clear();
        assertThat(instance.isEmpty(), is(equalTo(true)));
    }
    
    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> keys = instance.keySet();
        assertThat(keys.size(), is(equalTo(3)));
        for (Integer key : keys) {
            assertThat(att[key], is(equalTo(instance.get(key))));
        }
    }
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertThat(instance.put(0, 4.0), is(equalTo(1.0)));
        assertThat(instance.put(1, 5.0), is(equalTo(2.0)));
        assertThat(instance.put(2, 6.0), is(equalTo(3.0)));
        assertThat(instance.value(0), is(equalTo(4.0)));
        assertThat(instance.value(1), is(equalTo(5.0)));
        assertThat(instance.value(2), is(equalTo(6.0)));
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
        assertThat(instance.value(0), is(equalTo(4.0)));
        assertThat(instance.value(1), is(equalTo(5.0)));
        assertThat(instance.value(2), is(equalTo(6.0)));
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
        assertThat(instance.size(), is(equalTo(3)));
    }
    
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertThat(instance.values(), containsInAnyOrder(1.0, 2.0, 3.0));
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertThat(instance.noAttributes(), is(equalTo(3)));
    }
    
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        String expected = "{" + Arrays.toString(att) + ";" + instance.classValue() + "}";
        assertThat(instance.toString(), is(equalTo(expected)));
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(0);
        assertThat(instance.size(), is(equalTo(2)));
        assertThat(instance.value(0), is(equalTo(2.0)));
        assertThat(instance.value(1), is(equalTo(3.0)));
    }
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);
        assertThat(instance1.hashCode(), is(equalTo(instance2.hashCode())));
    }
    
    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        double[] att3 = {4.0, 5.0, 6.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = (DenseInstance)instance1.copy();
        assertThat(instance1, is(equalTo(instance2)));
        assertThat(instance1.attributes, is(not(sameInstance(instance2.attributes))));
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(2);
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttributes(indices);
        assertThat(instance.size(), is(equalTo(1)));
        assertThat(instance.value(0), is(equalTo(2.0)));
    }
    
}
