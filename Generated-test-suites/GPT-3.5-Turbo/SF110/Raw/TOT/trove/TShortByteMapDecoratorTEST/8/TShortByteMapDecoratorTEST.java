import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        
        assertEquals(new Byte((byte)10), decorator.get((short)1));
    }
    
    @Test
    public void testPutNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put(null, (byte)10);
        
        assertEquals(new Byte((byte)10), decorator.get(null));
    }
    
    @Test
    public void testPutNullValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, null);
        
        assertNull(decorator.get((short)1));
    }
    
    @Test
    public void testGetNonExistingKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        assertNull(decorator.get((short)1));
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        
        decorator.remove((short)1);
        
        assertNull(decorator.get((short)1));
    }
    
    @Test
    public void testRemoveNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put(null, (byte)10);
        
        decorator.remove(null);
        
        assertNull(decorator.get(null));
    }
    
    @Test
    public void testRemoveNonExistingKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        assertNull(decorator.remove((short)1));
    }
    
    @Test
    public void testEntrySetSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);
        decorator.put((short)3, (byte)30);
        
        assertEquals(3, decorator.entrySet().size());
    }
    
    @Test
    public void testEntrySetContains() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);
        decorator.put((short)3, (byte)30);
        
        Map.Entry<Short, Byte> entry = new Map.Entry<Short, Byte>() {
            public Short getKey() {
                return (short)1;
            }
            
            public Byte getValue() {
                return (byte)10;
            }
            
            public Byte setValue(Byte value) {
                return null;
            }
        };
        
        assertTrue(decorator.entrySet().contains(entry));
    }
    
    @Test
    public void testEntrySetIterator() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        decorator.put((short)2, (byte)20);
        decorator.put((short)3, (byte)30);
        
        Map.Entry<Short, Byte> entry = decorator.entrySet().iterator().next();
        
        assertEquals(new Short((short)1), entry.getKey());
        assertEquals(new Byte((byte)10), entry.getValue());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAdd() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.entrySet().add(null);
    }
    
    @Test
    public void testEntrySetRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        
        Map.Entry<Short, Byte> entry = new Map.Entry<Short, Byte>() {
            public Short getKey() {
                return (short)1;
            }
            
            public Byte getValue() {
                return (byte)10;
            }
            
            public Byte setValue(Byte value) {
                return null;
            }
        };
        
        assertTrue(decorator.entrySet().remove(entry));
        assertNull(decorator.get((short)1));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAddAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.entrySet().addAll(null);
    }
    
    @Test
    public void testEntrySetClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        
        decorator.entrySet().clear();
        assertEquals(0, decorator.size());
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        
        assertTrue(decorator.containsValue((byte)10));
    }
    
    @Test
    public void testNotContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        assertFalse(decorator.containsValue((byte)10));
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        
        assertTrue(decorator.containsKey((short)1));
    }
    
    @Test
    public void testNotContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        assertFalse(decorator.containsKey((short)1));
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        
        assertEquals(1, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        assertTrue(decorator.isEmpty());
    }
    
    @Test
    public void testNotIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        
        assertFalse(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        Map<Short, Byte> map = new HashMap<>();
        map.put((short)1, (byte)10);
        map.put((short)2, (byte)20);
        
        decorator.putAll(map);
        
        assertEquals(new Byte((byte)10), decorator.get((short)1));
        assertEquals(new Byte((byte)20), decorator.get((short)2));
    }
    
    @Test
    public void testWrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        assertEquals(new Short((short)1), decorator.wrapKey((short)1));
    }
    
    @Test
    public void testUnwrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        assertEquals((short)1, decorator.unwrapKey(new Short((short)1)));
    }
    
    @Test
    public void testWrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        assertEquals(new Byte((byte)10), decorator.wrapValue((byte)10));
    }
    
    @Test
    public void testUnwrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        
        assertEquals((byte)10, decorator.unwrapValue(new Byte((byte)10)));
    }
    
    @Test
    public void testReadExternal() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            decorator.writeExternal(oos);
            oos.flush();
            
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            decorator.readExternal(ois);
            
            assertEquals(new Byte((byte)10), decorator.get((short)1));
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    
    @Test
    public void testWriteExternal() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short)1, (byte)10);
        
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            decorator.writeExternal(oos);
            oos.flush();
            
            byte[] data = bos.toByteArray();
            
            assertNotNull(data);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}