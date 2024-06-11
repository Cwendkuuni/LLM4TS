package org.junit.test;

import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testValuedEnumConstructor() {
        ValuedEnum testEnum = new ValuedEnum("Test", 1);
        assertEquals("Test", testEnum.getName());
        assertEquals(1, testEnum.getValue());
    }

    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnum("Enum2", 2);
        ValuedEnum expectedEnum = ValuedEnum.getEnum(ValuedEnum.class, 1);
        assertEquals(enum1, expectedEnum);

        expectedEnum = ValuedEnum.getEnum(ValuedEnum.class, 2);
        assertEquals(enum2, expectedEnum);
    }

    @Test
    public void testGetValue() {
        ValuedEnum testEnum = new ValuedEnum("Test", 1);
        assertEquals(1, testEnum.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnum("Enum2", 2);
        ValuedEnum enum3 = new ValuedEnum("Enum3", 1);

        assertEquals(0, enum1.compareTo(enum1));
        assertEquals(-1, enum1.compareTo(enum2));
        assertEquals(1, enum2.compareTo(enum1));
        assertEquals(0, enum1.compareTo(enum3));
    }

    @Test
    public void testToString() {
        ValuedEnum testEnum = new ValuedEnum("Test", 1);
        String expected = "ValuedEnum[Test=1]";
        assertEquals(expected, testEnum.toString());
    }

    @Test
    public void testGetEnumNullClass() {
        assertNull(ValuedEnum.getEnum(null, 1));
    }

    @Test
    public void testGetEnumInvalidValue() {
        assertNull(ValuedEnum.getEnum(ValuedEnum.class, 0));
    }

    @Test(expected = NullPointerException.class)
    public void testGetValueNullObject() {
        ValuedEnum testEnum = null;
        assertNull(testEnum.getValue());
    }

    @Test
    public void testCompareToSameObject() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        assertEquals(0, enum1.compareTo(enum1));
    }

    @Test
    public void testCompareToDifferentClasses() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        AnotherValuedEnum enum2 = new AnotherValuedEnum("AnotherEnum1", 1);
        assertNotEquals(0, enum1.compareTo(enum2));
    }

    @Test
    public void testToStringEmptyName() {
        ValuedEnum testEnum = new ValuedEnum("", 1);
        String expected = "ValuedEnum[]";
        assertEquals(expected, testEnum.toString());
    }

    @Test
    public void testToStringNoName() {
        ValuedEnum testEnum = new ValuedEnum(null, 1);
        String expected = "ValuedEnum[=1]";
        assertEquals(expected, testEnum.toString());
    }
}