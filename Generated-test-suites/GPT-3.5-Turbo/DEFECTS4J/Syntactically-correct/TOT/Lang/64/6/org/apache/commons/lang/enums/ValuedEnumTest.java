import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang.enums.ValuedEnum;

import static org.junit.Assert.*;

public class ValuedEnumTest {

    private static final int INVALID_VALUE = -1;

    private ValuedEnum enum1;
    private ValuedEnum enum2;
    private ValuedEnum enum3;
    private ValuedEnum enum4;

    @Before
    public void setUp() {
        enum1 = new ValuedEnumImpl("Enum 1", 1);
        enum2 = new ValuedEnumImpl("Enum 2", 2);
        enum3 = new ValuedEnumImpl("Enum 3", 3);
        enum4 = new ValuedEnumImpl("Enum 4", 4);
    }

    @Test
    public void testGetValue_validValue_success() {
        assertEquals(1, enum1.getValue());
        assertEquals(2, enum2.getValue());
        assertEquals(3, enum3.getValue());
        assertEquals(4, enum4.getValue());
    }

    @Test
    public void testGetEnum_enumExists_success() {
        assertEquals(enum1, ValuedEnum.getEnum(ValuedEnumImpl.class, 1));
        assertEquals(enum2, ValuedEnum.getEnum(ValuedEnumImpl.class, 2));
        assertEquals(enum3, ValuedEnum.getEnum(ValuedEnumImpl.class, 3));
        assertEquals(enum4, ValuedEnum.getEnum(ValuedEnumImpl.class, 4));
    }

    @Test
    public void testGetEnum_enumDoesNotExist_null() {
        assertNull(ValuedEnum.getEnum(ValuedEnumImpl.class, INVALID_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnum_nullEnumClass_exceptionThrown() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test
    public void testCompareTo_sameType_smallerValue_negativeValue() {
        assertTrue(enum1.compareTo(enum2) < 0);
        assertTrue(enum2.compareTo(enum3) < 0);
        assertTrue(enum3.compareTo(enum4) < 0);
    }

    @Test
    public void testCompareTo_sameType_greaterValue_positiveValue() {
        assertTrue(enum4.compareTo(enum3) > 0);
        assertTrue(enum3.compareTo(enum2) > 0);
        assertTrue(enum2.compareTo(enum1) > 0);
    }

    @Test
    public void testCompareTo_sameType_sameValue_zero() {
        assertTrue(enum1.compareTo(enum1) == 0);
        assertTrue(enum2.compareTo(enum2) == 0);
        assertTrue(enum3.compareTo(enum3) == 0);
        assertTrue(enum4.compareTo(enum4) == 0);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_differentTypes_exceptionThrown() {
        enum1.compareTo(new OtherValuedEnumImpl("Other Enum", 1));
    }

    @Test(expected = NullPointerException.class)
    public void testCompareTo_nullObject_exceptionThrown() {
        enum1.compareTo(null);
    }

    @Test
    public void testToString_validEnum_correctStringRepresentation() {
        assertEquals("ValuedEnumImpl[Enum 1=1]", enum1.toString());
        assertEquals("ValuedEnumImpl[Enum 2=2]", enum2.toString());
        assertEquals("ValuedEnumImpl[Enum 3=3]", enum3.toString());
        assertEquals("ValuedEnumImpl[Enum 4=4]", enum4.toString());
    }

    private static class ValuedEnumImpl extends ValuedEnum {
        protected ValuedEnumImpl(String name, int value) {
            super(name, value);
        }
    }

    private static class OtherValuedEnumImpl extends ValuedEnum {
        protected OtherValuedEnumImpl(String name, int value) {
            super(name, value);
        }
    }
}