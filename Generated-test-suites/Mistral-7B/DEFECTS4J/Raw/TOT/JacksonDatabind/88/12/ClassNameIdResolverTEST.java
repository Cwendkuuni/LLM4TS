package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassNameIdResolverTest {

    @Test
    public void testIdFromValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType baseType = typeFactory.constructType(ClassNameIdResolver.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with simple classes
        Object simpleValue = new Object();
        assertEquals("java.lang.Object", resolver.idFromValue(simpleValue));

        Class simpleClass = Integer.class;
        assertEquals("java.lang.Integer", resolver.idFromValue(new Integer(1)));

        // Test with arrays
        int[] intArray = {1, 2, 3};
        assertEquals("java.lang.int[]", resolver.idFromValue(intArray));

        // Test with collections
        List<Integer> intList = Arrays.asList(1, 2, 3);
        assertEquals("java.util.ArrayList", resolver.idFromValue(intList));

        // Test with custom classes
        Class customClass = CustomClass.class;
        assertEquals("com.example.CustomClass", resolver.idFromValue(new CustomClass()));

        // Test with enum classes
        MyEnum myEnum = MyEnum.VALUE1;
        assertEquals("com.example.MyEnum", resolver.idFromValue(myEnum));

        // Test with generic classes
        List<MyEnum> enumList = Arrays.asList(MyEnum.VALUE1, MyEnum.VALUE2);
        TypeReference<List<MyEnum>> typeRef = new TypeReference<List<MyEnum>>(){};
        assertEquals("java.util.ArrayList<com.example.MyEnum>", resolver.idFromValue(enumList, typeRef));

        // Test with hidden classes (arrays and collections)
        Object[] hiddenArray = new Object[3];
        assertEquals("java.lang.Object[]", resolver.idFromValue(hiddenArray));

        HashMap<MyEnum, String> hiddenMap = new HashMap<>();
        assertEquals("java.util.HashMap<com.example.MyEnum,java.lang.String>", resolver.idFromValue(hiddenMap));

        // Test with inner classes
        InnerClass innerClass = new OuterClass().new InnerClass();
        assertEquals("com.example.OuterClass$InnerClass", resolver.idFromValue(innerClass));

        // Test with complex types (generics and nested classes)
        List<List<MyEnum>> nestedList = Arrays.asList(Arrays.asList(MyEnum.VALUE1, MyEnum.VALUE2), Arrays.asList(MyEnum.VALUE3, MyEnum.VALUE4));
        TypeReference<List<List<MyEnum>>> nestedTypeRef = new TypeReference<List<List<MyEnum>>>(){} ;
        assertEquals("java.util.ArrayList<java.util.ArrayList<com.example.MyEnum>>", resolver.idFromValue(nestedList, nestedTypeRef));

        // Test with unknown types
        assertNull(resolver.idFromValue(new Object()));
    }

    @Test
    public void testIdFromValueAndType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType baseType = typeFactory.constructType(ClassNameIdResolver.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with simple classes
        Object simpleValue = new Object();
        assertNull(resolver.idFromValueAndType(simpleValue, Object.class));

        Class simpleClass = Integer.class;
        assertEquals("java.lang.Integer", resolver.idFromValueAndType(new Integer(1), simpleClass));

        // Test with arrays
        int[] intArray = {1, 2, 3};
        assertEquals("java.lang.int[]", resolver.idFromValueAndType(intArray, int[].class));

        // Test with collections
        List<Integer> intList = Arrays.asList(1, 2, 3);
        assertEquals("java.util.ArrayList", resolver.idFromValueAndType(intList, List.class));

        // Test with custom classes
        Class customClass = CustomClass.class;
        assertEquals("com.example.CustomClass", resolver.idFromValueAndType(new CustomClass(), customClass));

        // Test with enum classes
        MyEnum myEnum = MyEnum.VALUE1;
        assertEquals("com.example.MyEnum", resolver.idFromValueAndType(myEnum, MyEnum.class));

        // Test with generic classes
        List<MyEnum> enumList = Arrays.asList(MyEnum.VALUE1, MyEnum.VALUE2);
        TypeReference<List<MyEnum>> typeRef = new TypeReference<List<MyEnum>>(){};
        assertEquals("java.util.ArrayList<com.example.MyEnum>", resolver.idFromValueAndType(enumList, List.class));

        // Test with hidden classes (arrays and collections)
        Object[] hiddenArray = new Object[3];
        assertNull(resolver.idFromValueAndType(hiddenArray, Object[].class));

        HashMap<MyEnum, String> hiddenMap = new HashMap<>();
        assertNull(resolver.idFromValueAndType(hiddenMap, HashMap.class));

        // Test with inner classes
        InnerClass innerClass = new OuterClass().new InnerClass();
        assertEquals("com.example.OuterClass$InnerClass", resolver.idFromValueAndType(innerClass, InnerClass.class));

        // Test with complex types (generics and nested classes)
        List<List<MyEnum>> nestedList = Arrays.asList(Arrays.asList(MyEnum.VALUE1, MyEnum.VALUE2), Arrays.asList(MyEnum.VALUE3, MyEnum.VALUE4));
        TypeReference<List<List<MyEnum>>> nestedTypeRef = new TypeReference<List<List<MyEnum>>>(){} ;
        assertEquals("java.util.ArrayList<java.util.ArrayList<com.example.MyEnum>>", resolver.idFromValueAndType(nestedList, List.class));

        // Test with unknown types
        assertNull(resolver.idFromValueAndType(new Object(), Object.class));
    }

    @Test
    public void testTypeFromId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType baseType = typeFactory.constructType(ClassNameIdResolver.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with simple classes
        assertEquals(Object.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.Object"));
        assertEquals(Integer.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.Integer"));

        // Test with arrays
        assertEquals(int[].class, resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.int[]"));

        // Test with collections
        assertEquals(List.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList"));

        // Test with custom classes
        assertEquals(CustomClass.class, resolver.typeFromId(mapper.getDeserializationContext(), "com.example.CustomClass"));

        // Test with enum classes
        assertEquals(MyEnum.class, resolver.typeFromId(mapper.getDeserializationContext(), "com.example.MyEnum"));

        // Test with hidden classes (arrays and collections)
        assertNull(resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.Object[]"));
        assertNull(resolver.typeFromId(mapper.getDeserializationContext(), "java.util.HashMap"));

        // Test with inner classes
        assertEquals(InnerClass.class, resolver.typeFromId(mapper.getDeserializationContext(), "com.example.OuterClass$InnerClass"));

        // Test with complex types (generics and nested classes)
        assertEquals(List.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList<java.util.ArrayList<com.example.MyEnum>>"));

        // Test with unknown types
        assertNull(resolver.typeFromId(mapper.getDeserializationContext(), "com.example.UnknownType"));
    }

    static class CustomClass {
        // Custom class for testing
    }

    static class OuterClass {
        static class InnerClass {
            // Inner class for testing
        }
    }

    enum MyEnum {
        VALUE1, VALUE2, VALUE3, VALUE4;
    }
}