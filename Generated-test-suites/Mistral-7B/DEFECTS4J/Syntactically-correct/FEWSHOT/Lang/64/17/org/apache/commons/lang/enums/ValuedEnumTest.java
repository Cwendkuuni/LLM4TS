package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enumClass = EnumTestClass.Enum1.class;
        ValuedEnum enumValue1 = ValuedEnum.getEnum(enumClass, 1);
        ValuedEnum enumValue2 = ValuedEnum.getEnum(enumClass, 2);

        assertEquals(EnumTestClass.Enum1_1.getName(), enumValue1.getName());
        assertEquals(EnumTestClass.Enum1_1.getValue(), enumValue1.getValue());

        assertEquals(EnumTestClass.Enum1_2.getName(), enumValue2.getName());
        assertEquals(EnumTestClass.Enum1_2.getValue(), enumValue2.getValue());
    }

    @Test
    public void testGetNullEnum() {
        ValuedEnum enumClass = EnumTestClass.Enum1.class;
        assertNull(ValuedEnum.getEnum(enumClass, 10));
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enumClass = EnumTestClass.Enum1.class;

        ValuedEnum enum1 = EnumTestClass.Enum1_1;
        ValuedEnum enum2 = EnumTestClass.Enum1_2;

        int comparisonResult = enum1.compareTo(enum2);
        assertEquals(-1, comparisonResult);

        comparisonResult = enum2.compareTo(enum1);
        assertEquals(1, comparisonResult);

        ValuedEnum enum3 = EnumTestClass.Enum1_1;
        int comparisonResult3 = enum1.compareTo(enum3);
        assertEquals(0, comparisonResult3);
    }

    @Test
    public void testToString() {
        ValuedEnum enumClass = EnumTestClass.Enum1.class;

        ValuedEnum enum1 = EnumTestClass.Enum1_1;
        String expectedToString1 = "EnumTestClass$Enum1[Enum1_1=1]";
        assertEquals(expectedToString1, enum1.toString());

        ValuedEnum enum2 = EnumTestClass.Enum1_2;
        String expectedToString2 = "EnumTestClass$Enum1[Enum1_2=2]";
        assertEquals(expectedToString2, enum2.toString());
    }

    @Test
    public void testValues() {
        ValuedEnum enumClass = EnumTestClass.Enum1.class;

        List<ValuedEnum> values = Arrays.asList(enumClass.values());
        List<ValuedEnum> expectedValues = Arrays.asList(EnumTestClass.Enum1_1, EnumTestClass.Enum1_2);

        assertEquals(expectedValues, values);
    }

    @Test
    public void testValuesOrdered() {
        ValuedEnum enumClass = EnumTestClass.Enum1.class;

        List<ValuedEnum> valuesOrdered = Collections.list(enumClass.values());
        Collections.sort(valuesOrdered);

        List<ValuedEnum> expectedValuesOrdered = Arrays.asList(EnumTestClass.Enum1_1, EnumTestClass.Enum1_2);

        assertEquals(expectedValuesOrdered, valuesOrdered);
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum enumClass = EnumTestClass.Enum1.class;

        Method getEnumMapMethod = ReflectionUtils.findMethod(enumClass, "getEnumMap", (Class[]) null);
        Map<Integer, ValuedEnum> enumMap = (Map<Integer, ValuedEnum>) ReflectionUtils.invokeMethod(getEnumMapMethod, null);

        Map<Integer, ValuedEnum> expectedEnumMap = new HashMap<>();
        expectedEnumMap.put(EnumTestClass.Enum1_1.getValue(), EnumTestClass.Enum1_1);
        expectedEnumMap.put(EnumTestClass.Enum1_2.getValue(), EnumTestClass.Enum1_2);

        assertEquals(expectedEnumMap, enumMap);
    }

    @Test
    public void testGetEnumMapWithNullEnumClass() {
        assertNull(ValuedEnum.getEnumMap(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumListWithNullEnumClass() {
        ValuedEnum.getEnumList(null);
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum enumClass = EnumTestClass.Enum1.class;

        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        List<ValuedEnum> expectedEnumList = Arrays.asList(EnumTestClass.Enum1_1, EnumTestClass.Enum1_2);

        assertEquals(expectedEnumList, enumList);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToWithNullObject() {
        ValuedEnum valuedEnum = EnumTestClass.Enum1_1;
        assertNull(valuedEnum.compareTo(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullValue() {
        ValuedEnum enumClass = EnumTestClass.Enum1.class;
        ValuedEnum.getEnum(enumClass, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNegativeValue() {
        ValuedEnum enumClass = EnumTestClass.Enum1.class;
        ValuedEnum.getEnum(enumClass, -1);
    }

    private static class EnumTestClass {
        public static class Enum1 extends ValuedEnum {
            public static final int Enum1_1_VALUE = 1;
            public static final int Enum1_2_VALUE = 2;

            public Enum1(String name, int value) {
                super(name, value);
            }

            public static Enum1 Enum1_1 = new Enum1("Enum1_1", Enum1_1_VALUE);
            public static Enum1 Enum1_2 = new Enum1("Enum1_2", Enum1_2_VALUE);
        }
    }

    private static class ReflectionUtils {
        private static Method findMethod(Class clazz, String methodName, Class[] parameterTypes) {
            try {
                return clazz.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        private static Object invokeMethod(Method method, Object target, Object... arguments) {
            try {
                return method.invoke(target, arguments);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}