import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        decorator.put((short) 1, (byte) 1);

        Map<Short, Byte> expected = new HashMap<>();
        expected.put((short) 1, (byte) 1);

        assertEquals(expected, decorator.getMap());
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);

        Byte value = decorator.get((short) 1);

        assertEquals(Byte.valueOf((byte) 1), value);
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);

        decorator.clear();

        assertEquals(new HashMap<Short, Byte>(), decorator.getMap());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);

        Byte removed = decorator.remove((short) 1);

        assertEquals(Byte.valueOf((byte) 1), removed);
        assertNull(decorator.get((short) 1));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        assertEquals(2, decorator.entrySet().size());
        assertTrue(decorator.entrySet().contains(new HashMap.SimpleEntry<>((short) 1, (byte) 1)));

        decorator.entrySet().remove(new HashMap.SimpleEntry<>((short) 1, (byte) 1));
        assertFalse(decorator.containsKey((short) 1));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);

        assertTrue(decorator.containsValue((byte) 1));
        assertFalse(decorator.containsValue((byte) 2));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);

        assertTrue(decorator.containsKey((short) 1));
        assertFalse(decorator.containsKey((short) 2));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);

        assertEquals(1, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        assertTrue(decorator.isEmpty());
        decorator.put((short) 1, (byte) 1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);

        decorator.putAll(map);

        assertEquals(map, decorator.getMap());
    }

    @Test
    public void testReadExternal() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        try {
            decorator.readExternal(null);
        } catch (Exception e) {
            assertTrue(e instanceof IOException || e instanceof ClassNotFoundException);
        }
    }

    @Test
    public void testWriteExternal() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        try {
            decorator.writeExternal(null);
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }
}