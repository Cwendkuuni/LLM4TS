import org.junit.*;
import static org.junit.Assert.*;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator map;

    @Before
    public void setUp() {
        map = new TShortByteMapDecorator();
    }

    @After
    public void tearDown() {
        map = null;
    }

    @Test
    public void testPutAndGet_NormalCase() {
        Short key = 1;
        Byte value = 10;

        assertNull(map.put(key, value));
        assertEquals(value, map.get(key));
    }

    @Test
    public void testPutAndGet_NullKey() {
        Short key = null;
        Byte value = 10;

        assertNull(map.put(key, value));
        assertEquals(value, map.get(key));
    }

    @Test
    public void testPutAndGet_NullValue() {
        Short key = 1;
        Byte value = null;

        assertNull(map.put(key, value));
        assertEquals(value, map.get(key));
    }

    @Test
    public void testGet_NonExistingKey() {
        Short key = 2;

        assertNull(map.get(key));
    }

    @Test
    public void testClear() {
        Short key = 1;
        Byte value = 10;

        assertNull(map.put(key, value));
        assertFalse(map.isEmpty());

        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove_NormalCase() {
        Short key = 1;
        Byte value = 10;

        assertNull(map.put(key, value));
        assertEquals(value, map.remove(key));
        assertNull(map.get(key));
    }

    @Test
    public void testRemove_NonExistingKey() {
        Short key = 2;

        assertNull(map.remove(key));
    }

    @Test
    public void testEntrySet_Size() {
        Short key1 = 1;
        Byte value1 = 10;
        Short key2 = 2;
        Byte value2 = 20;

        assertNull(map.put(key1, value1));
        assertNull(map.put(key2, value2));

        assertEquals(2, map.entrySet().size());
    }

    @Test
    public void testEntrySet_IsEmpty() {
        assertTrue(map.entrySet().isEmpty());

        Short key = 1;
        Byte value = 10;

        assertNull(map.put(key, value));
        assertFalse(map.entrySet().isEmpty());
    }

    @Test
    public void testEntrySet_Contains() {
        Short key = 1;
        Byte value = 10;

        assertNull(map.put(key, value));

        assertFalse(map.entrySet().contains(new Map.Entry<Short, Byte>() {
            public Short getKey() {
                return 2;
            }

            public Byte getValue() {
                return 20;
            }

            public Byte setValue(Byte value) {
                return null;
            }

            public boolean equals(Object obj) {
                if (obj instanceof Map.Entry) {
                    Map.Entry<Short, Byte> other = (Map.Entry<Short, Byte>) obj;
                    return getKey().equals(other.getKey()) && getValue().equals(other.getValue());
                }
                return false;
            }

            public int hashCode() {
                return getKey().hashCode() + getValue().hashCode();
            }
        }));

        assertTrue(map.entrySet().contains(new Map.Entry<Short, Byte>() {
            public Short getKey() {
                return 1;
            }

            public Byte getValue() {
                return 10;
            }

            public Byte setValue(Byte value) {
                return null;
            }

            public boolean equals(Object obj) {
                if (obj instanceof Map.Entry) {
                    Map.Entry<Short, Byte> other = (Map.Entry<Short, Byte>) obj;
                    return getKey().equals(other.getKey()) && getValue().equals(other.getValue());
                }
                return false;
            }

            public int hashCode() {
                return getKey().hashCode() + getValue().hashCode();
            }
        }));
    }

    @Test
    public void testEntrySet_Iterator() {
        Short key1 = 1;
        Byte value1 = 10;
        Short key2 = 2;
        Byte value2 = 20;

        assertNull(map.put(key1, value1));
        assertNull(map.put(key2, value2));

        int count = 0;
        for (Map.Entry<Short, Byte> entry : map.entrySet()) {
            if (entry.getKey().equals(key1) && entry.getValue().equals(value1)) {
                count++;
            }
            if (entry.getKey().equals(key2) && entry.getValue().equals(value2)) {
                count++;
            }
        }

        assertEquals(2, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_Add() {
        map.entrySet().add(new Map.Entry<Short, Byte>() {
            public Short getKey() {
                return 1;
            }

            public Byte getValue() {
                return 10;
            }

            public Byte setValue(Byte value) {
                return null;
            }

            public boolean equals(Object obj) {
                if (obj instanceof Map.Entry) {
                    Map.Entry<Short, Byte> other = (Map.Entry<Short, Byte>) obj;
                    return getKey().equals(other.getKey()) && getValue().equals(other.getValue());
                }
                return false;
            }

            public int hashCode() {
                return getKey().hashCode() + getValue().hashCode();
            }
        });
    }

    @Test
    public void testEntrySet_Remove() {
        Short key = 1;
        Byte value = 10;

        assertNull(map.put(key, value));
        assertTrue(map.entrySet().remove(new Map.Entry<Short, Byte>() {
            public Short getKey() {
                return 1;
            }

            public Byte getValue() {
                return 10;
            }

            public Byte setValue(Byte value) {
                return null;
            }

            public boolean equals(Object obj) {
                if (obj instanceof Map.Entry) {
                    Map.Entry<Short, Byte> other = (Map.Entry<Short, Byte>) obj;
                    return getKey().equals(other.getKey()) && getValue().equals(other.getValue());
                }
                return false;
            }

            public int hashCode() {
                return getKey().hashCode() + getValue().hashCode();
            }
        }));
        assertTrue(map.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_AddAll() {
        map.entrySet().addAll(new java.util.HashSet<Map.Entry<Short, Byte>>());
    }

    @Test
    public void testEntrySet_Clear() {
        Short key = 1;
        Byte value = 10;

        assertNull(map.put(key, value));
        assertFalse(map.entrySet().isEmpty());

        map.entrySet().clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testContainsValue() {
        Byte value1 = 10;
        Byte value2 = 20;

        assertFalse(map.containsValue(value1));
        assertFalse(map.containsValue(value2));

        Short key1 = 1;
        Short key2 = 2;

        assertNull(map.put(key1, value1));
        assertNull(map.put(key2, value2));

        assertTrue(map.containsValue(value1));
        assertTrue(map.containsValue(value2));
    }

    @Test
    public void testContainsKey() {
        Short key1 = 1;
        Short key2 = 2;

        assertFalse(map.containsKey(key1));
        assertFalse(map.containsKey(key2));

        Byte value1 = 10;
        Byte value2 = 20;

        assertNull(map.put(key1, value1));
        assertNull(map.put(key2, value2));

        assertTrue(map.containsKey(key1));
        assertTrue(map.containsKey(key2));
    }

    @Test
    public void testSize() {
        assertEquals(0, map.size());

        Short key = 1;
        Byte value = 10;

        assertNull(map.put(key, value));
        assertEquals(1, map.size());

        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(map.isEmpty());

        Short key = 1;
        Byte value = 10;

        assertNull(map.put(key, value));
        assertFalse(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator otherMap = new TShortByteMapDecorator();
        Short key1 = 1;
        Byte value1 = 10;
        Short key2 = 2;
        Byte value2 = 20;

        assertNull(otherMap.put(key1, value1));
        assertNull(otherMap.put(key2, value2));

        map.putAll(otherMap);

        assertEquals(2, map.size());
        assertEquals(value1, map.get(key1));
        assertEquals(value2, map.get(key2));
    }

    @Test
    public void testWrapKey() {
        short k = 1;

        Short wrappedKey = map.wrapKey(k);

        assertEquals(k, (short) wrappedKey);
    }

    @Test
    public void testUnwrapKey() {
        Short wrappedKey = 1;

        short unwrappedKey = map.unwrapKey(wrappedKey);

        assertEquals(wrappedKey, (short) unwrappedKey);
    }

    @Test
    public void testWrapValue() {
        byte v = 10;

        Byte wrappedValue = map.wrapValue(v);

        assertEquals(v, (byte) wrappedValue);
    }

    @Test
    public void testUnwrapValue() {
        Byte wrappedValue = 10;

        byte unwrappedValue = map.unwrapValue(wrappedValue);

        assertEquals(wrappedValue, (byte) unwrappedValue);
    }

}