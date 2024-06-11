package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.AbstractMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {
    private TShortByteMap map;
    private TShortByteMapDecorator decorator;

    @Before
    public void setUp() {
        map = new TShortByteMap();
        decorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);

        assertEquals((byte) 10, decorator.get((short) 1).byteValue());
        assertEquals((byte) 20, decorator.get((short) 2).byteValue());
    }

    @Test
    public void testPutWithNullKey() {
        assertEquals(0, decorator.size());

        decorator.put(null, (byte) 10);

        assertEquals((byte) 10, decorator.get(null).byteValue());
        assertEquals(1, decorator.size());
    }

    @Test
    public void testPutWithNullValue() {
        decorator.put((short) 1, null);

        assertNull(decorator.get((short) 1));
    }

    @Test
    public void testGetWithNullKey() {
        decorator.put((short) 1, (byte) 10);

        assertNull(decorator.get(null));
    }

    @Test
    public void testClear() {
        decorator.put((short) 1, (byte) 10);

        decorator.clear();

        assertEquals(0, decorator.size());
        assertNull(decorator.get((short) 1));
    }

    @Test
    public void testRemove() {
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);

        decorator.remove((short) 1);

        assertEquals(1, decorator.size());
        assertNull(decorator.get((short) 1));
        assertEquals((byte) 20, decorator.get((short) 2).byteValue());
    }

    @Test
    public void testContainsValue() {
        decorator.put((short) 1, (byte) 10);

        assertEquals(true, decorator.containsValue((byte) 10));
        assertEquals(false, decorator.containsValue((byte) 20));
    }

    @Test
    public void testContainsKey() {
        decorator.put((short) 1, (byte) 10);

        assertEquals(true, decorator.containsKey((short) 1));
        assertEquals(false, decorator.containsKey((short) 2));
    }

    @Test
    public void testSize() {
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertEquals(true, decorator.isEmpty());

        decorator.put((short) 1, (byte) 10);

        assertEquals(false, decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> source = new AbstractMap<Short, Byte>() {
            @Override
            public Set<Map.Entry<Short, Byte>> entrySet() {
                Set<Map.Entry<Short, Byte>> entries = new HashSet<>();
                entries.add(new SimpleEntry<>((short) 1, (byte) 10));
                entries.add(new SimpleEntry<>((short) 2, (byte) 20));
                return entries;
            }
        };

        decorator.putAll(source);

        assertEquals((byte) 10, decorator.get((short) 1).byteValue());
        assertEquals((byte) 20, decorator.get((short) 2).byteValue());
    }
}