import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    private DenseInstance instance;

    @Before
    public void setUp() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
    }

    @Test
    public void testValue() {
        assertEquals(2.0, instance.value(1), 0.0);
    }

    @Test
    public void testClear() {
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> set = instance.entrySet();
        assertEquals(3, set.size());
    }

    @Test
    public void testGet() {
        assertEquals(3.0, instance.get(2), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(1);
    }

    @Test
    public void testSize() {
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(0);
        double[] expected = {2.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.0);
    }

    @Test
    public void testEquals() {
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.equals(instance2));
    }

    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance) instance.copy();
        assertArrayEquals(instance.attributes, copy.attributes, 0.0);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        double[] expected = {2.0};
        assertArrayEquals(expected, instance.attributes, 0.0);
    }
}