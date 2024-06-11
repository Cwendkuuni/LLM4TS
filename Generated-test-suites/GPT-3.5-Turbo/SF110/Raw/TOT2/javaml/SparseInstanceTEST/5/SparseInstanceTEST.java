import static org.junit.Assert.*;
import org.junit.Test;

public class SparseInstanceTest {
    
    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstanceDefaultConstructor() {
        SparseInstance instance = new SparseInstance();
        assertNotNull(instance);
    }
    
    @Test
    public void testSparseInstanceConstructorWithDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 0.5);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.5, instance.get(0), 0.0001);
    }
    
    // Edge case: classValue is null
    @Test
    public void testSparseInstanceConstructorWithNullClassValue() {
        SparseInstance instance = new SparseInstance(5, 0.0, null);
        assertEquals(5, instance.noAttributes());
        assertNull(instance.classValue());
    }
    
    // Edge case: datavector is empty
    @Test
    public void testSparseInstanceConstructorWithEmptyDatavector() {
        double[] datavector = {};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "class");
        assertEquals(0, instance.size());
    }
    
    // Exception handling: trying to get a non-existing value
    @Test(expected = NullPointerException.class)
    public void testValueNonExistingKey() {
        SparseInstance instance = new SparseInstance();
        instance.value(2);
    }
    
    // Typical use case
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    // Exception handling: key is not an Integer
    @Test
    public void testContainsKeyWithNonIntegerKey() {
        SparseInstance instance = new SparseInstance();
        assertFalse(instance.containsKey("key"));
    }
    
    // Edge case: empty instance
    @Test
    public void testContainsValueWithEmptyInstance() {
        SparseInstance instance = new SparseInstance();
        assertFalse(instance.containsValue(3.0));
    }
    
    // Typical use case
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.entrySet().size());
    }
    
    @Test
    public void testGetValue() {
        SparseInstance instance = new SparseInstance();
        assertNull(instance.get(1));
    }
    
    // Edge case: empty instance
    @Test
    public void testIsEmptyWithEmptyInstance() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.keySet().isEmpty());
    }
    
    // Exception handling: key is not an Integer
    @Test(expected = ClassCastException.class)
    public void testPutWithNonIntegerKey() {
        SparseInstance instance = new SparseInstance();
        instance.put("key", 2.5);
    }
    
    @Test
    public void testPutAll() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 2.0);
        instance1.putAll(instance2);
        assertEquals(1, instance1.size());
    }
    
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        assertEquals(2.0, instance.remove(1), 0.0001);
    }
    
    // Exception handling: attempting to remove a non-existing key
    @Test(expected = NullPointerException.class)
    public void testRemoveNonExistingKey() {
        SparseInstance instance = new SparseInstance();
        instance.remove(5);
    }
    
    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.size());
    }
    
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.values().isEmpty());
    }
    
    // Edge case: instance with no attributes
    @Test
    public void testNoAttributesWithNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
    }
    
    // Exception handling: trying to remove a non-existing attribute
    @Test(expected = NullPointerException.class)
    public void testRemoveAttributeNonExisting() {
        SparseInstance instance = new SparseInstance();
        instance.removeAttribute(5);
    }
    
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        assertEquals("{},null}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        SparseInstance instance = new SparseInstance();
        assertEquals(31, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(instance, copy);
    }
    
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 2.0);
        instance.put(2, 5.0);
        instance.removeAttributes(new HashSet<>(Arrays.asList(1, 2)));
        assertTrue(instance.isEmpty());
    }
}