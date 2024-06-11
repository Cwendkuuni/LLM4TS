package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(enumClass, "JavaVersion[ Java 1.0 ]");
        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum(enumClass, "JavaVersion[ Java 1.1 ]");
        ValuedEnum javaVersion1_2 = ValuedEnum.getEnum(enumClass, "JavaVersion[ Java 1.2 ]");
        ValuedEnum javaVersion1_3 = ValuedEnum.getEnum(enumClass, "JavaVersion[ Java 1.3 ]");

        assertEquals(javaVersion1_0, ValuedEnum.JAVA1_0);
        assertEquals(javaVersion1_1, ValuedEnum.JAVA1_1);
        assertEquals(javaVersion1_2, ValuedEnum.JAVA1_2);
        assertEquals(javaVersion1_3, ValuedEnum.JAVA1_3);

        assertNull(ValuedEnum.getEnum(enumClass, "Unknown"));
    }

    @Test
    public void testGetEnumByValue() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;

        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(enumClass, 100);
        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum(enumClass, 110);
        ValuedEnum javaVersion1_2 = ValuedEnum.getEnum(enumClass, 120);
        ValuedEnum javaVersion1_3 = ValuedEnum.getEnum(enumClass, 130);

        assertEquals(javaVersion1_0, ValuedEnum.JAVA1_0);
        assertEquals(javaVersion1_1, ValuedEnum.JAVA1_1);
        assertEquals(javaVersion1_2, ValuedEnum.JAVA1_2);
        assertEquals(javaVersion1_3, ValuedEnum.JAVA1_3);

        assertNull(ValuedEnum.getEnum(enumClass, 99));
    }

    @Test
    public void testGetEnumMap() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;

        @SuppressWarnings("unchecked")
        Map<String, ValuedEnum> enumMap = (Map<String, ValuedEnum>) ValuedEnum.getEnumMap(enumClass);

        assertEquals(java.util.Collections.singletonMap("JavaVersion[ Java 1.0 ]", ValuedEnum.JAVA1_0), enumMap.get("JavaVersion[ Java 1.0 ]"));
        assertEquals(java.util.Collections.singletonMap("JavaVersion[ Java 1.1 ]", ValuedEnum.JAVA1_1), enumMap.get("JavaVersion[ Java 1.1 ]"));
        assertEquals(java.util.Collections.singletonMap("JavaVersion[ Java 1.2 ]", ValuedEnum.JAVA1_2), enumMap.get("JavaVersion[ Java 1.2 ]"));
        assertEquals(java.util.Collections.singletonMap("JavaVersion[ Java 1.3 ]", ValuedEnum.JAVA1_3), enumMap.get("JavaVersion[ Java 1.3 ]"));

        assertNull(enumMap.get("Unknown"));
    }

    @Test
    public void testGetEnumList() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;

        @SuppressWarnings("unchecked")
        List<ValuedEnum> enumList = (List<ValuedEnum>) ValuedEnum.getEnumList(enumClass);

        assertEquals(java.util.Arrays.asList(ValuedEnum.JAVA1_0, ValuedEnum.JAVA1_1, ValuedEnum.JAVA1_2, ValuedEnum.JAVA1_3), enumList);
    }

    @Test
    public void testCompareTo() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;

        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(enumClass, "JavaVersion[ Java 1.0 ]");
        ValuedEnum javaVersion1_1 = ValvedEnum.getEnum(enumClass, "JavaVersion[ Java 1.1 ]");
        ValuedEnum javaVersion1_2 = ValvedEnum.getEnum(enumClass, "JavaVersion[ Java 1.2 ]");
        ValvedEnum javaVersion1_3 = ValvedEnum.getEnum(enumClass, "JavaVersion[ Java 1.3 ]");

        assertEquals(-1, javaVersion1_0.compareTo(javaVersion1_1));
        assertEquals(0, javaVersion1_0.compareTo(javaVersion1_0));
        assertEquals(1, javaVersion1_0.compareTo(javaVersion1_2));

        assertEquals(-1, javaVersion1_1.compareTo(javaVersion1_0));
        assertEquals(0, javaVersion1_1.compareTo(javaVersion1_1));
        assertEquals(1, javaVersion1_1.compareTo(javaVersion1_2));

        assertEquals(-1, javaVersion1_2.compareTo(javaVersion1_0));
        assertEquals(0, javaVersion1_2.compareTo(javaVersion1_2));
        assertEquals(1, javaVersion1_2.compareTo(javaVersion1_3));

        assertEquals(-1, javaVersion1_3.compareTo(javaVersion1_0));
        assertEquals(0, javaVersion1_3.compareTo(javaVersion1_3));
        assertEquals(1, javaVersion1_3.compareTo(javaVersion1_2));

        // Test with different class loader
        Class<DifferentValuedEnum> differentEnumClass = DifferentValuedEnum.class;

        DifferentValuedEnum differentJavaVersion1_0 = DifferentValuedEnum.getEnum(differentEnumClass, "DifferentJavaVersion[ Java 1.0 ]");
        ValuedEnum javaVersion1_0_again = ValuedEnum.getEnum(enumClass, "JavaVersion[ Java 1.0 ]");

        assertEquals(javaVersion1_0_again.getValue() - differentJavaVersion1_0.getValue(), differentJavaVersion1_0.compareTo(javaVersion1_0_again));
    }
}

class DifferentValuedEnum extends ValuedEnum {
    public DifferentValuedEnum(String name, int value) {
        super(name, value);
    }
}