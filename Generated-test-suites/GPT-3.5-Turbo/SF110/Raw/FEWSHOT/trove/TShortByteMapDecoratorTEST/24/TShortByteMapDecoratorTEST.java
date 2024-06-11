package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.iterator.TByteIterator;
import java.util.AbstractSet;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        assertEquals((byte) 2, (byte) decorator.get((short) 1));
        assertEquals((byte) 4, (byte) decorator.get((short) 3));
        assertNull(decorator.get(null));
    }

    @Test
    public void testSizeAndIsEmpty() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        assertTrue(decorator.isEmpty());
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        assertEquals((byte) 2, (byte) decorator.remove((short) 1));
        assertEquals((byte) 4, (byte) decorator.remove((short) 3));
        assertNull(decorator.remove(null));
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        assertEquals(2, decorator.entrySet().size());

        TShortByteIterator iterator = decorator.getMap().iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            short key = iterator.key();
            byte value = iterator.value();
            assertTrue(decorator.containsKey(key));
            assertTrue(decorator.containsValue(value));
        }
    }
}