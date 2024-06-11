
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DenseInstanceTest {

    private DenseInstance denseInstance;

    @Before
    public void setUp() {
        double[] attributes = {1.1, 2.2, 3.3};
        Double classValue = 1.0;
        denseInstance = new DenseInstance(attributes, classValue);
    }

    @Test
    public void testValue() {
        assertEquals(1.1, denseInstance.value(0), 0.0001);
        assertEquals(2.2, denseInstance.value(1), 0.0001);
        assertEquals(3.3, denseInstance.value(2), 0.0001);
    }

    @Test
    public void testClear() {
        denseInstance.clear();
        assertTrue(Arrays.equals(new double[3], denseInstance.attributes));
    }

    @Test
    public void testContainsKey() {
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));

        assertFalse(denseInstance.containsKey(-1));
        assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        assertTrue(denseInstance.containsValue(1.1));
        assertTrue(denseInstance.containsValue(2.2));
        assertTrue(denseInstance.containsValue(3.3));

        assertFalse(denseInstance.containsValue(1.0));
        assertFalse(denseInstance.containsValue(2.0));
        assertFalse(denseInstance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();

        assertEquals(3, entrySet.size());

        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();

            assertEquals(denseInstance.value(key), value, 0.0001);
        }
    }

    @Test
    public void testGet() {
        assertEquals(1.1, denseInstance.get(0), 0.0001);
        assertEquals(2.2, denseInstance.get(1), 0.0001);
        assertEquals(3.3, denseInstance.get(2), 0.0001);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        Set<Integer> keySet = denseInstance.keySet();

        assertEquals(3, keySet.size());

        for (int key : keySet) {
            assertTrue(denseInstance.containsKey(key));
        }
    }

    @Test
    public void testPut() {
        assertEquals(1.1, denseInstance.put(0, 4.4), 0.0001);
        assertEquals(4.4, denseInstance.value(0), 0.0001);

        assertEquals(2.2, denseInstance.put(1, 5.5), 0.0001);
        assertEquals(5.5, denseInstance.value(1), 0.0001);

        assertEquals(3.3, denseInstance.put(2, 6.6), 0.0001);
        assertEquals(6.6, denseInstance.value(2), 0.0001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 4.4);
        map.put(1, 5.5);
        map.put(2, 6.6);

        denseInstance.putAll(map);

        assertEquals(4.4, denseInstance.value(0), 0.0001);
        assertEquals(5.5, denseInstance.value(1), 0.0001);
        assertEquals(6.6, denseInstance.value(2), 0.0001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        denseInstance.remove(0);
    }

    @Test
    public void testSize() {
        assertEquals(3, denseInstance.size());
    }

    @Test
    public void testValues() {
        ArrayDeque<Double> values = denseInstance.values();

        assertEquals(3, values.size());

        int index = 0;
        for (double value : values) {
            assertEquals(denseInstance.value(index++), value, 0.0001);
        }
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        assertEquals("{[1.1, 2.2, 3.3];1.0}", denseInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(0);

        assertEquals(2.2, denseInstance.value(0), 0.0001);
        assertEquals(3.3, denseInstance.value(1), 0.0001);
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 31 * 1 + Arrays.hashCode(denseInstance.attributes);

        assertEquals(expectedHashCode, denseInstance.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance sameInstance = new DenseInstance(denseInstance.attributes, denseInstance.classValue());
        assertTrue(denseInstance.equals(sameInstance));

        double[] otherAttributes = {1.1, 2.2, 3.3, 4.4};
        DenseInstance differentInstance = new DenseInstance(otherAttributes, denseInstance.classValue());
        assertFalse(denseInstance.equals(differentInstance));

        DenseInstance nullInstance = null;
        assertFalse(denseInstance.equals(nullInstance));

        assertFalse(denseInstance.equals("not an instance of DenseInstance"));
    }

    @Test
    public void testCopy() {
        DenseInstance copyInstance = denseInstance.copy();

        assertTrue(denseInstance.equals(copyInstance));
        assertNotSame(denseInstance, copyInstance);
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(2);

        denseInstance.removeAttributes(indices);

        assertEquals(2, denseInstance.noAttributes());
        assertEquals(2.2, denseInstance.value(0), 0.0001);
        assertEquals(0.0, denseInstance.value(1), 0.0001);
    }
}
