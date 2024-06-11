import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setup() {
        mapDecorator = new TShortByteMapDecorator();
    }

    // Test put() method
    @Test
    public void testPut() {
        Short key1 = 1;
        Byte value1 = 1;
        Byte result1 = mapDecorator.put(key1, value1);
        Assert.assertEquals(value1, result1);

        Short key2 = 2;
        Byte value2 = 2;
        Byte result2 = mapDecorator.put(key2, value2);
        Assert.assertEquals(value2, result2);
    }

    // Test get() method
    @Test
    public void testGet() {
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        Byte result1 = mapDecorator.get(key1);
        Assert.assertEquals(value1, result1);

        Short key2 = 2;
        Byte result2 = mapDecorator.get(key2);
        Assert.assertNull(result2);
    }

    // Test remove() method
    @Test
    public void testRemove() {
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        Byte result1 = mapDecorator.remove(key1);
        Assert.assertEquals(value1, result1);

        Short key2 = 2;
        Byte result2 = mapDecorator.remove(key2);
        Assert.assertNull(result2);
    }

    // Test clear() method
    @Test
    public void testClear() {
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        mapDecorator.clear();
        Assert.assertTrue(mapDecorator.isEmpty());
    }

    // Test entrySet() method
    @Test
    public void testEntrySet() {
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        Map.Entry<Short, Byte> entry = mapDecorator.entrySet().iterator().next();
        Assert.assertEquals(key1, entry.getKey());
        Assert.assertEquals(value1, entry.getValue());

        mapDecorator.clear();
        Assert.assertTrue(mapDecorator.entrySet().isEmpty());
    }

    // Test containsValue() method
    @Test
    public void testContainsValue() {
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        Assert.assertTrue(mapDecorator.containsValue(value1));

        Byte value2 = 2;
        Assert.assertFalse(mapDecorator.containsValue(value2));
    }

    // Test containsKey() method
    @Test
    public void testContainsKey() {
        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        Assert.assertTrue(mapDecorator.containsKey(key1));

        Short key2 = 2;
        Assert.assertFalse(mapDecorator.containsKey(key2));
        Assert.assertFalse(mapDecorator.containsKey(null));
    }

    // Test size() method
    @Test
    public void testSize() {
        Assert.assertEquals(0, mapDecorator.size());

        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        Assert.assertEquals(1, mapDecorator.size());
    }

    // Test isEmpty() method
    @Test
    public void testIsEmpty() {
        Assert.assertTrue(mapDecorator.isEmpty());

        Short key1 = 1;
        Byte value1 = 1;
        mapDecorator.put(key1, value1);
        Assert.assertFalse(mapDecorator.isEmpty());
    }

    // Test putAll() method
    @Test
    public void testPutAll() {
        Map<Short, Byte> testMap = new HashMap<>();
        testMap.put((short) 1, (byte) 1);
        testMap.put((short) 2, (byte) 2);
        mapDecorator.putAll(testMap);
        Assert.assertEquals(2, mapDecorator.size());
    }

    // Test wrapKey() method
    @Test
    public void testWrapKey() {
        short key = 1;
        Short wrappedKey = mapDecorator.wrapKey(key);
        Assert.assertEquals(key, (short) wrappedKey);
    }

    // Test unwrapKey() method
    @Test
    public void testUnwrapKey() {
        Short key = 1;
        short unwrappedKey = mapDecorator.unwrapKey(key);
        Assert.assertEquals((short) key, unwrappedKey);
    }

    // Test wrapValue() method
    @Test
    public void testWrapValue() {
        byte value = 1;
        Byte wrappedValue = mapDecorator.wrapValue(value);
        Assert.assertEquals(value, (byte) wrappedValue);
    }

    // Test unwrapValue() method
    @Test
    public void testUnwrapValue() {
        Byte value = 1;
        byte unwrappedValue = mapDecorator.unwrapValue(value);
        Assert.assertEquals((byte) value, unwrappedValue);
    }

    // Test readExternal() and writeExternal() methods
    @Test
    public void testSerializable() {
        Map<Short, Byte> testMap = new HashMap<>();
        testMap.put((short) 1, (byte) 1);
        mapDecorator.putAll(testMap);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            mapDecorator.writeExternal(oos);
            oos.flush();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            TShortByteMapDecorator newMapDecorator = new TShortByteMapDecorator();
            newMapDecorator.readExternal(ois);

            Assert.assertEquals(mapDecorator.size(), newMapDecorator.size());
            for (Map.Entry<Short, Byte> entry : mapDecorator.entrySet()) {
                Assert.assertTrue(newMapDecorator.containsKey(entry.getKey()));
                Assert.assertEquals(entry.getValue(), newMapDecorator.get(entry.getKey()));
            }
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
}