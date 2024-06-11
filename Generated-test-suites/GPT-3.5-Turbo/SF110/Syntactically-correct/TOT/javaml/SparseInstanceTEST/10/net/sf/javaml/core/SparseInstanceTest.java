import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    private SparseInstance sInstance;

    @Before
    public void setUp() {
        sInstance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        sInstance.setNoAttributes(5);
        assertEquals(5, sInstance.noAttributes());
    }

    @Test
    public void testSparseInstance1() {
        SparseInstance instance = new SparseInstance();
        assertNotNull(instance);
    }

    @Test
    public void testSparseInstance2() {
        SparseInstance instance = new SparseInstance(3);
        assertNotNull(instance);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testSparseInstance3() {
        SparseInstance instance = new SparseInstance(4, 0.5);
        assertNotNull(instance);
        assertEquals(4, instance.noAttributes());
        assertEquals(0.5, instance.get(0), 0.001);
        assertEquals(0.5, instance.get(1), 0.001);
        assertEquals(0.5, instance.get(2), 0.001);
        assertEquals(0.5, instance.get(3), 0.001);
    }
    
    @Test
    public void testSparseInstance4() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(2, 0.8, classValue);
        assertNotNull(instance);
        assertEquals(2, instance.noAttributes());
        assertEquals(0.8, instance.get(0), 0.001);
        assertEquals(0.8, instance.get(1), 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstance5() {
        double[] datavector = {0.1, 0.2, 0.0, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertNotNull(instance);
        assertEquals(4, instance.noAttributes());
        assertEquals(0.1, instance.get(0), 0.001);
        assertEquals(0.2, instance.get(1), 0.001);
        assertEquals(0.3, instance.get(3), 0.001);
    }

    @Test
    public void testSparseInstance6() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector, 0.2);
        assertNotNull(instance);
        assertEquals(4, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(4.0, instance.get(3), 0.001);
        assertEquals(0.2, instance.defaultValue, 0.001);
    }

    @Test
    public void testValue() {
        double[] datavector = {0.5, 1.5};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(0.5, instance.value(0), 0.001);
        assertEquals(1.5, instance.value(1), 0.001);
    }

    @Test
    public void testClear() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsValue(0.2));
        assertFalse(instance.containsValue(0.4));
    }

    @Test
    public void testEntrySet() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(0, 0.1)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1, 0.2)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(2, 0.3)));
    }

    @Test
    public void testGet() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(0.1, instance.get(0), 0.001);
        assertEquals(instance.defaultValue, instance.get(3), 0.001);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(sInstance.isEmpty());
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }

    @Test
    public void testPut() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertNull(instance.put(3, 0.4));
        assertEquals(4, instance.noAttributes());
        assertEquals(0.4, instance.get(3), 0.001);
    }

    @Test
    public void testPutAll() {
        double[] datavector = {0.1, 0.2};
        SparseInstance instance = new SparseInstance(datavector);
        Map<Integer, Double> map = new HashMap<>();
        map.put(2, 0.3);
        map.put(3, 0.4);
        instance.putAll(map);
        assertEquals(4, instance.noAttributes());
        assertEquals(0.4, instance.get(3), 0.001);
    }

    @Test
    public void testRemove() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(0.1, instance.remove(0), 0.001);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(0));
    }

    @Test
    public void testSize() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(0.1));
        assertTrue(values.contains(0.2));
        assertTrue(values.contains(0.3));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(-1, sInstance.noAttributes());
        double[] datavector = {0.1, 0.2, 0.3, 0.4};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(4, instance.noAttributes());
        instance.remove(1);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        double[] datavector = {0.1, 0.2, 0.3, 0.4};
        SparseInstance instance = new SparseInstance(datavector);
        instance.removeAttribute(1);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testToString() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        String expected = "{0=0.1;1=0.2;2=0.3;null}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance1 = new SparseInstance(datavector);
        SparseInstance instance2 = new SparseInstance(datavector);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance1 = new SparseInstance(datavector);
        SparseInstance instance2 = new SparseInstance(datavector);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(instance.toString(), copy.toString());
        assertNotSame(instance, copy);
    }

    @Test
    public void testRemoveAttributes() {
        double[] datavector = {0.1, 0.2, 0.3, 0.4, 0.5};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(3);
        instance.removeAttributes(indices);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(3));
    }

}