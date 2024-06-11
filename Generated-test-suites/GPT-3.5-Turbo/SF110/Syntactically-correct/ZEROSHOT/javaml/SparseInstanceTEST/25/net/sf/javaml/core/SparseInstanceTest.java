package net.sf.javaml.core;

import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class SparseInstanceTest {

	@Test
	public void testSetNoAttributes() {
		SparseInstance s = new SparseInstance();
		s.setNoAttributes(5);
		assertEquals(5, s.noAttributes);
	}

	@Test
	public void testSparseInstance() {
		SparseInstance s = new SparseInstance();
		assertEquals(-1, s.noAttributes);
	}

	@Test
	public void testSparseInstanceInt() {
		SparseInstance s = new SparseInstance(5);
		assertEquals(5, s.noAttributes);
	}

	@Test
	public void testSparseInstanceIntDouble() {
		SparseInstance s = new SparseInstance(5, 0.0);
		assertEquals(5, s.noAttributes);
		assertEquals(0.0, s.defaultValue, 0.001);
	}

	@Test
	public void testSparseInstanceIntObject() {
		SparseInstance s = new SparseInstance(5, "classValue");
		assertEquals(5, s.noAttributes);
		assertEquals("classValue", s.classValue());
	}

	@Test
	public void testSparseInstanceIntDoubleObject() {
		SparseInstance s = new SparseInstance(5, 0.0, "classValue");
		assertEquals(5, s.noAttributes);
		assertEquals(0.0, s.defaultValue, 0.001);
		assertEquals("classValue", s.classValue());
	}

	@Test
	public void testSparseInstanceDoubleArray() {
		double[] datavector = {1.0, 2.0, 3.0};
		SparseInstance s = new SparseInstance(datavector);
		assertEquals(3, s.noAttributes);
	}

	@Test
	public void testSparseInstanceDoubleArrayDouble() {
		double[] datavector = {1.0, 2.0, 3.0};
		SparseInstance s = new SparseInstance(datavector, 0.0);
		assertEquals(3, s.noAttributes);
		assertEquals(0.0, s.defaultValue, 0.001);
	}

	@Test
	public void testSparseInstanceDoubleArrayObject() {
		double[] datavector = {1.0, 2.0, 3.0};
		SparseInstance s = new SparseInstance(datavector, "classValue");
		assertEquals(3, s.noAttributes);
		assertEquals("classValue", s.classValue());
	}

	@Test
	public void testSparseInstanceDoubleArrayDoubleObject() {
		double[] datavector = {1.0, 2.0, 3.0};
		SparseInstance s = new SparseInstance(datavector, 0.0, "classValue");
		assertEquals(3, s.noAttributes);
		assertEquals(0.0, s.defaultValue, 0.001);
		assertEquals("classValue", s.classValue());
	}

	@Test
	public void testInitiate() {
		double[] datavector = {1.0, 2.0, 3.0};
		SparseInstance s = new SparseInstance();
		s.initiate(datavector);
		assertEquals(3, s.noAttributes);
		assertEquals(1.0, s.get(0), 0.001);
		assertEquals(2.0, s.get(1), 0.001);
		assertEquals(3.0, s.get(2), 0.001);
	}

	@Test
	public void testValue() {
		double[] datavector = {1.0, 2.0, 3.0};
		SparseInstance s = new SparseInstance(datavector);
		assertEquals(1.0, s.value(0), 0.001);
	}

	@Test
	public void testClear() {
		double[] datavector = {1.0, 2.0, 3.0};
		SparseInstance s = new SparseInstance(datavector);
		s.clear();
		assertEquals(0, s.size());
	}

	@Test
	public void testContainsKey() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		assertTrue(s.containsKey(0));
		assertFalse(s.containsKey(1));
	}

	@Test
	public void testContainsValue() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		assertTrue(s.containsValue(1.0));
		assertFalse(s.containsValue(2.0));
	}

	@Test
	public void testEntrySet() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		Set<Map.Entry<Integer, Double>> entries = s.entrySet();
		assertEquals(1, entries.size());
	}

	@Test
	public void testGet() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		assertEquals(1.0, s.get(0), 0.001);
		assertEquals(0.0, s.get(1), 0.001);
	}

	@Test
	public void testIsEmpty() {
		SparseInstance s = new SparseInstance();
		assertTrue(s.isEmpty());
		s.put(0, 1.0);
		assertFalse(s.isEmpty());
	}

	@Test
	public void testKeySet() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		TreeSet<Integer> set = s.keySet();
		assertEquals(1, set.size());
	}

	@Test
	public void testPut() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		assertEquals(1.0, s.get(0), 0.001);
	}

	@Test
	public void testPutAll() {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		map.put(0, 1.0);
		map.put(1, 2.0);
		map.put(2, 3.0);
		SparseInstance s = new SparseInstance();
		s.putAll(map);
		assertEquals(1.0, s.get(0), 0.001);
		assertEquals(2.0, s.get(1), 0.001);
		assertEquals(3.0, s.get(2), 0.001);
	}

	@Test
	public void testRemove() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		s.remove(0);
		assertEquals(0, s.size());
	}

	@Test
	public void testSize() {
		SparseInstance s = new SparseInstance();
		assertEquals(0, s.size());
		s.put(0, 1.0);
		assertEquals(1, s.size());
	}

	@Test
	public void testValues() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		Collection<Double> values = s.values();
		assertEquals(1, values.size());
	}

	@Test
	public void testNoAttributes() {
		SparseInstance s = new SparseInstance();
		assertEquals(0, s.noAttributes());
		s.put(0, 1.0);
		assertEquals(1, s.noAttributes());
	}

	@Test
	public void testRemoveAttribute() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		s.put(1, 2.0);
		s.put(2, 3.0);
		s.removeAttribute(1);
		assertEquals(2, s.size());
		assertEquals(1.0, s.get(0), 0.001);
		assertEquals(3.0, s.get(1), 0.001);
	}

	@Test
	public void testToString() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		assertEquals("{0=1.0;null}", s.toString());
	}

	@Test
	public void testHashCode() {
		SparseInstance s1 = new SparseInstance();
		s1.put(0, 1.0);
		s1.put(1, 2.0);
		SparseInstance s2 = new SparseInstance();
		s2.put(0, 1.0);
		s2.put(1, 2.0);
		assertEquals(s1.hashCode(), s2.hashCode());
	}

	@Test
	public void testEquals() {
		SparseInstance s1 = new SparseInstance();
		s1.put(0, 1.0);
		s1.put(1, 2.0);
		SparseInstance s2 = new SparseInstance();
		s2.put(0, 1.0);
		s2.put(1, 2.0);
		assertTrue(s1.equals(s2));
	}

	@Test
	public void testCopy() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		s.put(1, 2.0);
		Instance copy = s.copy();
		assertTrue(copy instanceof SparseInstance);
		assertEquals(s.noAttributes(), copy.noAttributes());
		assertEquals(s.classValue(), copy.classValue());
		assertEquals(s.get(0), copy.get(0));
		assertEquals(s.get(1), copy.get(1));
	}

	@Test
	public void testRemoveAttributes() {
		SparseInstance s = new SparseInstance();
		s.put(0, 1.0);
		s.put(1, 2.0);
		s.put(2, 3.0);
		Set<Integer> indices = new HashSet<Integer>();
		indices.add(0);
		indices.add(2);
		s.removeAttributes(indices);
		assertEquals(1, s.size());
		assertEquals(2.0, s.get(0), 0.001);
	}

}