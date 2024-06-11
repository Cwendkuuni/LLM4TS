package org.apache.commons.lang.enums;
import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        // create test object
        ValuedEnum enumItem = new ValuedEnumImpl("Test", 123);

        // check if getValue() returns the correct value
        assertEquals(123, enumItem.getValue());
    }

    @Test
    public void testCompareTo() {
        // create test objects
        ValuedEnum enumItem1 = new ValuedEnumImpl("Test1", 123);
        ValuedEnum enumItem2 = new ValuedEnumImpl("Test2", 456);

        // check compareTo() for less than
        assertTrue(enumItem1.compareTo(enumItem2) < 0);
        // check compareTo() for greater than
        assertTrue(enumItem2.compareTo(enumItem1) > 0);
        // check compareTo() for equal
        assertTrue(enumItem1.compareTo(enumItem1) == 0);
    }

    @Test
    public void testToString() {
        // create test object
        ValuedEnum enumItem = new ValuedEnumImpl("Test", 123);

        // check if toString() returns the expected format
        assertEquals("ValuedEnumImpl[Test=123]", enumItem.toString());
    }
    
    // Test Edge Cases and Exceptions
    @Test
    public void testGetEnum_invalidEnumClass() {
        // check if getEnum() throws IllegalArgumentException for null enumClass
        try {
            ValuedEnum.getEnum(null, 123);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testGetEnum_enumDoesNotExist() {
        // create test object
        ValuedEnum enumItem = new ValuedEnumImpl("Test", 123);

        // check if getEnum() returns null for non-existent value
        assertNull(ValuedEnum.getEnum(enumItem.getClass(), 456));
    }

    private static class ValuedEnumImpl extends ValuedEnum {

        private static final long serialVersionUID = 1L;

        public ValuedEnumImpl(String name, int value) {
            super(name, value);
        }
    }
}