package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;

public class ValuedEnumTest {

    private static final Class<ValuedEnum> VALUED_ENUM_CLASS = ValuedEnum.class;

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.0", 100);
        ValuedEnum java1_1 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.1", 110);
        ValuedEnum java1_2 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.2", 120);

        assertEquals(java1_0, ValuedEnum.getEnum("JavaVersion", java1_0.getValue()));
        assertEquals(java1_1, ValuedEnum.getEnum("JavaVersion", java1_1.getValue()));
        assertEquals(java1_2, ValuedEnum.getEnum("JavaVersion", java1_2.getValue()));

        assertNull(ValuedEnum.getEnum("JavaVersion", 121)); // invalid value
        assertNull(ValuedEnum.getEnum("JavaVersion", null)); // invalid enum
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum java1_0 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.0", 100);
        ValuedEnum java1_1 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.1", 110);
        ValuedEnum java1_2 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.2", 120);

        List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);

        assertEquals(3, enumList.size());
        assertEquals(java1_0, enumList.get(0));
        assertEquals(java1_1, enumList.get(1));
        assertEquals(java1_2, enumList.get(2));
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum java1_0 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.0", 100);
        ValuedEnum java1_1 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.1", 110);
        ValuedEnum java1_2 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.2", 120);

        List<ValuedEnum> enumList = Arrays.asList(java1_0, java1_1, java1_2);
        Map<Integer, ValuedEnum> enumMap = ValuedEnum.getEnumMap(VALUED_ENUM_CLASS, enumList.toArray(new ValuedEnum[0]));

        assertEquals(java1_0, enumMap.get(Integer.valueOf(100)));
        assertEquals(java1_1, enumMap.get(Integer.valueOf(110)));
        assertEquals(java1_2, enumMap.get(Integer.valueOf(120)));
        assertNull(enumMap.get(Integer.valueOf(121))); // invalid value
    }

    @Test
    public void testCompareTo() {
        ValuedEnum java1_0 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.0", 100);
        ValuedEnum java1_1 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.1", 110);
        ValuedEnum java1_2 = ValuedEnumTest.createEnum("JavaVersion", "Java 1.2", 120);

        assertEquals(-1, java1_0.compareTo(java1_1));
        assertEquals(0, java1_0.compareTo(java1_0));
        assertEquals(1, java1_1.compareTo(java1_0));
        assertEquals(1, java1_1.compareTo(java1_1)); // same instance
        assertEquals(2, java1_2.compareTo(java1_1));
    }

    private static ValuedEnum createEnum(String enumType, String name, int value) {
        try {
            Method valueMethod = VALUED_ENUM_CLASS.getDeclaredMethod("values");
            valueMethod.setAccessible(true);
            Object[] enumConstants = (Object[]) valueMethod.invoke(null);
            Class enumClass = Class.forName(enumType);
            Method constructor = enumClass.getConstructor(String.class, int.class);
            constructor.setAccessible(true);
            return (ValuedEnum) constructor.newInstance(name, value);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            throw new RuntimeException("Failed to create test enum", e);
        }
    }
}