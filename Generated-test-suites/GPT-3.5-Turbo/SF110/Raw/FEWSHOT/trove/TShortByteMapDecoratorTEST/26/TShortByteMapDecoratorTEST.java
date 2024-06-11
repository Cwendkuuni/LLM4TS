package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    
    @Test
    public void testGetMap() {
        TShortByteMap map = new TShortByteMap();
        decorator = new TShortByteMapDecorator(map);
        assertEquals(map, decorator.getMap());
    }
    
    @Test
    public void testPut() {
        decorator.put((short)1, (byte)2);
        assertEquals((byte)2, decorator.get((short)1).byteValue());
        
        decorator.put((short)1, null);
        assertNull(decorator.get((short)1));
    }
    
    @Test
    public void testGet() {
        assertNull(decorator.get((short)1));
        decorator.put((short)1, (byte)2);
        assertEquals((byte)2, decorator.get((short)1).byteValue());
    }
    
    @Test
    public void testClear() {
        decorator.put((short)1, (byte)2);
        decorator.clear();
        assertNull(decorator.get((short)1));
    }
    
    @Test
    public void testRemove() {
        decorator.put((short)1, (byte)2);
        decorator.remove((short)1);
        assertNull(decorator.get((short)1));
    }
    
    @Test
    public void testEntrySet() {
        decorator.put((short)1, (byte)2);
        
        decorator.entrySet().forEach(entry -> {
            assertEquals((short)1, entry.getKey().shortValue());
            assertEquals((byte)2, entry.getValue().byteValue());
        });
        
        decorator.clear();
        assertTrue(decorator.entrySet().isEmpty());
    }
    
    @Test
    public void testContainsValue() {
        decorator.put((short)1, (byte)2);
        assertTrue(decorator.containsValue((byte)2));
    }
    
    @Test
    public void testContainsKey() {
        decorator.put((short)1, (byte)2);
        assertTrue(decorator.containsKey((short)1));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, decorator.size());
        decorator.put((short)1, (byte)2);
        assertEquals(1, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        decorator.put((short)1, (byte)2);
        assertTrue(!decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        decorator.put((short)1, (byte)2);
        decorator.put((short)3, (byte)4);
        
        TShortByteMap map = new TShortByteMap();
        map.put((short)5, (byte)6);
        map.put((short)7, (byte)8);
        
        decorator.putAll(map);
        
        assertEquals((byte)6, decorator.get((short)5).byteValue());
        assertEquals((byte)8, decorator.get((short)7).byteValue());
    }
    
    @Test
    public void testWrapKey() {
        assertEquals((short)1, decorator.wrapKey((short)1).shortValue());
    }
    
    @Test
    public void testUnwrapKey() {
        assertEquals((short)1, decorator.unwrapKey((short)1));
    }
    
    @Test
    public void testWrapValue() {
        assertEquals((byte)2, decorator.wrapValue((byte)2).byteValue());
    }
    
    @Test
    public void testUnwrapValue() {
        assertEquals((byte)2, decorator.unwrapValue((byte)2));
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        decorator.readExternal(out);
        
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        decorator.writeExternal(in);
    }
}