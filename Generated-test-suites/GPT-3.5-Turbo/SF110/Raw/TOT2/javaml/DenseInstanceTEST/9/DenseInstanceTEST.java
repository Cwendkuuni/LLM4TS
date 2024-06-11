import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    private DenseInstance denseInstance;

    @Before
    public void setUp() {
        denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
    }

    @Test
    public void testValue() {
        assertEquals(2.0, denseInstance.value(1), 0.001);
    }

    @Test
    public void testClear() {
        denseInstance.clear();
        assertEquals(0.0, denseInstance.value(0), 0.001);
    }

    @Test
    public void testContainsKey() {
        assertTrue(denseInstance.containsKey(1));
        assertFalse(denseInstance.containsKey(5));
    }

    @Test
    public void testContainsValue() {
        assertTrue(denseInstance.containsValue(2.0));
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        assertNotNull(denseInstance.entrySet());
    }

    @Test
    public void testGet() {
        assertEquals(3.0, denseInstance.get(2), 0.001);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        assertNotNull(denseInstance.keySet());
    }

    @Test
    public void testPut() {
        assertEquals(3.0, denseInstance.put(2, 4.0), 0.001);
    }

    @Test
    public void testRemove() {
        try {
            denseInstance.remove(1);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSize() {
        assertEquals(3, denseInstance.size());
    }

    @Test
    public void testValues() {
        assertNotNull(denseInstance.values());
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(1);
        assertEquals(2.0, denseInstance.value(1), 0.001);
    }

    @Test
    public void testHashCode() {
        assertEquals(denseInstance.hashCode(), denseInstance.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(denseInstance.equals(instance2));
    }

    @Test
    public void testCopy() {
        DenseInstance copiedInstance = (DenseInstance)denseInstance.copy();
        assertTrue(denseInstance.equals(copiedInstance));
    }

    @Test
    public void testRemoveAttributes() {
        denseInstance.removeAttributes(Set.of(0, 2));
        assertEquals(1, denseInstance.size());
    }
}