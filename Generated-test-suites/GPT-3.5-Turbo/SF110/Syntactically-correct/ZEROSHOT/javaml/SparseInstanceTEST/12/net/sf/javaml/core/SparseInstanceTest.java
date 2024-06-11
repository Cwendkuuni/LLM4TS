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
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        int noAttributes = instance.noAttributes();
        assertEquals(-1, noAttributes);
    }
    
    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(3);
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
    }
    
    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(3, 0.5);
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
    }
    
    @Test
    public void testSparseInstanceWithNoAttributesAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(3, classValue);
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testSparseInstanceWithAllParameters() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(3, 0.5, classValue);
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }
    
    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.5);
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }
    
    @Test
    public void testSparseInstanceWithDataVectorAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testSparseInstanceWithDataVectorAndAllParameters() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.5, classValue);
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testInitiate() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }
    
    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        double value = instance.value(1);
        assertEquals(2.0, value, 0.001);
    }
    
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertEquals(3, instance.entrySet().size());
    }
    
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(0.0, instance.get(3), 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertEquals(3, instance.keySet().size());
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        Double oldValue = instance.put(0, 1.0);
        assertNull(oldValue);
        assertEquals(1.0, instance.value(0), 0.001);
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(2, instance.size());
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        Double removedValue = instance.remove(0);
        assertEquals(1.0, removedValue, 0.001);
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        int size = instance.size();
        assertEquals(2, size);
    }
    
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertEquals(3, instance.values().size());
    }
    
    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
    }
    
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        instance.removeAttribute(1);
        int noAttributes = instance.noAttributes();
        assertEquals(2, noAttributes);
        assertNull(instance.get(1));
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        String str = instance.toString();
        assertEquals("{0=1.0;1=2.0;2=3.0;null}", str);
    }
    
    @Test
    public void testHashCode() {
        SparseInstance instance = new SparseInstance();
        assertEquals(961, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        assertTrue(instance1.equals(instance2));
        
        double[] datavector = {1.0, 2.0, 3.0};
        instance1.initiate(datavector);
        assertFalse(instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.setClassValue(new Object());
        
        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(instance, copy);
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.put(3, 4.0);
        
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        
        instance.removeAttributes(indices);
        
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(2));
    }
}