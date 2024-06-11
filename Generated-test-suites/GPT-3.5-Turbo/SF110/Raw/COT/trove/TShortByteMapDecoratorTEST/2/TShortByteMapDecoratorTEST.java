
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
    
    @Test
    public void testPut() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertNull(mapDecorator.put((short)1, (byte)2));
        assertEquals((byte)2, (byte)mapDecorator.getMap().get((short)1));
    }
    
    @Test
    public void testGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.getMap().put((short)1, (byte)2);
        assertEquals((byte)2, (byte)mapDecorator.get((short)1));
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.getMap().put((short)1, (byte)2);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.getMap().put((short)1, (byte)2);
        mapDecorator.getMap().put((short)2, (byte)3);
        assertEquals((byte)2, (byte)mapDecorator.remove((short)1));
        assertNull(mapDecorator.get((short)1));
    }
    
    @Test
    public void testEntrySet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.getMap().put((short)1, (byte)2);
        mapDecorator.getMap().put((short)2, (byte)3);
        assertEquals(2, mapDecorator.entrySet().size());
        assertTrue(mapDecorator.entrySet().contains(new AbstractMap.SimpleEntry<Short, Byte>((short)1, (byte)2)));
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.getMap().put((short)1, (byte)2);
        assertTrue(mapDecorator.containsValue((byte)2));
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.getMap().put((short)1, (byte)2);
        assertTrue(mapDecorator.containsKey((short)1));
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertEquals(0, mapDecorator.size());
        mapDecorator.getMap().put((short)1, (byte)2);
        assertEquals(1, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.getMap().put((short)1, (byte)2);
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short)1, (byte)2);
        mapDecorator.putAll(map);
        assertEquals((byte)2, (byte)mapDecorator.get((short)1));
    }
    
    @Test
    public void testWrapKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertEquals((short)1, (short)mapDecorator.wrapKey((short)1));
    }
    
    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertEquals((short)1, (short)mapDecorator.unwrapKey((short)1));
    }
    
    @Test
    public void testWrapValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertEquals((byte)1, (byte)mapDecorator.wrapValue((byte)1));
    }
    
    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertEquals((byte)1, (byte)mapDecorator.unwrapValue((byte)1));
    }
}
