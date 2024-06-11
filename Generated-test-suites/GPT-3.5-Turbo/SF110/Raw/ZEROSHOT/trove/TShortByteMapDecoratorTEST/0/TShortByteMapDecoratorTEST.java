import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Map.Entry;
import gnu.trove.map.TShortByteMap;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {
    
    @Test
    public void testPut() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 1;
        Byte value = 2;
        
        decorator.put(key, value);
        assertTrue(map.containsValue((byte) 2));
    }
    
    @Test
    public void testGet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 1;
        Byte value = 2;
        
        decorator.put(key, value);
        Byte retrievedValue = decorator.get(key);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 1;
        Byte value = 2;
        
        decorator.put(key, value);
        assertFalse(map.isEmpty());
        
        decorator.clear();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 1;
        Byte value = 2;
        
        decorator.put(key, value);
        decorator.remove(key);
        
        assertFalse(map.containsKey((short) 1));
    }
    
    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 1;
        Byte value = 2;
        
        decorator.put(key, value);
        
        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        
        assertTrue(iterator.hasNext());
        
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 1;
        Byte value = 2;
        
        decorator.put(key, value);
        
        assertTrue(decorator.containsValue((byte) 2));
        assertFalse(decorator.containsValue((byte) 3));
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 1;
        Byte value = 2;
        
        decorator.put(key, value);
        
        assertTrue(decorator.containsKey(key));
        assertFalse(decorator.containsKey((short) 2));
    }
    
    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 1;
        Byte value = 2;
        
        decorator.put(key, value);
        
        assertEquals(1, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        assertTrue(decorator.isEmpty());
        
        Short key = 1;
        Byte value = 2;
        
        decorator.put(key, value);
        
        assertFalse(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Map<Short, Byte> inputMap = new HashMap<>();
        inputMap.put((short) 1, (byte) 2);
        inputMap.put((short) 3, (byte) 4);
        inputMap.put((short) 5, (byte) 6);
        
        decorator.putAll(inputMap);
        
        assertEquals(2, decorator.get((short) 1));
        assertEquals(4, decorator.get((short) 3));
        assertEquals(6, decorator.get((short) 5));
    }
    
    @Test
    public void testWrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 1;
        Short wrappedKey = decorator.wrapKey((short) 1);
        
        assertEquals(key, wrappedKey);
    }
    
    @Test
    public void testUnwrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = 1;
        Short unwrappedKey = decorator.unwrapKey((short) 1);
        
        assertEquals(key, unwrappedKey);
    }
    
    @Test
    public void testWrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Byte value = 2;
        Byte wrappedValue = decorator.wrapValue((byte) 2);
        
        assertEquals(value, wrappedValue);
    }
    
    @Test
    public void testUnwrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Byte value = 2;
        Byte unwrappedValue = decorator.unwrapValue((byte) 2);
        
        assertEquals(value, unwrappedValue);
    }
    
    @Test
    public void testReadExternal() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((short) 1, (byte) 2);
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        
        decorator.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        
        TShortByteMapDecorator readDecorator = new TShortByteMapDecorator();
        readDecorator.readExternal(objectInputStream);
        
        assertEquals(decorator.get((short) 1), readDecorator.get((short) 1));
    }
    
    @Test
    public void testWriteExternal() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((short) 1, (byte) 2);
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        
        decorator.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        
        assertNotNull(byteArray);
        assertTrue(byteArray.length > 0);
    }
}