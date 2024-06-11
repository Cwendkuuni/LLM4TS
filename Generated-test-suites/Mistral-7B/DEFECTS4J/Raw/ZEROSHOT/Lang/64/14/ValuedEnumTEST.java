package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = new TestValuedEnum1(1, "One");
        ValuedEnum enum2 = new TestValuedEnum1(2, "Two");
        ValuedEnum enum3 = new TestValuedEnum1(3, "Three");

        Class<TestValuedEnum1> enumClass = TestValuedEnum1.class;

        // Test with value
        ValuedEnum testEnum = ValuedEnum.getEnum(enumClass, 1);
        assertEquals(enum1, testEnum);

        // Test with null value
        testEnum = ValuedEnum.getEnum(enumClass, null);
        assertNull(testEnum);

        // Test with invalid value
        testEnum = ValuedEnum.getEnum(enumClass, 5);
        assertNull(testEnum);

        // Test with same class but different enum
        TestValuedEnum2 enum4 = new TestValuedEnum2(4, "Four");
        assertNull(ValuedEnum.getEnum(enumClass, 4));

        // Test with different class
        TestValuedEnum2 enum5 = new TestValuedEnum2(5, "Five");
        assertNull(ValuedEnum.getEnum(enumClass, 5));
        assertNull(ValuedEnum.getEnum(TestValuedEnum2.class, 5));
        assertSame(enum5, ValuedEnum.getEnum(TestValuedEnum2.class, 5));
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum enum1 = new TestValuedEnum1(1, "One");
        ValuedEnum enum2 = new TestValuedEnum1(2, "Two");
        ValuedEnum enum3 = new TestValuedEnum1(3, "Three");

        Class<TestValuedEnum1> enumClass = TestValuedEnum1.class;

        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);

        assertEquals(3, enumList.size());
        assertEquals(enum1, enumList.get(0));
        assertEquals(enum2, enumList.get(1));
        assertEquals(enum3, enumList.get(2));
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum enum1 = new TestValuedEnum1(1, "One");
        ValuedEnum enum2 = new TestValuedEnum1(2, "Two");
        ValuedEnum enum3 = new TestValuedEnum1(3, "Three");

        Class<TestValuedEnum1> enumClass = TestValuedEnum1.class;

        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);

        Map<Integer, ValuedEnum> enumMap = ValuedEnum.getEnumMap(enumClass);

        assertEquals(3, enumMap.size());
        assertEquals(enum1, enumMap.get(1));
        assertEquals(enum2, enumMap.get(2));
        assertEquals(enum3, enumMap.get(3));

        assertEquals(enum1, enumList.get(0));
        assertEquals(enum2, enumList.get(1));
        assertEquals(enum3, enumList.get(2));
    }

    @Test
    public void testToString() {
        ValuedEnum enum1 = new TestValuedEnum1(1, "One");
        assertEquals("TestValuedEnum1[One=1]", enum1.toString());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new TestValuedEnum1(1, "One");
        ValuedEnum enum2 = new TestValuedEnum1(2, "Two");
        ValuedEnum enum3 = new TestValuedEnum1(1, "One");

        assertEquals(0, enum1.compareTo(enum3));
        assertEquals(-1, enum3.compareTo(enum1));
        assertEquals(-1, enum1.compareTo(enum2));
        assertEquals(1, enum2.compareTo(enum1));
    }

    static class TestValuedEnum1 extends ValuedEnum {

        TestValuedEnum1(int value, String name) {
            super(name, value);
        }

    }

    static class TestValuedEnum2 extends ValuedEnum {

        TestValuedEnum2(int value, String name) {
            super(name, value);
        }

    }

}