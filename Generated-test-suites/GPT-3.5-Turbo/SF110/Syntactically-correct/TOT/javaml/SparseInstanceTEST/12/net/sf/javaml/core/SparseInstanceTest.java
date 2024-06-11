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
    public void testConstructors() {
        SparseInstance instance1 = new SparseInstance();
        assertEquals(-1, instance1.noAttributes());
        
        SparseInstance instance2 = new SparseInstance(5);
        assertEquals(5, instance2.noAttributes());
        
        SparseInstance instance3 = new SparseInstance(5, 2.0);
        assertEquals(5, instance3.noAttributes());
        assertEquals(2.0, instance3.get(0), 0.001);
        
        Object classValue = new Object();
        SparseInstance instance4 = new SparseInstance(5, classValue);
        assertEquals(5, instance4.noAttributes());
        assertEquals(classValue, instance4.classValue());
        
        SparseInstance instance5 = new SparseInstance(5, 3.0, classValue);
        assertEquals(5, instance5.noAttributes());
        assertEquals(3.0, instance5.get(0), 0.001);
        assertEquals(classValue, instance5.classValue());
    }
    
    @Test
    public void testInitiate() {
        double[] datavector1 = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance();
        instance1.initiate(datavector1);
        assertEquals(3, instance1.noAttributes());
        
        double[] datavector2 = {0.0, 0.0, 0.0};
        SparseInstance instance2 = new SparseInstance();
        instance2.initiate(datavector2);
        assertEquals(0, instance2.noAttributes());
        
        double[] datavector3 = {0.0, 1.0, 2.0};
        SparseInstance instance3 = new SparseInstance();
        instance3.initiate(datavector3);
        assertEquals(2, instance3.noAttributes());
        assertFalse(instance3.containsKey(0));
        assertTrue(instance3.containsKey(1));
        assertTrue(instance3.containsKey(2));
        assertEquals(1.0, instance3.get(1), 0.001);
        assertEquals(2.0, instance3.get(2), 0.001);
          
        double[] datavector4 = {1.0, 0.0, 3.0, 0.0};
        SparseInstance instance4 = new SparseInstance();
        instance4.initiate(datavector4);
        assertEquals(3, instance4.noAttributes());
        assertFalse(instance4.containsKey(1));
        assertTrue(instance4.containsKey(0));
        assertTrue(instance4.containsKey(2));
        assertEquals(1.0, instance4.get(0), 0.001);
        assertEquals(3.0, instance4.get(2), 0.001);
    }
    
    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        int pos = 2;
        instance.put(pos, 3.0);
        assertEquals(3.0, instance.value(pos), 0.001);
    }
    
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }
    
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertEquals(2, instance.entrySet().size());
    }
    
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertEquals(1.0, instance.get(1), 0.001);
        assertEquals(2.0, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3), 0.001); // Default value
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance1 = new SparseInstance();
        assertTrue(instance1.isEmpty());
        
        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 1.0);
        assertFalse(instance2.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertEquals(2, instance.keySet().size());
        assertTrue(instance.keySet().contains(1));
        assertTrue(instance.keySet().contains(2));
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        assertEquals(1.0, instance.get(1), 0.001);
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 1.0);
        instance1.put(2, 2.0);
        
        SparseInstance instance2 = new SparseInstance();
        instance2.putAll(instance1);
        assertEquals(2, instance2.size());
        assertEquals(1.0, instance2.get(1), 0.001);
        assertEquals(2.0, instance2.get(2), 0.001);
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.remove(1);
        assertFalse(instance.containsKey(1));
    }
    
    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertEquals(2, instance.size());
    }
    
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertEquals(2, instance.values().size());
        assertTrue(instance.values().contains(1.0));
        assertTrue(instance.values().contains(2.0));
    }
    
    @Test
    public void testnoAttributes() {
        SparseInstance instance1 = new SparseInstance();
        assertEquals(0, instance1.noAttributes());
        
        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 1.0);
        assertEquals(2, instance2.noAttributes());
        
        SparseInstance instance3 = new SparseInstance();
        instance3.put(2, 3.0);
        assertEquals(3, instance3.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.removeAttribute(1);
        assertFalse(instance.containsKey(1));
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        String expected = "{1=1.0;null}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        assertEquals(instance1.hashCode(), instance2.hashCode());
        
        instance1.put(1, 1.0);
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
        
        instance2.put(1, 1.0);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        assertTrue(instance1.equals(instance2));
        
        instance1.put(1, 1.0);
        assertFalse(instance1.equals(instance2));
        
        instance2.put(1, 1.0);
        assertTrue(instance1.equals(instance2));
        
        Object obj = new Object();
        assertFalse(instance1.equals(obj));
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 1.0);
        
        SparseInstance instance2 = (SparseInstance) instance1.copy();
        assertTrue(instance1.equals(instance2));
        assertNotSame(instance1, instance2);
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.put(3, 3.0);
        
        instance.removeAttributes(Set.of(1, 3));
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
        assertEquals(1, instance.noAttributes());
    }
}