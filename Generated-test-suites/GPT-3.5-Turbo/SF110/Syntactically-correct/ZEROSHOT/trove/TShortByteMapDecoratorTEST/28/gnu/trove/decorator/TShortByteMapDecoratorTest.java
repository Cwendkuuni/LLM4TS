import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class TShortByteMapDecoratorTest {
    TShortByteMapDecorator mapDecorator;
    TShortByteMap map;

    @Before
    public void setUp() {
        map = new TShortByteMap();
        mapDecorator = new TShortByteMapDecorator(map);
    }

    @After
    public void tearDown() {
        map = null;
        mapDecorator = null;
    }

    @Test
    public void testPutAndGet() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
        assertEquals(value, map.get((short) 1));
    }

    @Test
    public void testPutNullKey() {
        Byte value = 2;
        mapDecorator.put(null, value);
        assertEquals(value, mapDecorator.get(null));
        assertEquals(value, map.get(map.getNoEntryKey()));
    }

    @Test
    public void testPutNullValue() {
        Short key = 1;
        mapDecorator.put(key, null);
        assertNull(mapDecorator.get(key));
        assertEquals(map.getNoEntryValue(), map.get((short) 1));
    }

    @Test
    public void testGetNullKey() {
        assertNull(mapDecorator.get(null));
        assertEquals(map.getNoEntryValue(), map.get(map.getNoEntryKey()));
    }

    @Test
    public void testClear() {
        map.put((short) 1, (byte) 2);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        Short key = 1;
        Byte value = 2;
        map.put((short) 1, (byte) 2);
        Byte removedValue = mapDecorator.remove(key);
        assertEquals(value, removedValue);
        assertNull(mapDecorator.get(key));
        assertEquals(map.getNoEntryValue(), map.get((short) 1));
    }

    @Test
    public void testRemoveNullKey() {
        assertNull(mapDecorator.remove(null));
        assertNull(mapDecorator.get(null));
        assertEquals(map.getNoEntryValue(), map.get(map.getNoEntryKey()));
    }

    @Test
    public void testEntrySetSize() {
        assertEquals(0, mapDecorator.entrySet().size());

        map.put((short) 1, (byte) 2);
        assertEquals(1, mapDecorator.entrySet().size());

        map.put((short) 2, (byte) 3);
        assertEquals(2, mapDecorator.entrySet().size());
    }

    @Test
    public void testEntrySetIsEmpty() {
        assertTrue(mapDecorator.entrySet().isEmpty());

        map.put((short) 1, (byte) 2);
        assertFalse(mapDecorator.entrySet().isEmpty());

        map.remove((short) 1);
        assertTrue(mapDecorator.entrySet().isEmpty());
    }

    @Test
    public void testEntrySetContains() {
        map.put((short) 1, (byte) 2);
        map.put((short) 2, (byte) 3);
        map.put((short) 3, (byte) 4);

        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();

        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 2)));
        assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 3)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Short, Byte>((short) 2, (byte) 3)));
        assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<Short, Byte>((short) 2, (byte) 4)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Short, Byte>((short) 3, (byte) 4)));
        assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<Short, Byte>((short) 3, (byte) 5)));
    }

    @Test
    public void testEntrySetIterator() {
        map.put((short) 1, (byte) 2);
        map.put((short) 2, (byte) 3);
        map.put((short) 3, (byte) 4);

        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Iterator<Map.Entry<Short, Byte>> iterator = entrySet.iterator();

        assertTrue(iterator.hasNext());

        Map.Entry<Short, Byte> entry = iterator.next();
        assertEquals((short) 1, (short) entry.getKey());
        assertEquals((byte) 2, (byte) entry.getValue());

        assertTrue(iterator.hasNext());

        entry = iterator.next();
        assertEquals((short) 2, (short) entry.getKey());
        assertEquals((byte) 3, (byte) entry.getValue());

        assertTrue(iterator.hasNext());

        entry = iterator.next();
        assertEquals((short) 3, (short) entry.getKey());
        assertEquals((byte) 4, (byte) entry.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAdd() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        entrySet.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 2));
    }

    @Test
    public void testEntrySetRemove() {
        map.put((short) 1, (byte) 2);
        map.put((short) 2, (byte) 3);

        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        assertTrue(entrySet.remove(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 2)));
        assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 2)));
        assertEquals(1, mapDecorator.size());
        assertFalse(map.containsKey((short) 1));

        assertFalse(entrySet.remove(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 3)));
        assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 3)));
        assertEquals(1, mapDecorator.size());

        assertTrue(entrySet.remove(new AbstractMap.SimpleEntry<Short, Byte>((short) 2, (byte) 3)));
        assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<Short, Byte>((short) 2, (byte) 3)));
        assertEquals(0, mapDecorator.size());
        assertFalse(map.containsKey((short) 2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAddAll() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        entrySet.addAll(Collections.singletonList(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 2)));
    }

    @Test
    public void testEntrySetClear() {
        map.put((short) 1, (byte) 2);
        map.put((short) 2, (byte) 3);

        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        entrySet.clear();
        assertEquals(0, mapDecorator.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testContainsValue() {
        map.put((short) 1, (byte) 2);
        map.put((short) 2, (byte) 3);

        assertTrue(mapDecorator.containsValue((byte) 2));
        assertTrue(mapDecorator.containsValue((byte) 3));
        assertFalse(mapDecorator.containsValue((byte) 4));
    }

    @Test
    public void testContainsKey() {
        map.put((short) 1, (byte) 2);
        map.put((short) 2, (byte) 3);

        assertTrue(mapDecorator.containsKey((short) 1));
        assertTrue(mapDecorator.containsKey((short) 2));
        assertFalse(mapDecorator.containsKey((short) 3));
    }

    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());

        map.put((short) 1, (byte) 2);
        assertEquals(1, mapDecorator.size());

        map.put((short) 2, (byte) 3);
        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());

        map.put((short) 1, (byte) 2);
        assertFalse(mapDecorator.isEmpty());

        map.remove((short) 1);
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> testMap = new HashMap<>();
        testMap.put((short) 1, (byte) 2);
        testMap.put((short) 2, (byte) 3);

        mapDecorator.putAll(testMap);
        assertFalse(mapDecorator.isEmpty());
        assertEquals(2, mapDecorator.size());
        assertTrue(mapDecorator.containsKey((short) 1));
        assertTrue(mapDecorator.containsKey((short) 2));
        assertEquals((byte) 2, (byte) mapDecorator.get((short) 1));
        assertEquals((byte) 3, (byte) mapDecorator.get((short) 2));
    }

    @Test
    public void testWrapKey() {
        short k = 1;
        Short wrappedKey = mapDecorator.wrapKey(k);
        assertEquals(k, (short) wrappedKey);
    }

    @Test
    public void testUnwrapKey() {
        Short key = 1;
        short unwrappedKey = mapDecorator.unwrapKey(key);
        assertEquals((short) key, unwrappedKey);
    }

    @Test
    public void testWrapValue() {
        byte v = 2;
        Byte wrappedValue = mapDecorator.wrapValue(v);
        assertEquals(v, (byte) wrappedValue);
    }

    @Test
    public void testUnwrapValue() {
        Byte value = 2;
        byte unwrappedValue = mapDecorator.unwrapValue(value);
        assertEquals((byte) value, unwrappedValue);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(mapDecorator);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TShortByteMapDecorator deserializedMapDecorator = (TShortByteMapDecorator) objectInputStream.readObject();

        assertEquals(mapDecorator, deserializedMapDecorator);
    }

    @Test
    public void testWriteExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(mapDecorator);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TShortByteMapDecorator deserializedMapDecorator = (TShortByteMapDecorator) objectInputStream.readObject();

        assertEquals(mapDecorator, deserializedMapDecorator);
    }
}