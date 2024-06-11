import static org.junit.Assert.*;
import org.junit.*;

public class DenseInstanceTest {

    private DenseInstance instance;

    @Before
    public void setUp() {
        instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
    }

    @Test
    public void testValue_Positive() {
        double result = instance.value(1);
        assertEquals(2.0, result, 0.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testValue_InvalidPosition() {
        instance.value(3);
    }

    @Test
    public void testClear() {
        instance.clear();

        double[] attributes = instance.getAttributes();
        for (double attribute : attributes) {
            assertEquals(0.0, attribute, 0.0);
        }
    }

    @Test
    public void testContainsKey_Positive() {
        boolean result = instance.containsKey(1);
        assertTrue(result);
    }

    @Test
    public void testContainsKey_Negative() {
        boolean result = instance.containsKey(-1);
        assertFalse(result);
    }

    @Test
    public void testContainsKey_OutOfBounds() {
        boolean result = instance.containsKey(3);
        assertFalse(result);
    }

    @Test
    public void testContainsValue_Positive() {
        boolean result = instance.containsValue(3.0);
        assertTrue(result);
    }

    @Test
    public void testContainsValue_Negative() {
        boolean result = instance.containsValue(4.0);
        assertFalse(result);
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());

        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(instance.value(key), value, 0.0);
        }
    }

    @Test
    public void testGet() {
        double result = instance.get(0);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testIsEmpty_True() {
        boolean result = instance.isEmpty();
        assertFalse(result);
    }

    @Test
    public void testIsEmpty_False() {
        instance.clear();
        boolean result = instance.isEmpty();
        assertTrue(result);
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = instance.keySet();

        assertEquals(0, (int) keySet.first());
        assertEquals(1, (int) keySet.last());

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertTrue(keySet.contains(i));
        }
    }

    @Test
    public void testPut() {
        Double result = instance.put(1, 4.0);

        assertEquals(2.0, result, 0.0);
        assertEquals(4.0, instance.get(1), 0.0);
    }

    @Test
    public void testPutAll() {
        instance.putAll(Map.of(1, 4.0, 2, 5.0));

        assertEquals(4.0, instance.get(1), 0.0);
        assertEquals(5.0, instance.get(2), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(0);
    }

    @Test
    public void testSize() {
        int result = instance.size();
        assertEquals(3, result);
    }

    @Test
    public void testValues() {
        Collection<Double> values = instance.values();

        assertEquals(3, values.size());

        for (Double value : values) {
            assertTrue(instance.containsValue(value));
        }
    }

    @Test
    public void testNoAttributes() {
        int result = instance.noAttributes();
        assertEquals(3, result);
    }

    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0];null}";
        String result = instance.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(3.0, instance.get(1), 0.0);
    }

    @Test
    public void testHashCode() {
        int result = instance.hashCode();
        assertEquals(1099310571, result);
    }

    @Test
    public void testEquals_True() {
        DenseInstance other = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        boolean result = instance.equals(other);
        assertTrue(result);
    }

    @Test
    public void testEquals_False() {
        DenseInstance other = new DenseInstance(new double[]{4.0, 5.0, 6.0});

        boolean result = instance.equals(other);
        assertFalse(result);
    }

    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance) instance.copy();

        assertEquals(instance.noAttributes(), copy.noAttributes());

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i), copy.value(i), 0.0);
        }

        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        instance.removeAttributes(Set.of(1));

        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(3.0, instance.get(1), 0.0);
    }

}