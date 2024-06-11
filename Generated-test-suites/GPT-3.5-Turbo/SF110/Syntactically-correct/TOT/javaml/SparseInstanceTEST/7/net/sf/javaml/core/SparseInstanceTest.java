import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

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
    public void testSparseInstance1() {
        int noAttributes = 4;
        double defaultValue = 0.5;
        instance = new SparseInstance(noAttributes, defaultValue);
        assertEquals(noAttributes, instance.noAttributes());
        assertEquals(defaultValue, instance.get(0), 0.0001);
    }

    @Test
    public void testSparseInstance2() {
        int noAttributes = 3;
        double[] datavector = {1.0, 2.0, 3.0};
        instance = new SparseInstance(datavector);
        assertEquals(noAttributes, instance.noAttributes());
        assertEquals(datavector[0], instance.value(0), 0.0001);
        assertEquals(datavector[1], instance.value(1), 0.0001);
        assertEquals(datavector[2], instance.value(2), 0.0001);
    }

    @Test
    public void testValue() {
        int pos = 2;
        double value = 1.5;
        instance.put(pos, value);
        assertEquals(value, instance.value(pos), 0.0001);
    }

    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(0, 1.0)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1, 2.0)));
    }

    @Test
    public void testGet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(instance.defaultValue, instance.get(2), 0.0001);
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
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
    }

    @Test
    public void testPut() {
        Integer key = 3;
        Double value = 2.5;
        Double previousValue = instance.put(key, value);
        assertNull(previousValue);
        assertEquals(1, instance.size());
        assertEquals(value, instance.get(key), 0.0001);

        Double updatedValue = 3.5;
        Double updatedPreviousValue = instance.put(key, updatedValue);
        assertEquals(value, updatedPreviousValue, 0.0001);
        assertEquals(1, instance.size());
        assertEquals(updatedValue, instance.get(key), 0.0001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(map.size(), instance.size());
        assertEquals(map.get(0), instance.get(0), 0.0001);
        assertEquals(map.get(1), instance.get(1), 0.0001);
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        Double removedValue = instance.remove(0);
        assertEquals(1, instance.size());
        assertEquals(1, instance.noAttributes());
        assertNull(instance.get(0));
        assertEquals(1, removedValue, 0.0001);
    }

    @Test
    public void testSize() {
        assertEquals(0, instance.size());
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
    }

    @Test
    public void testValues() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Collection<Double> values = instance.values();
        assertEquals(2, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, instance.noAttributes());
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
        instance.put(1, 2.0);
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.put(3, 4.0);

        instance.removeAttribute(0);
        assertEquals(3, instance.size());
        assertEquals(3, instance.noAttributes());
        assertNull(instance.get(0));
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
        assertEquals(4.0, instance.get(3), 0.0001);
    }

    @Test
    public void testToString() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.setClassValue("class");
        String expected = "{" + instance.data.toString() + ";" + instance.classValue() + "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.setClassValue("class");

        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(instance.size(), copy.size());
        assertEquals(instance.get(0), copy.get(0), 0.0001);
        assertEquals(instance.get(1), copy.get(1), 0.0001);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.put(3, 4.0);

        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(1);
        indicesToRemove.add(3);
        instance.removeAttributes(indicesToRemove);

        assertEquals(2, instance.size());
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.0001);
        assertNull(instance.get(1));
        assertEquals(3.0, instance.get(2), 0.0001);
        assertNull(instance.get(3));
    }

    @After
    public void tearDown() {
        instance = null;
    }
}