package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        String name = "TestEnum";
        int value = 10;
        ValuedEnum enumObject = new ValuedEnum(name, value) {
        };
        assertEquals(name, enumObject.getName());
        assertEquals(value, enumObject.getValue());
    }

    @Test
    public void testGetEnum() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        Method getEnumMethod = enumClass.getMethod("getEnum", String.class);
        String javaVersion = "JAVA1_0";
        ValuedEnum expectedEnum = new ValuedEnum("JavaVersionEnum", 100) {
        };
        ValuedEnum actualEnum = (ValuedEnum) getEnumMethod.invoke(null, javaVersion);
        assertEquals(expectedEnum, actualEnum);
    }

    @Test
    public void testGetEnumWithInvalidValue() throws NoSuchMethodException, InvocationTargetException {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        Method getEnumMethod = enumClass.getMethod("getEnum", int.class);
        int javaVersion = 200;
        ValuedEnum actualEnum = (ValuedEnum) getEnumMethod.invoke(null, javaVersion);
        assertNull(actualEnum);
    }

    @Test
    public void testGetEnumWithInvalidClass() throws NoSuchMethodException, InvocationTargetException {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        Method getEnumMethod = enumClass.getMethod("getEnum", Object.class);
        Object invalidObject = new Object();
        ValuedEnum actualEnum = (ValuedEnum) getEnumMethod.invoke(null, invalidObject);
        assertNull(actualEnum);
    }

    @Test
    public void testGetEnumMap() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        Method getEnumMapMethod = enumClass.getMethod("getEnumMap");
        List enumList = (List) getEnumMapMethod.invoke(null);
        assertEquals(4, enumList.size()); // Assuming there are 4 subclasses
    }

    @Test
    public void testGetEnumList() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        Method getEnumListMethod = enumClass.getMethod("getEnumList");
        List enumList = (List) getEnumListMethod.invoke(null);
        assertEquals(4, enumList.size()); // Assuming there are 4 subclasses
    }

    @Test
    public void testIterator() throws NoSuchMethodException, InvocationTargetException {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        Method iteratorMethod = enumClass.getMethod("iterator");
        Iterator iterator = (Iterator) iteratorMethod.invoke(null);
        int count = 0;
        while (iterator.hasNext()) {
            ValuedEnum next = (ValuedEnum) iterator.next();
            count++;
        }
        assertEquals(4, count); // Assuming there are 4 subclasses
    }
}