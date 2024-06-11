import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {
	
	SparseInstance instance;
	
	@Test
	public void testSetNoAttributes() {
		instance = new SparseInstance();
		instance.setNoAttributes(5);
		assertEquals(5, instance.noAttributes());
	}
	
	@Test
	public void testSparseInstance() {
		instance = new SparseInstance();
		assertEquals(-1, instance.noAttributes());
	}
	
	@Test
	public void testSparseInstanceWithNoAttributes() {
		instance = new SparseInstance(5);
		assertEquals(5, instance.noAttributes());
		assertEquals(0.0, instance.defaultValue, 0.001);
		assertNull(instance.classValue());
		assertTrue(instance.isEmpty());
	}
	
	@Test
	public void testSparseInstanceWithDefaultValue() {
		instance = new SparseInstance(5, 1.0);
		assertEquals(1.0, instance.defaultValue, 0.001);
	}
	
	@Test
	public void testSparseInstanceWithClassValue() {
		instance = new SparseInstance(5, "A");
		assertEquals("A", instance.classValue());
	}
	
	@Test
	public void testSparseInstanceWithDefaultValueAndClassValue() {
		instance = new SparseInstance(5, 1.0, "A");
		assertEquals(1.0, instance.defaultValue, 0.001);
		assertEquals("A", instance.classValue());
	}

	@Test
	public void testSparseInstanceWithDataVector() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertEquals(3, instance.noAttributes());
		assertEquals(0.0, instance.defaultValue, 0.001);
		assertNull(instance.classValue());
		assertFalse(instance.isEmpty());
	}
	
	@Test
	public void testSparseInstanceWithDataVectorAndDefaultValue() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector, 1.0);
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertEquals(0.0, instance.get(0), 0.001);
		assertEquals(0.0, instance.get(1), 0.001);
		assertEquals(0.0, instance.get(2), 0.001);
		assertEquals(1.0, instance.defaultValue, 0.001);
	}
	
	@Test
	public void testSparseInstanceWithDataVectorAndClassValue() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector, "A");
		assertEquals("A", instance.classValue());
	}
	
	@Test
	public void testSparseInstanceWithDataVectorAndDefaultValueAndClassValue() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector, 1.0, "A");
		assertEquals(1.0, instance.defaultValue, 0.001);
		assertEquals("A", instance.classValue());
	}
	
	@Test
	public void testInitiate() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance();
		instance.initiate(datavector);
		assertEquals(3, instance.noAttributes());
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertEquals(1.0, instance.get(0), 0.001);
		assertEquals(2.0, instance.get(1), 0.001);
		assertEquals(3.0, instance.get(2), 0.001);
	}
	
	@Test
	public void testValue() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertEquals(1.0, instance.value(0), 0.001);
		assertEquals(2.0, instance.value(1), 0.001);
		assertEquals(3.0, instance.value(2), 0.001);
	}
	
	@Test
	public void testClear() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertFalse(instance.isEmpty());
		instance.clear();
		assertTrue(instance.isEmpty());
	}
	
	@Test
	public void testContainsKey() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertFalse(instance.containsKey(3));
	}
	
	@Test
	public void testContainsValue() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertTrue(instance.containsValue(1.0));
		assertTrue(instance.containsValue(2.0));
		assertTrue(instance.containsValue(3.0));
		assertFalse(instance.containsValue(4.0));
	}
	
	@Test
	public void testEntrySet() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertNotNull(instance.entrySet());
		assertEquals(3, instance.entrySet().size());
	}
	
	@Test
	public void testGet() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertEquals(1.0, instance.get(0), 0.001);
		assertEquals(2.0, instance.get(1), 0.001);
		assertEquals(3.0, instance.get(2), 0.001);
		assertEquals(0.0, instance.get(3), 0.001);
	}
	
	@Test
	public void testIsEmpty() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertFalse(instance.isEmpty());
		instance.clear();
		assertTrue(instance.isEmpty());
	}
	
	@Test
	public void testKeySet() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertNotNull(instance.keySet());
		assertEquals(3, instance.keySet().size());
		assertTrue(instance.keySet().contains(0));
		assertTrue(instance.keySet().contains(1));
		assertTrue(instance.keySet().contains(2));
		assertFalse(instance.keySet().contains(3));
	}
	
	@Test
	public void testPut() {
		instance = new SparseInstance();
		assertNull(instance.put(0, 1.0));
		assertNotNull(instance.put(0, 2.0));
	}
	
	@Test
	public void testPutAll() {
		SparseInstance sourceInstance = new SparseInstance();
		sourceInstance.put(0, 1.0);
		sourceInstance.put(1, 2.0);
		sourceInstance.put(2, 3.0);
		
		instance = new SparseInstance();
		instance.putAll(sourceInstance);
		
		assertEquals(1.0, instance.get(0), 0.001);
		assertEquals(2.0, instance.get(1), 0.001);
		assertEquals(3.0, instance.get(2), 0.001);
	}
	
	@Test
	public void testRemove() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertEquals(1.0, instance.remove(0), 0.001);
		assertFalse(instance.containsKey(0));
	}
	
	@Test
	public void testSize() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertEquals(3, instance.size());
	}
	
	@Test
	public void testValues() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		assertNotNull(instance.values());
		assertEquals(3, instance.values().size());
		assertTrue(instance.values().contains(1.0));
		assertTrue(instance.values().contains(2.0));
		assertTrue(instance.values().contains(3.0));
		assertFalse(instance.values().contains(4.0));
	}
	
	@Test
	public void testNoAttributes() {
		instance = new SparseInstance();
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		assertEquals(2, instance.noAttributes());
	}
	
	@Test
	public void testRemoveAttribute() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		instance.removeAttribute(1);
		assertFalse(instance.containsKey(1));
		assertEquals(2.0, instance.get(2), 0.001);
		assertEquals(2, instance.noAttributes());
	}
	
	@Test
	public void testToString() {
		instance = new SparseInstance();
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		assertEquals("{0=1.0;null}", instance.toString());
	}
	
	@Test
	public void testHashCode() {
		instance = new SparseInstance();
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		assertEquals(31 * 1 + 31 * 2 + 0, instance.hashCode());
	}
	
	@Test
	public void testEquals() {
		instance = new SparseInstance();
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		SparseInstance sameInstance = new SparseInstance();
		sameInstance.put(0, 1.0);
		sameInstance.put(1, 2.0);
		SparseInstance differentInstance = new SparseInstance();
		differentInstance.put(0, 1.0);
		differentInstance.put(1, 3.0);
		
		assertTrue(instance.equals(sameInstance));
		assertFalse(instance.equals(differentInstance));
	}
	
	@Test
	public void testCopy() {
		instance = new SparseInstance();
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		SparseInstance copyInstance = (SparseInstance) instance.copy();
		
		assertEquals(instance.toString(), copyInstance.toString());
	}
	
	@Test
	public void testRemoveAttributes() {
		double[] datavector = {1.0, 2.0, 3.0};
		instance = new SparseInstance(datavector);
		instance.removeAttributes(new HashSet<Integer>(Arrays.asList(1, 2)));
		assertFalse(instance.containsKey(1));
		assertFalse(instance.containsKey(2));
		assertEquals(1, instance.noAttributes());
	}
	
}