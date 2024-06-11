import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.Set;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map.Entry;
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import java.io.*;

public class TShortByteMapDecoratorTest {
    
    private TShortByteMapDecorator decorator;
    private TShortByteMap map;
    
    @Before
    public void setup() {
        map = new TShortByteMap();
        decorator = new TShortByteMapDecorator(map);
    }
    
    @Test
    public void testPut() {
        // Test with non-null key and non-null value
        Short key1 = 1;
        Byte value1 = 2;
        Byte result1 = decorator.put(key1, value1);
        assertTrue(map.containsKey((short)1));
        assertTrue(map.containsValue((byte)2));
        assertEquals(value1, result1);
        
        // Test with null key and non-null value
        Short key2 = null;
        Byte value2 = 3;
        Byte result2 = decorator.put(key2, value2);
        assertTrue(map.containsKey(map.getNoEntryKey()));
        assertTrue(map.containsValue((byte)3));
        assertEquals(value2, result2);
        
        // Test with non-null key and null value
        Short key3 = 4;
        Byte value3 = null;
        Byte result3 = decorator.put(key3, value3);
        assertTrue(map.containsKey((short)4));
        assertTrue(map.containsValue(map.getNoEntryValue()));
        assertEquals(value3, result3);
        
        // Test with null key and null value
        Short key4 = null;
        Byte value4 = null;
        Byte result4 = decorator.put(key4, value4);
        assertTrue(map.containsKey(map.getNoEntryKey()));
        assertTrue(map.containsValue(map.getNoEntryValue()));
        assertEquals(value4, result4);
    }
    
    @Test
    public void testGet() {
        // Test with non-null key
        Short key1 = 1;
        Byte value1 = 2;
        decorator.put(key1, value1);
        Byte result1 = decorator.get(key1);
        assertEquals(value1, result1);
        
        // Test with null key
        Short key2 = null;
        Byte value2 = 3;
        decorator.put(key2, value2);
        Byte result2 = decorator.get(key2);
        assertEquals(value2, result2);
    }
    
    @Test
    public void testClear() {
        Short key1 = 1;
        Byte value1 = 2;
        decorator.put(key1, value1);
        decorator.clear();
        assertFalse(map.containsKey((short)1));
        assertFalse(map.containsValue((byte)2));
    }
    
    @Test
    public void testRemove() {
        // Test with non-null key
        Short key1 = 1;
        Byte value1 = 2;
        decorator.put(key1, value1);
        Byte result1 = decorator.remove(key1);
        assertFalse(map.containsKey((short)1));
        assertFalse(map.containsValue((byte)2));
        assertEquals(value1, result1);
        
        // Test with null key
        Short key2 = null;
        Byte value2 = 3;
        decorator.put(key2, value2);
        Byte result2 = decorator.remove(key2);
        assertFalse(map.containsKey(map.getNoEntryKey()));
        assertFalse(map.containsValue((byte)3));
        assertEquals(value2, result2);
    }
    
    @Test
    public void testEntrySet() {
        // Test size
        Short key1 = 1;
        Byte value1 = 2;
        decorator.put(key1, value1);
        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(1, entrySet.size());
        
        // Test isEmpty
        assertFalse(entrySet.isEmpty());
        
        // Test contains
        assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            public Short getKey() {
                return key1;
            }
            public Byte getValue() {
                return value1;
            }
            public Byte setValue(Byte value) {
                return null;
            }
        }));
        
        // Test iterator
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());
        assertFalse(iterator.hasNext());
        
        // Test remove
        iterator.remove();
        assertFalse(map.containsKey((short)1));
        assertFalse(map.containsValue((byte)2));
    }
    
    @Test
    public void testContainsValue() {
        // Test with non-null value
        Byte value1 = 2;
        decorator.put((short)1, value1);
        assertTrue(decorator.containsValue(value1));
        
        // Test with null value
        Byte value2 = null;
        decorator.put((short)2, value2);
        assertTrue(decorator.containsValue(value2));
    }
    
    @Test
    public void testContainsKey() {
        // Test with non-null key
        Short key1 = 1;
        decorator.put(key1, (byte)2);
        assertTrue(decorator.containsKey(key1));
        
        // Test with null key
        Short key2 = null;
        decorator.put(key2, (byte)3);
        assertTrue(decorator.containsKey(key2));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, decorator.size());
        decorator.put((short)1, (byte)2);
        assertEquals(1, decorator.size());
        decorator.put((short)2, (byte)3);
        assertEquals(2, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        decorator.put((short)1, (byte)2);
        assertFalse(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Short, Byte> putMap = new HashMap<>();
        putMap.put((short)1, (byte)2);
        putMap.put((short)2, (byte)3);
        decorator.putAll(putMap);
        assertTrue(decorator.containsKey((short)1));
        assertTrue(decorator.containsKey((short)2));
        assertTrue(decorator.containsValue((byte)2));
        assertTrue(decorator.containsValue((byte)3));
    }
    
    @Test
    public void testWrapKey() {
        short k = 1;
        Short wrappedKey = decorator.wrapKey(k);
        assertEquals(k, (short)wrappedKey);
    }
    
    @Test
    public void testUnwrapKey() {
        Short key = 1;
        short unwrappedKey = decorator.unwrapKey(key);
        assertEquals(key, (short)unwrappedKey);
    }
    
    @Test
    public void testWrapValue() {
        byte v = 2;
        Byte wrappedValue = decorator.wrapValue(v);
        assertEquals(v, (byte)wrappedValue);
    }
    
    @Test
    public void testUnwrapValue() {
        Byte value = 2;
        byte unwrappedValue = decorator.unwrapValue(value);
        assertEquals(value, (byte)unwrappedValue);
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(decorator);
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator) objectInputStream.readObject();
        
        assertEquals(decorator.size(), deserializedDecorator.size());
        assertEquals(decorator.getMap().getNoEntryKey(), deserializedDecorator.getMap().getNoEntryKey());
        assertEquals(decorator.getMap().getNoEntryValue(), deserializedDecorator.getMap().getNoEntryValue());
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(decorator);
        
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        assertTrue(byteArray.length > 0);
    }
}