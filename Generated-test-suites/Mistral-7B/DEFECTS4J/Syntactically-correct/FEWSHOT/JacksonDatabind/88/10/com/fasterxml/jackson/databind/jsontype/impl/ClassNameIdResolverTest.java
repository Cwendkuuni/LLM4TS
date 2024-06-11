package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.Class;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ClassNameIdResolverTest {

    private static final TypeFactory typeFactory = new TypeFactoryBuilder()
            .build();

    @Test
    public void testClassNameIdResolver() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        // Test idFromValue
        assertEquals("java.lang.String", resolver.idFromValue(null));
        assertEquals("java.lang.String", resolver.idFromValue(""));
        assertEquals("java.lang.String[]", resolver.idFromValue(new String[]{}));
        assertEquals("java.util.List<java.lang.String>", resolver.idFromValue(Arrays.asList("foo", "bar")));

        // Test idFromValueAndType
        assertEquals("java.lang.String", resolver.idFromValueAndType(null, null));
        assertEquals("java.lang.String", resolver.idFromValueAndType("", String.class));
        assertEquals("java.lang.String[]", resolver.idFromValueAndType(new String[0], String[].class));
        assertEquals("java.util.List<java.lang.String>", resolver.idFromValueAndType(Arrays.asList("foo", "bar"), List.class));

        // Test typeFromId
        TypeReference<List<String>> listTypeRef = new TypeReference<List<String>>() {};
        assertEquals(String.class, resolver.typeFromId(null, "java.lang.String").getRawClass());
        assertEquals(listTypeRef.getType(), resolver.typeFromId(null, "java.util.List<java.lang.String>"));

        // Test special cases
        assertEquals("java.util.ArrayList", resolver._idFrom(null, ArrayList.class, typeFactory));
        assertEquals("java.util.EnumMap<java.lang.String, java.lang.String>",
                resolver._idFrom(null, EnumMap.class, typeFactory));
        assertEquals("java.util.EnumSet<java.lang.String>",
                resolver._idFrom(EnumSet.of("foo"), HashSet.class, typeFactory));
        assertEquals("java.util.Collections$unmodifiableList",
                resolver._idFrom(Arrays.asList("foo", "bar"), ArrayList.class, typeFactory));
    }

    @Test
    public void testIdFromValueWithCustomBaseType() throws IOException {
        ClassNameIdResolver<MyObject> resolver = new ClassNameIdResolver<>(MyObject.class, typeFactory);
        assertEquals("com.example.MyObject", resolver.idFromValue(new MyObject()));
    }

    @Test
    public void testIdFromValueWithEnum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);
        assertEquals("java.lang.String", resolver.idFromValue(MyEnum.FOO));
        assertEquals("java.lang.String", resolver.idFromValue(MyEnum.BAR.getClass()));
    }

    @Test
    public void testIdFromValueWithGenerics() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);
        Type listType = new TypeReference<List<String>>() {}.getType();
        assertEquals("java.util.List<java.lang.String>", resolver.idFromValueAndType(null, listType));

        Type mapType = new TypeReference<Map<String, String>>() {}.getType();
        assertEquals("java.util.Map<java.lang.String, java.lang.String>",
                resolver.idFromValueAndType(null, mapType));

        Type arrayType = new TypeReference<String[]>() {}.getType();
        assertEquals("java.lang.String[]", resolver.idFromValueAndType(null, arrayType));

        Type listOfArraysType = new TypeReference<String[][]>() {}.getType();
        assertEquals("java.lang.String[][]", resolver.idFromValueAndType(null, listOfArraysType));

        Type listOfGenericTypeType = new TypeReference<List<MyCustomType>>() {}.getType();
        assertEquals("com.example.MyCustomType", resolver.idFromValueAndType(new MyCustomType(), listOfGenericTypeType).getType()
                .getComponentType()
                .getRawClass()
                .getName());
    }

    @Test
    public void testIdFromValueWithArrays() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);
        String[] arr = new String[]{"foo", "bar"};
        assertEquals("java.lang.String[]", resolver.idFromValue(arr));
    }

    @Test
    public void testIdFromValueWithPrimitiveArrays() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);
        byte[] arr = new byte[10];
        assertEquals("byte[]", resolver.idFromValue(arr));
    }

    @Test
    public void testIdFromValueWithAnonymousClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);
        Object anonymousClass = new Object() {
            // empty class
        };
        assertEquals("java.lang.Object", resolver.idFromValue(anonymousClass));
    }

    @Test
    public void testIdFromValueWithInnerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);
        InnerClass innerClass = new OuterClass().new InnerClass();
        assertEquals("com.example.OuterClass$InnerClass", resolver.idFromValue(innerClass));
    }

    @Test
    public void testIdFromValueWithAnonymousInnerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);
        Object anonymousInnerClass = new OuterClass() {}.new InnerClass() {
            // anonymous inner class
        };
        assertEquals("java.lang.Object", resolver.idFromValue(anonymousInnerClass));
    }

    @Test
    public void testIdFromValueWithNestedGenericClasses() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);
        Type outerListType = new TypeReference<List<NestedGenericClass>>() {}.getType();
        Type innerListType = new TypeReference<List<NestedGenericClass.InnerClass>>() {}.getType();
        assertEquals("com.example.NestedGenericClass$InnerClass",
                resolver.idFromValueAndType(new NestedGenericClass.InnerClass(), innerListType).getType()
                        .getComponentType()
                        .getName());
        assertEquals("com.example.NestedGenericClass",
                resolver.idFromValueAndType(new NestedGenericClass(), outerListType).getType()
                        .getComponentType()
                        .getName());
    }

    static class MyEnum {
        public static final MyEnum FOO = new MyEnum();
        public static final MyEnum BAR = new MyEnum();
    }

    static class MyObject {
        // empty class
    }

    static class MyCustomType {
        // empty class
    }

    static class OuterClass {
        static class InnerClass {
            // empty class
        }

        class NestedGenericClass<T> {
            static class InnerClass<U> {
                // empty class
            }
        }
    }
}