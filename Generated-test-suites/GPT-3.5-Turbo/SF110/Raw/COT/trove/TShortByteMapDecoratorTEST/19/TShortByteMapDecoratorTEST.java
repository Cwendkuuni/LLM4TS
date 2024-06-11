

import gnu.trove.map.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator tShortByteMapDecorator;

    @Test
    public void testPut() {
        TShortByteMap tShortByteMap = new TShortByteHashMap();
        tShortByteMapDecorator = new TShortByteMapDecorator(tShortByteMap);
        Assert.assertEquals(null, tShortByteMapDecorator.put((short) 1, (byte) 1));
    }

    @Test
    public void testGet() {
        TShortByteMap tShortByteMap = new TShortByteHashMap();
        tShortByteMap.put((short) 1, (byte) 1);
        tShortByteMapDecorator = new TShortByteMapDecorator(tShortByteMap);
        Assert.assertEquals(Byte.valueOf("1"), tShortByteMapDecorator.get((short) 1));
    }

    @Test
    public void testClear() {
        TShortByteMap tShortByteMap = new TShortByteHashMap();
        tShortByteMap.put((short) 1, (byte) 1);
        tShortByteMapDecorator = new TShortByteMapDecorator(tShortByteMap);
        tShortByteMapDecorator.clear();
        Assert.assertEquals(0, tShortByteMapDecorator.size());
    }

    @Test
    public void testRemove() {
        TShortByteMap tShortByteMap = new TShortByteHashMap();
        tShortByteMap.put((short) 1, (byte) 1);
        tShortByteMapDecorator = new TShortByteMapDecorator(tShortByteMap);
        Assert.assertEquals(Byte.valueOf("1"), tShortByteMapDecorator.remove((short) 1));
    }

    @Test
    public void testEntrySet() {
        TShortByteMap tShortByteMap = new TShortByteHashMap();
        tShortByteMap.put((short) 1, (byte) 1);
        tShortByteMapDecorator = new TShortByteMapDecorator(tShortByteMap);
        Set<Map.Entry<Short, Byte>> set = tShortByteMapDecorator.entrySet();
        Assert.assertEquals(1, set.size());
    }

    @Test
    public void testContainsValue() {
        TShortByteMap tShortByteMap = new TShortByteHashMap();
        tShortByteMap.put((short) 1, (byte) 1);
        tShortByteMapDecorator = new TShortByteMapDecorator(tShortByteMap);
        Assert.assertTrue(tShortByteMapDecorator.containsValue((byte) 1));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap tShortByteMap = new TShortByteHashMap();
        tShortByteMap.put((short) 1, (byte) 1);
        tShortByteMapDecorator = new TShortByteMapDecorator(tShortByteMap);
        Assert.assertTrue(tShortByteMapDecorator.containsKey((short) 1));
    }

    @Test
    public void testSize() {
        TShortByteMap tShortByteMap = new TShortByteHashMap();
        tShortByteMap.put((short) 1, (byte) 1);
        tShortByteMapDecorator = new TShortByteMapDecorator(tShortByteMap);
        Assert.assertEquals(1, tShortByteMapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMap tShortByteMap = new TShortByteHashMap();
        tShortByteMapDecorator = new TShortByteMapDecorator(tShortByteMap);
        Assert.assertTrue(tShortByteMapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap tShortByteMap = new TShortByteHashMap();
        tShortByteMap.put((short) 1, (byte) 1);
        tShortByteMapDecorator = new TShortByteMapDecorator(tShortByteMap);
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 2, (byte) 2);
        tShortByteMapDecorator.putAll(map);
        Assert.assertEquals(2, tShortByteMapDecorator.size());
    }
}
