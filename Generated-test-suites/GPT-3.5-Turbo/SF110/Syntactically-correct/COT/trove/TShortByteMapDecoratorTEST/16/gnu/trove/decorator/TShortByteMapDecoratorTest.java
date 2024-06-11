import org.junit.Test;
import gnu.trove.decorator.TShortByteMapDecorator;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
    
    @Test
    public void testPut() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNull(map.put((short)1, (byte)10));
        assertEquals(Byte.valueOf((byte)10), map.put((short)1, (byte)20));
    }
    
    @Test
    public void testGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short)1, (byte)10);
        assertEquals(Byte.valueOf((byte)10), map.get((short)1));
        assertNull(map.get((short)2));
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short)1, (byte)10);
        map.clear();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short)1, (byte)10);
        assertEquals(Byte.valueOf((byte)10), map.remove((short)1));
        assertNull(map.remove((short)2));
    }
    
    @Test
    public void testEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        map.put((short)3, (byte)30);
        assertEquals(3, map.entrySet().size());
        assertTrue(map.entrySet().contains(new AbstractMap.SimpleEntry<>((short)1, (byte)10)));
        assertFalse(map.entrySet().contains(new AbstractMap.SimpleEntry<>((short)4, (byte)40)));
        map.entrySet().remove(new AbstractMap.SimpleEntry<>((short)2, (byte)20));
        assertEquals(2, map.entrySet().size());
        assertNull(map.get((short)2));
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short)1, (byte)10);
        assertTrue(map.containsValue((byte)10));
        assertFalse(map.containsValue((byte)20));
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short)1, (byte)10);
        assertTrue(map.containsKey((short)1));
        assertFalse(map.containsKey((short)2));
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertEquals(0, map.size());
        map.put((short)1, (byte)10);
        assertEquals(1, map.size());
        map.remove((short)1);
        assertEquals(0, map.size());
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertTrue(map.isEmpty());
        map.put((short)1, (byte)10);
        assertFalse(map.isEmpty());
        map.remove((short)1);
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Map<Short, Byte> otherMap = new HashMap<>();
        otherMap.put((short)1, (byte)10);
        otherMap.put((short)2, (byte)20);
        map.putAll(otherMap);
        assertEquals(Byte.valueOf((byte)10), map.get((short)1));
        assertEquals(Byte.valueOf((byte)20), map.get((short)2));
    }
    
    // Test for readExternal() and writeExternal() omitted as they cannot be tested directly
    
}