
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);

        assertEquals((Short) 1, mapDecorator.get(1.0));
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);

        assertEquals((Short) 1, mapDecorator.get(1.0));
        assertNull(mapDecorator.get(2.0));
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.clear();

        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        Short removedValue = mapDecorator.remove(1.0);

        assertEquals((Short) 1, removedValue);
        assertEquals(0, mapDecorator.size());
        assertNull(mapDecorator.get(1.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        assertEquals(2, mapDecorator.entrySet().size());
        assertTrue(mapDecorator.entrySet().contains(new Entry<Double, Short>() {
            public boolean equals(Object obj) {
                return obj instanceof Entry &&
                        ((Entry) obj).getKey().equals(1.0) &&
                        ((Entry) obj).getValue().equals((short) 1);
            }

            public Double getKey() {
                return 1.0;
            }

            public Short getValue() {
                return (short) 1;
            }

            public Short setValue(Short value) {
                return null;
            }

            public int hashCode() {
                return 1.0.hashCode() + 1;
            }
        }));

        assertFalse(mapDecorator.entrySet().contains(new Entry<Double, Short>() {
            public boolean equals(Object obj) {
                return obj instanceof Entry &&
                        ((Entry) obj).getKey().equals(3.0) &&
                        ((Entry) obj).getValue().equals((short) 3);
            }

            public Double getKey() {
                return 3.0;
            }

            public Short getValue() {
                return (short) 3;
            }

            public Short setValue(Short value) {
                return null;
            }

            public int hashCode() {
                return 3.0.hashCode() + 3;
            }
        }));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);

        assertTrue(mapDecorator.containsValue((short) 1));
        assertFalse(mapDecorator.containsValue((short) 2));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);

        assertTrue(mapDecorator.containsKey(1.0));
        assertFalse(mapDecorator.containsKey(2.0));
        assertFalse(mapDecorator.containsKey(null));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        assertTrue(mapDecorator.isEmpty());

        mapDecorator.put(1.0, (short) 1);

        assertFalse(mapDecorator.isEmpty());

        mapDecorator.remove(1.0);

        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        mapDecorator.putAll(new HashMap<Double, Short>() {{
            put(1.0, (short) 1);
            put(2.0, (short) 2);
        }});

        assertEquals(2, mapDecorator.size());
        assertEquals((Short) 1, mapDecorator.get(1.0));
        assertEquals((Short) 2, mapDecorator.get(2.0));
    }

    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        assertEquals((Double) 1.0, mapDecorator.wrapKey(1.0));
        assertEquals((Double) 2.0, mapDecorator.wrapKey(2.0));
    }

    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        assertEquals(1.0, mapDecorator.unwrapKey(1.0));
        assertEquals(2.0, mapDecorator.unwrapKey(2.0));
    }

    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        assertEquals((Short) 1, mapDecorator.wrapValue((short) 1));
        assertEquals((Short) 2, mapDecorator.wrapValue((short) 2));
    }

    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        assertEquals((short) 1, mapDecorator.unwrapValue((short) 1));
        assertEquals((short) 2, mapDecorator.unwrapValue((short) 2));
    }
}
