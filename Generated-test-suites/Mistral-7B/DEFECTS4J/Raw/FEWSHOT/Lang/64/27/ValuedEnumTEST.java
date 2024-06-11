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
        ValuedEnum java1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum java1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };
        ValuedEnum java1_2 = new ValuedEnum("JavaVersion1_2", 120) {
        };

        ValuedEnum enumValue = ValuedEnum.getEnum(enumClass, 100);
        assertEquals(java1_0, enumValue);

        enumValue = ValuedEnum.getEnum(enumClass, 110);
        assertEquals(java1_1, enumValue);

        enumValue = ValuedEnum.getEnum(enumClass, 120);
        assertEquals(java1_2, enumValue);

        enumValue = ValuedEnum.getEnum(enumClass, 130);
        assertNull(enumValue);
    }

    @Test
    public void testGetEnum_NullClass() {
        assertNull(ValuedEnum.getEnum(null, 100));
    }

    @Test
    public void testGetEnum_NullValue() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        assertNull(ValuedEnum.getEnum(enumClass, null));
    }

    @Test
    public void testGetEnum_WrongClass() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        assertNull(ValuedEnum.getEnum(Object.class, 100));

        try {
            Method method = Object.class.getMethod("getValue");
            assertNull(ValuedEnum.getEnum(Object.class, 100, method));
        } catch (NoSuchMethodException e) {
            // expected
        }
    }

    @Test
    public void testGetEnum_WrongValue() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        assertNull(ValuedEnum.getEnum(enumClass, 200));
    }

    @Test
    public void testCompareTo() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum java1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum java1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };
        ValuedEnum java1_2 = new ValuedEnum("JavaVersion1_2", 120) {
        };

        int compareResult = java1_0.compareTo(java1_1);
        assertEquals(-1, compareResult);

        compareResult = java1_1.compareTo(java1_0);
        assertEquals(1, compareResult);

        compareResult = java1_1.compareTo(java1_1);
        assertEquals(0, compareResult);

        compareResult = java1_1.compareTo(java1_2);
        assertEquals(-1, compareResult);

        compareResult = java1_2.compareTo(java1_1);
        assertEquals(1, compareResult);

        compareResult = java1_2.compareTo(java1_2);
        assertEquals(0, compareResult);
    }

    @Test
    public void testCompareTo_SameClassLoader() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum java1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum java1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };
        ValuedEnum java1_2 = new ValuedEnum("JavaVersion1_2", 120) {
        };

        ValuedEnum otherJava1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };

        int compareResult = java1_0.compareTo(java1_1);
        assertEquals(-1, compareResult);

        compareResult = java1_1.compareTo(java1_0);
        assertEquals(1, compareResult);

        compareResult = java1_1.compareTo(otherJava1_1);
        assertEquals(0, compareResult);
    }

    @Test
    public void testCompareTo_DifferentClassLoader() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum java1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum java1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };

        Class<?> otherClass = ValuedEnumDifferentClassLoader.class;
        ValuedEnum otherJava1_1 = new ValuedEnumDifferentClassLoader("JavaVersion1_1", 110) {
        };

        int compareResult = java1_0.compareTo(java1_1);
        assertEquals(0);

        compareResult = java1_1.compareTo(java1_0);
        assertEquals(0);

        compareResult = java1_1.compareTo(otherJava1_1);
        assertEquals(Integer.compare(110, otherJava1_1.getValue()));
    }

    @Test
    public void testToString() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum java1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum java1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };

        assertEquals("ValuedEnum[JavaVersion1_0=100]", java1_0.toString());
        assertEquals("ValuedEnum[JavaVersion1_1=110]", java1_1.toString());
    }

    private static class ValuedEnumDifferentClassLoader extends ValuedEnum {
        public ValuedEnumDifferentClassLoader(String name, int value) {
            super(name, value);
        }
    }
}