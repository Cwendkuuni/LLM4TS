
package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.map.TShortByteMap;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Entry;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator decorator;

    @Before
    public void setUp() {
        decorator = new TShortByteMapDecorator();
    }

    @Test
    public void testPut() {
        Short key = 1;
        Byte value = 2;
        assertNull(decorator.put(key, value));
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testGet_NullKey() {
        assertNull(decorator.get(null));
    }

    @Test
    public void testGet_InvalidKey() {
        Object key = "key";
        assertNull(decorator.get(key));
    }

    @Test
    public void testGet_NoEntryValue() {
        decorator.getMap().put((short) 1, decorator.getMap().getNoEntryValue());
        assertNull(decorator.get((short) 1));
    }

    @Test
    public void testClear() {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        Short key1 = 1;
        Byte value1 = 2;
        decorator.put(key1, value1);
        assertEquals(value1, decorator.remove(key1));
        assertNull(decorator.remove(key1));
    }

    @Test
    public void testEntrySet() {
        Short key = 1;
        Byte value = 2;
        decorator.put(key, value);

        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(1, entrySet.size());

        Iterator<Map.Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key, entry.getKey());
        assertEquals(value, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        Byte value = 1;
        decorator.put((short) 1, value);
        assertTrue(decorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        Short key = 1;
        decorator.put(key, (byte) 1);
        assertTrue(decorator.containsKey(key));
    }

    @Test
    public void testSize() {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        decorator.put((short) 1, (byte) 1);
        assertTrue(!decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new AbstractMap<Short, Byte>() {
            @Override
            public Set<Entry<Short, Byte>> entrySet() {
                return null;
            }
        };
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        decorator.putAll(map);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testWrapKey() {
        assertEquals(1, decorator.wrapKey((short) 1).shortValue());
    }

    @Test
    public void testUnwrapKey() {
        assertEquals((short) 1, decorator.unwrapKey((short) 1));
    }

    @Test
    public void testWrapValue() {
        assertEquals(1, decorator.wrapValue((byte) 1).byteValue());
    }

    @Test
    public void testUnwrapValue() {
        assertEquals((byte) 1, decorator.unwrapValue((byte) 1));
    }

    @Test
    public void testReadExternal() {
        // TODO: Implement this test
    }

    @Test
    public void testWriteExternal() {
        // TODO: Implement this test
    }
}
