package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.AbstractMap;
import java.util.Set;
import java.util.Collection;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();

        decorator.put((short)1, (byte)5);
        assertEquals((byte)5, map.get((short)1));

        decorator.put((short)2, (byte)10);
        assertEquals((byte)10, map.get((short)2));

        decorator.put((short)3, (byte)15);
        assertEquals((byte)15, map.get((short)3));

        decorator.put((short)4, null);
        assertNull(map.get((short)4));
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();

        map.put((short)1, (byte)5);
        assertEquals((byte)5, decorator.get((short)1));

        map.put((short)2, (byte)10);
        assertEquals((byte)10, decorator.get((short)2));

        map.put((short)3, (byte)15);
        assertEquals((byte)15, decorator.get((short)3));

        map.put((short)4, null);
        assertNull(decorator.get((short)4));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();

        map.put((short)1, (byte)5);
        map.put((short)2, (byte)10);
        map.put((short)3, (byte)15);

        decorator.clear();

        assertEquals(0, map.size());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();

        map.put((short)1, (byte)5);
        map.put((short)2, (byte)10);
        map.put((short)3, (byte)15);

        decorator.remove((short)2);
        assertNull(map.get((short)2));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();

        map.put((short)1, (byte)5);
        map.put((short)2, (byte)10);
        map.put((short)3, (byte)15);

        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();
        
        assertEquals(3, entrySet.size());

        assertEquals(true, entrySet.contains(new AbstractMap.SimpleEntry<>(Short.valueOf((short)1), Byte.valueOf((byte)5))));
        assertEquals(true, entrySet.contains(new AbstractMap.SimpleEntry<>(Short.valueOf((short)2), Byte.valueOf((byte)10))));
        assertEquals(true, entrySet.contains(new AbstractMap.SimpleEntry<>(Short.valueOf((short)3), Byte.valueOf((byte)15))));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();

        map.put((short)1, (byte)5);
        map.put((short)2, (byte)10);
        map.put((short)3, (byte)15);

        assertEquals(true, decorator.containsValue((byte)5));
        assertEquals(true, decorator.containsValue((byte)10));
        assertEquals(true, decorator.containsValue((byte)15));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();

        map.put((short)1, (byte)5);
        map.put((short)2, (byte)10);
        map.put((short)3, (byte)15);

        assertEquals(true, decorator.containsKey((short)1));
        assertEquals(true, decorator.containsKey((short)2));
        assertEquals(true, decorator.containsKey((short)3));
        assertEquals(false, decorator.containsKey((short)4));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();

        map.put((short)1, (byte)5);
        map.put((short)2, (byte)10);
        map.put((short)3, (byte)15);

        assertEquals(3, decorator.size());

        decorator.clear();

        assertEquals(0, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();

        assertEquals(true, decorator.isEmpty());

        map.put((short)1, (byte)5);
        map.put((short)2, (byte)10);
        map.put((short)3, (byte)15);

        assertEquals(false, decorator.isEmpty());

        decorator.clear();

        assertEquals(true, decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator1 = new TShortByteMapDecorator();
        TShortByteMap map1 = decorator1.getMap();

        map1.put((short)1, (byte)5);
        map1.put((short)2, (byte)10);

        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
        TShortByteMap map2 = decorator2.getMap();

        map2.put((short)3, (byte)15);
        map2.put((short)4, (byte)20);

        decorator1.putAll(map2);

        assertEquals((byte)15, map1.get((short)3));
        assertEquals((byte)20, map1.get((short)4));
    }

    @Test
    public void testWrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short wrappedKey = decorator.wrapKey((short)5);

        assertEquals(Short.valueOf((short)5), wrappedKey);
    }

    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        short unwrappedKey = decorator.unwrapKey(Short.valueOf((short)5));

        assertEquals((short)5, unwrappedKey);
    }

    @Test
    public void testWrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Byte wrappedValue = decorator.wrapValue((byte)10);

        assertEquals(Byte.valueOf((byte)10), wrappedValue);
    }

    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        byte unwrappedValue = decorator.unwrapValue(Byte.valueOf((byte)10));

        assertEquals((byte)10, unwrappedValue);
    }

}