import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.decorator.TDoubleShortMapDecorator;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator map;
    
    @Before
    public void setUp() {
        map = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void testPutAndGet_NormalValues() {
        Double key1 = 1.5;
        Short value1 = 10;
        map.put(key1, value1);
        assertEquals(value1, map.get(key1));
        
        Double key2 = 2.0;
        Short value2 = 20;
        map.put(key2, value2);
        assertEquals(value2, map.get(key2));
    }
    
    @Test
    public void testPutAndGet_NullKey() {
        Double key = null;
        Short value = 10;
        map.put(key, value);
        assertEquals(value, map.get(key));
    }
    
    @Test
    public void testPutAndGet_NullValue() {
        Double key = 1.5;
        Short value = null;
        map.put(key, value);
        assertEquals(value, map.get(key));
    }
    
    @Test
    public void testPutAndGet_NoEntryKey() {
        Double key = -999.9;
        Short value = 10;
        map.put(key, value);
        assertEquals(value, map.get(key));
    }
    
    @Test
    public void testPutAndGet_NoEntryValue() {
        Double key = 1.5;
        Short value = -999;
        map.put(key, value);
        assertEquals(value, map.get(key));
    }
    
    @Test
    public void testClear() {
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        map.clear();
        assertNull(map.get(key));
    }
    
    @Test
    public void testRemove_NormalKey() {
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        Short removedValue = map.remove(key);
        assertEquals(value, removedValue);
        assertNull(map.get(key));
    }
    
    @Test
    public void testRemove_NullKey() {
        Double key = null;
        Short value = 10;
        map.put(key, value);
        Short removedValue = map.remove(key);
        assertEquals(value, removedValue);
        assertNull(map.get(key));
    }
    
    @Test
    public void testRemove_NoEntryKey() {
        Double key = -999.9;
        Short value = 10;
        map.put(key, value);
        Short removedValue = map.remove(key);
        assertEquals(value, removedValue);
        assertNull(map.get(key));
    }
    
    @Test
    public void testEntrySet_Size() {
        Double key1 = 1.5;
        Short value1 = 10;
        map.put(key1, value1);
        
        Double key2 = 2.0;
        Short value2 = 20;
        map.put(key2, value2);
        
        assertEquals(2, map.entrySet().size());
    }
    
    @Test
    public void testEntrySet_IsEmpty() {
        assertTrue(map.entrySet().isEmpty());
        
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        
        assertFalse(map.entrySet().isEmpty());
    }
    
    @Test
    public void testEntrySet_Contains() {
        Double key1 = 1.5;
        Short value1 = 10;
        map.put(key1, value1);
        
        Entry<Double, Short> entry1 = new Entry<Double, Short>() {
            public Double getKey() {
                return key1;
            }
            
            public Short getValue() {
                return value1;
            }
            
            public Short setValue(Short value) {
                return value1;
            }
        };
        
        assertTrue(map.entrySet().contains(entry1));
        
        Double key2 = 2.0;
        Short value2 = 20;
        Entry<Double, Short> entry2 = new Entry<Double, Short>() {
            public Double getKey() {
                return key2;
            }
            
            public Short getValue() {
                return value2;
            }
            
            public Short setValue(Short value) {
                return value2;
            }
        };
        
        assertFalse(map.entrySet().contains(entry2));
    }
    
    @Test
    public void testEntrySet_Iterator() {
        Double key1 = 1.5;
        Short value1 = 10;
        map.put(key1, value1);
        
        Double key2 = 2.0;
        Short value2 = 20;
        map.put(key2, value2);
        
        Iterator<Entry<Double, Short>> iterator = map.entrySet().iterator();
        assertTrue(iterator.hasNext());
        
        Entry<Double, Short> entry1 = iterator.next();
        assertEquals(key1, entry1.getKey());
        assertEquals(value1, entry1.getValue());
        
        assertTrue(iterator.hasNext());
        
        Entry<Double, Short> entry2 = iterator.next();
        assertEquals(key2, entry2.getKey());
        assertEquals(value2, entry2.getValue());
        
        assertFalse(iterator.hasNext());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_Add() {
        Entry<Double, Short> entry = new Entry<Double, Short>() {
            public Double getKey() {
                return 1.5;
            }
            
            public Short getValue() {
                return 10;
            }
            
            public Short setValue(Short value) {
                return 10;
            }
        };
        
        map.entrySet().add(entry);
    }
    
    @Test
    public void testEntrySet_Remove_Existing() {
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        
        Entry<Double, Short> entry = new Entry<Double, Short>() {
            public Double getKey() {
                return key;
            }
            
            public Short getValue() {
                return value;
            }
            
            public Short setValue(Short value) {
                return value;
            }
        };
        
        assertTrue(map.entrySet().remove(entry));
        assertNull(map.get(key));
    }
    
    @Test
    public void testEntrySet_Remove_NonExisting() {
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        
        Entry<Double, Short> entry = new Entry<Double, Short>() {
            public Double getKey() {
                return 2.0;
            }
            
            public Short getValue() {
                return 20;
            }
            
            public Short setValue(Short value) {
                return value;
            }
        };
        
        assertFalse(map.entrySet().remove(entry));
        assertNotNull(map.get(key));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_AddAll() {
        Collection<Entry<Double, Short>> entries = new ArrayList<>();
        entries.add(new Entry<Double, Short>() {
            public Double getKey() {
                return 1.5;
            }
            
            public Short getValue() {
                return 10;
            }
            
            public Short setValue(Short value) {
                return 10;
            }
        });
        
        map.entrySet().addAll(entries);
    }
    
    @Test
    public void testContainsValue_ExistingValue() {
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        
        assertTrue(map.containsValue(value));
    }
    
    @Test
    public void testContainsValue_NonExistingValue() {
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        
        assertFalse(map.containsValue(20));
    }
    
    @Test
    public void testContainsKey_ExistingKey() {
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        
        assertTrue(map.containsKey(key));
    }
    
    @Test
    public void testContainsKey_NullKey() {
        Double key = null;
        Short value = 10;
        map.put(key, value);
        
        assertTrue(map.containsKey(key));
    }
    
    @Test
    public void testContainsKey_NoEntryKey() {
        Double key = -999.9;
        Short value = 10;
        map.put(key, value);
        
        assertTrue(map.containsKey(key));
    }
    
    @Test
    public void testContainsKey_NonExistingKey() {
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        
        assertFalse(map.containsKey(2.0));
    }
    
    @Test
    public void testSize_EmptyMap() {
        assertEquals(0, map.size());
    }
    
    @Test
    public void testSize_NonEmptyMap() {
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        
        assertEquals(1, map.size());
    }
    
    @Test
    public void testIsEmpty_EmptyMap() {
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testIsEmpty_NonEmptyMap() {
        Double key = 1.5;
        Short value = 10;
        map.put(key, value);
        
        assertFalse(map.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> other = new HashMap<>();
        other.put(1.5, 10);
        other.put(2.0, 20);
        
        map.putAll(other);
        
        assertEquals(10, map.get(1.5).intValue());
        assertEquals(20, map.get(2.0).intValue());
    }
    
    @Test
    public void testWrapAndUnwrapKey() {
        double key = 1.5;
        Double wrappedKey = map.wrapKey(key);
        assertEquals(key, map.unwrapKey(wrappedKey), 0.0001);
    }
    
    @Test
    public void testWrapAndUnwrapValue() {
        short value = 10;
        Short wrappedValue = map.wrapValue(value);
        assertEquals(value, map.unwrapValue(wrappedValue));
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        
        Double key = 1.5;
        Short value = 10;
        
        map.put(key, value);
        map.writeExternal(out);
        
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        
        TDoubleShortMapDecorator newMap = new TDoubleShortMapDecorator();
        newMap.readExternal(in);
        
        assertEquals(value, newMap.get(key));
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        
        Double key = 1.5;
        Short value = 10;
        
        map.put(key, value);
        map.writeExternal(out);
        
        byte[] bytes = byteOut.toByteArray();
        
        assertTrue(bytes.length > 0);
    }
}