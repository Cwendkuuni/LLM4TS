import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        ValuedEnum v = new ValuedEnumImpl("name", 100);
        assertEquals("name", v.getName());
        assertEquals(100, v.getValue());
    }

    @Test
    public void testGetEnum() {
        ValuedEnum v = ValuedEnum.getEnum(ValuedEnumImpl.class, 100);
        assertNotNull(v);
        assertEquals("name", v.getName());
        assertEquals(100, v.getValue());
    }

    @Test
    public void testGetEnum_NullEnumClass() {
        try {
            ValuedEnum.getEnum(null, 100);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Enum Class must not be null", e.getMessage());
        }
    }

    @Test
    public void testGetEnumList() {
        List list = ValuedEnum.getEnumList(ValuedEnumImpl.class);
        assertNotNull(list);
        assertEquals(1, list.size());
        ValuedEnum v = (ValuedEnum) list.get(0);
        assertEquals("name", v.getName());
        assertEquals(100, v.getValue());
    }

    @Test
    public void testGetEnumMap() {
        Map map = ValuedEnum.getEnumMap(ValuedEnumImpl.class);
        assertNotNull(map);
        assertEquals(1, map.size());
        ValuedEnum v = (ValuedEnum) map.get("name");
        assertNotNull(v);
        assertEquals("name", v.getName());
        assertEquals(100, v.getValue());
    }

    @Test
    public void testIterator() {
        Iterator iter = ValuedEnum.iterator(ValuedEnumImpl.class);
        assertNotNull(iter);
        assertTrue(iter.hasNext());
        ValuedEnum v = (ValuedEnum) iter.next();
        assertEquals("name", v.getName());
        assertEquals(100, v.getValue());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testGetValue() {
        ValuedEnum v = new ValuedEnumImpl("name", 100);
        assertEquals(100, v.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum v1 = new ValuedEnumImpl("name", 100);
        ValuedEnum v2 = new ValuedEnumImpl("name", 200);
        assertTrue(v1.compareTo(v2) < 0);
        assertTrue(v2.compareTo(v1) > 0);
        assertEquals(0, v1.compareTo(v1));
    }

    @Test
    public void testToString() {
        ValuedEnum v = new ValuedEnumImpl("name", 100);
        assertEquals("ValuedEnumImpl[name=100]", v.toString());
    }

    private static class ValuedEnumImpl extends ValuedEnum {
        protected ValuedEnumImpl(String name, int value) {
            super(name, value);
        }
    }
}