package gnu.trove.decorator;

import org.junit.Before;
import org.junit.Test;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.hash.TShortByteHashMap;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator decorators;
    private TShortByteMap map;

    @Before
    public void setUp() {
        map = new TShortByteHashMap();
        decorators = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPut() {
        decorators.put((short) 1, (byte) 1);
        decorators.put((short) 2, (byte) 2);

        assertEquals(2, decorators.size());
        assertEquals((Byte) 1, decorators.get((short) 1));
        assertEquals((Byte) 2, decorators.get((short) 2));
    }

    @Test
    public void testGet() {
        decorators.put((short) 1, (byte) 1);
        decorators.put((short) 2, (byte) 2);

        assertEquals((Byte) 1, decorators.get((short) 1));
        assertEquals((Byte) 2, decorators.get((short) 2));
        assertNull(decorators.get((short) 3));
    }

    @Test
    public void testClear() {
        decorators.put((short) 1, (byte) 1);
        decorators.put((short) 2, (byte) 2);

        decorators.clear();

        assertEquals(0, decorators.size());
        assertNull(decorators.get((short) 1));
        assertNull(decorators.get((short) 2));
    }

    @Test
    public void testRemove() {
        decorators.put((short) 1, (byte) 1);
        decorators.put((short) 2, (byte) 2);

        decorators.remove((short) 1);

        assertEquals(1, decorators.size());
        assertNull(decorators.get((short) 1));
        assertEquals((Byte) 2, decorators.get((short) 2));
    }

    @Test
    public void testEntrySet() {
        decorators.put((short) 1, (byte) 1);
        decorators.put((short) 2, (byte) 2);

        Set<Map.Entry<Short, Byte>> entrySet = decorators.entrySet();

        assertEquals(2, entrySet.size());

        boolean found1 = false;
        boolean found2 = false;

        for (Map.Entry<Short, Byte> entry : entrySet) {
            if (entry.getKey() == 1 && entry.getValue() == 1)
                found1 = true;
            if (entry.getKey() == 2 && entry.getValue() == 2)
                found2 = true;
        }

        assertTrue(found1);
        assertTrue(found2);
    }

    @Test
    public void testContainsValue() {
        decorators.put((short) 1, (byte) 1);
        decorators.put((short) 2, (byte) 2);

        assertTrue(decorators.containsValue((byte) 1));
        assertTrue(decorators.containsValue((byte) 2));
        assertFalse(decorators.containsValue((byte) 3));
    }

    @Test
    public void testContainsKey() {
        decorators.put((short) 1, (byte) 1);
        decorators.put((short) 2, (byte) 2);

        assertTrue(decorators.containsKey((short) 1));
        assertTrue(decorators.containsKey((short) 2));
        assertFalse(decorators.containsKey((short) 3));
    }

    @Test
    public void testSize() {
        decorators.put((short) 1, (byte) 1);
        decorators.put((short) 2, (byte) 2);

        assertEquals(2, decorators.size());

        decorators.remove((short) 1);

        assertEquals(1, decorators.size());

        decorators.clear();

        assertEquals(0, decorators.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorators.isEmpty());

        decorators.put((short) 1, (byte) 1);

        assertFalse(decorators.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> other = new TShortByteHashMap();
        other.put((short) 1, (byte) 1);
        other.put((short) 2, (byte) 2);

        decorators.putAll(other);

        assertEquals(2, decorators.size());
        assertEquals((Byte) 1, decorators.get((short) 1));
        assertEquals((Byte) 2, decorators.get((short) 2));
    }

    @Test
    public void testWrapKey() {
        Short key = (short) 1;
        Short wrappedKey = decorators.wrapKey((short) 1);

        assertEquals(key, wrappedKey);
    }

    @Test
    public void testUnwrapKey() {
        short key = (short) 1;
        short unwrappedKey = decorators.unwrapKey((short) 1);

        assertEquals(key, unwrappedKey);
    }

    @Test
    public void testWrapValue() {
        Byte value = (byte) 1;
        Byte wrappedValue = decorators.wrapValue((byte) 1);

        assertEquals(value, wrappedValue);
    }

    @Test
    public void testUnwrapValue() {
        byte value = (byte) 1;
        byte unwrappedValue = decorators.unwrapValue((byte) 1);

        assertEquals(value, unwrappedValue);
    }

    @Test
    public void testReadExternal() {
        // TODO: implement test case
        fail("Not yet implemented");
    }

    @Test
    public void testWriteExternal() {
        // TODO: implement test case
        fail("Not yet implemented");
    }
}