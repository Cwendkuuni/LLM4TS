import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;

import java.io.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator decorator;
    private TDoubleShortMap map;

    @Before
    public void setUp() {
        map = new TDoubleShortHashMap();
        decorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPut() {
        Double key = 1.0;
        Short value = 2;
        Short oldValue = decorator.put(key, value);

        Assert.assertNull(oldValue);
        Assert.assertEquals(1, map.size());
        Assert.assertTrue(map.containsKey(1.0));
        Assert.assertTrue(map.containsValue((short) 2));
    }

    @Test
    public void testPutNullKey() {
        Double key = null;
        Short value = 2;
        Short oldValue = decorator.put(key, value);

        Assert.assertNull(oldValue);
        Assert.assertEquals(1, map.size());
        Assert.assertTrue(map.containsKey(map.getNoEntryKey()));
        Assert.assertTrue(map.containsValue((short) 2));
    }

    @Test(expected = ClassCastException.class)
    public void testPutWrongKeyType() {
        decorator.put("key", (short) 2);
    }

    @Test
    public void testPutNoEntryKey() {
        map.put(map.getNoEntryKey(), (short) 1);
        Double key = map.getNoEntryKey();
        Short value = 2;
        Short oldValue = decorator.put(key, value);

        Assert.assertNull(oldValue);
        Assert.assertEquals(1, map.size());
        Assert.assertTrue(map.containsKey(map.getNoEntryKey()));
        Assert.assertTrue(map.containsValue((short) 2));
    }

    @Test
    public void testGet() {
        Double key = 1.0;
        Short value = 2;
        map.put(1.0, (short) 2);

        Short result = decorator.get(key);

        Assert.assertEquals(value, result);
    }

    @Test
    public void testGetNoEntryValue() {
        Double key = 1.0;
        map.put(1.0, map.getNoEntryValue());

        Short result = decorator.get(key);

        Assert.assertNull(result);
    }

    @Test(expected = ClassCastException.class)
    public void testGetWrongKeyType() {
        decorator.get("key");
    }

    @Test
    public void testClear() {
        map.put(1.0, (short) 2);

        decorator.clear();

        Assert.assertEquals(0, map.size());
        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        Double key = 1.0;
        Short value = 2;
        map.put(1.0, (short) 2);

        Short result = decorator.remove(key);

        Assert.assertEquals(value, result);
        Assert.assertEquals(0, map.size());
        Assert.assertFalse(map.containsKey(1.0));
        Assert.assertFalse(map.containsValue((short) 2));
    }

    @Test
    public void testRemoveNoEntryKey() {
        map.put(map.getNoEntryKey(), (short) 1);
        Double key = map.getNoEntryKey();

        Short result = decorator.remove(key);

        Assert.assertNull(result);
        Assert.assertEquals(0, map.size());
        Assert.assertFalse(map.containsKey(map.getNoEntryKey()));
        Assert.assertFalse(map.containsValue((short) 1));
    }

    @Test(expected = ClassCastException.class)
    public void testRemoveWrongKeyType() {
        decorator.remove("key");
    }

    @Test
    public void testEntrySet() {
        map.put(1.0, (short) 2);
        map.put(2.0, (short) 3);
        map.put(3.0, (short) 4);

        int entrySetSize = decorator.entrySet().size();

        Assert.assertEquals(map.size(), entrySetSize);
    }

    @Test
    public void testEntrySetEmptyMap() {
        int entrySetSize = decorator.entrySet().size();

        Assert.assertEquals(0, entrySetSize);
    }

    @Test
    public void testContainsValue() {
        map.put(1.0, (short) 2);

        boolean result = decorator.containsValue((short) 2);

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsValueNotPresent() {
        boolean result = decorator.containsValue((short) 2);

        Assert.assertFalse(result);
    }

    @Test
    public void testContainsKey() {
        Double key = 1.0;
        map.put(1.0, (short) 2);

        boolean result = decorator.containsKey(key);

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsKeyNotPresent() {
        boolean result = decorator.containsKey(1.0);

        Assert.assertFalse(result);
    }

    @Test
    public void testSize() {
        map.put(1.0, (short) 2);
        map.put(2.0, (short) 3);
        map.put(3.0, (short) 4);

        int size = decorator.size();

        Assert.assertEquals(map.size(), size);
    }

    @Test
    public void testIsEmpty() {
        boolean isEmpty = decorator.isEmpty();

        Assert.assertTrue(isEmpty);
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> inputMap = new HashMap<>();
        inputMap.put(1.0, (short) 2);
        inputMap.put(2.0, null);

        decorator.putAll(inputMap);

        Assert.assertEquals(2, map.size());
        Assert.assertTrue(map.containsKey(1.0));
        Assert.assertTrue(map.containsKey(2.0));
        Assert.assertTrue(map.containsValue((short) 2));
        Assert.assertNull(map.get(2.0));
    }

    @Test
    public void testPutAllEmptyMap() {
        Map<Double, Short> inputMap = new HashMap<>();

        decorator.putAll(inputMap);

        Assert.assertEquals(0, map.size());
    }

    @Test
    public void testWrapKey() {
        double key = 2.0;

        Double wrappedKey = decorator.wrapKey(key);

        Assert.assertEquals(key, (double) wrappedKey, 0.0);
    }

    @Test
    public void testUnwrapKey() {
        Double key = 2.0;

        double unwrappedKey = decorator.unwrapKey(key);

        Assert.assertEquals(key, unwrappedKey, 0.0);
    }

    @Test
    public void testWrapValue() {
        short value = 2;

        Short wrappedValue = decorator.wrapValue(value);

        Assert.assertEquals(value, (short) wrappedValue);
    }

    @Test
    public void testUnwrapValue() {
        Short value = 2;

        short unwrappedValue = decorator.unwrapValue(value);

        Assert.assertEquals(value, (short) unwrappedValue);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        decorator.put(1.0, (short) 2);
        decorator.writeExternal(objectOutputStream);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        decorator = new TDoubleShortMapDecorator();
        decorator.readExternal(objectInputStream);

        Assert.assertEquals(1, map.size());
        Assert.assertTrue(map.containsKey(1.0));
        Assert.assertTrue(map.containsValue((short) 2));
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        decorator.writeExternal(objectOutputStream);

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        Assert.assertEquals(0, objectInputStream.readByte());
        TDoubleShortMap deserializedMap = (TDoubleShortMap) objectInputStream.readObject();
        Assert.assertNotNull(deserializedMap);
    }

}