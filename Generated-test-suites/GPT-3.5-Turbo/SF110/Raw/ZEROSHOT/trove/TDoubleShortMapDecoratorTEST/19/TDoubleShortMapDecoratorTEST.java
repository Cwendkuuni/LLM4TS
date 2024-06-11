import org.junit.Test;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.put(1.0, (short) 1));
        assertNull(map.put(2.0, (short) 2));
        assertNull(map.put(3.0, (short) 3));
        assertEquals(3, map.size());
        assertEquals((short) 1, (short) map.get(1.0));
        assertEquals((short) 2, (short) map.get(2.0));
        assertEquals((short) 3, (short) map.get(3.0));
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.get(1.0));
        map.put(1.0, (short) 1);
        assertEquals((short) 1, (short) map.get(1.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        map.clear();
        assertEquals(0, map.size());
        assertNull(map.get(1.0));
        assertNull(map.get(2.0));
        assertNull(map.get(3.0));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        assertNotNull(map.remove(1.0));
        assertNull(map.remove(1.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        assertEquals(3, map.entrySet().size());
        assertTrue(map.entrySet().contains(new Entry<Double, Short>() {
            @Override
            public Double getKey() {
                return 1.0;
            }

            @Override
            public Short getValue() {
                return (short) 1;
            }

            @Override
            public Short setValue(Short value) {
                return null;
            }
        }));
        assertFalse(map.entrySet().contains(new Entry<Double, Short>() {
            @Override
            public Double getKey() {
                return 4.0;
            }

            @Override
            public Short getValue() {
                return (short) 4;
            }

            @Override
            public Short setValue(Short value) {
                return null;
            }
        }));
        map.entrySet().remove(new Entry<Double, Short>() {
            @Override
            public Double getKey() {
                return 1.0;
            }

            @Override
            public Short getValue() {
                return (short) 1;
            }

            @Override
            public Short setValue(Short value) {
                return null;
            }
        });
        assertEquals(2, map.entrySet().size());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertFalse(map.containsValue((short) 1));
        map.put(1.0, (short) 1);
        assertTrue(map.containsValue((short) 1));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertFalse(map.containsKey(1.0));
        map.put(1.0, (short) 1);
        assertTrue(map.containsKey(1.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        map.put(1.0, (short) 1);
        assertEquals(1, map.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertTrue(map.isEmpty());
        map.put(1.0, (short) 1);
        assertFalse(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        TDoubleShortMapDecorator map2 = new TDoubleShortMapDecorator();
        map2.putAll(map);
        assertEquals(map.size(), map2.size());
        assertEquals(map.get(1.0), map2.get(1.0));
        assertEquals(map.get(2.0), map2.get(2.0));
        assertEquals(map.get(3.0), map2.get(3.0));
    }

    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(1.0, map.wrapKey(1.0), 0);
    }

    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(1.0, map.unwrapKey(1.0), 0);
    }

    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals((short) 1, (short) map.wrapValue((short) 1));
    }

    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals((short) 1, (short) map.unwrapValue((short) 1));
    }
}