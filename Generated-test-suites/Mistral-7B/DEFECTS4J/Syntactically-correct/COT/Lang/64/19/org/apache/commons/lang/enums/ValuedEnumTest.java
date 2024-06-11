package org.apache.commons.lang.enums;

import static org.junit.Assert.*;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.enums.ValuedEnum;

import java.lang.reflect.Method;
import java.util.List;

public class ValuedEnumTest {

    // Test methods for abstract class ValuedEnum

    @org.junit.Test
    public void testGetEnum() throws Exception {
        ValuedEnum enumClass = ValuedEnum.class.getMember("JAVA1_0").get(null);
        ValuedEnum result = ValuedEnum.getEnum(enumClass.getClass(), enumClass.getValue());
        assertEquals(enumClass, result);
    }

    @org.junit.Test
    public void testGetEnumNullClass() {
        try {
            ValuedEnum.getEnum(null, 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The Enum Class must not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testGetEnumNullValue() {
        try {
            ValuedEnum.getEnum(ValuedEnum.class, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The Enum value must not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testGetEnumInvalidValue() {
        try {
            ValuedEnum enumClass = ValuedEnum.class.getMember("JAVA1_0").get(null);
            ValuedEnum result = ValuedEnum.getEnum(enumClass.getClass(), 1000);
            assertNull(result);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testCompareToEqual() {
        ValuedEnum enum1 = ValuedEnum.class.getMember("JAVA1_0").get(null);
        ValuedEnum enum2 = ValuedEnum.class.getMember("JAVA1_0").get(null);
        int comparisonResult = enum1.compareTo(enum2);
        assertEquals(0, comparisonResult);
    }

    @org.junit.Test
    public void testCompareToLess() {
        ValuedEnum enum1 = ValuedEnum.class.getMember("JAVA1_0").get(null);
        ValuedEnum enum2 = ValuedEnum.class.getMember("JAVA1_1").get(null);
        int comparisonResult = enum1.compareTo(enum2);
        assertLess(comparisonResult);
    }

    @org.junit.Test
    public void testCompareToGreater() {
        ValuedEnum enum1 = ValuedEnum.class.getMember("JAVA1_1").get(null);
        ValuedEnum enum2 = ValuedEnum.class.getMember("JAVA1_0").get(null);
        int comparisonResult = enum1.compareTo(enum2);
        assertGreater(comparisonResult);
    }

    @org.junit.Test
    public void testToString() {
        ValuedEnum enumInstance = ValuedEnum.class.getMember("JAVA1_0").get(null);
        assertEquals("JavaVersion[Java 1.0=100]", enumInstance.toString());
    }

    // Test methods for protected methods

    @org.junit.Test(expected = IllegalAccessException.class)
    public void testProtectedGetEnum() {
        ValuedEnum enumClass = ValuedEnum.class.getMember("JAVA1_0").get(null);
        ValuedEnum result = (ValuedEnum) ValuedEnum.getEnum(enumClass.getClass(), enumClass.getValue()); // Should fail
    }

    @org.junit.Test(expected = InvocationTargetException.class)
    public void testProtectedGetEnumException() throws Exception {
        Method getEnumMethod = ValuedEnum.class.getMethod("getEnum", Class.class, int.class);
        getEnumMethod.setAccessible(true);
        ValuedEnum enumClass = ValuedEnum.class.getMember("JAVA1_0").get(null);
        ValuedEnum result = (ValuedEnum) getEnumMethod.invoke(null, null, enumClass.getValue()); // Should throw NullPointerException
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testGetEnumNullList() throws Exception {
        Method getEnumMethod = ValuedEnum.class.getMethod("getEnum", Class.class, int.class);
        getEnumMethod.setAccessible(true);
        List emptyList = new ArrayList();
        ValuedEnum result = (ValuedEnum) getEnumMethod.invoke(null, emptyList.getClass(), 1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetEnumIndexOutOfBounds() throws Exception {
        List list = Enum.getEnumList(ValuedEnum.class);
        Method getEnumMethod = ValuedEnum.class.getMethod("getEnum", Class.class, int.class);
        getEnumMethod.setAccessible(true);
        ValuedEnum result = (ValuedEnum) getEnumMethod.invoke(null, ValuedEnum.class, list.size());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetEnumNegativeValue() throws Exception {
        Method getEnumMethod = ValuedEnum.class.getMethod("getEnum", Class.class, int.class);
        getEnumMethod.setAccessible(true);
        ValuedEnum result = (ValuedEnum) getEnumMethod.invoke(null, ValuedEnum.class, -1);
    }

    @org.junit.Test
    public void testToStringNullName() {
        ValuedEnum enumInstance = ValuedEnum.class.getMember("JAVA1_0").get(null);
        String name = null;
        try {
            String result = enumInstance.toString();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testToStringNullValue() {
        ValuedEnum enumInstance = ValuedEnum.class.getMember("JAVA1_0").get(null);
        int value = 0;
        try {
            String result = enumInstance.toString();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    private void assertLess(int comparisonResult) {
        assertTrue(comparisonResult < 0);
    }

    private void assertGreater(int comparisonResult) {
        assertTrue(comparisonResult > 0);
    }
}