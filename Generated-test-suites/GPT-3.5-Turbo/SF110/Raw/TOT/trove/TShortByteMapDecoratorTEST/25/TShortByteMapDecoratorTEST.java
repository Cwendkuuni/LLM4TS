import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        assertEquals(Byte.valueOf((byte) 2), decorator.get((short) 1));
        assertNull(decorator.get(null));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.clear();
        assertTrue(decorator.isEmpty());
        assertNull(decorator.get((short) 1));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        assertEquals(Byte.valueOf((byte) 2), decorator.remove((short) 1));
        assertNull(decorator.remove(null));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        assertTrue(decorator.containsValue((byte) 2));
        assertFalse(decorator.containsValue((byte) 3));
        assertFalse(decorator.containsValue(null));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        assertTrue(decorator.containsKey((short) 1));
        assertFalse(decorator.containsKey((short) 2));
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 2);
        map.put((short) 3, (byte) 4);
        decorator.putAll(map);
        assertEquals(Byte.valueOf((byte) 2), decorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 4), decorator.get((short) 3));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        assertEquals(2, decorator.entrySet().size());
    }

    @Test
    public void testWrapAndUnwrapMethods() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(Short.valueOf((short) 1), decorator.wrapKey((short) 1));
        assertEquals((short) 2, decorator.unwrapKey(Short.valueOf((short) 2)));
        assertEquals(Byte.valueOf((byte) 3), decorator.wrapValue((byte) 3));
        assertEquals((byte) 4, decorator.unwrapValue(Byte.valueOf((byte) 4)));
    }

    @Test
    public void testExternalization() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);

        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(byteOutStream);

            decorator.writeExternal(objOutStream);
            objOutStream.flush();

            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
            ObjectInputStream objInStream = new ObjectInputStream(byteInStream);

            decorator.readExternal(objInStream);
            assertEquals(Byte.valueOf((byte) 2), decorator.get((short) 1));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}