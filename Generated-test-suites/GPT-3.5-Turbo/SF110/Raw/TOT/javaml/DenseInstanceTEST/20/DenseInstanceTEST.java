import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    DenseInstance denseInstance;

    @Before
    public void beforeEachTest() {
        double[] att = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(att);
    }

    @Test
    public void testConstructorWithAttributeArray() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithAttributeArrayAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        String classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertArrayEquals(att, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);
        assertEquals(size, instance.attributes.length);
    }

    @Test
    public void testValue() {
        int pos = 1;
        assertEquals(denseInstance.attributes[pos], denseInstance.value(pos), 0.0);
    }

    @Test
    public void testClear() {
        denseInstance.clear();
        assertArrayEquals(new double[denseInstance.attributes.length], denseInstance.attributes, 0.0);
    }

    @Test
    public void testContainsKeyWithValidIndex() {
        int validIndex = 1;
        assertTrue(denseInstance.containsKey(validIndex));
    }

    @Test
    public void testContainsKeyWithInvalidIndex() {
        int invalidIndex = -1;
        assertFalse(denseInstance.containsKey(invalidIndex));
    }

    @Test
    public void testContainsValue() {
        double validValue = 2.0;
        double invalidValue = 4.0;
        assertTrue(denseInstance.containsValue(validValue));
        assertFalse(denseInstance.containsValue(invalidValue));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Map<Integer, Double> map = new HashMap<>();
        int i = 0;
        for (double attribute : denseInstance.attributes) {
            map.put(i++, attribute);
        }
        assertEquals(map.entrySet(), entrySet);
    }

    @Test
    public void testGet() {
        int key = 1;
        assertEquals(denseInstance.attributes[key], denseInstance.get(key), 0.0);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = denseInstance.keySet();
        TreeSet<Integer> keys = new TreeSet<>();
        for (int i = 0; i < denseInstance.attributes.length; ++i) {
            keys.add(i);
        }
        assertEquals(keys, keySet);
    }

    @Test
    public void testPut() {
        int key = 1;
        double value = 4.0;
        double oldValue = denseInstance.attributes[key];
        DenseInstance instance = denseInstance.put(key, value);
        assertEquals(value, denseInstance.get(key), 0.0);
        assertEquals(oldValue, instance, 0.0);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        denseInstance.putAll(map);
        assertEquals(map, denseInstance.entrySet());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        int key = 1;
        denseInstance.remove(key);
    }

    @Test
    public void testSize() {
        assertEquals(denseInstance.attributes.length, denseInstance.size());
    }

    @Test
    public void testValues() {
        Collection<Double> values = denseInstance.values();
        List<Double> vals = new ArrayList<>();
        for (double attribute : denseInstance.attributes) {
            vals.add(attribute);
        }
        assertEquals(vals, values);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(denseInstance.attributes.length, denseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        String attributes = Arrays.toString(denseInstance.attributes);
        String classValue = denseInstance.classValue();
        String expectedResult = "{" + attributes + ";" + classValue + "}";
        assertEquals(expectedResult, denseInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        int index = 1;
        double[] tmp = denseInstance.attributes.clone();
        double[] expectedAttributes = new double[tmp.length - 1];
        System.arraycopy(tmp, 0, expectedAttributes, 0, index);
        System.arraycopy(tmp, index + 1, expectedAttributes, index, tmp.length - index - 1);
        denseInstance.removeAttribute(index);
        assertArrayEquals(expectedAttributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 1;
        expectedHashCode = 31 * expectedHashCode + Arrays.hashCode(denseInstance.attributes);
        assertEquals(expectedHashCode, denseInstance.hashCode());
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(denseInstance.equals(denseInstance));
    }

    @Test
    public void testEqualsWithNullObject() {
        assertFalse(denseInstance.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Object object = new Object();
        assertFalse(denseInstance.equals(object));
    }

    @Test
    public void testEqualsWithDifferentAttributes() {
        double[] otherAttributes = {4.0, 5.0, 6.0};
        DenseInstance otherInstance = new DenseInstance(otherAttributes);
        assertFalse(denseInstance.equals(otherInstance));
    }

    @Test
    public void testEqualsWithSameAttributes() {
        double[] sameAttributes = denseInstance.attributes.clone();
        DenseInstance sameInstance = new DenseInstance(sameAttributes);
        assertTrue(denseInstance.equals(sameInstance));
    }

    @Test
    public void testCopy() {
        DenseInstance copiedInstance = denseInstance.copy();
        assertArrayEquals(denseInstance.attributes, copiedInstance.attributes, 0.0);
        assertEquals(denseInstance.classValue(), copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(0);
        indicesToRemove.add(2);
        double[] tmp = denseInstance.attributes.clone();
        double[] expectedAttributes = new double[tmp.length - indicesToRemove.size()];
        int index = 0;
        for (int i = 0; i < tmp.length; ++i) {
            if (!indicesToRemove.contains(i)) {
                expectedAttributes[index++] = tmp[i];
            }
        }
        denseInstance.removeAttributes(indicesToRemove);
        assertArrayEquals(expectedAttributes, denseInstance.attributes, 0.0);
    }

}