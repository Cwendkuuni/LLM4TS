import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(mapDecorator.get((short) 1), (Byte) (byte) 2);
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(mapDecorator.get((short) 1), (Byte) (byte) 2);
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.remove((short) 1);
        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 2);
        assertTrue(mapDecorator.containsValue((byte) 2));
        assertFalse(mapDecorator.containsValue((byte) 3));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 2);
        assertTrue(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 2));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 2);
        assertEquals(mapDecorator.size(), 1);
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put((short) 1, (byte) 2);
        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator1 = new TShortByteMapDecorator();
        mapDecorator1.put((short) 1, (byte) 2);
        
        TShortByteMapDecorator mapDecorator2 = new TShortByteMapDecorator();
        mapDecorator2.putAll(mapDecorator1);
        
        assertEquals(mapDecorator2.get((short) 1), (Byte) (byte) 2);
    }
}