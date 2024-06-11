package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @ org.junit.Test
    public void testIdFromValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        // Test with simple class
        Class<?> simpleClass = String.class;
        String id = resolver.idFromValue(new String("Hello"));
        JavaType type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(String.class, type.getRawClass());

        // Test with Enum class
        Class<?> enumClass = Enum.class;
        Enum[] enumValues = Enum.values();
        Enum enumValue = enumValues[0];
        id = resolver.idFromValue(enumValue);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(enumValue.getClass(), type.getRawClass());

        // Test with generic class
        TypeReference<List<Integer>> listTypeRef = new TypeReference<List<Integer>>() {};
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        id = resolver.idFromValue(list);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(List.class, type.getRawClass());
        assertEquals(Integer.class, type.getComponentType().getRawClass());

        // Test with inner class
        Class<?> outerClass = Object.class;
        Class<?> innerClass = Integer.class;
        id = resolver.idFromValue(new InnerClassImpl());
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(outerClass, type.getRawClass());
        assertEquals(innerClass, type.getComponentType().getRawClass());

        // Test with unknown class
        Class<?> unknownClass = Class.forName("com.nonexistent.UnknownClass");
        id = resolver.idFromValue(new UnknownClass());
        assertNull(resolver.typeFromId(mapper.getDeserializationContext(), id));

        // Test with Java collections
        Class<?> enumSetClass = EnumSet.class;
        EnumSet<String> enumSet = EnumSet.allOf(String.class);
        id = resolver.idFromValue(enumSet);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertTrue(Map.class.isAssignableFrom(type.getRawClass()));
        assertTrue(Map.class.isAssignableFrom(type.getComponentType().getRawClass()));

        Class<?> enumMapClass = EnumMap.class;
        EnumMap<String, Integer> enumMap = new EnumMap<>(String.class);
        enumMap.put(String.valueOf('A'), 1);
        id = resolver.idFromValue(enumMap);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertTrue(Map.class.isAssignableFrom(type.getRawClass()));
        assertEquals(String.class, type.getComponentType().getKeyType());
        assertEquals(Integer.class, type.getComponentType().getRawClass());

        // Test with arrays and collections created using Arrays and Collections
        String[] strings = new String[]{"A", "B", "C"};
        id = resolver.idFromValue(strings);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(String[].class, type.getRawClass());

        List<Integer> arrayList = Arrays.asList(1, 2, 3);
        id = resolver.idFromValue(arrayList);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(Integer.class, type.getComponentType().getRawClass());

        // Test with custom base type
        Class<?> baseType = CustomBaseType.class;
        ClassNameIdResolver customResolver = new ClassNameIdResolver(baseType, typeFactory);
        CustomBaseType customValue = new CustomBaseType("Custom");
        id = customResolver.idFromValue(customValue);
        type = customResolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(baseType, type.getRawClass());
    }

    @ org.junit.Test
    public void testIdFromValueAndType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        // Test with simple class
        Class<?> simpleClass = String.class;
        String value = "Hello";
        String id = resolver.idFromValueAndType(value, simpleClass);
        JavaType type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(String.class, type.getRawClass());

        // Test with Enum class
        Class<?> enumClass = Enum.class;
        Enum enumValue = Enum.valueOf(enumClass, "VALUE_1");
        id = resolver.idFromValueAndType(enumValue, enumClass);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(enumValue.getClass(), type.getRawClass());

        // Test with generic class
        TypeReference<List<Integer>> listTypeRef = new TypeReference<List<Integer>>() {};
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        id = resolver.idFromValueAndType(list, List.class);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(List.class, type.getRawClass());
        assertEquals(Integer.class, type.getComponentType().getRawClass());

        // Test with inner class
        Class<?> outerClass = Object.class;
        Class<?> innerClass = Integer.class;
        id = resolver.idFromValueAndType(new InnerClassImpl(), outerClass);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(outerClass, type.getRawClass());
        assertEquals(innerClass, type.getComponentType().getRawClass());

        // Test with unknown class
        Class<?> unknownClass = Class.forName("com.nonexistent.UnknownClass");
        id = resolver.idFromValueAndType(new UnknownClass(), Object.class);
        assertNull(resolver.typeFromId(mapper.getDeserializationContext(), id));

        // Test with Java collections
        Class<?> enumSetClass = EnumSet.class;
        EnumSet<String> enumSet = EnumSet.allOf(String.class);
        id = resolver.idFromValueAndType(enumSet, Map.class);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertTrue(Map.class.isAssignableFrom(type.getRawClass()));
        assertTrue(Map.class.isAssignableFrom(type.getComponentType().getRawClass()));

        Class<?> enumMapClass = EnumMap.class;
        EnumMap<String, Integer> enumMap = new EnumMap<>(String.class);
        enumMap.put(String.valueOf('A'), 1);
        id = resolver.idFromValueAndType(enumMap, Map.class);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertTrue(Map.class.isAssignableFrom(type.getRawClass()));
        assertEquals(String.class, type.getComponentType().getKeyType());
        assertEquals(Integer.class, type.getComponentType().getRawClass());

        // Test with arrays and collections created using Arrays and Collections
        String[] strings = new String[]{"A", "B", "C"};
        id = resolver.idFromValueAndType(strings, String[].class);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(String[].class, type.getRawClass());

        List<Integer> arrayList = Arrays.asList(1, 2, 3);
        id = resolver.idFromValueAndType(arrayList, ArrayList.class);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(Integer.class, type.getComponentType().getRawClass());

        // Test with custom base type
        Class<?> baseType = CustomBaseType.class;
        ClassNameIdResolver customResolver = new ClassNameIdResolver(baseType, typeFactory);
        CustomBaseType customValue = new CustomBaseType("Custom");
        id = resolver.idFromValueAndType(customValue, baseType);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(baseType, type.getRawClass());
    }

    @org.junit.Test
    public void testRegisterSubtype() {
        // This method is not used with class name - based resolvers
    }

    static class CustomBaseType {
        private final String name;

        public CustomBaseType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class InnerClassImpl {
        // Empty inner class
    }

    static class UnknownClass {
        // Empty unknown class
    }
}