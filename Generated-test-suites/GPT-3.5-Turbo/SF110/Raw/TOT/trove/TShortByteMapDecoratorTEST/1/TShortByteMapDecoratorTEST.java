import static org.junit.Assert.*;

import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        assertEquals(1, map.size());
        assertEquals(Byte.valueOf((byte) 1), map.get((short) 1));
    }

    @Test
    public void testPutNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put(null, (byte) 1);
        
        assertEquals(1, map.size());
        assertEquals(Byte.valueOf((byte) 1), map.get(null));
    }
    
    @Test
    public void testPutNullValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, null);
        
        assertEquals(1, map.size());
        assertNull(map.get((short) 1));
    }
    
    @Test
    public void testPutNullKeyAndValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put(null, null);
        
        assertEquals(1, map.size());
        assertNull(map.get(null));
    }

    @Test
    public void testGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        Byte result = map.get((short) 1);
        assertEquals(Byte.valueOf((byte) 1), result);
    }

    @Test
    public void testGetNonExistingKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        Byte result = map.get((short) 2);
        assertNull(result);
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        Byte removed = map.remove((short) 1);
        assertEquals(Byte.valueOf((byte) 1), removed);
        assertEquals(0, map.size());
    }

    @Test
    public void testRemoveNonExistingKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        Byte removed = map.remove((short) 2);
        assertNull(removed);
    }
    
    @Test
    public void testEntrySetEmpty() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertTrue(map.entrySet().isEmpty());
    }
    
    @Test
    public void testEntrySetNonEmpty() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        
        assertEquals(2, map.entrySet().size());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAdd() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.entrySet().add(new Entry<Short, Byte>() {
            public Short getKey() { return (short) 1; }
            public Byte getValue() { return (byte) 1; }
            public Byte setValue(Byte value) { return null; }
        });
    }
    
    @Test
    public void testEntrySetContains() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        Entry<Short, Byte> entry = new Entry<Short, Byte>() {
            public Short getKey() { return (short) 1; }
            public Byte getValue() { return (byte) 1; }
            public Byte setValue(Byte value) { return null; }
        };
        assertTrue(map.entrySet().contains(entry));
    }
    
    @Test
    public void testEntrySetIterator() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        
        int count = 0;
        for (Entry<Short, Byte> entry : map.entrySet()) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            count++;
        }
        
        assertEquals(2, count);
    }
    
    @Test
    public void testEntrySetRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        Entry<Short, Byte> entry = new Entry<Short, Byte>() {
            public Short getKey() { return (short) 1; }
            public Byte getValue() { return (byte) 1; }
            public Byte setValue(Byte value) { return null; }
        };
        assertTrue(map.entrySet().remove(entry));
        assertFalse(map.containsKey((short) 1));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAddAll() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.entrySet().addAll(null);
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        assertTrue(map.containsValue((byte) 1));
    }
    
    @Test
    public void testContainsValueNonExistingValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        assertFalse(map.containsValue((byte) 2));
    }
    
    @Test
    public void testContainsValueWrongValueType() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        assertFalse(map.containsValue(1));
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        assertTrue(map.containsKey((short) 1));
    }
    
    @Test
    public void testContainsKeyNonExistingKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        assertFalse(map.containsKey((short) 2));
    }
    
    @Test
    public void testContainsKeyNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put(null, (byte) 1);
        
        assertTrue(map.containsKey(null));
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        
        assertEquals(2, map.size());
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertTrue(map.isEmpty());
        
        map.put((short) 1, (byte) 1);
        assertFalse(map.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Map<Short, Byte> inputMap = new HashMap<>();
        inputMap.put((short) 1, (byte) 1);
        inputMap.put((short) 2, (byte) 2);
        
        map.putAll(inputMap);
        
        assertEquals(2, map.size());
        assertEquals(Byte.valueOf((byte) 1), map.get((short) 1));
        assertEquals(Byte.valueOf((byte) 2), map.get((short) 2));
    }
    
    @Test
    public void testWrapKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        short key = 1;
        
        Short wrappedKey = map.wrapKey(key);
        
        assertEquals(key, (short) wrappedKey);
    }
    
    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = 1;
        
        short unwrappedKey = map.unwrapKey(key);
        
        assertEquals(key, (short) unwrappedKey);
    }
    
    @Test
    public void testWrapValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        byte value = 1;
        
        Byte wrappedValue = map.wrapValue(value);
        
        assertEquals(value, (byte) wrappedValue);
    }
    
    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = 1;
        
        byte unwrappedValue = map.unwrapValue(value);
        
        assertEquals(value, (byte) unwrappedValue);
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        TShortByteMap inputMap = new TShortByteHashMap();
        inputMap.put((short) 1, (byte) 1);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(inputMap);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        map.readExternal(ois);
        
        assertEquals(1, map.size());
        assertEquals(Byte.valueOf((byte) 1), map.get((short) 1));
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        map.writeExternal(oos);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TShortByteMapDecorator deserializedMap = new TShortByteMapDecorator();
        deserializedMap.readExternal(ois);
        
        assertEquals(1, deserializedMap.size());
        assertEquals(Byte.valueOf((byte) 1), deserializedMap.get((short) 1));
    }
}