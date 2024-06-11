package org.apache.commons.lang.enums;

import org.apache.commons.lang.ClassUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Test class for ValuedEnum.
 */
public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;

        // Test getting enum by name
        JavaVersionEnum javaVersionEnum1 = JavaVersionEnum.getEnum("Java 1.0");
        Assert.assertNotNull(javaVersionEnum1);
        Assert.assertEquals("Java 1.0", javaVersionEnum1.getName());
        Assert.assertEquals(100, javaVersionEnum1.getValue());

        // Test getting enum by value
        JavaVersionEnum javaVersionEnum2 = JavaVersionEnum.getEnum(100);
        Assert.assertNotNull(javaVersionEnum2);
        Assert.assertEquals("Java 1.0", javaVersionEnum2.getName());
        Assert.assertEquals(100, javaVersionEnum2.getValue());

        // Test getting null when enum is not found
        JavaVersionEnum javaVersionEnum3 = JavaVersionEnum.getEnum("Unknown");
        Assert.assertNull(javaVersionEnum3);
    }

    @Test
    public void testGetEnumMap() {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;

        List<JavaVersionEnum> javaVersionEnumList = JavaVersionEnum.getEnumList(javaVersionEnumClass);
        Map<Integer, JavaVersionEnum> javaVersionEnumMap = JavaVersionEnum.getEnumMap(javaVersionEnumClass);

        // Test getting enum from map
        JavaVersionEnum javaVersionEnum = javaVersionEnumMap.get(100);
        Assert.assertNotNull(javaVersionEnum);
        Assert.assertEquals("Java 1.0", javaVersionEnum.getName());
        Assert.assertEquals(100, javaVersionEnum.getValue());

        // Test getting null when enum is not found in map
        JavaVersionEnum javaVersionEnumNull = javaVersionEnumMap.get(200);
        Assert.assertNull(javaVersionEnumNull);

        // Test that map contains all enums
        for (JavaVersionEnum javaVersionEnum : javaVersionEnumList) {
            Assert.assertTrue(javaVersionEnumMap.containsKey(javaVersionEnum.getValue()));
            Assert.assertEquals(javaVersionEnum, javaVersionEnumMap.get(javaVersionEnum.getValue()));
        }
    }

    @Test
    public void testGetEnumList() {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;

        List<JavaVersionEnum> javaVersionEnumList = JavaVersionEnum.getEnumList(javaVersionEnumClass);

        // Test size
        Assert.assertEquals(4, javaVersionEnumList.size());

        // Test getting enum by index
        JavaVersionEnum javaVersionEnum1 = javaVersionEnumList.get(0);
        Assert.assertEquals("Java 1.0", javaVersionEnum1.getName());
        Assert.assertEquals(100, javaVersionEnum1.getValue());

        JavaVersionEnum javaVersionEnum2 = javaVersionEnumList.get(1);
        Assert.assertEquals("Java 1.1", javaVersionEnum2.getName());
        Assert.assertEquals(110, javaVersionEnum2.getValue());

        // Test getting null when index is out of bounds
        JavaVersionEnum javaVersionEnum3 = javaVersionEnumList.get(4);
        Assert.assertNull(javaVersionEnum3);
    }

    @Test
    public void testCompareTo() {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;

        List<JavaVersionEnum> javaVersionEnumList = JavaVersionEnum.getEnumList(javaVersionEnumClass);

        JavaVersionEnum javaVersionEnum1 = javaVersionEnumList.get(0);
        JavaVersionEnum javaVersionEnum2 = javaVersionEnumList.get(1);
        JavaVersionEnum javaVersionEnum3 = javaVersionEnumList.get(2);
        JavaVersionEnum javaVersionEnum4 = javaVersionEnumList.get(3);

        int comparisonResult1 = javaVersionEnum1.compareTo(javaVersionEnum2);
        Assert.assertLessThan(0, comparisonResult1);

        int comparisonResult2 = javaVersionEnum2.compareTo(javaVersionEnum1);
        Assert.assertGreaterThan(0, comparisonResult2);

        int comparisonResult3 = javaVersionEnum1.compareTo(javaVersionEnum1);
        Assert.assertEquals(0, comparisonResult3);

        int comparisonResult4 = javaVersionEnum3.compareTo(javaVersionEnum4);
        Assert.assertLessThan(0, comparisonResult4);

        int comparisonResult5 = javaVersionEnum4.compareTo(javaVersionEnum3);
        Assert.assertGreaterThan(0, comparisonResult5);

        JavaVersionEnum javaVersionEnum5 = new JavaVersionEnum("Java 5.0", 105);
        int comparisonResult6 = javaVersionEnum1.compareTo(javaVersionEnum5);
        Assert.assertLessThan(0, comparisonResult6);
    }

    @Test
    public void testToString() {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;

        List<JavaVersionEnum> javaVersionEnumList = JavaVersionEnum.getEnumList(javaVersionEnumClass);

        for (JavaVersionEnum javaVersionEnum : javaVersionVersionList) {
            String toString = javaVersionEnum.toString();
            String expectedString = javaVersionEnum.getClass().getSimpleName() + "[name=" + javaVersionEnum.getName() + ", value=" + javaVersionEnum.getValue() + "]";
            Assert.assertEquals(expectedString, toString);
        }
    }

    @Test
    public void testGetValue() {
        Class<JavaVersionEnum> javaVersionEnumClass = JavaVersionEnum.class;

        List<JavaVersionEnum> javaVersionEnumList = JavaVersionEnum.getEnumList(javaVersionEnumClass);

        for (JavaVersionEnum javaVersionEnum : javaVersionEnumList) {
            int value = javaVersionEnum.getValue();
            Assert.assertTrue(javaVersionVersionList.contains(javaVersionEnum));
            Assert.assertEquals(javaVersionEnum.getName(), JavaVersionEnum.getName(value));
        }
    }

    private static class JavaVersionEnum extends ValuedEnum {
        private JavaVersionEnum(String name, int value) {
            super(name, value);
        }

        public static String getName(int value) {
            for (ValuedEnum javaVersionEnum : ValuedEnumTest.javaVersionEnumList) {
                if (javaVersionEnum.getValue() == value) {
                    return javaVersionEnum.getName();
                }
            }
            throw new IllegalArgumentException("Value not found");
        }
    }

    private static List<JavaVersionEnum> javaVersionEnumList;

    static {
        javaVersionEnumList = List.of(
                new JavaVersionEnum("Java 1.0", 100),
                new JavaVersionEnum("Java 1.1", 110),
                new JavaVersionEnum("Java 1.2", 120),
                new JavaVersionEnum("Java 1.3", 130),
                new JavaVersionEnum("Java 1.4", 140),
                new JavaVersionEnum("Java 1.5", 150),
                new JavaVersionEnum("Java 1.6", 160),
                new JavaVersionEnum("Java 1.7", 170),
                new JavaVersionEnum("Java 1.8", 180)
        );
    }
}