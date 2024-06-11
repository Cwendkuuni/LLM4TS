package org.apache.commons.lang.enums;

import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testAbstractValuedEnum() {
        // Test the abstract class itself, which is not possible in normal circumstances,
        // but we can create a stub implementation for testing purposes
        ValuedEnumEnum valueEnumEnum = new ValuedEnumEnum("TestEnum", 1);

        assertEquals("TestEnum[name=TestEnum,value=1]", valueEnumEnum.toString());
        assertEquals(1, valueEnumEnum.getValue());

        ValuedEnum anotherValueEnum = new ValuedEnum("AnotherTestEnum", 2);
        int result = valueEnumEnum.compareTo(anotherValueEnum);
        assertEquals(-1, result);
    }

    // Insert test cases for subclasses of ValuedEnum here

    private static class ValuedEnumEnum extends ValuedEnum {
        ValuedEnumEnum(String name, int value) {
            super(name, value);
        }
    }
}