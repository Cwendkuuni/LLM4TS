package org.apache.commons.lang.enums;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum(ValuedEnum.class, "JavaVersionEnum", 1);
        assertNotNull(javaVersionEnum);
        assertEquals("JavaVersionEnum", javaVersionEnum.getName());
        assertEquals(1, javaVersionVersion.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullName() {
        Class<ValuedEnum> valuedEnumClass = ValuedEnum.class;
        ValuedEnum.getEnum(valuedEnumClass, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNegativeValue() {
        Class<ValuedEnum> valuedEnumClass = ValuedEnum.class;
        ValuedEnum.getEnum(valuedEnumClass, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumWithNullValue() {
        Class<ValuedEnum> valuedEnumClass = ValuedEnum.class;
        ValuedEnum.getEnum(valuedEnumClass, null);
    }

    @Test
    public void testGetEnumMap() {
        List<ValuedEnum> javaVersionEnums = Enum.getEnumList(ValuedEnum.class);
        Map<Integer, ValuedEnum> enumMap = ValuedEnum.getEnumMap(ValuedEnum.class);
        assertEquals(javaVersionEnums.size(), enumMap.size());
        for (ValuedEnum javaVersionEnum : javaVersionEnums) {
            ValuedEnum value = enumMap.get(javaVersionEnum.getValue());
            assertEquals(javaVersionEnum, value);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumMapWithNullClass() {
        ValuedEnum.getEnumMap(null);
    }

    @Test
    public void testGetEnumList() {
        List<ValuedEnum> javaVersionEnums = ValuedEnum.getEnumList(ValuedEnum.class);
        assertEquals(javaVersionEnums.size(), Enum.getValueNames(ValuedEnum.class).length);
        for (ValuedEnum javaVersionEnum : javaVersionEnums) {
            assertEquals(javaVersionEnum.getName(), Enum.getValueNames(ValuedEnum.class)[javaVersionEnum.ordinal()]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumListWithNullClass() {
        ValuedEnum.getEnumList(null);
    }

    @Test
    public void testIterator() {
        List<ValuedEnum> javaVersionEnums = Enum.getEnumList(ValuedEnum.class);
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(ValuedEnum.class);
        assertEquals(javaVersionEnums.size(), 0);
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), javaVersionEnums.remove(0));
            assertEquals(javaVersionEnums.size(), iterator.size() - 1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullClass() {
        ValuedEnum.iterator(null);
    }
}