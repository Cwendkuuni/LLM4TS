
import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals(1.0, denseInstance.value(0), 0.0001);
        assertEquals(2.0, denseInstance.value(1), 0.0001);
        assertEquals(3.0, denseInstance.value(2), 0.0001);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        denseInstance.clear();
        double[] clearedAtt = {0.0, 0.0, 0.0};
        assertArrayEquals(clearedAtt, denseInstance.getAttributes(), 0.0001);
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsKey(-1));
        assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertTrue(denseInstance.containsValue(1.0));
        assertTrue(denseInstance.containsValue(2.0));
        assertTrue(denseInstance.containsValue(3.0));
        assertFalse(denseInstance.containsValue(0.0));
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals(3, denseInstance.entrySet().size());
        assertTrue(denseInstance.entrySet().contains(new AbstractMap.SimpleEntry<Integer, Double>(0, 1.0)));
        assertTrue(denseInstance.entrySet().contains(new AbstractMap.SimpleEntry<Integer, Double>(1, 2.0)));
        assertTrue(denseInstance.entrySet().contains(new AbstractMap.SimpleEntry<Integer, Double>(2, 3.0)));
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals((Double) 1.0, denseInstance.get(0));
        assertEquals((Double) 2.0, denseInstance.get(1));
        assertEquals((Double) 3.0, denseInstance.get(2));
    }

    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals(3, denseInstance.size());
    }

    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals(3, denseInstance.values().size());
        assertTrue(denseInstance.values().contains(1.0));
        assertTrue(denseInstance.values().contains(2.0));
        assertTrue(denseInstance.values().contains(3.0));
    }

    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals("{[1.0, 2.0, 3.0];null}", denseInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        denseInstance.removeAttribute(1);
        double[] newAtt = {1.0, 3.0};
        assertArrayEquals(newAtt, denseInstance.getAttributes(), 0.0001);
    }

    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals(96179624, denseInstance.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(att1);
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance2 = new DenseInstance(att2);
        assertEquals(denseInstance1, denseInstance2);
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        DenseInstance copyInstance = (DenseInstance) denseInstance.copy();
        assertArrayEquals(att, copyInstance.getAttributes(), 0.0001);
        assertNull(copyInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        indices.add(2);
        denseInstance.removeAttributes(indices);
        double[] newAtt = {1.0};
        assertArrayEquals(newAtt, denseInstance.getAttributes(), 0.0001);
    }
}

