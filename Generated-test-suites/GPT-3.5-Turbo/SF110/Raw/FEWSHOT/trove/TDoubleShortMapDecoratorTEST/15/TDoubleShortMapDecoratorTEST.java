package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TDoubleShortMap;

public class TDoubleShortMapDecoratorTest {
	
	TDoubleShortMap _map = new TDoubleShortMap();

	@Test
	public void testPut() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		Double key1 = 1.0;
		Short value1 = 2;
		assertEquals(null, decorator.put(key1, value1));
		
		Double key2 = null;
		Short value2 = 3;
		assertEquals(null, decorator.put(key2, value2));
		
		assertEquals(value1, decorator.put(key1, value2));
	}
	
	@Test
	public void testGet() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		Double key1 = 1.0;
		Short value1 = 2;
		assertNull(decorator.get(key1));
		
		Double key2 = null;
		Short value2 = 3;
		assertNull(decorator.get(key2));
		
		_map.put(1.0, 2);
		_map.put(2.0, 3);
		
		assertEquals(value1, decorator.get(key1));
		assertEquals(value2, decorator.get(key2));
	}
	
	@Test
	public void testClear() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		_map.put(1.0, 2);
		_map.put(2.0, 3);
		
		decorator.clear();
		
		assertEquals(0, decorator.size());
		assertTrue(decorator.isEmpty());
	}
	
	@Test
	public void testRemove() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		Double key1 = 1.0;
		Short value1 = 2;
		assertNull(decorator.remove(key1));
		
		Double key2 = null;
		Short value2 = 3;
		assertNull(decorator.remove(key2));
		
		_map.put(1.0, 2);
		_map.put(2.0, 3);
		
		assertEquals(value1, decorator.remove(key1));
		assertEquals(null, decorator.get(key1));
	}
	
	@Test
	public void testEntrySet() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		_map.put(1.0, 2);
		_map.put(2.0, 3);

		Set<Map.Entry<Double, Short>> entrySet = decorator.entrySet();
		Iterator<Map.Entry<Double, Short>> it = entrySet.iterator();
		
		Map.Entry<Double, Short> entry = it.next();
		assertEquals(1.0, entry.getKey(), 0.0);
		assertEquals(2, entry.getValue().shortValue());
		
		entry = it.next();
		assertEquals(2.0, entry.getKey(), 0.0);
		assertEquals(3, entry.getValue().shortValue());
		
		assertEquals(2, entrySet.size());
		assertTrue(entrySet.contains(entry));
		assertTrue(entrySet.isEmpty());
		
		entrySet.clear();
		
		assertEquals(0, entrySet.size());
		assertTrue(entrySet.isEmpty());
		
	}
	
	@Test
	public void testContainsValue() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		_map.put(1.0, 2);
		_map.put(2.0, 3);
		
		assertTrue(decorator.containsValue(new Short((short)2)));
		assertTrue(decorator.containsValue(new Short((short)3)));
	}
	
	@Test
	public void testContainsKey() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		_map.put(1.0, 2);
		_map.put(2.0, 3);
		
		assertTrue(decorator.containsKey(1.0));
		assertTrue(decorator.containsKey(2.0));
	}
	
	@Test
	public void testSize() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		_map.put(1.0, 2);
		_map.put(2.0, 3);
		
		assertEquals(2, decorator.size());
	}
	
	@Test
	public void testIsEmpty() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		assertTrue(decorator.isEmpty());
		
		_map.put(1.0, 2);
		_map.put(2.0, 3);
		
		assertTrue(!decorator.isEmpty());
	}
	
	@Test
	public void testPutAll() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		Map<Double, Short> putMap = new AbstractMap<Double, Short>() {
			@Override
			public Set<java.util.Map.Entry<Double, Short>> entrySet() {
				return null;
			}
		};
		
		putMap.put(1.0, (short)2);
		putMap.put(2.0, (short)3);
		
		decorator.putAll(putMap);
		
		assertEquals((short)2, decorator.get(1.0).shortValue());
		assertEquals((short)3, decorator.get(2.0).shortValue());
	}
	
	@Test
	public void testWrapKey() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		Double key = 1.0;
		assertEquals(1.0, decorator.wrapKey(1.0), 0.0);
	}
	
	@Test
	public void testUnwrapKey() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		Double key = 1.0;
		assertEquals(1.0, decorator.unwrapKey(key), 0.0);
	}
	
	@Test
	public void testWrapValue() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		Short value = 2;
		assertEquals((short)2, decorator.wrapValue(value).shortValue());
	}
	
	@Test
	public void testUnwrapValue() {
		TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
		
		Short value = 2;
		assertEquals((short)2, decorator.unwrapValue(value));
	}
}