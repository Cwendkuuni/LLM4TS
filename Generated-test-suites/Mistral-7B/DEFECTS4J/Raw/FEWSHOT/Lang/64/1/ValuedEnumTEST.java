package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.EnumUtils;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enumClass = ValuedEnumTestEnum.class;
        assertEquals(ValuedEnumTestEnum.ONE, ValuedEnum.getEnum(enumClass, "ONE"));
        assertEquals(ValuedEnumTestEnum.TWO, ValuedEnum.getEnum(enumClass, "TWO"));
        assertEquals(ValuedEnumTestEnum.THREE, ValuedEnum.getEnum(enumClass, "THREE"));
        assertNull(ValuedEnum.getEnum(enumClass, "FOO"));
    }

    @Test
    public void testGetEnumValue() {
        ValuedEnum enumClass = ValuedEnumTestEnum.class;
        assertEquals(1, ValuedEnum.getEnumValue(enumClass, ValuedEnumTestEnum.ONE));
        assertEquals(2, ValuedEnum.getEnumValue(enumClass, ValuedEnumTestEnum.TWO));
        assertEquals(3, ValuedEnum.getEnumValue(enumClass, ValuedEnumTestEnum.THREE));
        assertNull(ValuedEnum.getEnumValue(enumClass, "FOO"));
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum enumClass = ValuedEnumTestEnum.class;
        List map = ValuedEnum.getEnumMap(enumClass);
        assertEquals(3, map.size());
        assertEquals(ValuedEnumTestEnum.ONE, map.get(1));
        assertEquals(ValuedEnumTestEnum.TWO, map.get(2));
        assertEquals(ValuedEnumTestEnum.THREE, map.get(3));
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum enumClass = ValuedEnumTestEnum.class;
        List list = ValuedEnum.getEnumList(enumClass);
        assertEquals(3, list.size());
        assertEquals(ValuedEnumTestEnum.ONE, list.get(0));
        assertEquals(ValuedEnumTestEnum.TWO, list.get(1));
        assertEquals(ValuedEnumTestEnum.THREE, list.get(2));
    }

    @Test
    public void testGetEnumIterator() {
        ValuedEnum enumClass = ValuedEnumTestEnum.class;
        Iterator iterator = ValuedEnum.iterator(enumClass);
        assertEquals(3, iterator.nextCount());
        assertEquals(ValuedEnumTestEnum.ONE, iterator.next());
        assertEquals(ValuedEnumTestEnum.TWO, iterator.next());
        assertEquals(ValuedEnumTestEnum.THREE, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCompareTo() {
        ValuedEnumTestEnum first = ValuedEnumTestEnum.ONE;
        ValuedEnumTestEnum second = ValuedEnumTestEnum.TWO;
        ValuedEnumTestEnum third = ValuedEnumTestEnum.THREE;

        assertEquals(-1, first.compareTo(second));
        assertEquals(1, second.compareTo(first));
        assertEquals(0, first.compareTo(first));
        assertEquals(0, second.compareTo(second));
        assertEquals(2, third.compareTo(first));
        assertEquals(-1, third.compareTo(second));
        assertEquals(1, third.compareTo(third));
    }

    @Test
    public void testToString() {
        ValuedEnumTestEnum one = ValuedEnumTestEnum.ONE;
        assertEquals("ValuedEnumTestEnum[ONE=1]", one.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullEnumClass() {
        ValuedEnum.getEnum(null, "ONE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumValueNullEnumClass() {
        ValuedEnum enumClass = null;
        ValuedEnumTestEnum value = ValuedEnumTestEnum.ONE;
        ValuedEnum.getEnumValue(enumClass, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumValueNullValue() {
        ValuedEnum enumClass = ValuedEnumTestEnum.class;
        String value = null;
        ValuedEnum.getEnumValue(enumClass, value);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumValueNullValue2() {
        ValuedEnum enumClass = null;
        ValuedEnumTestEnum value = null;
        ValuedEnum.getEnumValue(enumClass, value);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumMapNullEnumClass() {
        ValuedEnum enumClass = null;
        ValuedEnum.getEnumMap(enumClass);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumListNullEnumClass() {
        ValuedEnum enumClass = null;
        ValuedEnum.getEnumList(enumClass);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumIteratorNullEnumClass() {
        ValuedEnum enumClass = null;
        ValuedEnum.iterator(enumClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumValueDifferentClass() throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<ValuedEnumTestEnum> enumClass1 = ValuedEnumTestEnum.class;
        ValuedEnumTestEnum value = ValuedEnumTestEnum.ONE;
        Method getValue = null;
        try {
            getValue = ClassUtils.getPublicStaticMethod(enumClass1, "getValue", null);
        } catch (NoSuchMethodException e) {
            fail("Method getValue not found");
        }
        Class<ValuedEnumTestEnum2> enumClass2 = ValuedEnumTestEnum2.class;
        Object enumObject = enumClass2.newInstance();
        Object result = getValue.invoke(enumObject);
        assertEquals(0, result.hashCode());
    }

    @Test
    public void testToStringDifferentClass() {
        ValuedEnumTestEnum one = ValuedEnumTestEnum.ONE;
        assertEquals("ValuedEnumTestEnum[ONE=1]", one.toString());
        ValuedEnumTestEnum2 two = ValuedEnumTestEnum2.ONE;
        assertEquals("ValuedEnumTestEnum2[ONE=1]", two.toString());
    }

    private static class ValuedEnumTestEnum extends ValuedEnum {
        public static final int ONE = 1;
        public static final int TWO = 2;
        public static final int THREE = 3;

        public static final ValuedEnumTestEnum ONE = new ValuedEnumTestEnum("ONE", ONE);
        public static final ValuedEnumTestEnum TWO = new ValuedEnumTestEnum("TWO", TWO);
        public static final ValuedEnumTestEnum THREE = new ValuedEnumTestEnum("THREE", THREE);

        private String iToString;

        protected ValuedEnumTestEnum(String name, int value) {
            super(name, value);
        }
    }

    private static class ValuedEnumTestEnum2 extends ValuedEnum {
        public static final int ONE = 1;

        public static final ValuedEnumTestEnum2 ONE = new ValuedEnumTestEnum2("ONE", ONE);

        private String iToString;

        protected ValuedEnumTestEnum2(String name, int value) {
            super(name, value);
        }

        @Override
        public String toString() {
            if (iToString == null) {
                iToString = "ValuedEnumTestEnum2[ONE=1]";
            }
            return iToString;
        }
    }
}