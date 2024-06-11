import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

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
        assertNull(decorator.put(1.0, (short) 1));
        assertTrue(map.containsKey(1.0));
        assertTrue(map.containsValue((short) 1));

        assertNull(decorator.put(null, (short) 2));
        assertTrue(map.containsKey(map.getNoEntryKey()));
        assertTrue(map.containsValue((short) 2));

        Short existingValue = decorator.put(1.0, (short) 3);
        assertEquals(1, existingValue.intValue());
        assertTrue(map.containsKey(1.0));
        assertTrue(map.containsValue((short) 3));
    }

    @Test
    public void testGet() {
        map.put(1.0, (short) 1);
        assertEquals((short) 1, (short) decorator.get(1.0));

        assertEquals(null, decorator.get(2.0));
        assertEquals(null, (short) decorator.get(null));
    }

    @Test
    public void testClear() {
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        decorator.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        assertEquals((short) 1, (short) decorator.remove(1.0));
        assertFalse(map.containsKey(1.0));
        assertFalse(map.containsValue((short) 1));

        assertEquals(null, decorator.remove(3.0));
        assertEquals(null, decorator.remove(null));
    }

    @Test
    public void testEntrySet() {
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        Set<Map.Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0, (short) 1)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(2.0, (short) 2)));

        assertTrue(entrySet.isEmpty());

        entrySet.add(new AbstractMap.SimpleEntry<>(3.0, (short) 3));
        assertFalse(map.containsKey(3.0));
        assertFalse(map.containsValue((short) 3));

        entrySet.remove(new AbstractMap.SimpleEntry<>(2.0, (short) 2));
        assertFalse(map.containsKey(2.0));
        assertFalse(map.containsValue((short) 2));

        entrySet.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testContainsValue() {
        map.put(1.0, (short) 1);
        assertTrue(decorator.containsValue((short) 1));

        assertFalse(decorator.containsValue((short) 2));
        assertFalse(decorator.containsValue(null));
        assertFalse(decorator.containsValue("test"));
    }

    @Test
    public void testContainsKey() {
        map.put(1.0, (short) 1);
        assertTrue(decorator.containsKey(1.0));

        assertFalse(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(null));
        assertFalse(decorator.containsKey("test"));
    }

    @Test
    public void testSize() {
        assertEquals(0, decorator.size());

        map.put(1.0, (short) 1);
        assertEquals(1, decorator.size());

        map.remove(1.0);
        assertEquals(0, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());

        map.put(1.0, (short) 1);
        assertFalse(decorator.isEmpty());

        map.remove(1.0);
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> inputMap = new HashMap<>();
        inputMap.put(1.0, (short) 1);
        inputMap.put(2.0, (short) 2);

        decorator.putAll(inputMap);
        assertEquals(2, map.size());
        assertTrue(map.containsKey(1.0));
        assertTrue(map.containsKey(2.0));
        assertTrue(map.containsValue((short) 1));
        assertTrue(map.containsValue((short) 2));
    }

    @Test
    public void testWrapKey() {
        double key = 1.0;
        Double wrappedKey = decorator.wrapKey(key);
        assertEquals(key, wrappedKey, 0.0);
    }

    @Test
    public void testUnwrapKey() {
        Double key = 1.0;
        double unwrappedKey = decorator.unwrapKey(key);
        assertEquals(key, unwrappedKey, 0.0);
    }

    @Test
    public void testWrapValue() {
        short value = 1;
        Short wrappedValue = decorator.wrapValue(value);
        assertEquals(value, wrappedValue.shortValue());
    }

    @Test
    public void testUnwrapValue() {
        Short value = 1;
        short unwrappedValue = decorator.unwrapValue(value);
        assertEquals(value, unwrappedValue);
    }

    @Test
    public void testReadExternal() {
        decorator.readExternal(null);
        assertEquals(0, map.size());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            decorator.writeExternal(objectOutputStream);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            decorator.readExternal(objectInputStream);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(0, map.size());
    }

    @Test
    public void testWriteExternal() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            decorator.writeExternal(objectOutputStream);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            decorator.readExternal(objectInputStream);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(0, map.size());
    }
}