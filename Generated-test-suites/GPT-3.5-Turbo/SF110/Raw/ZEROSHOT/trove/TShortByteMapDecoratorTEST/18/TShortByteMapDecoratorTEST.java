import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator decorator;
    private TShortByteMap map;

    @Before
    public void setUp() {
        map = new TShortByteMap();
        decorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        Short key1 = 1;
        Byte value1 = 10;
        decorator.put(key1, value1);
        assertEquals(value1, decorator.get(key1));

        Short key2 = 2;
        Byte value2 = 20;
        decorator.put(key2, value2);
        assertEquals(value2, decorator.get(key2));
    }

    @Test
    public void testClear() {
        Short key = 1;
        Byte value = 10;
        decorator.put(key, value);
        decorator.clear();
        assertNull(decorator.get(key));
    }

    @Test
    public void testRemove() {
        Short key = 1;
        Byte value = 10;
        decorator.put(key, value);
        decorator.remove(key);
        assertNull(decorator.get(key));
    }

    @Test
    public void testContainsValue() {
        Byte value = 10;
        decorator.put((short)1, value);
        assertTrue(decorator.containsValue(value));
        assertFalse(decorator.containsValue((byte)20));
    }

    @Test
    public void testContainsKey() {
        Short key = 1;
        decorator.put(key, (byte)10);
        assertTrue(decorator.containsKey(key));
        assertFalse(decorator.containsKey((short)2));
    }

    @Test
    public void testSize() {
        assertEquals(0, decorator.size());

        decorator.put((short)1, (byte)10);
        assertEquals(1, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());

        decorator.put((short)1, (byte)10);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        HashMap<Short, Byte> hashMap = new HashMap<>();
        hashMap.put((short)1, (byte)10);
        hashMap.put((short)2, (byte)20);
        decorator.putAll(hashMap);

        assertEquals((byte)10, (byte)decorator.get((short)1));
        assertEquals((byte)20, (byte)decorator.get((short)2));
    }

    @Test
    public void testWrapKey() {
        short key = 1;
        Short wrappedKey = decorator.wrapKey(key);
        assertEquals(key, (short)wrappedKey);
    }

    @Test
    public void testUnwrapKey() {
        Short key = 1;
        short unwrappedKey = decorator.unwrapKey(key);
        assertEquals((short)key, unwrappedKey);
    }

    @Test
    public void testWrapValue() {
        byte value = 10;
        Byte wrappedValue = decorator.wrapValue(value);
        assertEquals(value, (byte)wrappedValue);
    }

    @Test
    public void testUnwrapValue() {
        Byte value = 10;
        byte unwrappedValue = decorator.unwrapValue(value);
        assertEquals((byte)value, unwrappedValue);
    }
}