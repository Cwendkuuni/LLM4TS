package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final TypeFactory typeFactory = new TypeFactoryBuilder()
            .build();

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);

        // Test with null value
        assertNull(resolver.idFromValue(null));

        // Test with primitive value
        assertNull(resolver.idFromValue(1));

        // Test with String value
        assertEquals("java.lang.String", resolver.idFromValue(""));

        // Test with enum value
        Enum e = Enum.valueOf(Enum.class, "FOO");
        assertEquals(Enum.class.getName(), resolver.idFromValue(e));

        // Test with custom class value
        Object obj = new Object() {
        };
        assertEquals(obj.getClass().getName(), resolver.idFromValue(obj));

        // Test with custom class with generic type value
        List list = Arrays.asList(new Object() {
        }, new Object() {
        });
        assertEquals("java.util.ArrayList", resolver.idFromValue(list));

        // Test with custom class with inner class value
        Class<?> outerClass = Object.class;
        Class<?> innerClass = new Class<>() {}.getDeclaredInnerClass("InnerClass");
        Object innerObj = new Object() {
            InnerClass inner = new InnerClass();
        };
        assertEquals(outerClass.getName() + "$" + innerClass.getSimpleName(), resolver.idFromValue(innerObj));

        // Test with custom class with super class value
        Class<?> superClass = Object.class;
        Class<?> subClass = new Class<>() {}.getSuperclass();
        Object subObj = new Object() {
        };
        assertEquals(superClass.getName(), resolver.idFromValue(subObj));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);

        // Test with null value and null type
        assertNull(resolver.idFromValueAndType(null, null));

        // Test with primitive value and Object type
        assertNull(resolver.idFromValueAndType(1, Object.class));

        // Test with String value and String type
        assertEquals("java.lang.String", resolver.idFromValueAndType("", String.class));

        // Test with enum value and Enum type
        Enum e = Enum.valueOf(Enum.class, "FOO");
        assertEquals(Enum.class.getName(), resolver.idFromValueAndType(e, Enum.class));

        // Test with custom class value and Object type
        Object obj = new Object() {
        };
        assertEquals(obj.getClass().getName(), resolver.idFromValueAndType(obj, Object.class));

        // Test with custom class value and custom class type
        List list = Arrays.asList(new Object() {
        }, new Object() {
        });
        assertEquals("java.util.ArrayList", resolver.idFromValueAndType(list, List.class));

        // Test with custom class value and custom class with inner class type
        Class<?> outerClass = Object.class;
        Class<?> innerClass = new Class<>() {}.getDeclaredInnerClass("InnerClass");
        Object innerObj = new Object() {
            InnerClass inner = new InnerClass();
        };
        assertEquals(outerClass.getName() + "$" + innerClass.getSimpleName(), resolver.idFromValueAndType(innerObj, outerClass.getClass()));

        // Test with custom class value and custom class with super class type
        Class<?> superClass = Object.class;
        Class<?> subClass = new Class<>() {}.getSuperclass();
        Object subObj = new Object() {
        };
        assertEquals(superClass.getName(), resolver.idFromValueAndType(subObj, superClass));
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new DatabindContext();
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);

        // Test with null id
        assertNull(resolver.typeFromId(context, null));

        // Test with basic class name id
        assertEquals(String.class, resolver.typeFromId(context, "java.lang.String"));

        // Test with custom class name id
        Class<?> customClass = new Class<CustomClass>() {
        }.getDeclaredClass();
        assertEquals(customClass, resolver.typeFromId(context, customClass.getName()));

        // Test with custom class name id with generic type
        Class<?> listType = typeFactory.constructCollectionType(ArrayList.class, String.class);
        assertEquals(listType, resolver.typeFromId(context, listType.getName()));

        // Test with custom class name id with inner class
        Class<?> outerClass = Object.class;
        Class<?> innerClass = new Class<>() {}.getDeclaredInnerClass("InnerClass");
        Class<?> innerType = innerClass.getDeclaredConstructor(Object.class).getGenericParameterTypes()[0];
        Class<?> innerGenericType = (Class<?>) innerType.getGenericComponentType();
        Class<?> outerGenericType = typeFactory.constructType(outerClass.getName() + "<?" + innerGenericType.getName() + ">");
        assertEquals(outerGenericType, resolver.typeFromId(context, outerClass.getName() + "$" + innerClass.getSimpleName()));

        // Test with custom class name id with super class
        Class<?> superClass = Object.class;
        Class<?> subClass = new Class<SubClass>() {}.getSuperclass();
        Class<?> subType = resolver.typeFromId(context, subClass.getName());
        assertEquals(superClass, subType.getSuperclass());
    }
}