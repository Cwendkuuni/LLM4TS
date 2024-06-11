package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.EnumUtils;
import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = new TestEnum1(0, "One");
        ValuedEnum enum2 = new TestEnum1(1, "Two");

        assertEquals(enum1, TestEnum1.getEnum(TestEnum1.class, 0));
        assertEquals(enum2, TestEnum1.getEnum(TestEnum1.class, 1));

        assertNull(TestEnum1.getEnum(TestEnum1.class, -1));
        assertNull(TestEnum1.getEnum(TestEnum1.class, 2));
    }

    @Test
    public void testGetEnumList() {
        TestEnum1 enum1 = new TestEnum1(0, "One");
        TestEnum1 enum2 = new TestEnum1(1, "Two");

        List<ValuedEnum> enumList = TestEnum1.getEnumList(TestEnum1.class);
        assertEquals(2, enumList.size());
        assertTrue(enumList.contains(enum1));
        assertTrue(enumList.contains(enum2));
    }

    @Test
    public void testGetEnumMap() {
        TestEnum1 enum1 = new TestEnum1(0, "One");
        TestEnum1 enum2 = new TestEnum1(1, "Two");

        Map<Integer, ValuedEnum> enumMap = TestEnum1.getEnumMap(TestEnum1.class);
        assertEquals(2, enumMap.size());
        assertEquals(enum1, enumMap.get(0));
        assertEquals(enum2, enumMap.get(1));
    }

    @Test
    public void testIterator() {
        TestEnum1 enum1 = new TestEnum1(0, "One");
        TestEnum1 enum2 = new TestEnum1(1, "Two");

        Iterator<ValuedEnum> iterator = TestEnum1.iterator(TestEnum1.class);
        assertEquals(2, iterator.nextCount());

        assertSame(enum1, iterator.next());
        assertSame(enum2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCompareTo() {
        TestEnum1 enum1 = new TestEnum1(0, "One");
        TestEnum1 enum2 = new TestEnum1(1, "Two");

        assertEquals(0, enum1.compareTo(enum1));
        assertEquals(-1, enum1.compareTo(enum2));
        assertEquals(1, enum2.compareTo(enum1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        TestEnum1.getEnum(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNegativeValue() {
        TestEnum1.getEnum(TestEnum1.class, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumWithNullValue() {
        TestEnum1.getEnum(TestEnum1.class, null);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithOtherClass() {
        TestEnum1 enum1 = new TestEnum1(0, "One");
        TestEnum2 enum2 = new TestEnum2(2, "Two");

        enum1.compareTo(enum2);
    }

    @Test
    public void testToString() {
        TestEnum1 enum1 = new TestEnum1(0, "One");

        assertEquals("TestEnum1[One=0]", enum1.toString());
    }

    private static class TestEnum1 extends ValuedEnum {
        private static final long serialVersionUID = 1L;

        TestEnum1(int value, String name) {
            super(name, value);
        }

        public static ValuedEnum getEnum(Class<? extends ValuedEnum> enumClass, int value) {
            return (ValuedEnum) super.getEnum(enumClass, value);
        }

        public static List<ValuedEnum> getEnumList(Class<? extends ValuedEnum> enumClass) {
            return EnumUtils.getEnumList(enumClass);
        }

        public static Map<Integer, ValuedEnum> getEnumMap(Class<? extends ValuedEnum> enumClass) {
            return EnumUtils.getEnumMap(enumClass);
        }

        public static Iterator<ValuedEnum> iterator(Class<? extends ValuedEnum> enumClass) {
            return EnumUtils.iterator(enumClass);
        }

        public static Class<TestEnum1> getEnumClass() {
            return TestEnum1.class;
        }
    }

    private static class TestEnum2 extends ValuedEnum {
        private static final long serialVersionUID = 1L;

        TestEnum2(int value, String name) {
            super(name, value);
        }
    }
}