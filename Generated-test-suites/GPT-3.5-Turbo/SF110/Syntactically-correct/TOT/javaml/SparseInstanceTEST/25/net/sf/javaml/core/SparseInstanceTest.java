import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    private SparseInstance instance;

    @Before
    public void setup() {
        instance = new SparseInstance(3);
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testNoAttrConstructor() {
        SparseInstance newInstance = new SparseInstance();
        assertEquals(-1, newInstance.noAttributes());
    }

    @Test
    public void testDefaultValueConstructor() {
        SparseInstance newInstance = new SparseInstance(5, 2.0);
        assertEquals(5, newInstance.noAttributes());
        assertEquals(2.0, newInstance.get(0), 0);
    }

    @Test
    public void testClassValueConstructor() {
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(5, 2.0, classValue);
        assertEquals(5, newInstance.noAttributes());
        assertEquals(classValue, newInstance.classValue());
    }

    @Test
    public void testDatavectorConstructor() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance newInstance = new SparseInstance(datavector);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(1.0, newInstance.get(0), 0);
        assertEquals(2.0, newInstance.get(1), 0);
        assertEquals(3.0, newInstance.get(2), 0);
    }

    @Test
    public void testValue() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        assertEquals(1.0, instance.value(0), 0);
        assertEquals(2.0, instance.value(1), 0);
        assertEquals(3.0, instance.value(2), 0);
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

        assertEquals(2, instance.entrySet().size());
    }

    @Test
    public void testGet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        assertEquals(1.0, instance.get(0), 0);
        assertEquals(2.0, instance.get(1), 0);
        assertEquals(0.0, instance.get(2), 0);
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
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        assertEquals(1.0, instance.get(0), 0);
        assertEquals(2.0, instance.get(1), 0);
    }

    @Test
    public void testPutAll() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        SparseInstance newInstance = new SparseInstance();
        newInstance.putAll(instance);

        assertEquals(1.0, newInstance.get(0), 0);
        assertEquals(2.0, newInstance.get(1), 0);
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        assertEquals(1.0, instance.remove(0), 0);
        assertNull(instance.remove(0));

        assertFalse(instance.containsKey(0));
    }

    @Test
    public void testSize() {
        assertEquals(0, instance.size());

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
        assertEquals(3, instance.noAttributes());

        instance.put(3, 1.0);
        assertEquals(4, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        instance.removeAttribute(1);

        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0);
        assertEquals(3.0, instance.get(1), 0);
    }

    @Test
    public void testToString() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        assertEquals("{0=1.0;null}", instance.toString());
    }

    @Test
    public void testEquals() {
        SparseInstance newInstance = new SparseInstance();
        SparseInstance newInstanceSame = new SparseInstance();

        assertTrue(newInstance.equals(newInstance));
        assertTrue(newInstance.equals(newInstanceSame));
        assertFalse(newInstance.equals(null));

        newInstance.put(0, 1.0);
        assertFalse(newInstance.equals(newInstanceSame));

        newInstanceSame.put(0, 1.0);
        assertTrue(newInstance.equals(newInstanceSame));
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        SparseInstance copy = instance.copy();

        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertEquals(instance.get(0), copy.get(0));
        assertEquals(instance.get(1), copy.get(1));
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        instance.removeAttributes(new HashSet<>(Arrays.asList(0, 1)));

        assertEquals(1, instance.noAttributes());
        assertEquals(3.0, instance.get(0), 0);
    }

}