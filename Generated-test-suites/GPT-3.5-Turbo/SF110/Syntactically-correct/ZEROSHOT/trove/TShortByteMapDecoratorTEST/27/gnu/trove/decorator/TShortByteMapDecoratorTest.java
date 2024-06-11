import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setup() {
        TShortByteMap map = new TShortByteMap();
        mapDecorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);

        Assert.assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testClear() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);

        mapDecorator.clear();

        Assert.assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);

        Byte removedValue = mapDecorator.remove(key);

        Assert.assertEquals(value, removedValue);
        Assert.assertNull(mapDecorator.get(key));
    }

    @Test
    public void testContainsValue() {
        Byte value = 2;
        mapDecorator.put((short) 1, value);

        Assert.assertTrue(mapDecorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);

        Assert.assertTrue(mapDecorator.containsKey(key));
    }

    @Test
    public void testSize() {
        Short key1 = 1;
        Byte value1 = 2;
        Short key2 = 3;
        Byte value2 = 4;

        mapDecorator.put(key1, value1);
        mapDecorator.put(key2, value2);

        Assert.assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> sourceMap = new TShortByteMap();
        sourceMap.put((short) 1, (byte) 2);
        sourceMap.put((short) 3, (byte) 4);

        mapDecorator.putAll(sourceMap);

        Assert.assertEquals(sourceMap.size(), mapDecorator.size());
        Assert.assertTrue(mapDecorator.containsKey((short) 1));
        Assert.assertTrue(mapDecorator.containsKey((short) 3));
    }

    // Test entrySet
    @Test
    public void testEntrySetSize() {
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.put((short) 3, (byte) 4);

        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();

        Assert.assertEquals(mapDecorator.size(), entrySet.size());
    }

    @Test
    public void testEntrySetContains() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);

        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Map.Entry<Short, Byte> entry = new AbstractMap.SimpleEntry<>(key, value);

        Assert.assertTrue(entrySet.contains(entry));
    }

    @Test
    public void testEntrySetIterator() {
        Short key1 = 1;
        Byte value1 = 2;
        Short key2 = 3;
        Byte value2 = 4;

        mapDecorator.put(key1, value1);
        mapDecorator.put(key2, value2);

        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        int count = 0;
        for (Map.Entry<Short, Byte> entry : entrySet) {
            Assert.assertTrue(entry.getKey() == key1 || entry.getKey() == key2);
            Assert.assertTrue(entry.getValue() == value1 || entry.getValue() == value2);
            count++;
        }
        Assert.assertEquals(mapDecorator.size(), count);
    }

}