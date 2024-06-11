import net.sf.javaml.core.SparseInstance;
import org.junit.*;
import static org.junit.Assert.*;

public class SparseInstanceTest {
    
    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(10);
        assertEquals(10, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        assertNotNull(instance);
        assertEquals(-1, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstance1() {
        SparseInstance instance = new SparseInstance(10);
        assertNotNull(instance);
        assertEquals(10, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSparseInstance2() {
        SparseInstance instance = new SparseInstance(10, 5.0);
        assertNotNull(instance);
        assertEquals(10, instance.noAttributes());
        assertEquals(5.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSparseInstance3() {
        SparseInstance instance = new SparseInstance(10, "class");
        assertNotNull(instance);
        assertEquals(10, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals("class", instance.classValue());
    }
    
    @Test
    public void testSparseInstance4() {
        SparseInstance instance = new SparseInstance(10, 5.0, "class");
        assertNotNull(instance);
        assertEquals(10, instance.noAttributes());
        assertEquals(5.0, instance.defaultValue, 0.001);
        assertEquals("class", instance.classValue());
    }
    
    @Test
    public void testSparseInstance5() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertNotNull(instance);
        assertEquals(4, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSparseInstance6() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector, 5.0);
        assertNotNull(instance);
        assertEquals(4, instance.noAttributes());
        assertEquals(5.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSparseInstance7() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector, "class");
        assertNotNull(instance);
        assertEquals(4, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals("class", instance.classValue());
    }
    
    @Test
    public void testSparseInstance8() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector, 5.0, "class");
        assertNotNull(instance);
        assertEquals(4, instance.noAttributes());
        assertEquals(5.0, instance.defaultValue, 0.001);
        assertEquals("class", instance.classValue());
    }
    
    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.value(0), 0.001);
    }
    
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.clear();
        assertEquals(true, instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(true, instance.containsKey(0));
        assertEquals(false, instance.containsKey(1));
    }
    
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(true, instance.containsValue(1.0));
        assertEquals(false, instance.containsValue(2.0));
    }
    
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.entrySet().size());
    }
    
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(0.0, instance.get(1), 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertEquals(true, instance.isEmpty());
        instance.put(0, 1.0);
        assertEquals(false, instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.keySet().size());
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.001);
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.remove(0);
        assertEquals(false, instance.containsKey(0));
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
    }
    
    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
        instance.remove(0);
        assertEquals(0, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.removeAttribute(0);
        assertEquals(0, instance.size());
        assertEquals(0, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals("{0=1.0;null}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        int hash = instance.hashCode();
        assertEquals(hash, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        assertEquals(true, instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        SparseInstance instance2 = (SparseInstance) instance1.copy();
        assertEquals(true, instance1.equals(instance2));
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttributes(instance.keySet());
        assertEquals(0, instance.size());
        assertEquals(0, instance.noAttributes());
    }
}