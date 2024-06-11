import org.junit.*;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Byte result = decorator.put((short)1, (byte)10);
        assertEquals(Byte.valueOf((byte) 10), result);

        result = decorator.put((short)2, (byte)20);
        assertEquals(Byte.valueOf((byte) 20), result);

        result = decorator.put(null, (byte)30);
        assertEquals(Byte.valueOf((byte) 30), result);
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);

        Byte result = decorator.get((short)1);
        assertEquals(Byte.valueOf((byte) 10), result);

        result = decorator.get((short)2);
        assertEquals(Byte.valueOf((byte) 20), result);

        result = decorator.get(null);
        assertNull(result);
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);
        decorator.clear();

        int size = decorator.size();
        assertEquals(0, size);
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);

        Byte result = decorator.remove((short)1);
        assertEquals(Byte.valueOf((byte) 10), result);

        result = decorator.remove((short)2);
        assertEquals(Byte.valueOf((byte) 20), result);

        result = decorator.remove(null);
        assertNull(result);
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);

        boolean result = decorator.containsValue((byte)10);
        assertTrue(result);

        result = decorator.containsValue((byte)20);
        assertTrue(result);

        result = decorator.containsValue((byte)30);
        assertFalse(result);
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);

        boolean result = decorator.containsKey((short)1);
        assertTrue(result);

        result = decorator.containsKey((short)2);
        assertTrue(result);

        result = decorator.containsKey((short)3);
        assertFalse(result);
    }

}