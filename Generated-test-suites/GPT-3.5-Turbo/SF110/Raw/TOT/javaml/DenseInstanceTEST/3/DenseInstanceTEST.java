import static org.junit.Assert.*;
import org.junit.*;

public class DenseInstanceTest {

    private DenseInstance instance;

    @Before
    public void setUp() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0};
        instance = new DenseInstance(attributes);
    }

    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
        assertEquals(4.0, instance.value(3), 0.0001);
    }

    @Test
    public void testClear() {
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.attributes, 0.0001);
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(5));
    }

    @Test
    public void testContainsValue() {
        assertFalse(instance.containsValue(0));
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(5.0));
    }

    @Test
    public void testEntrySet() {
        assertNotNull(instance.entrySet());
        assertEquals(4, instance.entrySet().size());
    }

    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(4.0, instance.get(3), 0.0001);
        assertNull(instance.get(-1));
        assertNull(instance.get(5));
    }

    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        assertNotNull(instance.keySet());
        assertEquals(4, instance.keySet().size());
        assertTrue(instance.keySet().contains(0));
        assertFalse(instance.keySet().contains(5));
    }

    @Test
    public void testPut() {
        assertEquals(1.0, instance.put(0, 100.0), 0.0001);
        assertEquals(100.0, instance.get(0), 0.0001);
        assertNull(instance.put(5, 5.0));
        assertEquals(5.0, instance.get(5), 0.0001);
        instance.put(0, 1.0);
        assertNull(instance.put(-1, -1.0));
    }

    @Test
    public void testPutAll() {
        DenseInstance anotherInstance = new DenseInstance(new double[]{5.0, 6.0});
        instance.putAll(anotherInstance);
        assertEquals(2.0, instance.get(0), 0.0001);
        assertEquals(6.0, instance.get(1), 0.0001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(0);
    }

    @Test
    public void testSize() {
        assertEquals(4, instance.size());
        instance.clear();
        assertEquals(0, instance.size());
    }

    @Test
    public void testValues() {
        assertNotNull(instance.values());
        assertEquals(4, instance.values().size());
        assertTrue(instance.values().contains(1.0));
        assertFalse(instance.values().contains(5.0));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(4, instance.noAttributes());
    }

    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0, 4.0];null}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(1);
        double[] expected = {1.0, 3.0, 4.0};
        assertArrayEquals(expected, instance.attributes, 0.0001);
    }

    @Test
    public void testHashCode() {
        DenseInstance sameInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        assertEquals(instance.hashCode(), sameInstance.hashCode());

        DenseInstance differentInstance = new DenseInstance(new double[]{4.0, 3.0, 2.0, 1.0});
        assertNotEquals(instance.hashCode(), differentInstance.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance sameInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        assertTrue(instance.equals(sameInstance));

        DenseInstance differentInstance = new DenseInstance(new double[]{4.0, 3.0, 2.0, 1.0});
        assertFalse(instance.equals(differentInstance));

        assertFalse(instance.equals("not an instance"));
    }

    @Test
    public void testCopy() {
        DenseInstance copyInstance = (DenseInstance) instance.copy();
        assertNotSame(instance, copyInstance);
        assertArrayEquals(instance.attributes, copyInstance.attributes, 0.0001);
        assertEquals(instance.classValue(), copyInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        instance.removeAttributes(new HashSet<Integer>() {{
            add(1);
            add(3);
        }});
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.0001);
    }

}