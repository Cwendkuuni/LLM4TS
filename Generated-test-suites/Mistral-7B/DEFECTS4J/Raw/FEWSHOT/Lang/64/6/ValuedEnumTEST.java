package org.apache.commons.lang.enums;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(enumClass, "JavaVersion[Java 1.0]");
        assertEquals("JavaVersion[Java 1.0]", javaVersion1_0.toString());
        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum(enumClass, 110);
        assertEquals("JavaVersion[Java 1.1]", javaVersion1_1.toString());
        assertNull(ValuedEnum.getEnum(enumClass, null));
        assertNull(ValuedEnum.getEnum(enumClass, -1));
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        assertEquals(4, enumList.size());
        for (ValuedEnum valuedEnum : enumList) {
            assertNotNull(valuedEnum);
        }
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum enumClass = ValuedEnum.class;
        Map<ValuedEnum, Integer> enumMap = ValuedEnum.getEnumMap(enumClass);
        assertEquals(4, enumMap.size());
        for (Map.Entry<ValuedEnum, Integer> entry : enumMap.entrySet()) {
            assertEquals(entry.getKey().getValue(), entry.getValue().intValue());
        }
    }

    @Test
    public void testCompareTo() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(ValuedEnum.class, "JavaVersion[Java 1.0]");
        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum(ValuedEnum.class, "JavaVersion[Java 1.1]");
        ValuedEnum javaVersion1_2 = ValuedEnum.getEnum(ValuedEnum.class, "JavaVersion[Java 1.2]");

        Method compareToMethod = javaClassWithValue(ValuedEnum.class).getMethod("compareTo", Object.class);
        int comparisonJavaVersion1_0JavaVersion1_1 = (Integer) compareToMethod.invoke(javaVersion1_0, javaVersion1_1);
        int comparisonJavaVersion1_0JavaVersion1_2 = (Integer) compareToMethod.invoke(javaVersion1_0, javaVersion1_2);

        assertEquals(-1, comparisonJavaVersion1_0JavaVersion1_1);
        assertEquals(1, comparisonJavaVersion1_0JavaVersion1_2);
    }
}

class JavaClassWithValue<T> {
    private final Class<T> clazz;

    JavaClassWithValue(Class<T> clazz) {
        this.clazz = clazz;
    }

    private Class<?> javaClassWithValue() {
        try {
            return Class.forName("org.apache.commons.lang.enums.ValuedEnumWithValue" + getEnumClassNameSuffix(clazz));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String getEnumClassNameSuffix(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        return "_" + simpleName.replaceAll("(.)[A-Z]", "$1_") + "Value";
    }
}