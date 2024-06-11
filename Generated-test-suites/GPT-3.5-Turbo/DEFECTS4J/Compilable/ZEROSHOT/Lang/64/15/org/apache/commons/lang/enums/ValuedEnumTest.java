package org.apache.commons.lang.enums;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        ValuedEnum enum1 = new ValuedEnumImpl("Enum1", 1);
        assertEquals(1, enum1.getValue());

        ValuedEnum enum2 = new ValuedEnumImpl("Enum2", 2);
        assertEquals(2, enum2.getValue());

        ValuedEnum enum3 = new ValuedEnumImpl("Enum3", 3);
        assertEquals(3, enum3.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new ValuedEnumImpl("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnumImpl("Enum2", 2);
        ValuedEnum enum3 = new ValuedEnumImpl("Enum3", 3);

        assertTrue(enum1.compareTo(enum1) == 0);
        assertTrue(enum2.compareTo(enum2) == 0);
        assertTrue(enum3.compareTo(enum3) == 0);

        assertTrue(enum1.compareTo(enum2) < 0);
        assertTrue(enum1.compareTo(enum3) < 0);
        assertTrue(enum2.compareTo(enum3) < 0);

        assertTrue(enum2.compareTo(enum1) > 0);
        assertTrue(enum3.compareTo(enum1) > 0);
        assertTrue(enum3.compareTo(enum2) > 0);
    }

    @Test
    public void testToString() {
        ValuedEnum enum1 = new ValuedEnumImpl("Enum1", 1);
        assertEquals("ValuedEnumImpl[Enum1=1]", enum1.toString());

        ValuedEnum enum2 = new ValuedEnumImpl("Enum2", 2);
        assertEquals("ValuedEnumImpl[Enum2=2]", enum2.toString());

        ValuedEnum enum3 = new ValuedEnumImpl("Enum3", 3);
        assertEquals("ValuedEnumImpl[Enum3=3]", enum3.toString());
    }

    private static class ValuedEnumImpl extends ValuedEnum {
        protected ValuedEnumImpl(String name, int value) {
            super(name, value);
        }
    }
}