import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

	@Test
	public void testValue() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
		assertEquals(2.0, denseInstance.value(1), 0.001);
	}

	@Test
	public void testClear() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        denseInstance.clear();
		assertArrayEquals(new double[]{0.0, 0.0, 0.0}, denseInstance.getAttributes(), 0.001);
	}

	@Test
	public void testContainsKey_ValidKey() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertTrue(denseInstance.containsKey(1));
	}

	@Test
	public void testContainsKey_InvalidKey() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertFalse(denseInstance.containsKey(5));
	}

	@Test
	public void testContainsValue_ValidValue() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertTrue(denseInstance.containsValue(2.0));
	}

	@Test
	public void testContainsValue_InvalidValue() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertFalse(denseInstance.containsValue(4.0));
	}

	@Test
	public void testEntrySet() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(3, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(0, 1.0)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(1, 2.0)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(2, 3.0)));
	}

	@Test
	public void testGet() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals(2.0, denseInstance.get(1), 0.001);
	}

	@Test
	public void testIsEmpty() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertFalse(denseInstance.isEmpty());
	}

	@Test
	public void testKeySet() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
	}

	@Test
	public void testPut() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        DenseInstance copy = denseInstance.copy();
        double oldValue = denseInstance.put(1, 4.0);
        assertEquals(2.0, oldValue, 0.001);
        assertEquals(4.0, denseInstance.value(1), 0.001);
	}

	@Test
	public void testPutAll() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        double[] newAttributes = {4.0, 5.0, 6.0};
        Map<Integer, Double> newValues = new HashMap<Integer, Double>();
        newValues.put(0, 4.0);
        newValues.put(1, 5.0);
        newValues.put(2, 6.0);
        denseInstance.putAll(newValues);
        assertArrayEquals(newAttributes, denseInstance.getAttributes(), 0.001);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRemove() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        denseInstance.remove(1);
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
        Collection<Double> values = denseInstance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
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
        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, denseInstance.getAttributes(), 0.001);
	}

	@Test
	public void testHashCode() {
		double[] att1 = {1.0, 2.0, 3.0};
		double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(att1);
        DenseInstance denseInstance2 = new DenseInstance(att2);
        assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
	}

	@Test
	public void testEquals_SameInstance() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals(denseInstance, denseInstance);
	}

	@Test
	public void testEquals_NullInstance() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertNotEquals(null, denseInstance);
	}

	@Test
	public void testEquals_DifferentClass() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertNotEquals(denseInstance, "Not an instance");
	}

	@Test
	public void testEquals_DifferentAttributes() {
		double[] att1 = {1.0, 2.0, 3.0};
		double[] att2 = {1.0, 3.0, 2.0};
        DenseInstance denseInstance1 = new DenseInstance(att1);
        DenseInstance denseInstance2 = new DenseInstance(att2);
        assertNotEquals(denseInstance1, denseInstance2);
	}

	@Test
	public void testCopy() {
		double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        DenseInstance copy = denseInstance.copy();
        assertEquals(denseInstance, copy);
        assertNotSame(denseInstance, copy);
	}

	@Test
	public void testRemoveAttributes() {
		double[] att = {1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance denseInstance = new DenseInstance(att);
        Set<Integer> indicesToRemove = new HashSet<Integer>(Arrays.asList(1, 3));
        denseInstance.removeAttributes(indicesToRemove);
        double[] expectedAttributes = {1.0, 3.0, 5.0};
        assertArrayEquals(expectedAttributes, denseInstance.getAttributes(), 0.001);
	}

}