package org.apache.commons.lang.enums;

import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersionEnum = ValuedEnum.class.getEnumConstants()[0];
        ValuedEnum enumInstance = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0_VALUE");
        assertEquals(javaVersionEnum, enumInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullClass() {
        ValuedEnum.getEnum(null, "JAVA1_0_VALUE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullValue() {
        ValuedEnum.getEnum(ValuedEnum.class, null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumNullEnum() {
        ValuedEnum.getEnum(null, "JAVA1_0_VALUE");
    }

    @Test
    public void testGetEnumValue() {
        ValuedEnum javaVersionEnum = ValuedEnum.class.getEnumConstants()[0];
        int value = javaVersionEnum.getValue();
        assertEquals(100, value);
    }

    @Test
    public void testGetEnumMethod() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Method method = enumClass.getMethod("getEnum", String.class, int.class);
        ValuedEnum javaVersionEnum = (ValuedEnum) method.invoke(null, "JavaVersionEnum", 100);
        assertEquals(javaVersionEnum.getClass(), ValuedEnum.class);
        assertEquals(javaVersionEnum.getValue(), 100);
    }

    @Test
    public void testGetEnumMap() {
        List<ValuedEnum> list = ValuedEnum.getEnumList(ValuedEnum.class);
        Map<Integer, ValuedEnum> enumMap = ValuedEnum.getEnumMap();
        for (ValuedEnum enumInstance : list) {
            Integer key = enumInstance.getValue();
            ValuedEnum value = enumMap.get(key);
            assertEquals(enumInstance, value);
        }
    }

    @Test
    public void testGetEnumList() {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(ValuedEnum.class);
        assertEquals(enumList.size(), 4); // Assuming there are 4 enum values
        for (ValuedEnum enumInstance : enumList) {
            System.out.println(enumInstance);
        }
    }

    @Test
    public void testToString() {
        ValuedEnum javaVersionEnum = ValuedEnum.class.getEnumConstants()[0];
        String toString = javaVersionEnum.toString();
        assertTrue(toString.contains("JavaVersion"));
        assertTrue(toString.contains("Java 1.0"));
        assertTrue(toString.contains("100"));
    }

    @Test
    public void testCompareTo() {
        ValuedEnum javaVersionEnum1 = ValuedEnum.class.getEnumConstants()[0];
        ValuedEnum javaVersionEnum2 = ValuedEnum.class.getEnumConstants()[1];
        int result1 = javaVersionEnum1.compareTo(javaVersionEnum1);
        int result2 = javaVersionEnum1.compareTo(javaVersionEnum2);
        int result3 = javaVersionEnum2.compareTo(javaVersionEnum1);
        int result4 = javaVersionEnum2.compareTo(javaVersionEnum2);
        assertEquals(0, result1);
        assertEquals(-1, result2);
        assertEquals(1, result3);
        assertEquals(0, result4);
    }
}