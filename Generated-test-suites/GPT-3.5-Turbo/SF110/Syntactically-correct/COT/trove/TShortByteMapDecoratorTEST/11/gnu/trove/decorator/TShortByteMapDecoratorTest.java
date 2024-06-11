
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 1);

        assertEquals(Byte.valueOf((byte) 1), mapDecorator.get((short) 1));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 1);
        mapDecorator.clear();

        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 1);
        mapDecorator.remove((short) 1);

        assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 1);

        assertTrue(mapDecorator.containsValue((byte) 1));
        assertFalse(mapDecorator.containsValue((byte) 2));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 1);

        assertTrue(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 2));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short) 1, (byte) 1);

        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();

        assertTrue(mapDecorator.isEmpty());

        mapDecorator.put((short) 1, (byte) 1);

        assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new AbstractMap<Short, Byte>() {
            @Override
            public Set<Entry<Short, Byte>> entrySet() {
                return null;
            }
        };
        map.put((short) 1, (byte) 1);

        mapDecorator.putAll(map);

        assertEquals(Byte.valueOf((byte) 1), mapDecorator.get((short) 1));
    }
}
