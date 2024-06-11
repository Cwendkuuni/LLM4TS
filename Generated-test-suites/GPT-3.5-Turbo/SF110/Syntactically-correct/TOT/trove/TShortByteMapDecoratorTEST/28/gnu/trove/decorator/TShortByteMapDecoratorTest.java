import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator decorator;

    @Before
    public void setUp() {
        decorator = new TShortByteMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        decorator.put((short) 1, (byte) 10);
        Byte value = decorator.get((short) 1);
        Assert.assertNotNull(value);
        Assert.assertEquals((byte) 10, (byte) value);
    }

    @Test
    public void testPutNullValueAndGet() {
        decorator.put((short) 1, null);
        Byte value = decorator.get((short) 1);
        Assert.assertNull(value);
    }

    @Test
    public void testPutNullKeyAndGet() {
        decorator.put(null, (byte) 10);
        Byte value = decorator.get(null);
        Assert.assertNotNull(value);
        Assert.assertEquals((byte) 10, (byte) value);
    }

    @Test
    public void testPutAndGetNoEntry() {
        decorator.put((short) 1, (byte) 10);
        Byte value = decorator.get((short) 2);
        Assert.assertNull(value);
    }

    @Test
    public void testClear() {
        decorator.put((short) 1, (byte) 10);
        decorator.clear();
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        decorator.put((short) 1, (byte) 10);
        Byte value = decorator.remove((short) 1);
        Assert.assertNotNull(value);
        Assert.assertEquals((byte) 10, (byte) value);
    }

    @Test
    public void testRemoveNoEntry() {
        decorator.put((short) 1, (byte) 10);
        Byte value = decorator.remove((short) 2);
        Assert.assertNull(value);
    }

    @Test
    public void testContainsKey() {
        decorator.put((short) 1, (byte) 10);
        Assert.assertTrue(decorator.containsKey((short) 1));
        Assert.assertFalse(decorator.containsKey((short) 2));
    }

    @Test
    public void testContainsValue() {
        decorator.put((short) 1, (byte) 10);
        Assert.assertTrue(decorator.containsValue((byte) 10));
        Assert.assertFalse(decorator.containsValue((byte) 20));
    }

    @Test
    public void testSize() {
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);
        decorator.put((short) 3, (byte) 30);
        Assert.assertEquals(3, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 10);
        map.put((short) 2, (byte) 20);
        decorator.putAll(map);
        Assert.assertEquals(2, decorator.size());
        Assert.assertTrue(decorator.containsKey((short) 1));
        Assert.assertTrue(decorator.containsValue((byte) 10));
        Assert.assertTrue(decorator.containsKey((short) 2));
        Assert.assertTrue(decorator.containsValue((byte) 20));
    }

    @Test
    public void testWrapKey() {
        Short key = decorator.wrapKey((short) 1);
        Assert.assertEquals((short) 1, (short) key);
    }

    @Test
    public void testUnwrapKey() {
        short key = decorator.unwrapKey((short) 1);
        Assert.assertEquals((short) 1, (short) key);
    }

    @Test
    public void testWrapValue() {
        Byte value = decorator.wrapValue((byte) 10);
        Assert.assertEquals((byte) 10, (byte) value);
    }

    @Test
    public void testUnwrapValue() {
        byte value = decorator.unwrapValue((byte) 10);
        Assert.assertEquals((byte) 10, (byte) value);
    }

    // Additional test for the entrySet() method
    @Test
    public void testEntrySet() {
        decorator.put((short) 1, (byte) 10);
        decorator.put((short) 2, (byte) 20);
        decorator.put((short) 3, (byte) 30);
        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        
        // Test size
        Assert.assertEquals(3, entrySet.size());
        
        // Test isEmpty
        Assert.assertFalse(entrySet.isEmpty());
        
        // Test contains
        Assert.assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>((short) 1, (byte) 10)));
        Assert.assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>((short) 2, (byte) 20)));
        Assert.assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>((short) 3, (byte) 30)));
        Assert.assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<>((short) 4, (byte) 40)));
        
        // Test iterator
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Assert.assertTrue(iterator.hasNext());
        
        Entry<Short, Byte> entry = iterator.next();
        Assert.assertEquals((short) 1, (short) entry.getKey());
        Assert.assertEquals((byte) 10, (byte) entry.getValue());
        
        entry = iterator.next();
        Assert.assertEquals((short) 2, (short) entry.getKey());
        Assert.assertEquals((byte) 20, (byte) entry.getValue());
        
        entry = iterator.next();
        Assert.assertEquals((short) 3, (short) entry.getKey());
        Assert.assertEquals((byte) 30, (byte) entry.getValue());
        
        Assert.assertFalse(iterator.hasNext());
        
        // Test remove
        iterator = entrySet.iterator();
        iterator.next();
        iterator.remove();
        Assert.assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<>((short) 1, (byte) 10)));
        Assert.assertEquals(2, entrySet.size());
    }
}