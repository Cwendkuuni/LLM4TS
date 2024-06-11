import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {
	
	@Test
	public void testConstructorWithAtt() {
		double[] att = {1.0, 2.0, 3.0};
		Object classValue = "class";
		DenseInstance instance = new DenseInstance(att, classValue);
		
		assertArrayEquals(att, instance.value());
		assertEquals(classValue, instance.classValue());
	}
	
	@Test
	public void testConstructorWithSize() {
		int size = 3;
		DenseInstance instance = new DenseInstance(size);
		
		double[] expected = new double[size];
		assertArrayEquals(expected, instance.value());
		assertNull(instance.classValue());
	}
	
	@Test
	public void testValue() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		assertEquals(1.0, instance.value(0), 0.0001);
		assertEquals(2.0, instance.value(1), 0.0001);
		assertEquals(3.0, instance.value(2), 0.0001);
	}
	
	@Test
	public void testClear() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		instance.clear();
		
		double[] expected = {0.0, 0.0, 0.0};
		assertArrayEquals(expected, instance.value());
	}
	
	@Test
	public void testContainsKey() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertFalse(instance.containsKey(-1));
		assertFalse(instance.containsKey(3));
		assertFalse(instance.containsKey("key"));
	}
	
	@Test
	public void testContainsValue() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		assertTrue(instance.containsValue(1.0));
		assertTrue(instance.containsValue(2.0));
		assertTrue(instance.containsValue(3.0));
		assertFalse(instance.containsValue(4.0));
		assertFalse(instance.containsValue("value"));
	}
	
	@Test
	public void testEntrySet() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		Map<Integer, Double> expected = new HashMap<>();
		expected.put(0, 1.0);
		expected.put(1, 2.0);
		expected.put(2, 3.0);
		
		assertEquals(expected.entrySet(), instance.entrySet());
	}
	
	@Test
	public void testGet() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		assertEquals(1.0, instance.get(0), 0.0001);
		assertEquals(2.0, instance.get(1), 0.0001);
		assertEquals(3.0, instance.get(2), 0.0001);
	}
	
	@Test
	public void testIsEmpty() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		assertFalse(instance.isEmpty());
	}
	
	@Test
	public void testKeySet() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		SortedSet<Integer> expected = new TreeSet<>();
		expected.add(0);
		expected.add(1);
		expected.add(2);
		
		assertEquals(expected, instance.keySet());
	}
	
	@Test
	public void testPut() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		Integer key = 1;
		Double value = 4.0;
		
		Double oldValue = instance.put(key, value);
		
		assertEquals(2.0, oldValue, 0.0001);
		assertEquals(value, instance.get(key));
	}
	
	@Test
	public void testPutAll() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		Map<Integer, Double> map = new HashMap<>();
		map.put(0, 4.0);
		map.put(1, 5.0);
		map.put(2, 6.0);
		
		instance.putAll(map);
		
		assertEquals(4.0, instance.get(0), 0.0001);
		assertEquals(5.0, instance.get(1), 0.0001);
		assertEquals(6.0, instance.get(2), 0.0001);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		instance.remove(0);
	}
	
	@Test
	public void testSize() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		assertEquals(att.length, instance.size());
	}
	
	@Test
	public void testValues() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		Collection<Double> expected = new ArrayList<>();
		expected.add(1.0);
		expected.add(2.0);
		expected.add(3.0);
		
		assertEquals(expected, instance.values());
	}
	
	@Test
	public void testNoAttributes() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		assertEquals(att.length, instance.noAttributes());
	}
	
	@Test
	public void testToString() {
		double[] att = {1.0, 2.0, 3.0};
		Object classValue = "class";
		DenseInstance instance = new DenseInstance(att, classValue);
		
		String expected = "{[1.0, 2.0, 3.0]; class}";
		
		assertEquals(expected, instance.toString());
	}
	
	@Test
	public void testRemoveAttribute() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		int index = 1;
		
		instance.removeAttribute(index);
		
		double[] expected = {1.0, 3.0};
		assertArrayEquals(expected, instance.value());
	}
	
	@Test
	public void testHashCode() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		int expected = Arrays.hashCode(att);
		
		assertEquals(expected, instance.hashCode());
	}
	
	@Test
	public void testEquals() {
		// Test when compared with itself
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		assertTrue(instance.equals(instance));
		
		// Test when compared with another instance with same attributes
		DenseInstance other = new DenseInstance(att);
		
		assertTrue(instance.equals(other));
		assertTrue(other.equals(instance));
		
		// Test when compared with another instance with different attributes
		double[] att2 = {2.0, 3.0, 4.0};
		DenseInstance different = new DenseInstance(att2);
		
		assertFalse(instance.equals(different));
		assertFalse(different.equals(instance));
	}
	
	@Test
	public void testCopy() {
		double[] att = {1.0, 2.0, 3.0};
		Object classValue = "class";
		DenseInstance instance = new DenseInstance(att, classValue);
		
		Instance copy = instance.copy();
		
		assertNotSame(instance, copy);
		assertArrayEquals(att, copy.value());
		assertEquals(classValue, copy.classValue());
	}
	
	@Test
	public void testRemoveAttributes() {
		double[] att = {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(att);
		
		Set<Integer> indices = new HashSet<>();
		indices.add(1);
		
		instance.removeAttributes(indices);
		
		double[] expected = {1.0, 3.0};
		assertArrayEquals(expected, instance.value());
	}
}