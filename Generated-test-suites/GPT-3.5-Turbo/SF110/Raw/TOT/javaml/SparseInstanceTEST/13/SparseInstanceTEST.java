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
    public void testConstructorWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.value(0), 0.001);
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testConstructorWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.001);
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testConstructorWithNoAttributesAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testConstructorWithNoAttributesAndDefaultAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 1.0, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testConstructorWithDoubleArray() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testConstructorWithDoubleArrayAndDefaultValue() {
        double[] dataVector = {1.0, 0.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector, 0.0);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.001);
        assertFalse(instance.containsKey(1));
        assertEquals(3.0, instance.value(2), 0.001);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] dataVector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(dataVector, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testConstructorWithDoubleArrayAndDefaultAndClassValue() {
        double[] dataVector = {1.0, 0.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(dataVector, 0.0, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testValue() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }
    
    @Test
    public void testClear() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(3, instance.entrySet().size());
    }
    
    @Test
    public void testGet() {
        double[] dataVector = {1.0, 0.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(0.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3), 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(3, instance.keySet().size());
    }
    
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        assertNull(instance.put(0, 1.0));
        assertEquals(1.0, instance.get(0), 0.001);
        assertNull(instance.put(0, 2.0));
        assertEquals(2.0, instance.get(0), 0.001);
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
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertNull(instance.remove(3));
        assertEquals(3.0, instance.remove(2), 0.001);
        assertFalse(instance.containsKey(2));
    }
    
    @Test
    public void testSize() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(3, instance.size());
        instance.remove(2);
        assertEquals(2, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(3, instance.values().size());
    }
    
    @Test
    public void testNoAttributes() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(3.0, instance.get(1), 0.001);
        assertFalse(instance.containsKey(2));
    }
    
    @Test
    public void testToString() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals("{0=1.0;null}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(1898011466, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(dataVector);
        SparseInstance instance2 = new SparseInstance(dataVector);
        SparseInstance instance3 = new SparseInstance();
        assertEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
    }
    
    @Test
    public void testCopy() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertNotNull(copy);
        assertArrayEquals(instance.values().toArray(), copy.values().toArray());
        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(1, instance.noAttributes());
        assertEquals(2.0, instance.get(0), 0.001);
    }
}