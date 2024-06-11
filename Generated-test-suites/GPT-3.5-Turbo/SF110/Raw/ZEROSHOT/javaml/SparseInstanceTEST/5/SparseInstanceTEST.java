import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
    public void testDefaultConstructor() {
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testConstructorWithNoAttributes() {
        SparseInstance newInstance = new SparseInstance(4);
        assertEquals(4, newInstance.noAttributes());
    }

    @Test
    public void testConstructorWithNoAttributesAndDefaultValue() {
        SparseInstance newInstance = new SparseInstance(4, 0.5);
        assertEquals(4, newInstance.noAttributes());
        assertEquals(0.5, newInstance.defaultValue, 0.001);
    }

    @Test
    public void testConstructorWithNoAttributesAndClassValue() {
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(4, classValue);
        assertEquals(4, newInstance.noAttributes());
        assertSame(classValue, newInstance.classValue());
    }

    @Test
    public void testConstructorWithNoAttributesDefaultValueAndClassValue() {
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(4, 0.5, classValue);
        assertEquals(4, newInstance.noAttributes());
        assertEquals(0.5, newInstance.defaultValue, 0.001);
    }

    @Test
    public void testConstructorWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance newInstance = new SparseInstance(datavector);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(1.0, newInstance.value(0), 0.001);
        assertEquals(2.0, newInstance.value(1), 0.001);
        assertEquals(3.0, newInstance.value(2), 0.001);
    }

    @Test
    public void testConstructorWithDataVectorAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance newInstance = new SparseInstance(datavector, 0.5);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(1.0, newInstance.value(0), 0.001);
        assertEquals(2.0, newInstance.value(1), 0.001);
        assertEquals(3.0, newInstance.value(2), 0.001);
        assertEquals(0.5, newInstance.defaultValue, 0.001);
    }

    @Test
    public void testConstructorWithDataVectorAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(datavector, classValue);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(1.0, newInstance.value(0), 0.001);
        assertEquals(2.0, newInstance.value(1), 0.001);
        assertEquals(3.0, newInstance.value(2), 0.001);
        assertSame(classValue, newInstance.classValue());
    }

    @Test
    public void testConstructorWithDataVectorDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(datavector, 0.5, classValue);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(1.0, newInstance.value(0), 0.001);
        assertEquals(2.0, newInstance.value(1), 0.001);
        assertEquals(3.0, newInstance.value(2), 0.001);
        assertEquals(0.5, newInstance.defaultValue, 0.001);
        assertSame(classValue, newInstance.classValue());
    }

    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 0.0, 3.0};
        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testValue() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        double result = instance.value(1);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(0));
    }

    @Test
    public void testContainsValue() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(2.0));
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
        instance.put(1, 2.0);
        Double result = instance.get(1);
        assertEquals(2.0, result, 0.001);
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
        Double result = instance.put(0, 1.0);
        assertNull(result);
    }

    @Test
    public void testPutAll() {
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(2, instance.size());
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        Double result = instance.remove(0);
        assertEquals(1.0, result, 0.001);
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
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
        instance.clear();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(3.0, instance.value(1), 0.001);
        assertFalse(instance.containsKey(2));
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testToString() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.setClassValue(new Object());
        String result = instance.toString();
        assertTrue(result.contains("{0=1.0;"));
        assertTrue(result.contains(";classValue"));
    }

    @Test
    public void testHashCode() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        int result = instance.hashCode();
        assertNotEquals(0, result);
    }

    @Test
    public void testEquals() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
        assertFalse(instance.equals(new SparseInstance()));
        assertFalse(instance.equals(new SparseInstance(0)));
        assertFalse(instance.equals(new SparseInstance(2)));
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.setClassValue(new Object());
        Instance copy = instance.copy();
        assertTrue(copy instanceof SparseInstance);
        assertNotSame(copy, instance);
        assertEquals(instance.toString(), copy.toString());
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttributes(new HashSet<>(Arrays.asList(0, 2)));
        assertFalse(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
        assertEquals(1, instance.noAttributes());
    }

}