package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testValuedEnumAbstractClass() {
        assertNotNull(ValuedEnum.class);
        assertEquals(-7129650521543789085L, ValuedEnum.serialVersionUID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullEnumClass() {
        ValuedEnum.getEnum(null, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumNullValue() {
        ValuedEnum[] values = new ValuedEnum[0].getClass().getEnumConstants();
        ValuedEnum enum1 = values[0];
        ValuedEnum enum2 = null;
        ValuedEnum enum3 = ValuedEnum.getEnum(enum1.getClass(), enum2.getValue());
        assertNull(enum3);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumNullValueClass() {
        ValuedEnum enum1 = ValuedEnum.JAVA_1_0;
        ValuedEnum enum2 = null;
        ValuedEnum enum3 = ValuedEnum.getEnum(null, enum1.getValue());
        assertNull(enum3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumInvalidValue() {
        ValuedEnum[] values = new ValuedEnum[0].getClass().getEnumConstants();
        ValuedEnum enum1 = values[0];
        int invalidValue = 123;
        ValuedEnum enum2 = ValuedEnum.getEnum(enum1.getClass(), invalidValue);
        assertNull(enum2);
    }

    @Test
    public void testGetEnumValidValue() {
        ValuedEnum[] values = new ValuedEnum[0].getClass().getEnumConstants();
        ValuedEnum enum1 = values[0];
        ValuedEnum enum2 = ValuedEnum.JAVA_1_1;
        ValuedEnum enum3 = ValuedEnum.getEnum(enum1.getClass(), enum2.getValue());
        assertSame(enum2, enum3);
    }

    @Test
    public void testGetEnumValidValueDifferentClassLoader() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ValuedEnum[] values = new ValuedEnum[0].getClass().getEnumConstants();
        ValuedEnum enum1 = values[0];
        Class<ValuedEnum> enumClass = (Class<ValuedEnum>) Class.forName("org.apache.commons.lang.enums.JavaVersionEnum");
        Object enum2 = enumClass.getEnumConstants()[0];
        Method method = ValuedEnum.class.getDeclaredMethod("getValue");
        method.setAccessible(true);
        int value = (int) method.invoke(enum2);
        ValuedEnum enum3 = ValuedEnum.getEnum(enum1.getClass(), value);
        assertSame(enum2, enum3);
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum[] values = new ValuedEnum[0].getClass().getEnumConstants();
        List<Map.Entry<Enum, Integer>> map = Collections.list(ValuedEnum.getEnumMap(values[0].getClass()));
        for (Map.Entry<Enum, Integer> entry : map) {
            assertEquals(entry.getKey().getClass(), ValuedEnum.class);
            assertEquals(entry.getValue(), entry.getKey().ordinal() * 10);
        }
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum[] values = new ValuedEnum[0].getClass().getEnumConstants();
        List<ValuedEnum> list = Collections.list(ValuedEnum.getEnumList(values[0].getClass()));
        assertEquals(values.length, list.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], list.get(i));
        }
    }

    @Test
    public void testIterator() {
        ValuedEnum[] values = new ValuedEnum[0].getClass().getEnumConstants();
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(values[0].getClass());
        for (int i = 0; i < values.length; i++) {
            assertSame(values[i], iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCompareToSameEnum() {
        ValuedEnum enum1 = ValuedEnum.JAVA_1_0;
        ValuedEnum enum2 = ValuedEnum.JAVA_1_0;
        assertEquals(0, enum1.compareTo(enum2));
    }

    @Test
    public void testCompareToDifferentEnumValues() {
        ValuedEnum enum1 = ValuedEnum.JAVA_1_0;
        ValuedEnum enum2 = ValuedEnum.JAVA_1_1;
        int expected = enum1.getValue() - enum2.getValue();
        int result = enum1.compareTo(enum2);
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToDifferentEnumClasses() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ValuedEnum enum1 = ValuedEnum.JAVA_1_0;
        Class<ValuedEnum> enum1Class = enum1.getClass();
        Class<ValuedEnum> enum2Class = (Class<ValuedEnum>) Class.forName("org.apache.commons.lang.enums.DifferentEnumClass");
        Object enum2 = enum2Class.getEnumConstants()[0];
        int expected = enum1.getValue() - ((ValuedEnum) enum2).getValue();
        int result = enum1.compareTo(enum2);
        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        ValuedEnum enum1 = ValuedEnum.JAVA_1_0;
        String toString = enum1.toString();
        String shortName = ClassUtils.getShortClassName(enum1.getClass());
        String expected = shortName + "[" + enum1.getName() + "=" + enum1.getValue() + "]";
        assertEquals(expected, toString);
    }
}