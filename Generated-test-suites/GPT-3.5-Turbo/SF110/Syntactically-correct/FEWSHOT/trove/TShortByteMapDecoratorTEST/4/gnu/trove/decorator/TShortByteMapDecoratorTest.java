package gnu.trove.decorator;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.put((short) 2, (byte) 3);
        mapDecorator.put((short) 3, (byte) 4);

        Assert.assertEquals(Byte.valueOf((byte) 2), mapDecorator.get((short) 1));
        Assert.assertEquals(Byte.valueOf((byte) 3), mapDecorator.get((short) 2));
        Assert.assertEquals(Byte.valueOf((byte) 4), mapDecorator.get((short) 3));
    }

    @Test
    public void testPutNullValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.put((short) 1, null);

        Assert.assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testPutNullKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.put(null, (byte) 2);

        Assert.assertEquals(Byte.valueOf((byte) 2), mapDecorator.get(null));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.put((short) 2, (byte) 3);

        mapDecorator.remove((short) 1);
        Assert.assertNull(mapDecorator.get((short) 1));
        Assert.assertEquals(Byte.valueOf((byte) 3), mapDecorator.get((short) 2));
    }

    @Test
    public void testRemoveNullKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.put(null, (byte) 2);

        mapDecorator.remove(null);
        Assert.assertNull(mapDecorator.get(null));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.put((short) 2, (byte) 3);

        Assert.assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());

        Assert.assertTrue(mapDecorator.isEmpty());
        mapDecorator.put((short) 1, (byte) 2);
        Assert.assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.put((short) 1, (byte) 2);

        Assert.assertTrue(mapDecorator.containsKey((short) 1));
        Assert.assertFalse(mapDecorator.containsKey((short) 2));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.put((short) 1, (byte) 2);

        Assert.assertTrue(mapDecorator.containsValue((byte) 2));
        Assert.assertFalse(mapDecorator.containsValue((byte) 3));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.put((short) 2, (byte) 3);

        mapDecorator.clear();
        Assert.assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.put((short) 2, (byte) 3);

        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Assert.assertEquals(2, entrySet.size());
        for (Map.Entry<Short, Byte> entry : entrySet) {
            if (entry.getKey().equals((short) 1)) {
                Assert.assertEquals(Byte.valueOf((byte) 2), entry.getValue());
            } else if (entry.getKey().equals((short) 2)) {
                Assert.assertEquals(Byte.valueOf((byte) 3), entry.getValue());
            } else {
                Assert.fail("Unexpected entry found");
            }
        }
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> inputMap = new HashMap<>();
        inputMap.put((short) 1, (byte) 2);
        inputMap.put((short) 2, (byte) 3);

        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
        mapDecorator.putAll(inputMap);

        Assert.assertEquals(Byte.valueOf((byte) 2), mapDecorator.get((short) 1));
        Assert.assertEquals(Byte.valueOf((byte) 3), mapDecorator.get((short) 2));
    }
}