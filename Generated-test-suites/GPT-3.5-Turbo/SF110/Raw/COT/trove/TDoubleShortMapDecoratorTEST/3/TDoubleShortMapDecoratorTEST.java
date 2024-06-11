
import gnu.trove.map.*;
import gnu.trove.map.hash.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class TDoubleShortMapDecoratorTest {

    // create a TDoubleShortMapDecorator object for testing
    private TDoubleShortMapDecorator createTestObject() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        return new TDoubleShortMapDecorator(map);
    }

    // Test the put() method
    @Test
    public void testPut() {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        Double key = 3.0;
        Short value = 3;
        Assert.assertNull(mapDecorator.put(key, value));
        Assert.assertEquals(value, mapDecorator.getMap().get(key));
    }

    // Test the get() method
    @Test
    public void testGet() {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        Double key = 1.0;
        Short value = 1;
        Assert.assertEquals(value, mapDecorator.get(key));
    }

    // Test the clear() method
    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        mapDecorator.clear();
        Assert.assertTrue(mapDecorator.isEmpty());
    }

    // Test the remove() method
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        Double key = 2.0;
        Assert.assertEquals((Short) 2, mapDecorator.remove(key));
        Assert.assertNull(mapDecorator.get(key));
    }

    // Test the entrySet() method
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        Assert.assertEquals(2, mapDecorator.entrySet().size());

        for (Map.Entry<Double, Short> entry : mapDecorator.entrySet()) {
            Double key = entry.getKey();
            Short value = entry.getValue();
            Assert.assertEquals(value, mapDecorator.get(key));
        }
    }

    // Test the containsValue() method
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        Assert.assertTrue(mapDecorator.containsValue((short) 1));
        Assert.assertFalse(mapDecorator.containsValue((short) 3));
    }

    // Test the containsKey() method
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        Assert.assertTrue(mapDecorator.containsKey(2.0));
        Assert.assertFalse(mapDecorator.containsKey(3.0));
    }

    // Test the size() method
    @Test
    public void testSize() {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        Assert.assertEquals(2, mapDecorator.size());
        mapDecorator.put(3.0, (short) 3);
        Assert.assertEquals(3, mapDecorator.size());
    }

    // Test the isEmpty() method
    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        Assert.assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        Assert.assertTrue(mapDecorator.isEmpty());
    }

    // Test the putAll() method
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(3.0, (short) 3);
        map.put(4.0, (short) 4);
        mapDecorator.putAll(map);
        Assert.assertEquals(4, mapDecorator.size());
    }

    // Test the readExternal() method
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(mapDecorator);
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TDoubleShortMapDecorator deserializedMapDecorator = (TDoubleShortMapDecorator) objectInputStream.readObject();
        objectInputStream.close();

        Assert.assertEquals(mapDecorator.size(), deserializedMapDecorator.size());
    }

    // Test the writeExternal() method
    @Test
    public void testWriteExternal() throws IOException {
        TDoubleShortMapDecorator mapDecorator = createTestObject();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(mapDecorator);
        objectOutputStream.close();

        Assert.assertTrue(byteArrayOutputStream.toByteArray().length > 0);
    }
}

