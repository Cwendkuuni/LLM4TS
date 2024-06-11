
package org.apache.commons.lang.enums;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testValuedEnumConstructor() {
        String name = "TestEnum";
        int value = 1;
        ValuedEnum testEnum = new ValuedEnum(name, value);
        assertEquals(name, testEnum.getName());
        assertEquals(value, testEnum.getValue());
    }

    @Test
    public void testGetEnum() {
        ClazzOfValuedEnum enumClass = new ClazzOfValuedEnum();
        ValuedEnum expectedEnum = new ValuedEnum("ExpectedEnum", 1);
        int value = 1;
        ValuedEnum testEnum = ValuedEnum.getEnum(enumClass.getClass(), value);
        assertEquals(expectedEnum, testEnum);
    }

    @Test
    public void testGetValue() {
        ValuedEnum testEnum = new ValuedEnum("TestEnum", 1);
        int expectedValue = 1;
        int actualValue = testEnum.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnum("Enum2", 2);
        int expectedResult = -1;
        int actualResult = enum1.compareTo(enum2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToString() {
        ValuedEnum testEnum = new ValuedEnum("TestEnum", 1);
        String expectedString = "TestEnum[name=TestEnum, value=1]";
        String actualString = testEnum.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testEdgeCases() {
        try {
            new ValuedEnum(null, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new ValuedEnum("TestEnum", -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            ValuedEnum.getEnum(null, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            ValuedEnum.getEnum(ClazzOfValuedEnum.class, 1000);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        ValuedEnum testEnum = new ValuedEnum("TestEnum", 1);
        try {
            testEnum.toString();
        } catch (NullPointerException e) {
            fail("Expected String instead of NullPointerException");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        new ValuedEnum(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeValue() {
        new ValuedEnum("TestEnum", -1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumWithNonExistentEnumValue() {
        List<ValuedEnum> enumList = Enum.getEnumList(ClazzOfValuedEnum.class);
        ValuedEnum testEnum = (ValuedEnum) enumList.get(0);
        ValuedEnum.getEnum(ClazzOfValuedEnum.class, testEnum.getValue() + 1);
    }

    @Test
    public void testToStringWithNoiToStringDefined() {
        ValuedEnum testEnum = new ValuedEnum("TestEnumWithoutiToStringDefined", 1) {
            private String iToString;
            @Override
            public String toString() {
                return null;
            }
        };
        try {
            testEnum.toString();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}
