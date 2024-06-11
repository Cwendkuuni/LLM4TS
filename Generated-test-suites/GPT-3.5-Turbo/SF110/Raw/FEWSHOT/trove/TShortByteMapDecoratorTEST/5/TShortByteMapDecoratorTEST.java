package gnu.trove.decorator;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
    
    @Test
    public void testPutAndGet() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        decorator.put((short)1, (byte)10);
        assertEquals((byte)10, decorator.get((short)1).byteValue());
        
        decorator.put((short)2, null);
        assertNull(decorator.get((short)2));
    }
    
    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteHashMap();
        map.put((short)1, (byte)10);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.clear();
        
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteHashMap();
        map.put((short)1, (byte)10);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.remove((short)1);
        
        assertNull(decorator.get((short)1));
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteHashMap();
        map.put((short)1, (byte)10);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertTrue(decorator.containsValue((byte)10));
        assertFalse(decorator.containsValue((byte)20));
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteHashMap();
        map.put((short)1, (byte)10);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertTrue(decorator.containsKey((short)1));
        assertFalse(decorator.containsKey((short)2));
    }
    
    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteHashMap();
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(2, decorator.size());
        
        decorator.remove((short)1);
        assertEquals(1, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteHashMap();
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertTrue(decorator.isEmpty());
        
        decorator.put((short)1, (byte)10);
        assertFalse(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteHashMap();
        map.put((short)1, (byte)10);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Map<Short, Byte> newMap = new HashMap<>();
        newMap.put((short)2, (byte)20);
        newMap.put((short)3, (byte)30);
        
        decorator.putAll(newMap);
        
        assertEquals((byte)20, decorator.get((short)2).byteValue());
        assertEquals((byte)30, decorator.get((short)3).byteValue());
    }
    
    @Test
    public void testWrapKeyAndUnwrapKey() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short key = decorator.wrapKey((short)1);
        assertEquals((short)1, decorator.unwrapKey(key));
    }
    
    @Test
    public void testWrapValueAndUnwrapValue() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Byte value = decorator.wrapValue((byte)10);
        assertEquals((byte)10, decorator.unwrapValue(value));
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TShortByteMap map = new TShortByteHashMap();
        map.put((short)1, (byte)10);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.readExternal(new ObjectInputStream(new ByteArrayInputStream(serialize(map))));
        
        assertEquals((byte)10, decorator.get((short)1).byteValue());
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TShortByteMap map = new TShortByteHashMap();
        map.put((short)1, (byte)10);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decorator.writeExternal(new ObjectOutputStream(baos));
        byte[] bytes = baos.toByteArray();
        
        assertTrue(bytes.length > 0);
    }
    
    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        return baos.toByteArray();
    }
}