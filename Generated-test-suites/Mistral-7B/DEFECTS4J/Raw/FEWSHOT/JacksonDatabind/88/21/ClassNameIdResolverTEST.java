package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ClassNameIdResolverTest {

    private static final TypeFactory typeFactory = new TypeFactoryBuilder().build();

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        Object[] values = new Object[]{"String", "HelloWorld", "com.example.MyClass"};
        String[] expectedIds = new String[]{"java.lang.String", "class com.example.MyClass", "com.example.MyClass"};

        for (int i = 0; i < values.length; i++) {
            assertEquals(expectedIds[i], resolver.idFromValue(values[i]));
        }

        // Test null value
        assertNull(resolver.idFromValue(null));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        Object value = new Object(){};
        Class<?> type = Integer.class;

        String expectedId = type.getName();

        assertEquals(expectedId, resolver.idFromValueAndType(value, type));

        // Test null value and type
        assertNull(resolver.idFromValueAndType(null, null));
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        String[] ids = new String[]{"java.lang.String", "com.example.MyClass", "java.util.List", "java.util.ArrayList", "java.util.EnumSet", "java.util.EnumMap"};
        JavaType[] expectedTypes = new JavaType[]{String.class, Class.forName("com.example.MyClass"), List.class, ArrayList.class, EnumSet.class, EnumMap.class};

        for (int i = 0; i < ids.length; i++) {
            JavaType type = resolver.typeFromId(null, ids[i]);
            assertEquals(expectedTypes[i].getRawClass().getName(), type.getRawClass().getName());
        }

        // Test unknown id
        assertNull(resolver.typeFromId(null, "UnknownType"));
    }

    @Test
    public void testIdFromValueWithEnum() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        Object value = Enum.values()[0];

        String expectedId = value.getClass().getName();

        assertEquals(expectedId, resolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueWithEnumSubclass() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        Object value = Enum.valueOf(MyEnum.class, "SUB_VALUE");

        String expectedId = value.getClass().getName();

        assertEquals(expectedId, resolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueWithCustomCollection() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        List<String> customList = new ArrayList<>();
        customList.add("Element1");

        String expectedId = CustomList.class.getName();

        assertEquals(expectedId, resolver.idFromValue(customList));
    }

    @Test
    public void testTypeFromIdWithCollection() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        String id = "java.util.List";
        JavaType expectedType = typeFactory.constructCollectionType(List.class, String.class);

        JavaType type = resolver.typeFromId(null, id);

        assertEquals(expectedType.getRawClass().getName(), type.getRawClass().getName());
    }

    @Test
    public void testTypeFromIdWithMap() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        String id = "java.util.EnumMap";
        JavaType expectedType = typeFactory.constructMapType(EnumMap.class, String.class, String.class);

        JavaType type = resolver.typeFromId(null, id);

        assertEquals(expectedType.getRawClass().getName(), type.getRawClass().getName());
    }

    @Test
    public void testTypeFromIdWithArray() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        String id = "java.lang.String[]";
        JavaType expectedType = typeFactory.constructArrayType(String.class);

        JavaType type = resolver.typeFromId(null, id);

        assertEquals(expectedType.getRawClass().getName(), type.getRawClass().getName());
    }

    static class CustomList extends ArrayList<String>{};

    static enum MyEnum {
        VALUE1,
        SUB_VALUE
    }
}