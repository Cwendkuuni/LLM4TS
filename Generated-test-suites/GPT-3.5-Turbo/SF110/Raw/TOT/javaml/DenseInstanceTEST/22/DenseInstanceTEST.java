import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    private DenseInstance instance;

    @Before
    public void setUp() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes, "classValue");
    }

    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }

    @Test
    public void testClear() {
        instance.clear();
        for (double attribute : instance.attributes) {
            assertEquals(0.0, attribute, 0.0001);
        }
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        for (int i = 0; i < instance.attributes.length; i++) {
            assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(i, instance.attributes[i])));
        }
    }

    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
    }

    @Test
    public void testIsEmptyFalse() {
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = instance.keySet();
        for (int i = 0; i < instance.attributes.length; i++) {
            assertTrue(keySet.contains(i));
        }
    }

    @Test
    public void testPut() {
        assertEquals(1.0, instance.put(0, 5.0), 0.0001);
        assertEquals(2.0, instance.put(1, 6.0), 0.0001);
        assertEquals(3.0, instance.put(2, 7.0), 0.0001);
        assertEquals(5.0, instance.attributes[0], 0.0001);
        assertEquals(6.0, instance.attributes[1], 0.0001);
        assertEquals(7.0, instance.attributes[2], 0.0001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 5.0);
        map.put(1, 6.0);
        map.put(2, 7.0);
        instance.putAll(map);
        assertEquals(5.0, instance.attributes[0], 0.0001);
        assertEquals(6.0, instance.attributes[1], 0.0001);
        assertEquals(7.0, instance.attributes[2], 0.0001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveUnsupported() {
        instance.remove(0);
    }

    @Test
    public void testSize() {
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        Collection<Double> values = instance.values();
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
        assertFalse(values.contains(0.0));
        assertFalse(values.contains(4.0));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        assertEquals("{[1.0, 2.0, 3.0];classValue}", instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(1);
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0001);
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 31 * 1 + Arrays.hashCode(instance.attributes);
        assertEquals(expectedHashCode, instance.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance sameInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "classValue");
        assertEquals(instance, instance);
        assertEquals(instance, sameInstance);
        assertNotEquals(instance, new Object());
    }

    @Test
    public void testCopy() {
        DenseInstance copy = instance.copy();
        assertNotSame(instance, copy);
        assertArrayEquals(instance.attributes, copy.attributes, 0.0001);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        assertArrayEquals(new double[]{2.0}, instance.attributes, 0.0001);
    }
}