import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setUp() {
        TShortByteMap map = new TShortByteMap();
        mapDecorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testPutNullKey() {
        Byte value = 2;
        mapDecorator.put(null, value);
        assertEquals(value, mapDecorator.get(null));
    }

    @Test
    public void testPutNullValue() {
        Short key = 1;
        mapDecorator.put(key, null);
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testGetNullKey() {
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testClear() {
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(value, mapDecorator.remove(key));
        assertNull(mapDecorator.get(key));
    }

    @Test
    public void testRemoveNullKey() {
        assertNull(mapDecorator.remove(null));
    }

    @Test
    public void testEntrySetSize() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertEquals(1, mapDecorator.entrySet().size());
    }

    @Test
    public void testEntrySetIsEmpty() {
        assertTrue(mapDecorator.entrySet().isEmpty());
    }

    @Test
    public void testEntrySetContains() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.entrySet().contains(new Map.Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return key;
            }

            @Override
            public Byte getValue() {
                return value;
            }

            @Override
            public Byte setValue(Byte value) {
                return null;
            }
        }));
    }

    @Test
    public void testEntrySetIterator() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.entrySet().iterator().hasNext());
    }

    @Test
    public void testEntrySetAdd() {
        assertThrows(UnsupportedOperationException.class, () -> {
            mapDecorator.entrySet().add(new Map.Entry<Short, Byte>() {
                @Override
                public Short getKey() {
                    return 1;
                }

                @Override
                public Byte getValue() {
                    return 2;
                }

                @Override
                public Byte setValue(Byte value) {
                    return null;
                }
            });
        });
    }

    @Test
    public void testEntrySetRemove() {
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);
        assertTrue(mapDecorator.entrySet().remove(new Map.Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return key;
            }

            @Override
            public Byte getValue() {
                return value;
            }

            @Override
            public Byte setValue(Byte value) {
                return null;
            }
        }));
        assertFalse(mapDecorator.containsKey(key));
    }

    @Test
    public void testEntrySetAddAll() {
        assertThrows(UnsupportedOperationException.class, () -> {
            mapDecorator.entrySet().addAll(null);
        });
    }

    @Test
    public void testEntrySetClear() {
        mapDecorator.entrySet().clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsValue() {
        Byte value = 2;
        mapDecorator.put((short) 1, value);
        assertTrue(mapDecorator.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        Short key = 1;
        mapDecorator.put(key, (byte) 2);
        assertTrue(mapDecorator.containsKey(key));
    }

    @Test
    public void testContainsNullKey() {
        assertFalse(mapDecorator.containsKey(null));
    }

    @Test
    public void testSize() {
        mapDecorator.put((short) 1, (byte) 2);
        mapDecorator.put((short) 3, (byte) 4);
        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 1, (byte) 2);
        mapDecorator.putAll(map);
        assertEquals((byte) 2, (byte) mapDecorator.get((short) 1));
    }

    @Test
    public void testWrapKey() {
        short key = 1;
        assertEquals((short) key, (short) mapDecorator.wrapKey(key));
    }

    @Test
    public void testUnwrapKey() {
        Short key = 1;
        assertEquals((short) key, (short) mapDecorator.unwrapKey(key));
    }

    @Test
    public void testWrapValue() {
        byte value = 2;
        assertEquals((byte) value, (byte) mapDecorator.wrapValue(value));
    }

    @Test
    public void testUnwrapValue() {
        Byte value = 2;
        assertEquals((byte) value, (byte) mapDecorator.unwrapValue(value));
    }
}