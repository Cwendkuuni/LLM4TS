import static org.junit.Assert.*;
import org.junit.Test;
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TShortByteMapDecoratorTest {
    @Test
    public void testPut() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Byte value = decorator.put((short)1, (byte)10);
        assertNull(value);
        assertTrue(decorator.containsKey((short)1));
        assertTrue(decorator.containsValue((byte)10));
    }

    @Test
    public void testGet() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Byte value = decorator.get((short)1);
        assertEquals((byte)10, value.byteValue());
    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Byte value = decorator.remove((short)1);
        assertEquals((byte)10, value.byteValue());
        assertFalse(decorator.containsKey((short)1));
        assertFalse(decorator.containsValue((byte)10));
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        Iterator<Map.Entry<Short, Byte>> iterator = entrySet.iterator();
        Map.Entry<Short, Byte> entry1 = iterator.next();
        assertEquals((short)1, entry1.getKey().shortValue());
        assertEquals((byte)10, entry1.getValue().byteValue());
        Map.Entry<Short, Byte> entry2 = iterator.next();
        assertEquals((short)2, entry2.getKey().shortValue());
        assertEquals((byte)20, entry2.getValue().byteValue());
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertTrue(decorator.containsValue((byte)10));
        assertFalse(decorator.containsValue((byte)20));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertTrue(decorator.containsKey((short)1));
        assertFalse(decorator.containsKey((short)2));
    }

    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)10);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(1, decorator.size());
        decorator.put((short)2, (byte)20);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertTrue(decorator.isEmpty());
        decorator.put((short)1, (byte)10);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> sourceMap = new HashMap<>();
        sourceMap.put((short)1, (byte)10);
        sourceMap.put((short)2, (byte)20);
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.putAll(sourceMap);
        assertTrue(decorator.containsKey((short)1));
        assertTrue(decorator.containsKey((short)2));
        assertTrue(decorator.containsValue((byte)10));
        assertTrue(decorator.containsValue((byte)20));
    }

    @Test
    public void testWrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Short key = decorator.wrapKey((short)1);
        assertEquals((short)1, key.shortValue());
    }

    @Test
    public void testUnwrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        short key = decorator.unwrapKey((short)1);
        assertEquals((short)1, key);
    }

    @Test
    public void testWrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Byte value = decorator.wrapValue((byte)10);
        assertEquals((byte)10, value.byteValue());
    }

    @Test
    public void testUnwrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        byte value = decorator.unwrapValue((byte)10);
        assertEquals((byte)10, value);
    }

    @Test
    public void testReadExternal() {
        try {
            TShortByteMap map = new TShortByteMap();
            map.put((short)1, (byte)10);
            TShortByteMapDecorator decorator = new TShortByteMapDecorator();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(map);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            decorator.readExternal(ois);

            assertTrue(decorator.containsKey((short)1));
            assertEquals((byte)10, decorator.get((short)1).byteValue());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteExternal() {
        try {
            TShortByteMap map = new TShortByteMap();
            map.put((short)1, (byte)10);
            TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            decorator.writeExternal(oos);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            TShortByteMapDecorator newDecorator = new TShortByteMapDecorator();
            newDecorator.readExternal(ois);

            assertTrue(newDecorator.containsKey((short)1));
            assertEquals((byte)10, newDecorator.get((short)1).byteValue());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}