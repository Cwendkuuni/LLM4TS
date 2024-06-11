
import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {
    
    // Initialize a TDoubleShortMapDecorator object for testing
    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
    }
    
    // Test put() method
    @Test
    public void testPut() {
        // Test inserting a new key-value pair
        Double key1 = 1.0;
        Short value1 = 2;
        Short result1 = mapDecorator.put(key1, value1);
        assertNull(result1);
        assertTrue(mapDecorator.containsKey(key1));
        assertEquals(value1, mapDecorator.get(key1));
        
        // Test replacing an existing value
        Double key2 = 3.0;
        Short value2 = 4;
        mapDecorator.put(key2, value2);
        Short newValue2 = 5;
        Short result2 = mapDecorator.put(key2, newValue2);
        assertEquals(value2, result2);
        assertEquals(newValue2, mapDecorator.get(key2));
        
        // Test inserting null key and value
        Double key3 = null;
        Short value3 = null;
        Short result3 = mapDecorator.put(key3, value3);
        assertNull(result3);
        assertTrue(mapDecorator.containsKey(null));
        assertEquals(value3, mapDecorator.get(null));
        
        // Test inserting null key with non-null value
        Double key4 = null;
        Short value4 = 6;
        Short result4 = mapDecorator.put(key4, value4);
        assertNull(result4);
        assertTrue(mapDecorator.containsKey(null));
        assertEquals(value4, mapDecorator.get(null));
    }
    
    // Test get() method
    @Test
    public void testGet() {
        // Test retrieving a value for an existing key
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertEquals(value1, mapDecorator.get(key1));
        
        // Test retrieving a value for a non-existing key
        Double key2 = 3.0;
        assertNull(mapDecorator.get(key2));
        
        // Test retrieving a value for a null key
        Double key3 = null;
        Short value3 = 4;
        mapDecorator.put(key3, value3);
        assertEquals(value3, mapDecorator.get(key3));
    }
    
    // Test clear() method
    @Test
    public void testClear() {
        // Test clearing an empty map
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
        
        // Test clearing a non-empty map
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        Double key2 = 3.0;
        Short value2 = 4;
        mapDecorator.put(key2, value2);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }
    
    // Test remove() method
    @Test
    public void testRemove() {
        // Test removing an existing key-value pair
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        Short result1 = mapDecorator.remove(key1);
        assertEquals(value1, result1);
        assertFalse(mapDecorator.containsKey(key1));
        
        // Test removing a non-existing key
        Double key2 = 3.0;
        assertNull(mapDecorator.remove(key2));
        
        // Test removing a null key
        Double key3 = null;
        Short value3 = 4;
        mapDecorator.put(key3, value3);
        Short result3 = mapDecorator.remove(key3);
        assertEquals(value3, result3);
        assertFalse(mapDecorator.containsKey(key3));
    }
    
    // Test entrySet() method
    @Test
    public void testEntrySet() {
        // Test getting entrySet from an empty map
        Set<Entry<Double, Short>> entrySet = mapDecorator.entrySet();
        assertTrue(entrySet.isEmpty());
        
        // Test getting entrySet from a non-empty map
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        Double key2 = 3.0;
        Short value2 = 4;
        mapDecorator.put(key2, value2);
        
        entrySet = mapDecorator.entrySet();
        assertEquals(2, entrySet.size());
        
        // Test iterating through the entrySet
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Entry<Double, Short> entry = iterator.next();
            Double key = entry.getKey();
            Short value = entry.getValue();
            assertTrue(mapDecorator.containsKey(key));
            assertEquals(value, mapDecorator.get(key));
        }
        
    }
    
    // Test containsValue() method
    @Test
    public void testContainsValue() {
        // Test checking for the existence of an existing value
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsValue(value1));
        
        // Test checking for the existence of a non-existing value
        Short value2 = 3;
        assertFalse(mapDecorator.containsValue(value2));
    }
    
    // Test containsKey() method
    @Test
    public void testContainsKey() {
        // Test checking for the existence of an existing key
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertTrue(mapDecorator.containsKey(key1));
        
        // Test checking for the existence of a non-existing key
        Double key2 = 3.0;
        assertFalse(mapDecorator.containsKey(key2));
    }
    
    // Test size() method
    @Test
    public void testSize() {
        // Test getting the size of an empty map
        assertEquals(0, mapDecorator.size());
        
        // Test getting the size of a non-empty map
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertEquals(1, mapDecorator.size());
        
        Double key2 = 3.0;
        Short value2 = 4;
        mapDecorator.put(key2, value2);
        assertEquals(2, mapDecorator.size());
    }
    
    // Test isEmpty() method
    @Test
    public void testIsEmpty() {
        // Test checking if an empty map is empty
        assertTrue(mapDecorator.isEmpty());
        
        // Test checking if a non-empty map is empty
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        assertFalse(mapDecorator.isEmpty());
    }
    
    // Test putAll() method
    @Test
    public void testPutAll() {
        // Test putting all key-value pairs from an empty map
        Map<Double, Short> map = new HashMap<Double, Short>();
        mapDecorator.putAll(map);
        assertTrue(mapDecorator.isEmpty());
        
        // Test putting all key-value pairs from a non-empty map
        Double key1 = 1.0;
        Short value1 = 2;
        map.put(key1, value1);
        Double key2 = 3.0;
        Short value2 = 4;
        map.put(key2, value2);
        mapDecorator.putAll(map);
        assertTrue(mapDecorator.containsKey(key1));
        assertTrue(mapDecorator.containsKey(key2));
        assertEquals(value1, mapDecorator.get(key1));
        assertEquals(value2, mapDecorator.get(key2));
    }
    
    // Test wrapKey() method
    @Test
    public void testWrapKey() {
        // Test wrapping a double key
        double key1 = 1.0;
        Double wrappedKey1 = mapDecorator.wrapKey(key1);
        assertEquals(key1, wrappedKey1, 0.01);
        
        // Test wrapping a null key
        double key2 = mapDecorator.getMap().getNoEntryKey();
        Double wrappedKey2 = mapDecorator.wrapKey(key2);
        assertNull(wrappedKey2);
    }
    
    // Test unwrapKey() method
    @Test
    public void testUnwrapKey() {
        // Test unwrapping a Double key
        Double key1 = 1.0;
        double unwrappedKey1 = mapDecorator.unwrapKey(key1);
        assertEquals(key1, unwrappedKey1, 0.01);
        
        // Test unwrapping a null key
        double key2 = mapDecorator.getMap().getNoEntryKey();
        Double unwrappedKey2 = mapDecorator.unwrapKey(key2);
        assertNull(unwrappedKey2);
    }
    
    // Test wrapValue() method
    @Test
    public void testWrapValue() {
        // Test wrapping a short value
        short value1 = 2;
        Short wrappedValue1 = mapDecorator.wrapValue(value1);
        assertEquals(value1, (short)wrappedValue1);
    }
    
    // Test unwrapValue() method
    @Test
    public void testUnwrapValue() {
        // Test unwrapping a Short value
        Short value1 = 2;
        short unwrappedValue1 = mapDecorator.unwrapValue(value1);
        assertEquals(value1, (short)unwrappedValue1);
    }
    
    // Test readExternal() method
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        // Test reading external data into the map
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        mapDecorator.writeExternal(oos);
        oos.flush();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        TDoubleShortMapDecorator readMapDecorator = new TDoubleShortMapDecorator();
        readMapDecorator.readExternal(ois);
        assertTrue(readMapDecorator.containsKey(key1));
        assertEquals(value1, readMapDecorator.get(key1));
    }
    
    // Test writeExternal() method
    @Test
    public void testWriteExternal() throws IOException {
        // Test writing the map to external data
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        Double key1 = 1.0;
        Short value1 = 2;
        mapDecorator.put(key1, value1);
        mapDecorator.writeExternal(oos);
        oos.flush();
        byte[] externalData = bos.toByteArray();
        assertNotNull(externalData);
    }
}

