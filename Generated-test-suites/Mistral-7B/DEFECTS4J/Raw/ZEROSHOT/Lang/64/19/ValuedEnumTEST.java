package org.apache.commons.lang.enums;

import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
        ValuedEnum javaVersion1_2 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2");

        assertNotNull(javaVersion1_0);
        assertNotNull(javaVersion1_1);
        assertNotNull(javaVersion1_2);

        assertEquals(javaVersion1_0.getValue(), 100);
        assertEquals(javaVersion1_1.getValue(), 110);
        assertEquals(javaVersion1_2.getValue(), 120);
    }

    @Test
    public void testGetEnumNull() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        assertNull(ValuedEnum.getEnum(enumClass, null));
        assertNull(ValuedEnum.getEnum(enumClass, ""));
    }

    @Test
    public void testGetEnumValueNotExist() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        assertNull(ValuedEnum.getEnum(enumClass, 105));
    }

    @Test
    public void testGetEnumValueFromAnotherClass() throws Exception {
        Class<ValuedEnum> valuedEnumClass = ValuedEnum.class;
        Class<JavaVersionEnum> javaVersionEnumClass = Class.forName("org.apache.commons.lang.enums.JavaVersionEnum");

        Method getEnumMethod = valuedEnumClass.getDeclaredMethod("getEnum", String.class);
        Method getEnumValueMethod = javaVersionEnumClass.getDeclaredMethod("getValue");

        Object javaVersion1_0 = getEnumMethod.invoke(null, "JavaVersionEnum JAVA1_0");
        Object value1_0 = getEnumValueMethod.invoke(javaVersion1_0);

        assertEquals(javaVersion1_0.getClass(), JavaVersionEnum.class);
        assertEquals(value1_0, 100);
    }

    @Test
    public void testGetEnumValueFromAnotherClassLoader() throws Exception {
        Class<?> thisClass = this.getClass();
        ClassLoader thisClassLoader = thisClass.getClassLoader();
        Class<ValuedEnum> valuedEnumClass = Class.forName("org.apache.commons.lang.enums.ValuedEnum", true, new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return super.findClass(name);
            }
        });

        Class<JavaVersionEnum> javaVersionEnumClass = Class.forName("org.apache.commons.lang.enums.JavaVersionEnum", true, thisClassLoader);

        Method getEnumMethod = valuedEnumClass.getDeclaredMethod("getEnum", String.class, Class.class);
        Method getEnumValueMethod = javaVersionEnumClass.getDeclaredMethod("getValue");

        Object javaVersion1_0 = getEnumMethod.invoke(null, "JavaVersionEnum", javaVersionEnumClass);
        Object value1_0 = getEnumValueMethod.invoke(javaVersion1_0);

        assertEquals(javaVersion1_0.getClass(), JavaVersionEnum.class);
        assertEquals(value1_0, 100);
    }

    @Test
    public void testGetEnumMap() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(enumClass);

        assertEquals(enumList.size(), enumMap.size());
        for (ValuedEnum enumValue : enumList) {
            assertEquals(enumValue, enumMap.get(enumValue.getName()));
        }
    }

    @Test
    public void testGetEnumList() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);

        assertNotNull(enumList);
        assertEquals(3, enumList.size());

        for (ValuedEnum enumValue : enumList) {
            assertNotNull(enumValue);
        }
    }

    @Test
    public void testToString() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(enumClass, "JAVA1_0");

        assertEquals(javaVersion1_0.toString(), "ValuedEnum[JavaVersion=Java 1.0=100]");
    }
}