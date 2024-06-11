import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        double[] expectedAttributes = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);

        double[] expectedAttributes = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);

        double[] expectedAttributes = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0);
    }

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        instance.clear();

        double[] expectedAttributes = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 1.0);
        expectedMap.put(1, 2.0);
        expectedMap.put(2, 3.0);

        assertEquals(expectedMap.entrySet(), instance.entrySet());
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
    }

    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Set<Integer> expectedKeySet = new TreeSet<>();
        expectedKeySet.add(0);
        expectedKeySet.add(1);
        expectedKeySet.add(2);

        assertEquals(expectedKeySet, instance.keySet());
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(1.0, instance.put(0, 4.0), 0.0);
        assertEquals(4.0, instance.value(0), 0.0);
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);

        instance.putAll(map);

        assertEquals(4.0, instance.value(0), 0.0);
        assertEquals(5.0, instance.value(1), 0.0);
        assertEquals(6.0, instance.value(2), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        instance.remove(0);
    }

    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Collection<Double> expectedValues = new ArrayList<>();
        expectedValues.add(1.0);
        expectedValues.add(2.0);
        expectedValues.add(3.0);

        assertEquals(expectedValues, instance.values());
    }

    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        String expectedString = "{[1.0, 2.0, 3.0];null}";

        assertEquals(expectedString, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        instance.removeAttribute(1);

        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        int expectedHashCode = Objects.hash(1.0, 2.0, 3.0);

        assertEquals(expectedHashCode, instance.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);

        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);

        double[] att3 = {4.0, 5.0, 6.0};
        DenseInstance instance3 = new DenseInstance(att3);

        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        DenseInstance copy = (DenseInstance) instance.copy();

        assertArrayEquals(att, copy.attributes, 0.0);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);

        instance.removeAttributes(indices);

        double[] expectedAttributes = {2.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0);
    }
}