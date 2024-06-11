package org.apache.commons.lang.enums;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersionEnum = ValuedEnum.class.cast(ValuedEnum.getEnum("Java1_0"));
        assertEquals("Java 1.0", javaVersionEnum.getName());
        assertEquals(100, javaVersionEnum.getValue());

        javaVersionEnum = ValuedEnum.getEnum(100);
        assertEquals("Java 1.0", javaVersionEnum.getName());
        assertEquals(100, javaVersionEnum.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumNullEnumClass() {
        ValuedEnum.getEnum(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumInvalidEnum() {
        ValuedEnum.getEnum("InvalidEnum");
    }

    @Test
    public void testGetEnumList() {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList();
        assertEquals(4, enumList.size());

        for (ValuedEnum e : enumList) {
            Assert.assertNotNull(e.getName());
            Assert.assertNotNull(e.getValue());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumListNullEnumClass() {
        ValuedEnum.getEnumList(null);
    }

    @Test
    public void testToString() {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("Java1_0");
        String toString = javaVersion1_0.toString();
        assertTrue(toString.contains("JavaVersion"));
        assertTrue(toString.contains("Java 1.0=" + 100));
    }

    @Test
    public void testCompareToSameEnum() {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("Java1_0");
        ValuedEnum javaVersion1_0Copy = javaVersion1_0;

        int compareToResult = javaVersion1_0.compareTo(javaVersion1_0Copy);
        assertEquals(0, compareToResult);
    }

    @Test
    public void testCompareToDifferentEnum() {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("Java1_0");
        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum("Java1_1");

        int compareToResult = javaVersion1_0.compareTo(javaVersion1_1);
        Assert.assertLessThan(0, compareToResult);

        compareToResult = javaVersion1_1.compareTo(javaVersion1_0);
        Assert.assertGreaterThan(0, compareToResult);
    }

    @Test
    public void testGetEnumMap() {
        Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap();
        assertEquals(4, enumMap.size());

        ValuedEnum javaVersion1_0 = enumMap.get("Java1_0");
        assertEquals("Java 1.0", javaVersion1_0.getName());
        assertEquals(100, javaVersion1_0.getValue());

        ValuedEnum javaVersion1_1 = enumMap.get("Java1_1");
        assertEquals("Java 1.1", javaVersion1_1.getName());
        assertEquals(110, javaVersion1_1.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumMapNullEnumClass() {
        ValuedEnum.getEnumMap(null);
    }
}