package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enumClass = ValuedEnumTestEnum.class;
        assertEquals(ValuedEnumTestEnum.ONE, ValuedEnum.getEnum(enumClass, 1));
        assertEquals(ValuedEnumTestEnum.TWO, ValuedEnum.getEnum(enumClass, 2));
        assertNull(ValuedEnum.getEnum(enumClass, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test
    public void testCompareTo() {
        ValuedEnumTestEnum one = ValuedEnumTestEnum.ONE;
        ValuedEnumTestEnum two = ValuedEnumTestEnum.TWO;
        ValuedEnumTestEnum three = new ValuedEnumTestEnum("THREE", 3);

        assertEquals(-1, one.compareTo(two));
        assertEquals(1, two.compareTo(one));
        assertEquals(0, one.compareTo(one));
        assertEquals(2, three.compareTo(one));
    }

    @Test
    public void testToString() {
        ValuedEnumTestEnum one = ValuedEnumTestEnum.ONE;
        assertEquals("ValuedEnumTestEnum[ONE=1]", one.toString());

        ValuedEnumTestEnum custom = new ValuedEnumTestEnum("CUSTOM", 42) {
            @Override
            public String toString() {
                return "CustomEnum[" + getName() + "=" + getValue() + "]";
            }
        };
        assertEquals("CustomEnum[CUSTOM=42]", custom.toString());
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnumTestEnum[] values = ValuedEnumTestEnum.values();
        assertEquals(5, values.length);

        List<Map.Entry<ValuedEnumTestEnum, Integer>> map = ValuedEnum.getEnumMap(ValuedEnumTestEnum.class);
        assertEquals(5, map.size());

        for (Map.Entry<ValuedEnumTestEnum, Integer> entry : map) {
            assertEquals(entry.getKey(), entry.getValue());
        }
    }

    @Test
    public void testGetEnumList() {
        ValuedEnumTestEnum[] values = ValuedEnumTestEnum.values();
        assertEquals(5, values.length);

        List<ValuedEnumTestEnum> list = ValuedEnum.getEnumList(ValuedEnumTestEnum.class);
        assertEquals(5, list.size());

        for (ValuedEnumTestEnum value : list) {
            assertEquals(value, values[List.indexOf(values, value)]);
        }
    }

    @Test
    public void testIterator() {
        ValuedEnumTestEnum[] values = ValuedEnumTestEnum.values();
        List<ValuedEnumTestEnum> list = ValuedEnum.getEnumList(ValuedEnumTestEnum.class);

        Iterator<ValuedEnumTestEnum> iterator = ValuedEnum.iterator(ValuedEnumTestEnum.class);

        assertEquals(5, list.size());
        int index = 0;
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), values[index++]);
        }
    }

    private static class ValuedEnumTestEnum extends ValuedEnum {
        private static final long serialVersionUID = 1L;

        public static final ValuedEnumTestEnum ONE = new ValuedEnumTestEnum("ONE", 1);
        public static final ValuedEnumTestEnum TWO = new ValuedEnumTestEnum("TWO", 2);
        public static final ValuedEnumTestEnum THREE = new ValuedEnumTestEnum("THREE", 3);

        public ValuedEnumTestEnum(String name, int value) {
            super(name, value);
        }

        private String iToString;

        @Override
        public int compareTo(Object other) {
            try {
                Method method = getClass().getMethod("compareToCustom", Object.class);
                return ((Integer) method.invoke(this, other)).intValue();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return super.compareTo(other);
            }
        }

        // Custom compareTo method for testing
        @SuppressWarnings("unused")
        private int compareToCustom(Object other) {
            ValuedEnumTestEnum otherValuedEnum = (ValuedEnumTestEnum) other;
            return getName().compareToIgnoreCase(otherValuedEnum.getName());
        }
    }
}