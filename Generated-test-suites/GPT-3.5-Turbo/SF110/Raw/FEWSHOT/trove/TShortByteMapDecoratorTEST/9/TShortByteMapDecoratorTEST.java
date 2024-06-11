package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        assertEquals(Byte.valueOf((byte) 2), decorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 4), decorator.get((short) 3));
    }

    @Test
    public void testPutNull() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.put(null, null));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        assertEquals(Byte.valueOf((byte) 2), decorator.remove((short) 1));
        assertNull(decorator.remove((short) 1));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 3));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        assertTrue(decorator.containsValue((byte) 2));
        assertTrue(decorator.containsValue((byte) 4));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator1 = new TShortByteMapDecorator();
        decorator1.put((short) 1, (byte) 2);
        decorator1.put((short) 3, (byte) 4);

        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
        decorator2.putAll(decorator1);

        assertEquals(decorator1, decorator2);
    }

    @Test
    public void testWrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(Short.valueOf((short) 1), decorator.wrapKey((short) 1));
    }

    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals((short) 1, decorator.unwrapKey(Short.valueOf((short) 1)));
    }

    @Test
    public void testWrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(Byte.valueOf((byte) 2), decorator.wrapValue((byte) 2));
    }

    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals((byte) 2, decorator.unwrapValue(Byte.valueOf((byte) 2)));
    }

    @Test
    public void testReadExternal() {
        // TODO: write test for readExternal()
    }

    @Test
    public void testWriteExternal() {
        // TODO: write test for writeExternal()
    }

}