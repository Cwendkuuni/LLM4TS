import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)3, (byte)4);

        assertEquals((byte)2, mapDecorator.get((short)1).byteValue());
        assertEquals((byte)4, mapDecorator.get((short)3).byteValue());
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)3, (byte)4);

        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)3, (byte)4);

        assertEquals((byte)2, mapDecorator.remove((short)1).byteValue());
        assertNull(mapDecorator.remove((short)5));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)3, (byte)4);

        assertTrue(mapDecorator.containsKey((short)1));
        assertFalse(mapDecorator.containsKey((short)5));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)3, (byte)4);

        assertTrue(mapDecorator.containsValue((byte)2));
        assertFalse(mapDecorator.containsValue((byte)3));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        assertEquals(0, mapDecorator.size());

        mapDecorator.put((short)1, (byte)2);
        assertEquals(1, mapDecorator.size());

        mapDecorator.put((short)3, (byte)4);
        assertEquals(2, mapDecorator.size());

        mapDecorator.remove((short)1);
        assertEquals(1, mapDecorator.size());

        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> testMap = new HashMap<>();
        testMap.put((short)1, (byte)2);
        testMap.put((short)3, (byte)4);
        testMap.put((short)5, (byte)6);

        mapDecorator.putAll(testMap);

        assertEquals((byte)2, mapDecorator.get((short)1).byteValue());
        assertEquals((byte)4, mapDecorator.get((short)3).byteValue());
        assertEquals((byte)6, mapDecorator.get((short)5).byteValue());
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)3, (byte)4);
        mapDecorator.put((short)5, (byte)6);

        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        assertEquals(3, entrySet.size());

        Iterator<Map.Entry<Short, Byte>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Short, Byte> entry = iterator.next();
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
        }

        entrySet.clear();
        assertTrue(mapDecorator.isEmpty());
    }
}