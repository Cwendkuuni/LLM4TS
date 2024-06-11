import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.map.*;
import gnu.trove.decorator.TDoubleShortMapDecorator;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator mapDecorator;
    private TDoubleShortMap map;
    
    @Before
    public void setUp() {
        map = new TDoubleShortHashMap();
        mapDecorator = new TDoubleShortMapDecorator(map);
    }
    
    @Test
    public void testPut() {
        Double key = 1.0;
        Short value = 2;
        
        mapDecorator.put(key, value);
        
        assertEquals(value, map.get(key));
    }
    
    @Test
    public void testPutNullKey() {
        Double key = null;
        Short value = 2;
        
        mapDecorator.put(key, value);
        
        assertEquals(value, map.getNoEntryKey());
    }
    
    @Test
    public void testPutNullValue() {
        Double key = 1.0;
        Short value = null;
        
        mapDecorator.put(key, value);
        
        assertEquals(value, map.getNoEntryValue());
    }
    
    @Test
    public void testGet() {
        Double key = 1.0;
        Short value = 2;
        
        map.put(key, value);
        Short retrievedValue = mapDecorator.get(key);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testGetNullKey() {
        Double key = null;
        
        Short retrievedValue = mapDecorator.get(key);
        
        assertEquals(map.getNoEntryValue(), retrievedValue);
    }
    
    @Test
    public void testClear() {
        map.put(1.0, 2);
        mapDecorator.clear();
        
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        Double key = 1.0;
        Short value = 2;
        
        map.put(key, value);
        Short removedValue = mapDecorator.remove(key);
        
        assertEquals(value, removedValue);
    }
    
    @Test
    public void testRemoveNullKey() {
        Double key = null;
        
        Short removedValue = mapDecorator.remove(key);
        
        assertNull(removedValue);
    }
    
    // Additional test cases for edge cases and exceptions:
    
    @Test
    public void testEntrySetSize() {
        map.put(1.0, 2);
        map.put(3.0, 4);
        map.put(5.0, 6);
        
        assertEquals(map.size(), mapDecorator.entrySet().size());
    }
    
    @Test
    public void testContainsEntry() {
        Double key = 1.0;
        Short value = 2;
        
        map.put(key, value);
        assertTrue(mapDecorator.entrySet().contains(map.entrySet().iterator().next()));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAdd() {
        mapDecorator.entrySet().add(map.entrySet().iterator().next());
    }
    
    @Test
    public void testContainsValue() {
        Short value = 2;
        
        map.put(1.0, value);
        assertTrue(mapDecorator.containsValue(value));
    }
    
    @Test
    public void testContainsKey() {
        Double key = 1.0;
        
        map.put(key, 2);
        assertTrue(mapDecorator.containsKey(key));
    }
    
    @Test
    public void testSize() {
        map.put(1.0, 2);
        assertEquals(map.size(), mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> testMap = new HashMap<>();
        testMap.put(1.0, 2);
        testMap.put(3.0, 4);
        
        mapDecorator.putAll(testMap);
        
        assertEquals(testMap.size(), mapDecorator.size());
    }
    
    @Test
    public void testWrapKey() {
        double k = 1.0;
        assertEquals(k, mapDecorator.wrapKey(k), 0.0);
    }
    
    @Test
    public void testUnwrapKey() {
        Object key = 1.0;
        assertEquals(key, mapDecorator.unwrapKey(key));
    }
    
    @Test
    public void testWrapValue() {
        short k = 2;
        assertEquals(k, mapDecorator.wrapValue(k), 0);
    }
    
    @Test
    public void testUnwrapValue() {
        Object value = 2;
        assertEquals(value, mapDecorator.unwrapValue(value));
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(mapDecorator);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        
        TDoubleShortMapDecorator deserializedMap = (TDoubleShortMapDecorator) ois.readObject();
        
        assertNotNull(deserializedMap);
        assertEquals(mapDecorator.getMap(), deserializedMap.getMap());
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(mapDecorator);
        oos.close();
        
        byte[] bytes = baos.toByteArray();
        assertTrue(bytes.length > 0);
    }
}