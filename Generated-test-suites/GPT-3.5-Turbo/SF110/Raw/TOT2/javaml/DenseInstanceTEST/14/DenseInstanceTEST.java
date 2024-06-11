import static org.junit.Assert.*;
import org.junit.Test;

public class DenseInstanceTest {

    private DenseInstance denseInstance;

    @Before
    public void setUp() {
        double[] attributes = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(attributes, "class");
    }

    @Test
    public void testValue() {
        assertEquals(2.0, denseInstance.value(1), 0.0001);
    }

    @Test
    public void testClear() {
        denseInstance.clear();
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, denseInstance.getAttributes(), 0.0001);
    }

    @Test
    public void testContainsKey() {
        assertTrue(denseInstance.containsKey(1));
        assertFalse(denseInstance.containsKey(4));
    }

    @Test
    public void testContainsValue() {
        assertTrue(denseInstance.containsValue(2.0));
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        assertEquals(3, denseInstance.entrySet().size());
    }

    @Test
    public void testGet() {
        assertEquals(2.0, denseInstance.get(1), 0.0001);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        assertEquals(3, denseInstance.keySet().size());
    }

    @Test
    public void testPut() {
        assertEquals(2.0, denseInstance.put(1, 4.0), 0.0001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        denseInstance.remove(1);
    }

    @Test
    public void testSize() {
        assertEquals(3, denseInstance.size());
    }

    @Test
    public void testValues() {
        assertEquals(3, denseInstance.values().size());
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(1);
        assertArrayEquals(new double[]{1.0, 3.0}, denseInstance.getAttributes(), 0.0001);
    }

    @Test
    public void testHashCode() {
        assertEquals(123456, denseInstance.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance other = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class");
        assertTrue(denseInstance.equals(other));
    }

    @Test
    public void testCopy() {
        DenseInstance copy = denseInstance.copy();
        assertFalse(copy == denseInstance);
        assertArrayEquals(denseInstance.getAttributes(), copy.getAttributes(), 0.0001);
    }

    @Test
    public void testRemoveAttributes() {
        HashSet<Integer> indices = new HashSet<>();
        indices.add(1);
        denseInstance.removeAttributes(indices);
        assertArrayEquals(new double[]{1.0, 3.0}, denseInstance.getAttributes(), 0.0001);
    }

}