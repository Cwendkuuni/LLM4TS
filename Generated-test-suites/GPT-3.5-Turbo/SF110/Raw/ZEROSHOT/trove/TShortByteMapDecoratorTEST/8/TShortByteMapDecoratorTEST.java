import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator mapDecorator;
    private TShortByteMap map;
    
    @Before
    public void setUp() {
        map = new TShortByteHashMap();
        mapDecorator = new TShortByteMapDecorator(map);
    }
    
    @After
    public void tearDown() {
        map.clear();
        mapDecorator = null;
    }
    
    @Test
    public void testPut() {
        Short key = 1;
        Byte value = 2;
        assertEquals(value, mapDecorator.put(key, value));
        assertEquals(value, mapDecorator.get(key));
        
        assertNull(mapDecorator.put(null, value));
        assertEquals(value, mapDecorator.get(null));
    }
    
    @Test
    public void testGet() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
        
        assertNull(mapDecorator.get(null));
    }
    
    @Test
    public void testClear() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertFalse(map.isEmpty());
        
        mapDecorator.clear();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
        assertNull(mapDecorator.get(key));
        
        assertNull(mapDecorator.remove(null));
        assertNull(mapDecorator.get(null));
    }
    
    @Test
    public void testEntrySet() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        assertEquals(1, entrySet.size());
        Iterator<Map.Entry<Short, Byte>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());
        Map.Entry<Short, Byte> entry = iterator.next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
        assertFalse(iterator.hasNext());
        
        iterator.remove();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testContainsValue() {
        Byte value = 2;
        assertFalse(mapDecorator.containsValue(value));
        
        Short key = 1;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.containsValue(value));
    }
    
    @Test
    public void testContainsKey() {
        Short key = 1;
        assertFalse(mapDecorator.containsKey(key));
        
        mapDecorator.put(key, null);
        assertTrue(mapDecorator.containsKey(key));
        
        assertFalse(mapDecorator.containsKey(null));
        
        mapDecorator.put(null, null);
        assertTrue(mapDecorator.containsKey(null));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(1, mapDecorator.size());
        
        mapDecorator.put(null, null);
        assertEquals(2, mapDecorator.size());
        
        mapDecorator.remove(key);
        assertEquals(1, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertFalse(mapDecorator.isEmpty());
        
        mapDecorator.remove(key);
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Short, Byte> inputMap = new HashMap<>();
        inputMap.put((short)1, (byte)2);
        inputMap.put((short)3, (byte)4);
        
        mapDecorator.putAll(inputMap);
        
        assertEquals(inputMap.size(), mapDecorator.size());
        for (Map.Entry<Short, Byte> entry : inputMap.entrySet()) {
            assertTrue(mapDecorator.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), mapDecorator.get(entry.getKey()));
        }
    }
    
    @Test
    public void testWrapKey() {
        short key = 1;
        Short wrappedKey = mapDecorator.wrapKey(key);
        assertEquals(key, (short)wrappedKey);
    }
    
    @Test
    public void testUnwrapKey() {
        Short key = 1;
        short unwrappedKey = mapDecorator.unwrapKey(key);
        assertEquals((short)key, unwrappedKey);
    }
    
    @Test
    public void testWrapValue() {
        byte value = 2;
        Byte wrappedValue = mapDecorator.wrapValue(value);
        assertEquals(value, (byte)wrappedValue);
    }
    
    @Test
    public void testUnwrapValue() {
        Byte value = 2;
        byte unwrappedValue = mapDecorator.unwrapValue(value);
        assertEquals((byte)value, unwrappedValue);
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator deserializedMapDecorator = new TShortByteMapDecorator();
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(mapDecorator);
        objectOutputStream.close();
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        deserializedMapDecorator.readExternal(objectInputStream);
        
        assertEquals(mapDecorator.size(), deserializedMapDecorator.size());
        for (Map.Entry<Short, Byte> entry : mapDecorator.entrySet()) {
            assertTrue(deserializedMapDecorator.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), deserializedMapDecorator.get(entry.getKey()));
        }
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        mapDecorator.writeExternal(objectOutputStream);
        objectOutputStream.close();
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TShortByteMapDecorator deserializedMapDecorator = (TShortByteMapDecorator) objectInputStream.readObject();
        
        assertEquals(mapDecorator.size(), deserializedMapDecorator.size());
        for (Map.Entry<Short, Byte> entry : mapDecorator.entrySet()) {
            assertTrue(deserializedMapDecorator.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), deserializedMapDecorator.get(entry.getKey()));
        }
    }
}