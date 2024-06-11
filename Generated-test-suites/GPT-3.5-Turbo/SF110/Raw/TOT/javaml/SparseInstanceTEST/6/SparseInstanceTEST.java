import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        assertEquals(instance.noAttributes, 5);
    }

    @Test
    public void testSparseInstance() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance(5);
        SparseInstance instance3 = new SparseInstance(5, 0.5);
        SparseInstance instance4 = new SparseInstance(5, "Test");
        SparseInstance instance5 = new SparseInstance(5, 0.5, "Test");
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertNotNull(instance3);
        assertNotNull(instance4);
        assertNotNull(instance5);
    }

    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertEquals(instance.noAttributes, datavector.length);
        assertFalse(instance.isEmpty());
        assertFalse(instance.containsValue(0.0));
        assertTrue(instance.containsKey(1));
    }

    @Test
    public void testValue() {
        instance.put(1, 2.0);
        assertEquals(instance.value(1), 2.0, 0.001);
    }

    @Test
    public void testClear() {
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        instance.put(1, 2.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertNotNull(entrySet);
        assertEquals(entrySet.size(), 1);
        assertEquals(entrySet.iterator().next().getKey(), Integer.valueOf(1));
        assertEquals(entrySet.iterator().next().getValue(), Double.valueOf(2.0));
    }

    @Test
    public void testGet() {
        instance.put(1, 2.0);
        assertEquals(instance.get(1), Double.valueOf(2.0));
        assertEquals(instance.get(2), instance.defaultValue);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        instance.put(1, 2.0);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        instance.put(1, 2.0);
        Set<Integer> keySet = instance.keySet();
        assertNotNull(keySet);
        assertEquals(keySet.size(), 1);
        assertTrue(keySet.contains(1));
    }

    @Test
    public void testPut() {
        assertNull(instance.put(1, 2.0));
        assertEquals(instance.get(1), Double.valueOf(2.0));
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(instance.get(1), Double.valueOf(2.0));
    }

    @Test
    public void testRemove() {
        instance.put(1, 2.0);
        assertEquals(instance.remove(1), Double.valueOf(2.0));
        assertEquals(instance.remove(2), null);
    }

    @Test
    public void testSize() {
        assertEquals(instance.size(), 0);
        instance.put(1, 2.0);
        assertEquals(instance.size(), 1);
    }

    @Test
    public void testValues() {
        instance.put(1, 2.0);
        Set<Double> values = instance.values();
        assertNotNull(values);
        assertEquals(values.size(), 1);
        assertTrue(values.contains(2.0));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(instance.noAttributes(), 0);
        instance.put(1, 2.0);
        assertEquals(instance.noAttributes(), 2);
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertEquals(instance.noAttributes, 1);
        assertFalse(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
    }

    @Test
    public void testToString() {
        instance.put(1, 2.0);
        instance.setClassValue("Test");
        assertEquals(instance.toString(), "{1=2.0;Test}");
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        SparseInstance instance3 = new SparseInstance();
        instance1.put(1, 2.0);
        instance2.put(1, 2.0);
        instance3.put(1, 3.0);
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        SparseInstance instance3 = new SparseInstance();
        instance1.put(1, 2.0);
        instance2.put(1, 2.0);
        instance3.put(1, 3.0);
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        instance.put(1, 2.0);
        instance.setClassValue("Test");
        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(copy.noAttributes, instance.noAttributes);
        assertEquals(copy.classValue(), instance.classValue());
        assertEquals(copy.get(1), instance.get(1));

        // Modify copy and make sure instance values are not affected
        copy.put(1, 3.0);
        copy.setClassValue("Modified");
        assertNotEquals(copy.get(1), instance.get(1));
        assertNotEquals(copy.classValue(), instance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.put(3, 4.0);
        instance.put(4, 5.0);

        Set<Integer> indicesToRemove = Set.of(1, 3);
        instance.removeAttributes(indicesToRemove);

        assertEquals(instance.noAttributes, 2);
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
        assertTrue(instance.containsKey(2));
        assertTrue(instance.containsKey(4));
    }

}