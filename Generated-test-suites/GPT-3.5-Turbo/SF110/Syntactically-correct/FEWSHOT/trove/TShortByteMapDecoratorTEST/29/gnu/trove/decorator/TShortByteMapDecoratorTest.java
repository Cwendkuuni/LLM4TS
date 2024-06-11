package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.put((short) 1, (byte) 10));
        assertNull(decorator.put((short) 2, (byte) 20));
        assertEquals(Byte.valueOf((byte) 10), decorator.put((short) 1, (byte) 30));
        assertEquals(Byte.valueOf((byte) 20), decorator.put((short) 2, (byte) 40));
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);
        assertEquals(Byte.valueOf((byte) 10), decorator.get((short) 1));
        assertEquals(Byte.valueOf((byte) 20), decorator.get((short) 2));
        assertNull(decorator.get((short) 3));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);
        decorator.clear();
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);
        assertEquals(Byte.valueOf((byte) 10), decorator.remove((short) 1));
        assertEquals(Byte.valueOf((byte) 20), decorator.remove((short) 2));
        assertNull(decorator.remove((short) 3));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);
        assertEquals(true, decorator.containsValue((byte) 10));
        assertEquals(true, decorator.containsValue((byte) 20));
        assertEquals(false, decorator.containsValue((byte) 30));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);
        assertEquals(true, decorator.containsKey((short) 1));
        assertEquals(true, decorator.containsKey((short) 2));
        assertEquals(false, decorator.containsKey((short) 3));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);
        assertEquals(2, decorator.size());
        decorator.clear();
        assertEquals(0, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(true, decorator.isEmpty());
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);
        assertEquals(false, decorator.isEmpty());
        decorator.clear();
        assertEquals(true, decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);
        
        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
        decorator2.putAll(decorator);
        
        assertEquals(Byte.valueOf((byte) 10), decorator2.get((short) 1));
        assertEquals(Byte.valueOf((byte) 20), decorator2.get((short) 2));
    }

}