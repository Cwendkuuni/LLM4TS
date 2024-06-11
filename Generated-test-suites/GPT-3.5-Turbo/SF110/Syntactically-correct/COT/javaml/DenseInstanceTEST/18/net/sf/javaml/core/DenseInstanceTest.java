import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testPublicConstructorWithAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.value());
        assertNull(instance.classValue());
    }

    @Test
    public void testPublicConstructorWithAttributeAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "SampleClass";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertArrayEquals(att, instance.value());
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testPublicConstructorWithSize() {
        int size = 5;
        DenseInstance instance = new DenseInstance(size);
        double[] expectedAtt = new double[size];
        assertArrayEquals(expectedAtt, instance.value());
        assertNull(instance.classValue());
    }

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] expectedAtt = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedAtt, instance.value());
        assertNull(instance.classValue());
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> expectedEntrySet = new HashSet<>();
        expectedEntrySet.add(Map.entry(0, 1.0));
        expectedEntrySet.add(Map.entry(1, 2.0));
        expectedEntrySet.add(Map.entry(2, 3.0));
        assertEquals(expectedEntrySet, instance.entrySet());
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
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
        SortedSet<Integer> expectedKeySet = new TreeSet<>();
        expectedKeySet.add(0);
        expectedKeySet.add(1);
        expectedKeySet.add(2);
        assertEquals(expectedKeySet, instance.keySet());
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.put(0, 2.0), 0.001);
        assertEquals(2.0, instance.value(0), 0.001);
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instanceA = new DenseInstance(att);
        double[] att2 = {4.0, 5.0, 6.0};
        DenseInstance instanceB = new DenseInstance(att2);
        instanceA.putAll(instanceB);
        double[] expectedAtt = {4.0, 5.0, 6.0};
        assertArrayEquals(expectedAtt, instanceA.value());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.remove(1);
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
        Object classValue = "SampleClass";
        DenseInstance instance = new DenseInstance(att, classValue);
        String expectedString = "{[1.0, 2.0, 3.0];SampleClass}";
        assertEquals(expectedString, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        double[] expectedAtt = {1.0, 3.0};
        assertArrayEquals(expectedAtt, instance.value());
    }

    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instanceA = new DenseInstance(att);
        DenseInstance instanceB = new DenseInstance(att);
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instanceA = new DenseInstance(att);
        DenseInstance instanceB = new DenseInstance(att);
        assertTrue(instanceA.equals(instanceB));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "SampleClass";
        DenseInstance instance = new DenseInstance(att, classValue);
        DenseInstance copy = (DenseInstance)instance.copy();
        assertArrayEquals(att, copy.value());
        assertEquals(classValue, copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        double[] expectedAtt = {2.0};
        assertArrayEquals(expectedAtt, instance.value());
    }
}