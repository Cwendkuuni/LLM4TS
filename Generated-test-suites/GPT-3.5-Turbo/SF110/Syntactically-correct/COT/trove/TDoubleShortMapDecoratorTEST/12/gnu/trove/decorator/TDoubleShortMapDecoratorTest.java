package gnu.trove.decorator;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class TDoubleShortMapDecoratorTest {
    
    // Put method test
    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        assertNull(map.put(key, value));
        assertEquals(value, map.get(key));
        
        // Test putting null key
        Short nullKeyVal = 3;
        assertNull(map.put(null, nullKeyVal));
        assertEquals(nullKeyVal, map.get(null));
        
        // Test putting null value
        Double nullValueKey = 4.0;
        assertNull(map.put(nullValueKey, null));
        assertNull(map.get(nullValueKey));
        
        // Test replacing existing value
        Short newValue = 5;
        assertEquals(value, map.put(key, newValue));
        assertEquals(newValue, map.get(key));
    }
    
    // Get method test
    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        assertNull(map.get(key));
        
        map.put(key, value);
        assertEquals(value, map.get(key));
        
        // Test getting null key
        assertNull(map.get(null));
        
        // Test getting null value
        Double nullValueKey = 4.0;
        map.put(nullValueKey, null);
        assertNull(map.get(nullValueKey));
    }
    
    // Clear method test
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, 2);
        map.put(3.0, 4);
        map.clear();
        assertTrue(map.isEmpty());
    }
    
    // Remove method test
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        assertNull(map.remove(key));
        
        map.put(key, value);
        assertEquals(value, map.remove(key));
        assertNull(map.get(key));
    }
    
    // EntrySet method tests
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key1 = 1.0;
        Double key2 = 2.0;
        Short value1 = 2;
        Short value2 = 3;
        
        map.put(key1, value1);
        map.put(key2, value2);
        
        Set<Map.Entry<Double, Short>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());
        
        // Test entrySet size after removing an entry
        map.remove(key1);
        assertEquals(1, entrySet.size());
        
        // Test contains method of entrySet
        Map.Entry<Double, Short> entry = new AbstractMap.SimpleEntry<>(key2, value2);
        assertTrue(entrySet.contains(entry));
        
        // Test iterator of entrySet
        Iterator<Map.Entry<Double, Short>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(entry, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    // ContainsValue method test
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        assertFalse(map.containsValue(value));
        
        map.put(key, value);
        assertTrue(map.containsValue(value));
    }
    
    // ContainsKey method test
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        Short value = 2;
        assertFalse(map.containsKey(key));
        
        map.put(key, value);
        assertTrue(map.containsKey(key));
        
        // Test containsKey with null key
        assertTrue(map.containsKey(null));
    }
    
    // Size method test
    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        
        map.put(1.0, 2);
        assertEquals(1, map.size());
    }
    
    // IsEmpty method test
    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertTrue(map.isEmpty());
        
        map.put(1.0, 2);
        assertFalse(map.isEmpty());
    }
    
    // PutAll method test
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Map<Double, Short> inputMap = new HashMap<>();
        inputMap.put(1.0, 2);
        inputMap.put(3.0, 4);
        
        map.putAll(inputMap);
        
        assertEquals(2, map.size());
        assertEquals((Short) 2, map.get(1.0));
        assertEquals((Short) 4, map.get(3.0));
    }
    
    // wrapKey method test
    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        double key = 1.0;
        assertEquals((Double) key, map.wrapKey(key));
    }
    
    // unwrapKey method test
    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double key = 1.0;
        assertEquals((double) key, map.unwrapKey(key), 0.0);
    }
    
    // wrapValue method test
    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        short value = 2;
        assertEquals((Short) value, map.wrapValue(value));
    }
    
    // unwrapValue method test
    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Short value = 2;
        assertEquals((short) value, map.unwrapValue(value));
    }
    
    // readExternal method test
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        TDoubleShortMap inputMap = new TDoubleShortHashMap();
        inputMap.put(1.0, (short) 2);
        map._map = inputMap;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        map.writeExternal(oos);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        
        TDoubleShortMapDecorator newMap = new TDoubleShortMapDecorator();
        newMap.readExternal(ois);
        ois.close();
        
        assertEquals(inputMap, newMap._map);
    }
    
    // writeExternal method test
    @Test
    public void testWriteExternal() throws IOException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map._map = new TDoubleShortHashMap();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        map.writeExternal(oos);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readByte(); // skip the first byte written by writeExternal
        TDoubleShortMap result = (TDoubleShortMap) ois.readObject();
        ois.close();
        
        assertEquals(map._map, result);
    }
}