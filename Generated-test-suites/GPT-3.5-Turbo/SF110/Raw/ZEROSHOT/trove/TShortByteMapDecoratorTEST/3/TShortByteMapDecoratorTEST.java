import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {
    
    private TShortByteMapDecorator mapDecorator;
    private TShortByteMap map;
    
    @Before
    public void setup() {
        map = new TShortByteMap();
        mapDecorator = new TShortByteMapDecorator(map);
    }
    
    @Test
    public void testPutAndGet() {
        mapDecorator.put((short)1, (byte)2);
        assertEquals((byte)2, mapDecorator.get((short)1).byteValue());
    }
    
    @Test
    public void testPutNullKey() {
        mapDecorator.put(null, (byte)2);
        assertEquals((byte)2, mapDecorator.get(null).byteValue());
    }
    
    @Test
    public void testPutNullValue() {
        mapDecorator.put((short)1, null);
        assertNull(mapDecorator.get((short)1));
    }
    
    @Test
    public void testGetNonExistingKey() {
        assertNull(mapDecorator.get((short)3));
    }
    
    @Test
    public void testRemove() {
        mapDecorator.put((short)1, (byte)2);
        assertEquals((byte)2, mapDecorator.remove((short)1).byteValue());
    }
    
    @Test
    public void testRemoveNonExistingKey() {
        assertNull(mapDecorator.remove((short)3));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, mapDecorator.size());
        mapDecorator.put((short)1, (byte)2);
        assertEquals(1, mapDecorator.size());
        mapDecorator.put((short)2, (byte)3);
        assertEquals(2, mapDecorator.size());
        mapDecorator.remove((short)1);
        assertEquals(1, mapDecorator.size());
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put((short)1, (byte)2);
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.remove((short)1);
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        assertFalse(mapDecorator.containsKey((short)1));
        mapDecorator.put((short)1, (byte)2);
        assertTrue(mapDecorator.containsKey((short)1));
        assertFalse(mapDecorator.containsKey((short)2));
    }
    
    @Test
    public void testContainsValue() {
        assertFalse(mapDecorator.containsValue((byte)2));
        mapDecorator.put((short)1, (byte)2);
        assertTrue(mapDecorator.containsValue((byte)2));
        assertFalse(mapDecorator.containsValue((byte)3));
    }
    
    @Test
    public void testPutAll() {
        TShortByteMap map2 = new TShortByteMap();
        map2.put((short)1, (byte)2);
        map2.put((short)2, (byte)3);
        mapDecorator.putAll(map2);
        assertEquals(2, mapDecorator.size());
        assertEquals((byte)2, mapDecorator.get((short)1).byteValue());
        assertEquals((byte)3, mapDecorator.get((short)2).byteValue());
    }
    
    @Test
    public void testClear() {
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
        assertNull(mapDecorator.get((short)1));
    }
    
    // Additional tests for entrySet method
    
    @Test
    public void testEntrySetSize() {
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)2, (byte)3);
        assertEquals(2, mapDecorator.entrySet().size());
        mapDecorator.remove((short)1);
        assertEquals(1, mapDecorator.entrySet().size());
        mapDecorator.clear();
        assertEquals(0, mapDecorator.entrySet().size());
    }
    
    @Test
    public void testEntrySetIsEmpty() {
        assertTrue(mapDecorator.entrySet().isEmpty());
        mapDecorator.put((short)1, (byte)2);
        assertFalse(mapDecorator.entrySet().isEmpty());
        mapDecorator.remove((short)1);
        assertTrue(mapDecorator.entrySet().isEmpty());
    }
    
    @Test
    public void testEntrySetContains() {
        mapDecorator.put((short)1, (byte)2);
        Entry<Short, Byte> entry = new MapEntry<Short, Byte>((short)1, (byte)2);
        assertTrue(mapDecorator.entrySet().contains(entry));
        Entry<Short, Byte> nonExistingEntry = new MapEntry<Short, Byte>((short)2, (byte)3);
        assertFalse(mapDecorator.entrySet().contains(nonExistingEntry));
    }
    
    @Test
    public void testEntrySetIterator() {
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)2, (byte)3);
        Iterator<Entry<Short, Byte>> iterator = mapDecorator.entrySet().iterator();
        assertTrue(iterator.hasNext());
        Entry<Short, Byte> entry1 = iterator.next();
        assertEquals((short)1, entry1.getKey().shortValue());
        assertEquals((byte)2, entry1.getValue().byteValue());
        assertTrue(iterator.hasNext());
        Entry<Short, Byte> entry2 = iterator.next();
        assertEquals((short)2, entry2.getKey().shortValue());
        assertEquals((byte)3, entry2.getValue().byteValue());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testEntrySetRemove() {
        mapDecorator.put((short)1, (byte)2);
        mapDecorator.put((short)2, (byte)3);
        Entry<Short, Byte> entry = new MapEntry<>(1, 2);
        assertTrue(mapDecorator.entrySet().remove(entry));
        assertFalse(mapDecorator.entrySet().remove(entry));
        assertEquals(1, mapDecorator.size());
        assertFalse(mapDecorator.entrySet().isEmpty());
    }
    
    // Additional tests for wrapKey and unwrapKey methods
    
    @Test
    public void testWrapKey() {
        assertEquals(new Short((short)1), mapDecorator.wrapKey((short)1));
    }
    
    @Test
    public void testUnwrapKey() {
        assertEquals((short)1, mapDecorator.unwrapKey(new Short((short)1)));
    }
    
    // Additional tests for wrapValue and unwrapValue methods
    
    @Test
    public void testWrapValue() {
        assertEquals(new Byte((byte)2), mapDecorator.wrapValue((byte)2));
    }
    
    @Test
    public void testUnwrapValue() {
        assertEquals((byte)2, mapDecorator.unwrapValue(new Byte((byte)2)));
    }
    
    @Test
    public void testExternalize() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(out);
        mapDecorator.writeExternal(objectOut);
        objectOut.flush();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objectIn = new ObjectInputStream(in);
        TShortByteMapDecorator deserializedMapDecorator = new TShortByteMapDecorator();
        deserializedMapDecorator.readExternal(objectIn);
        assertEquals(mapDecorator.size(), deserializedMapDecorator.size());
        assertEquals(mapDecorator.get((short)1), deserializedMapDecorator.get((short)1));
    }
}