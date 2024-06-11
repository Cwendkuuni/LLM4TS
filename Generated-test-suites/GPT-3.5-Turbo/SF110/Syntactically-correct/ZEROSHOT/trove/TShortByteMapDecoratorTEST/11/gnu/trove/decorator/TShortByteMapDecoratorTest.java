
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TShortByteMapDecorator(new TShortByteHashMap());
    }

    @Test
    public void testPutAndGet() {
        mapDecorator.put((short) 1, (byte) 1);
        Assert.assertEquals(1, mapDecorator.get((short) 1).byteValue());
    }

    @Test
    public void testPutWithNullKey() {
        mapDecorator.put(null, (byte) 1);
        Assert.assertEquals(1, mapDecorator.get(mapDecorator.getMap().getNoEntryKey()).byteValue());
    }

    @Test
    public void testPutWithNullValue() {
        mapDecorator.put((short) 1, null);
        Assert.assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testRemove() {
        mapDecorator.put((short) 1, (byte) 1);
        Assert.assertEquals(1, mapDecorator.remove((short) 1).byteValue());
        Assert.assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testRemoveWithNullKey() {
        mapDecorator.put(null, (byte) 1);
        Assert.assertEquals(1, mapDecorator.remove(mapDecorator.getMap().getNoEntryKey()).byteValue());
    }

    @Test
    public void testSize() {
        mapDecorator.put((short) 1, (byte) 1);
        mapDecorator.put((short) 2, (byte) 2);
        Assert.assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(mapDecorator.isEmpty());
        mapDecorator.put((short) 1, (byte) 1);
        Assert.assertFalse(mapDecorator.isEmpty());
        mapDecorator.remove((short) 1);
        Assert.assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsKey() {
        mapDecorator.put((short) 1, (byte) 1);
        Assert.assertTrue(mapDecorator.containsKey((short) 1));
        Assert.assertFalse(mapDecorator.containsKey((short) 2));
    }

    @Test
    public void testContainsValue() {
        mapDecorator.put((short) 1, (byte) 1);
        Assert.assertTrue(mapDecorator.containsValue((byte) 1));
        Assert.assertFalse(mapDecorator.containsValue((byte) 2));
    }

    @Test
    public void testClear() {
        mapDecorator.put((short) 1, (byte) 1);
        mapDecorator.clear();
        Assert.assertEquals(0, mapDecorator.size());
        Assert.assertNull(mapDecorator.get((short) 1));
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        mapDecorator.putAll(map);
        Assert.assertEquals(2, mapDecorator.size());
        Assert.assertEquals(1, mapDecorator.get((short) 1).byteValue());
        Assert.assertEquals(2, mapDecorator.get((short) 2).byteValue());
    }
}
