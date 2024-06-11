package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enumClass = ValuedEnumTestEnum.class;
        assertEquals(ValuedEnumTestEnum.A, ValuedEnum.getEnum(enumClass, "A.NAME"));
        assertEquals(ValuedEnumTestEnum.B, ValuedEnum.getEnum(enumClass, 1));
        assertNull(ValuedEnum.getEnum(enumClass, "C.NAME"));
        assertNull(ValuedEnum.getEnum(enumClass, 3));
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum enumClass = ValuedEnumTestEnum.class;
        List<ValuedEnum> list = ValuedEnum.getEnumList(enumClass);
        assertEquals(3, list.size());
        assertSame(ValuedEnumTestEnum.A, list.get(0));
        assertSame(ValuedEnumTestEnum.B, list.get(1));
        assertSame(ValuedEnumTestEnum.C, list.get(2));
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum enumClass = ValuedEnumTestEnum.class;
        Map<String, ValuedEnum> map = ValuedEnum.getEnumMap(enumClass);
        assertEquals(3, map.size());
        assertEquals(ValuedEnumTestEnum.A, map.get("A.NAME"));
        assertEquals(ValuedEnumTestEnum.B, map.get(Integer.toString(1)));
        assertEquals(ValuedEnumTestEnum.C, map.get("C.NAME"));
    }

    @Test
    public void testCompareTo() {
        ValuedEnum a = ValuedEnumTestEnum.A;
        ValuedEnum b = ValuedEnumTestEnum.B;
        ValuedEnum c = ValuedEnumTestEnum.C;
        assertEquals(-1, a.compareTo(b));
        assertEquals(1, b.compareTo(a));
        assertEquals(0, a.compareTo(a));
        assertEquals(0, b.compareTo(b));
        assertEquals(2, c.compareTo(a));
        assertEquals(-2, a.compareTo(c));
    }

    @Test
    public void testToString() {
        ValuedEnum a = ValuedEnumTestEnum.A;
        assertEquals("ValuedEnumTestEnum[A.NAME=0]", a.toString());
    }

    @Test
    public void testValueOf() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Method valueOf = ValuedEnum.class.getDeclaredMethod("valueOf", String.class);
        valueOf.setAccessible(true);
        assertEquals(ValuedEnumTestEnum.A, valueOf.invoke(null, "A.NAME"));
        assertEquals(ValuedEnumTestEnum.B, valueOf.invoke(null, Integer.toString(1)));
        assertNull(valueOf.invoke(null, "D.NAME"));
        assertNull(valueOf.invoke(null, "10"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNull() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Method valueOf = ValuedEnum.class.getDeclaredMethod("valueOf", String.class);
        valueOf.setAccessible(true);
        valueOf.invoke(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfWrongClass() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Method valueOf = ValuedEnumTestEnum.class.getDeclaredMethod("valueOf", String.class);
        valueOf.setAccessible(true);
        valueOf.invoke(null, "A.NAME");
    }

    private static class ValuedEnumTestEnum extends ValuedEnum {
        private static final long serialVersionUID = 1L;

        public ValuedEnumTestEnum(String name, int value) {
            super(name, value);
        }

        public static final ValuedEnumTestEnum A = new ValuedEnumTestEnum("A", 0);
        public static final ValuedEnumTestEnum B = new ValuedEnumTestEnum("B", 1);
        public static final ValuedEnumTestEnum C = new ValuedEnumTestEnum("C", 2);
    }
}