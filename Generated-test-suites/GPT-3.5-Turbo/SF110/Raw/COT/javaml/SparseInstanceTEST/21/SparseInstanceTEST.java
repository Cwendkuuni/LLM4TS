import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SparseInstanceTest {

    private SparseInstance instance;

    @Before
    public void setUp() {
        instance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithoutAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.get(0), 0.0);
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDatavector() {
        double[] datavector = {1.0, 2.0, 0.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(4, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(3), 0.0);
    }

    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testContainsValue() {
        instance.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.entrySet().size());
    }

    @Test
    public void testGet() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(0.0, instance.get(1), 0.0);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.keySet().size());
    }

    @Test
    public void testPut() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.0);
    }

    @Test
    public void testPutAll() {
        SparseInstance other = new SparseInstance();
        other.put(0, 1.0);
        other.put(1, 2.0);

        instance.putAll(other);
        assertEquals(2, instance.size());
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        instance.remove(0);
        assertFalse(instance.containsKey(0));
    }

    @Test
    public void testSize() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.size());
    }

    @Test
    public void testValues() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.values().size());
    }

    @Test
    public void testNoAttributes() {
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(0);
        assertEquals(1, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.0);
    }

    @Test
    public void testToString() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertNotNull(instance.toString());
    }

    @Test
    public void testHashCode() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertNotNull(instance.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance other = new SparseInstance();
        other.put(0, 1.0);
        other.put(1, 2.0);

        assertFalse(instance.equals(other));
        assertFalse(instance.equals(null));

        assertTrue(instance.equals(instance));
        assertTrue(other.equals(other));

        assertTrue(instance.equals(instance.copy()));
        assertTrue(other.equals(other.copy()));
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(instance, copy);
        assertNotSame(instance, copy);
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        instance.removeAttributes(Set.of(0, 1));
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

}