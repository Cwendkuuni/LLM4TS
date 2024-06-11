import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short)2);
        mapDecorator.put(3.0, (short)4);
        mapDecorator.put(5.0, (short)6);
    }

    @Test
    public void testPut() {
        assertEquals((Short)2, mapDecorator.put(1.0, (short)3));
        assertEquals((Short)4, mapDecorator.put(3.0, (short)5));
        assertNull(mapDecorator.put(7.0, (short)8));
        assertNull(mapDecorator.put(null, null));
        assertEquals((Short)2, mapDecorator.put(null, (short)9));
    }

    @Test
    public void testGet() {
        assertEquals((Short)2, mapDecorator.get(1.0));
        assertEquals((Short)4, mapDecorator.get(3.0));
        assertEquals((Short)6, mapDecorator.get(5.0));
        assertNull(mapDecorator.get(7.0));
        assertNull(mapDecorator.get(null));
    }

    @Test
    public void testClear() {
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        assertEquals((Short)2, mapDecorator.remove(1.0));
        assertEquals((Short)4, mapDecorator.remove(3.0));
        assertEquals((Short)6, mapDecorator.remove(5.0));
        assertNull(mapDecorator.remove(7.0));
        assertNull(mapDecorator.remove(null));
    }

    @Test
    public void testEntrySet() {
        assertEquals(3, mapDecorator.entrySet().size());
        assertTrue(mapDecorator.entrySet().contains(new TDoubleShortMapDecoratorEntry(1.0, (short)2)));
        assertTrue(mapDecorator.entrySet().contains(new TDoubleShortMapDecoratorEntry(3.0, (short)4)));
        assertTrue(mapDecorator.entrySet().contains(new TDoubleShortMapDecoratorEntry(5.0, (short)6)));
        assertFalse(mapDecorator.entrySet().contains(new TDoubleShortMapDecoratorEntry(7.0, (short)8)));
        assertFalse(mapDecorator.entrySet().contains(new TDoubleShortMapDecoratorEntry(null, null)));
    }

    @Test
    public void testContainsValue() {
        assertTrue(mapDecorator.containsValue((short)2));
        assertTrue(mapDecorator.containsValue((short)4));
        assertTrue(mapDecorator.containsValue((short)6));
        assertFalse(mapDecorator.containsValue((short)8));
        assertFalse(mapDecorator.containsValue(null));
    }

    @Test
    public void testContainsKey() {
        assertTrue(mapDecorator.containsKey(1.0));
        assertTrue(mapDecorator.containsKey(3.0));
        assertTrue(mapDecorator.containsKey(5.0));
        assertFalse(mapDecorator.containsKey(7.0));
        assertFalse(mapDecorator.containsKey(null));
    }

    @Test
    public void testSize() {
        assertEquals(3, mapDecorator.size());
        mapDecorator.put(7.0, (short)8);
        assertEquals(4, mapDecorator.size());
        mapDecorator.remove(1.0);
        assertEquals(3, mapDecorator.size());
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator2 = new TDoubleShortMapDecorator();
        mapDecorator2.put(7.0, (short)9);
        mapDecorator2.put(10.0, (short)11);

        mapDecorator.putAll(mapDecorator2);

        assertEquals((Short)9, mapDecorator.get(7.0));
        assertEquals((Short)11, mapDecorator.get(10.0));
        
        // Testing putAll with null
        mapDecorator.putAll(null);
        assertEquals(2, mapDecorator.size());
    }

    private class TDoubleShortMapDecoratorEntry implements Map.Entry<Double, Short> {
        private final Double key;
        private Short value;

        public TDoubleShortMapDecoratorEntry(Double key, Short value) {
            this.key = key;
            this.value = value;
        }

        public Double getKey() {
            return key;
        }

        public Short getValue() {
            return value;
        }

        public Short setValue(Short value) {
            this.value = value;
            return value;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TDoubleShortMapDecoratorEntry)) return false;
            TDoubleShortMapDecoratorEntry entry = (TDoubleShortMapDecoratorEntry) o;
            return key.equals(entry.key) && value.equals(entry.value);
        }

        public int hashCode() {
            return key.hashCode() ^ value.hashCode();
        }
    }
}