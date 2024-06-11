package gnu.trove.decorator;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TShortByteMapDecoratorTest {
    
    private TShortByteMapDecorator decorator;
    private TShortByteMap map;
    
    @Before
    public void setUp() {
        map = new TShortByteHashMap();
        decorator = new TShortByteMapDecorator(map);
    }
    
    @Test
    public void testPut() {
        decorator.put((short) 1, (byte) 2);
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsValue((byte) 2));
        assertEquals((byte) 2, decorator.get((short) 1).byteValue());
    }
    
    @Test
    public void testGet() {
        map.put((short) 1, (byte) 2);
        assertEquals((byte) 2, decorator.get((short) 1).byteValue());
    }
    
    @Test
    public void testClear() {
        map.put((short) 1, (byte) 2);
        decorator.clear();
        assertFalse(decorator.containsKey((short) 1));
        assertFalse(decorator.containsValue((byte) 2));
    }
    
    @Test
    public void testRemove() {
        map.put((short) 1, (byte) 2);
        decorator.remove((short) 1);
        assertFalse(decorator.containsKey((short) 1));
        assertFalse(decorator.containsValue((byte) 2));
    }
    
    @Test
    public void testEntrySet() {
        map.put((short) 1, (byte) 2);
        
        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(1, entrySet.size());
        
        Map.Entry<Short, Byte> entry = entrySet.iterator().next();
        assertEquals((short) 1, entry.getKey().shortValue());
        assertEquals((byte) 2, entry.getValue().byteValue());
    }
    
    @Test
    public void testContainsValue() {
        map.put((short) 1, (byte) 2);
        assertTrue(decorator.containsValue((byte) 2));
        assertFalse(decorator.containsValue((byte) 3));
    }
    
    @Test
    public void testContainsKey() {
        map.put((short) 1, (byte) 2);
        assertTrue(decorator.containsKey((short) 1));
        assertFalse(decorator.containsKey((short) 2));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, decorator.size());
        map.put((short) 1, (byte) 2);
        assertEquals(1, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        map.put((short) 1, (byte) 2);
        assertFalse(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Short, Byte> inputMap = new HashMap<>();
        inputMap.put((short) 1, (byte) 2);
        inputMap.put((short) 3, (byte) 4);
        
        decorator.putAll(inputMap);
        
        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 3));
        assertTrue(decorator.containsValue((byte) 2));
        assertTrue(decorator.containsValue((byte) 4));
    }
    
    @Test
    public void testWrapKey() {
        assertEquals((short) 1, decorator.wrapKey((short) 1).shortValue());
    }
    
    @Test
    public void testUnwrapKey() {
        assertEquals((short) 1, decorator.unwrapKey((short) 1));
    }
    
    @Test
    public void testWrapValue() {
        assertEquals((byte) 2, decorator.wrapValue((byte) 2).byteValue());
    }
    
    @Test
    public void testUnwrapValue() {
        assertEquals((byte) 2, decorator.unwrapValue((byte) 2));
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        decorator.put((short) 1, (byte) 2);
        decorator.writeExternal(oos);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        
        decorator.readExternal(ois);
        ois.close();
        
        assertEquals(1, decorator.size());
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsValue((byte) 2));
    }
}