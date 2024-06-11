import java.util.Set;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {
	
	private SparseInstance instance;
	
	@Before
	public void setup() {
		instance = new SparseInstance();
	}
	
	@Test
	public void testSetNoAttributes() {
		int noAttributes = 5;
		instance.setNoAttributes(noAttributes);
		Assert.assertEquals(noAttributes, instance.noAttributes());
	}
	
	@Test
	public void testSparseInstance() {
		instance = new SparseInstance();
		Assert.assertNotNull(instance);
		Assert.assertEquals(-1, instance.noAttributes());
		Assert.assertEquals(0.0, instance.defaultValue, 0.0);
		Assert.assertNull(instance.classValue());
		Assert.assertTrue(instance.isEmpty());
		Assert.assertEquals(0, instance.size());
	}
	
	@Test
	public void testSparseInstanceWithNoAttributes() {
		int noAttributes = 5;
		instance = new SparseInstance(noAttributes);
		Assert.assertNotNull(instance);
		Assert.assertEquals(noAttributes, instance.noAttributes());
		Assert.assertEquals(0.0, instance.defaultValue, 0.0);
		Assert.assertNull(instance.classValue());
		Assert.assertTrue(instance.isEmpty());
		Assert.assertEquals(0, instance.size());
	}
	
	@Test
	public void testSparseInstanceWithDefaultValue() {
		double defaultValue = 2.0;
		instance = new SparseInstance(5, defaultValue);
		Assert.assertNotNull(instance);
		Assert.assertEquals(5, instance.noAttributes());
		Assert.assertEquals(defaultValue, instance.defaultValue, 0.0);
		Assert.assertNull(instance.classValue());
		Assert.assertTrue(instance.isEmpty());
		Assert.assertEquals(0, instance.size());
	}
	
	@Test
	public void testSparseInstanceWithClassValue() {
		String classValue = "class";
		instance = new SparseInstance(5, classValue);
		Assert.assertNotNull(instance);
		Assert.assertEquals(5, instance.noAttributes());
		Assert.assertEquals(0.0, instance.defaultValue, 0.0);
		Assert.assertEquals(classValue, instance.classValue());
		Assert.assertTrue(instance.isEmpty());
		Assert.assertEquals(0, instance.size());
	}
	
	@Test
	public void testSparseInstanceWithAllParameters() {
		String classValue = "class";
		instance = new SparseInstance(5, 2.0, classValue);
		Assert.assertNotNull(instance);
		Assert.assertEquals(5, instance.noAttributes());
		Assert.assertEquals(2.0, instance.defaultValue, 0.0);
		Assert.assertEquals(classValue, instance.classValue());
		Assert.assertTrue(instance.isEmpty());
		Assert.assertEquals(0, instance.size());
	}
	
	@Test
	public void testInitiate() {
		double[] datavector = {1.0, 2.0, 0.0, 3.0};
		instance.initiate(datavector);
		Assert.assertNotNull(instance);
		Assert.assertEquals(4, instance.noAttributes());
		Assert.assertEquals(0.0, instance.defaultValue, 0.0);
		Assert.assertNull(instance.classValue());
		
		Assert.assertTrue(instance.containsKey(0));
		Assert.assertTrue(instance.containsKey(1));
		Assert.assertTrue(instance.containsKey(3));
		
		Assert.assertEquals(0.0, instance.get(2), 0.0);
		Assert.assertEquals(1.0, instance.get(0), 0.0);
	}
	
	@Test
	public void testValue() {
		int pos = 2;
		Assert.assertEquals(0.0, instance.value(pos), 0.0);
		
		instance.put(pos, 3.0);
		Assert.assertEquals(3.0, instance.value(pos), 0.0);
	}
	
	@Test
	public void testClear() {
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		
		Assert.assertFalse(instance.isEmpty());
		Assert.assertEquals(2, instance.size());
		
		instance.clear();
		
		Assert.assertTrue(instance.isEmpty());
		Assert.assertEquals(0, instance.size());
	}
	
	@Test
	public void testContainsKey() {
		Assert.assertFalse(instance.containsKey(0));
		
		instance.put(0, 1.0);
		
		Assert.assertTrue(instance.containsKey(0));
	}
	
	@Test
	public void testContainsValue() {
		Assert.assertFalse(instance.containsValue(1.0));
		
		instance.put(0, 1.0);
		
		Assert.assertTrue(instance.containsValue(1.0));
	}
	
	@Test
	public void testEntrySet() {
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		
		Assert.assertEquals(2, entrySet.size());
	}
	
	@Test
	public void testGet() {
		Double defaultValue = 5.0;
		Assert.assertEquals(5.0, instance.get(0), 0.0);
		
		instance.put(0, 6.0);
		
		Assert.assertEquals(6.0, instance.get(0), 0.0);
	}
	
	@Test
	public void testIsEmpty() {
		Assert.assertTrue(instance.isEmpty());
		
		instance.put(0, 1.0);
		
		Assert.assertFalse(instance.isEmpty());
	}
	
	@Test
	public void testKeySet() {
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		
		Set<Integer> keySet = instance.keySet();
		
		Assert.assertEquals(2, keySet.size());
	}
	
	@Test
	public void testPut() {
		Assert.assertNull(instance.put(0, 1.0));
		
		Assert.assertTrue(instance.containsKey(0));
		Assert.assertEquals(1.0, instance.get(0), 0.0);
	}
	
	@Test
	public void testPutAll() {
		Map<Integer, Double> map = new HashMap<>();
		map.put(0, 1.0);
		map.put(1, 2.0);
		
		instance.putAll(map);
		
		Assert.assertEquals(2, instance.size());
		Assert.assertTrue(instance.containsKey(0));
		Assert.assertTrue(instance.containsKey(1));
		Assert.assertEquals(1.0, instance.get(0), 0.0);
		Assert.assertEquals(2.0, instance.get(1), 0.0);
	}
	
	@Test
	public void testRemove() {
		instance.put(0, 1.0);
		
		Assert.assertEquals(1.0, instance.remove(0), 0.0);
		
		Assert.assertFalse(instance.containsKey(0));
	}
	
	@Test
	public void testSize() {
		Assert.assertEquals(0, instance.size());
		
		instance.put(0, 1.0);
		
		Assert.assertEquals(1, instance.size());
	}
	
	@Test
	public void testValues() {
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		
		Collection<Double> values = instance.values();
		
		Assert.assertEquals(2, values.size());
	}
	
	@Test
	public void testNoAttributes() {
		instance.put(0, 1.0);
		
		Assert.assertEquals(1, instance.noAttributes());
	}
	
	@Test
	public void testRemoveAttribute() {
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		
		instance.removeAttribute(1);
		
		Assert.assertFalse(instance.containsKey(1));
		Assert.assertEquals(1, instance.size());
		Assert.assertEquals(1, instance.noAttributes());
	}
	
	@Test
	public void testToString() {
		instance.put(0, 1.0);
		instance.setClassValue("class");
		
		Assert.assertEquals("{0=1.0;class}", instance.toString());
	}
	
	@Test
	public void testHashCode() {
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		
		int hashCode = instance.hashCode();
		
		Assert.assertEquals(1900526279, hashCode);
	}
	
	@Test
	public void testEquals() {
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		
		Assert.assertFalse(instance.equals(null));
		Assert.assertFalse(instance.equals(new Object()));
		
		SparseInstance other = new SparseInstance();
		other.put(0, 1.0);
		other.put(1, 2.0);
		
		Assert.assertTrue(instance.equals(other));
	}
	
	@Test
	public void testCopy() {
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		instance.setClassValue("class");
		
		SparseInstance copy = (SparseInstance) instance.copy();
		
		Assert.assertEquals(2, copy.size());
		Assert.assertTrue(copy.containsKey(0));
		Assert.assertTrue(copy.containsKey(1));
		Assert.assertEquals(1.0, copy.get(0), 0.0);
		Assert.assertEquals(2.0, copy.get(1), 0.0);
		Assert.assertEquals("class", copy.classValue());
	}
	
	@Test
	public void testRemoveAttributes() {
		instance.put(0, 1.0);
		instance.put(1, 2.0);
		instance.put(2, 3.0);
		
		Set<Integer> indices = new HashSet<>();
		indices.add(1);
		indices.add(2);
		
		instance.removeAttributes(indices);
		
		Assert.assertEquals(1, instance.size());
		Assert.assertTrue(instance.containsKey(0));
		Assert.assertFalse(instance.containsKey(1));
		Assert.assertFalse(instance.containsKey(2));
	}
}