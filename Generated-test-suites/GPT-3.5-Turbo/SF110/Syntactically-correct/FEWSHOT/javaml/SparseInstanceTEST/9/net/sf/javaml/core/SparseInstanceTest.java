package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {

	private SparseInstance instance;
	private double[] datavector;

	@Before
	public void setUp() {
		instance = new SparseInstance();
		datavector = new double[] {0.0, 1.0, 2.0};
	}

	@Test
	public void testSetNoAttributes() {
		instance.setNoAttributes(3);
		assertEquals(3, instance.noAttributes());
	}

	@Test
	public void testSparseInstanceWithNoAttributes() {
		SparseInstance sparseInstance = new SparseInstance();
		assertEquals(-1, sparseInstance.noAttributes());
	}

	@Test
	public void testSparseInstanceWithNoAttributesDefaultValue() {
		SparseInstance sparseInstance = new SparseInstance();
		assertEquals(0.0, sparseInstance.defaultValue, 0.001);
	}

	@Test
	public void testSparseInstanceWithNoAttributesClassValue() {
		SparseInstance sparseInstance = new SparseInstance();
		assertEquals(null, sparseInstance.classValue());
	}

	@Test
	public void testSparseInstanceWithAttributesAndDefaultValue() {
		SparseInstance sparseInstance = new SparseInstance(3, 1.0);
		assertEquals(3, sparseInstance.noAttributes());
		assertEquals(1.0, sparseInstance.defaultValue, 0.001);
	}

	@Test
	public void testSparseInstanceWithAttributesAndClassValue() {
		SparseInstance sparseInstance = new SparseInstance(3, 1.0);
		sparseInstance.setClassValue("class");
		assertEquals(3, sparseInstance.noAttributes());
		assertEquals(1.0, sparseInstance.defaultValue, 0.001);
		assertEquals("class", sparseInstance.classValue());
	}

	@Test
	public void testSparseInstanceWithAttributesDefaultValueAndClassValue() {
		SparseInstance sparseInstance = new SparseInstance(3, 1.0, "class");
		assertEquals(3, sparseInstance.noAttributes());
		assertEquals(1.0, sparseInstance.defaultValue, 0.001);
		assertEquals("class", sparseInstance.classValue());
	}

	@Test
	public void testSparseInstanceWithDataVector() {
		SparseInstance sparseInstance = new SparseInstance(datavector);
		assertEquals(3, sparseInstance.noAttributes());
		assertEquals(0.0, sparseInstance.defaultValue, 0.001);
	}

	@Test
	public void testSparseInstanceWithDataVectorAndDefaultValue() {
		SparseInstance sparseInstance = new SparseInstance(datavector, 1.0);
		assertEquals(3, sparseInstance.noAttributes());
		assertEquals(1.0, sparseInstance.defaultValue, 0.001);
	}

	@Test
	public void testSparseInstanceWithDataVectorDefaultValueAndClassValue() {
		SparseInstance sparseInstance = new SparseInstance(datavector, 1.0, "class");
		assertEquals(3, sparseInstance.noAttributes());
		assertEquals(1.0, sparseInstance.defaultValue, 0.001);
		assertEquals("class", sparseInstance.classValue());
	}

	@Test
	public void testInitiate() {
		instance.initiate(datavector);
		assertEquals(3, instance.noAttributes());
		assertEquals(0.0, instance.defaultValue, 0.001);
		assertEquals(Double.valueOf(1.0), instance.get(1));
	}

	@Test
	public void testValue() {
		instance.put(1, 3.0);
		assertEquals(3.0, instance.value(1), 0.001);
	}

	@Test
	public void testClear() {
		instance.put(1, 3.0);
		instance.clear();
		assertTrue(instance.isEmpty());
	}

	@Test
	public void testContainsKey() {
		instance.put(1, 3.0);
		assertTrue(instance.containsKey(1));
		assertFalse(instance.containsKey(2));
	}

	@Test
	public void testContainsValue() {
		instance.put(1, 3.0);
		assertTrue(instance.containsValue(3.0));
		assertFalse(instance.containsValue(2.0));
	}

	@Test
	public void testEntrySet() {
		instance.put(1, 3.0);
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(1, entrySet.size());
	}

	@Test
	public void testGet() {
		instance.put(1, 3.0);
		assertEquals(Double.valueOf(3.0), instance.get(1));
		assertEquals(Double.valueOf(instance.defaultValue), instance.get(2));
	}

	@Test
	public void testIsEmpty() {
		assertTrue(instance.isEmpty());
		instance.put(1, 3.0);
		assertFalse(instance.isEmpty());
	}

	@Test
	public void testKeySet() {
		instance.put(1, 3.0);
		TreeSet<Integer> keySet = instance.keySet();
		assertEquals(1, keySet.size());
	}

	@Test
	public void testPut() {
		instance.put(1, 3.0);
		assertEquals(Double.valueOf(3.0), instance.get(1));
	}

	@Test
	public void testPutAll() {
		Map<Integer, Double> map = new HashMap<>();
		map.put(1, 3.0);
		instance.putAll(map);
		assertEquals(Double.valueOf(3.0), instance.get(1));
	}

	@Test
	public void testRemove() {
		instance.put(1, 3.0);
		instance.remove(1);
		assertFalse(instance.containsKey(1));
	}

	@Test
	public void testSize() {
		instance.put(1, 3.0);
		assertEquals(1, instance.size());
	}

	@Test
	public void testValues() {
		instance.put(1, 3.0);
		Collection<Double> values = instance.values();
		assertEquals(1, values.size());
	}

	@Test
	public void testNoAttributes() {
		instance.put(1, 3.0);
		assertEquals(2, instance.noAttributes());
	}

	@Test
	public void testRemoveAttribute() {
		instance.put(1, 3.0);
		instance.put(2, 4.0);
		assertEquals(3, instance.noAttributes());
		instance.removeAttribute(1);
		assertEquals(2, instance.noAttributes());
		assertFalse(instance.containsKey(1));
		assertEquals(Double.valueOf(4.0), instance.get(1));
	}

	@Test
	public void testToString() {
		instance.put(1, 3.0);
		String expected = "{1=3.0;null}";
		assertEquals(expected, instance.toString());
	}

	@Test
	public void testHashCode() {
		SparseInstance anotherInstance = new SparseInstance(3, 0.0);
		assertEquals(instance.hashCode(), anotherInstance.hashCode());
	}

	@Test
	public void testEquals() {
		SparseInstance anotherInstance = new SparseInstance(3, 0.0);
		assertTrue(instance.equals(anotherInstance));
	}

	@Test
	public void testCopy() {
		instance.put(1, 3.0);
		Instance copy = instance.copy();
		assertTrue(instance.equals(copy));
	}

	@Test
	public void testRemoveAttributes() {
		instance.put(1, 3.0);
		instance.put(2, 4.0);
		instance.put(3, 5.0);
		assertEquals(4, instance.noAttributes());
		Set<Integer> indices = new TreeSet<>();
		indices.add(2);
		indices.add(3);
		instance.removeAttributes(indices);
		assertEquals(2, instance.noAttributes());
		assertFalse(instance.containsKey(2));
		assertFalse(instance.containsKey(3));
		assertEquals(Double.valueOf(5.0), instance.get(2));
	}
}