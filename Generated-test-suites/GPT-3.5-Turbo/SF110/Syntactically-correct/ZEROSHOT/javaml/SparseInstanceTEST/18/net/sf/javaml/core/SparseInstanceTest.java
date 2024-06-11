package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstance1() {
        SparseInstance instance = new SparseInstance(5);
        assertTrue(instance.isEmpty());
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstance2() {
        SparseInstance instance = new SparseInstance(5, 0.5);
        assertTrue(instance.isEmpty());
        assertEquals(5, instance.noAttributes());
        assertEquals(0.5, instance.value(0), 0.0001);
    }
    
    @Test
    public void testSparseInstance3() {
        SparseInstance instance = new SparseInstance(5, "classValue");
        assertTrue(instance.isEmpty());
        assertEquals(5, instance.noAttributes());
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testSparseInstance4() {
        SparseInstance instance = new SparseInstance(5, 0.5, "classValue");
        assertTrue(instance.isEmpty());
        assertEquals(5, instance.noAttributes());
        assertEquals(0.5, instance.value(0), 0.0001);
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testSparseInstance5() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testSparseInstance6() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.5);
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
        assertEquals(0.5, instance.value(3), 0.0001);
    }
    
    @Test
    public void testSparseInstance7() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, "classValue");
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testSparseInstance8() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.5, "classValue");
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
        assertEquals(0.5, instance.value(3), 0.0001);
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testInitiate() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }
    
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }
    
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.entrySet().size());
        assertTrue(instance.entrySet().contains(instance.data.entrySet()));
    }
    
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(0.0, instance.get(2), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.keySet().size());
        assertTrue(instance.keySet().contains(0));
        assertTrue(instance.keySet().contains(1));
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        HashMap<Integer, Double> m = new HashMap<Integer, Double>();
        m.put(0, 1.0);
        m.put(1, 2.0);
        instance.putAll(m);
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.remove(0);
        assertFalse(instance.containsKey(0));
    }
    
    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.size());
    }
    
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.values().size());
        assertTrue(instance.values().contains(1.0));
        assertTrue(instance.values().contains(2.0));
    }
    
    @Test
    public void testNoAttributes1() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }
    
    @Test
    public void testNoAttributes2() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(0);
        assertFalse(instance.containsKey(0));
        assertEquals(1.0, instance.value(1), 0.0001);
        assertEquals(1, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals("{0=1.0;null}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1762646349, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);
        
        SparseInstance instance3 = new SparseInstance();
        instance3.put(0, 1.5);
        instance3.put(1, 2.0);
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        instance1.setClassValue("classValue");
        
        SparseInstance instance2 = (SparseInstance) instance1.copy();
        
        assertEquals(instance1, instance2);
        assertNotSame(instance1, instance2);
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.put(3, 4.0);
        
        HashSet<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        indices.add(3);
        
        instance.removeAttributes(indices);
        
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(3.0, instance.value(1), 0.0001);
        assertEquals(1, instance.noAttributes());
    }

}