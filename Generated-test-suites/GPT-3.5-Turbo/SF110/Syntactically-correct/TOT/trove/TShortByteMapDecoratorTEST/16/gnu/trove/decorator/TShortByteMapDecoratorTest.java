import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator decorator;

    @Before
    public void setUp() {
        decorator = new TShortByteMapDecorator();
    }

    @Test
    public void testPutAndGet() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        
        Byte value1 = decorator.get((short) 1);
        Byte value2 = decorator.get((short) 3);
        
        assertEquals((byte) 2, value1.byteValue());
        assertEquals((byte) 4, value2.byteValue());
    }

    @Test
    public void testPutWithNullKeyAndValue() {
        decorator.put(null, null);
        
        assertEquals(null, decorator.get(null));
    }

    @Test
    public void testPutAndGetWithNoEntryKeyAndValue() {
        decorator.put((short) -1, (byte) -1);
        decorator.put((short) 0, (byte) 0);

        Byte value1 = decorator.get((short) -1);
        Byte value2 = decorator.get((short) 0);

        assertEquals((byte) -1, value1.byteValue());
        assertEquals((byte) 0, value2.byteValue());
    }

    @Test
    public void testRemove() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        
        Byte value1 = decorator.remove((short) 1);
        Byte value2 = decorator.remove((short) 3);
        
        assertEquals((byte) 2, value1.byteValue());
        assertEquals((byte) 4, value2.byteValue());
        assertEquals(null, decorator.get((short) 1));
        assertEquals(null, decorator.get((short) 3));
    }
    
    @Test
    public void testRemoveWithNoEntryKey() {
        decorator.put((short) -1, (byte) -1);
        decorator.put((short) 0, (byte) 0);

        Byte value1 = decorator.remove(null);
        Byte value2 = decorator.remove((short) -1);
        Byte value3 = decorator.remove((short) 0);

        assertEquals(null, value1);
        assertEquals(null, value2);
        assertEquals(null, value3);
    }

    @Test
    public void testClear() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        decorator.clear();

        assertEquals(0, decorator.size());
        assertEquals(null, decorator.get((short) 1));
        assertEquals(null, decorator.get((short) 3));
    }

    @Test
    public void testContainsKey() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 3));
        assertFalse(decorator.containsKey((short) 5));
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testContainsValue() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        assertTrue(decorator.containsValue((byte) 2));
        assertTrue(decorator.containsValue((byte) 4));
        assertFalse(decorator.containsValue((byte) 6));
        assertFalse(decorator.containsValue(null));
    }

    @Test
    public void testSize() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());

        decorator.put((short) 1, (byte) 2);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator otherDecorator = new TShortByteMapDecorator();
        otherDecorator.put((short) 1, (byte) 2);
        otherDecorator.put((short) 3, (byte) 4);

        decorator.putAll(otherDecorator);

        Byte value1 = decorator.get((short) 1);
        Byte value2 = decorator.get((short) 3);

        assertEquals((byte) 2, value1.byteValue());
        assertEquals((byte) 4, value2.byteValue());
    }

    @Test
    public void testEntrySet() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        Set<Map.Entry<Short, Byte>> entries = decorator.entrySet();

        assertEquals(2, entries.size());
        for (Map.Entry<Short, Byte> entry : entries) {
            Short key = entry.getKey();
            Byte value = entry.getValue();
            assertTrue(key.equals((short) 1) || key.equals((short) 3));
            assertTrue(value.equals((byte) 2) || value.equals((byte) 4));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_add() {
        Set<Map.Entry<Short, Byte>> entries = decorator.entrySet();
        entries.add(new AbstractMap.SimpleEntry<>((short) 1, (byte) 2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_addAll() {
        Set<Map.Entry<Short, Byte>> entries = decorator.entrySet();
        entries.addAll(new ArrayList<>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_remove() {
        decorator.put((short) 1, (byte) 2);
        Set<Map.Entry<Short, Byte>> entries = decorator.entrySet();
        entries.remove(new AbstractMap.SimpleEntry<>((short) 1, (byte) 2));
    }

    @Test
    public void testIterator() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        Iterator<Map.Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
        assertTrue(iterator.hasNext());

        Map.Entry<Short, Byte> entry1 = iterator.next();
        Short key1 = entry1.getKey();
        Byte value1 = entry1.getValue();
        assertTrue(key1.equals((short) 1) || key1.equals((short) 3));
        assertTrue(value1.equals((byte) 2) || value1.equals((byte) 4));

        Map.Entry<Short, Byte> entry2 = iterator.next();
        Short key2 = entry2.getKey();
        Byte value2 = entry2.getValue();
        assertTrue(key2.equals((short) 1) || key2.equals((short) 3));
        assertTrue(value2.equals((byte) 2) || value2.equals((byte) 4));

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_remove() {
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        Iterator<Map.Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
        assertTrue(iterator.hasNext());

        iterator.next();
        iterator.remove();

        assertFalse(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 3));
        assertEquals(null, decorator.get((short) 1));
        assertEquals((byte) 4, decorator.get((short) 3).byteValue());
    }

    @Test
    public void testIterator_noNextOrRemove() {
        decorator.put((short) 1, (byte) 2);

        Iterator<Map.Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
        assertTrue(iterator.hasNext());

        iterator.remove();

        assertFalse(decorator.containsKey((short) 1));
        assertEquals(0, decorator.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_add() {
        decorator.entrySet().iterator().add(null);
    }

    @Test
    public void testWrapAndUnwrapKey() {
        Short wrappedKey = decorator.wrapKey((short) 1);
        short unwrappedKey = decorator.unwrapKey(wrappedKey);

        assertEquals((short) 1, unwrappedKey);
    }

    @Test
    public void testWrapAndUnwrapValue() {
        Byte wrappedValue = decorator.wrapValue((byte) 2);
        byte unwrappedValue = decorator.unwrapValue(wrappedValue);

        assertEquals((byte) 2, unwrappedValue);
    }

    @Test
    public void testExternalizable() throws Exception {
        TShortByteMapDecorator oldDecorator = new TShortByteMapDecorator();
        oldDecorator.put((short) 1, (byte) 2);
        oldDecorator.put((short) 3, (byte) 4);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        oldDecorator.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        TShortByteMapDecorator newDecorator = new TShortByteMapDecorator();
        newDecorator.readExternal(objectInputStream);

        assertEquals(oldDecorator.size(), newDecorator.size());
        assertEquals(oldDecorator.get((short) 1), newDecorator.get((short) 1));
        assertEquals(oldDecorator.get((short) 3), newDecorator.get((short) 3));
    }

}