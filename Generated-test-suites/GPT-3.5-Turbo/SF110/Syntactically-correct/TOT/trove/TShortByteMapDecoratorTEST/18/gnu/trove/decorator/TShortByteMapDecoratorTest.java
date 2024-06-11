import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;

public class TShortByteMapDecoratorTest {
    
    @Test
    public void testPut() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = 1;
        Byte value1 = 1;
        assertNull(mapDecorator.put(key1, value1));
        
        Short key2 = 2;
        Byte value2 = 2;
        assertNull(mapDecorator.put(key2, value2));
        
        Short key3 = null;
        Byte value3 = 3;
        assertNull(mapDecorator.put(null, value3));
        
        assertEquals(value1, mapDecorator.put(key1, value2));
        assertEquals(value2, mapDecorator.put(key2, null));
        assertEquals(null, mapDecorator.put(key3, value3));
    }
    
    @Test
    public void testGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        assertEquals(value1, mapDecorator.get(key1));
        
        Short key2 = 2;
        Byte value2 = 2;
        mapDecorator.put(key2, value2);
        assertEquals(value2, mapDecorator.get(key2));
        
        Short key3 = null;
        Byte value3 = 3;
        mapDecorator.put(key3, value3);
        assertEquals(value3, mapDecorator.get(key3));
        
        assertEquals(null, mapDecorator.get(3));
        assertEquals(null, mapDecorator.get(null));
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        
        Short key2 = 2;
        Byte value2 = 2;
        mapDecorator.put(key2, value2);
        
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
        assertFalse(mapDecorator.containsKey(key1));
        assertFalse(mapDecorator.containsKey(key2));
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        
        Short key2 = 2;
        Byte value2 = 2;
        mapDecorator.put(key2, value2);
        
        assertEquals(value1, mapDecorator.remove(key1));
        assertNull(mapDecorator.remove(3));
    }
    
    @Test
    public void testEntrySet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        
        Short key2 = 2;
        Byte value2 = 2;
        mapDecorator.put(key2, value2);
        
        Short key3 = null;
        Byte value3 = 3;
        mapDecorator.put(key3, value3);
        
        Map.Entry<Short, Byte> entry1 = new AbstractMap.SimpleEntry<Short, Byte>(key1, value1);
        Map.Entry<Short, Byte> entry2 = new AbstractMap.SimpleEntry<Short, Byte>(key2, value2);
        Map.Entry<Short, Byte> entry3 = new AbstractMap.SimpleEntry<Short, Byte>(key3, value3);
        
        assertTrue(mapDecorator.entrySet().contains(entry1));
        assertTrue(mapDecorator.entrySet().contains(entry2));
        assertTrue(mapDecorator.entrySet().contains(entry3));
        
        mapDecorator.entrySet().remove(entry1);
        assertFalse(mapDecorator.entrySet().contains(entry1));
        
        mapDecorator.entrySet().remove(entry3);
        assertFalse(mapDecorator.entrySet().contains(entry3));
        
        assertEquals(1, mapDecorator.size());
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        
        Short key2 = 2;
        Byte value2 = 2;
        mapDecorator.put(key2, value2);
        
        assertTrue(mapDecorator.containsValue(value1));
        assertTrue(mapDecorator.containsValue(value2));
        
        Byte value3 = 3;
        assertFalse(mapDecorator.containsValue(value3));

        assertTrue(mapDecorator.containsValue(null));
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        
        Short key2 = 2;
        Byte value2 = 2;
        mapDecorator.put(key2, value2);
        
        assertTrue(mapDecorator.containsKey(key1));
        assertTrue(mapDecorator.containsKey(key2));
        
        Short key3 = 3;
        assertFalse(mapDecorator.containsKey(key3));

        assertFalse(mapDecorator.containsKey(null));
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertEquals(0, mapDecorator.size());
        
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        assertEquals(1, mapDecorator.size());
        
        Short key2 = 2;
        Byte value2 = 2;
        mapDecorator.put(key2, value2);
        assertEquals(2, mapDecorator.size());
        
        Short key3 = null;
        Byte value3 = 3;
        mapDecorator.put(key3, value3);
        assertEquals(3, mapDecorator.size());
        
        mapDecorator.remove(key1);
        assertEquals(2, mapDecorator.size());
        
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        
        map.put((short)1, (byte)1);
        map.put((short)2, (byte)2);
        map.put((short)3, (byte)3);
        
        mapDecorator.putAll(map);
        
        assertEquals(3, mapDecorator.size());
        assertTrue(mapDecorator.containsKey((short)1));
        assertTrue(mapDecorator.containsKey((short)2));
        assertTrue(mapDecorator.containsKey((short)3));
    }
    
    @Test
    public void testWrapKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        assertEquals(key, mapDecorator.wrapKey((short)1));
    }
    
    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        assertEquals(key.shortValue(), mapDecorator.unwrapKey(key));
    }
    
    @Test
    public void testWrapValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Byte value = 1;
        assertEquals(value, mapDecorator.wrapValue((byte)1));
    }
    
    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Byte value = 1;
        assertEquals(value.byteValue(), mapDecorator.unwrapValue(value));
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        
        map.put((short)1, (byte)1);
        map.put((short)2, (byte)2);
        map.put((short)3, (byte)3);
        
        mapDecorator.putAll(map);
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        mapDecorator.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TShortByteMapDecorator deserializedMapDecorator = new TShortByteMapDecorator();
        deserializedMapDecorator.readExternal(objectInputStream);
        
        assertEquals(3, deserializedMapDecorator.size());
        assertTrue(deserializedMapDecorator.containsKey((short)1));
        assertTrue(deserializedMapDecorator.containsKey((short)2));
        assertTrue(deserializedMapDecorator.containsKey((short)3));
    }
    
    @Test
    public void testWriteExternal() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        
        map.put((short)1, (byte)1);
        map.put((short)2, (byte)2);
        map.put((short)3, (byte)3);
        
        mapDecorator.putAll(map);
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        mapDecorator.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TShortByteMapDecorator deserializedMapDecorator = new TShortByteMapDecorator();
        deserializedMapDecorator.readExternal(objectInputStream);
        
        assertEquals(3, deserializedMapDecorator.size());
        assertTrue(deserializedMapDecorator.containsKey((short)1));
        assertTrue(deserializedMapDecorator.containsKey((short)2));
        assertTrue(deserializedMapDecorator.containsKey((short)3));
    }
}