
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver classNameIdResolver;
    private JavaType baseType;

    @Before
    public void setup() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        baseType = typeFactory.constructType(Object.class);
        classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        Assert.assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        Class<?> type = Object.class;
        String name = "Test";
        // Test that the method does nothing
        classNameIdResolver.registerSubtype(type, name);
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String expectedId = value.getClass().getName();
        Assert.assertEquals(expectedId, classNameIdResolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        String expectedId = type.getName();
        Assert.assertEquals(expectedId, classNameIdResolver.idFromValueAndType(value, type));
    }

    @Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.Object";
        JavaType expectedType = baseType;
        DatabindContext context = null; // Fill in with appropriate context if needed
        Assert.assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void testTypeFromIdWithUnknownId() throws IOException {
        String id = "com.example.UnknownClass";
        DatabindContext context = null; // Fill in with appropriate context if needed
        JavaType expectedType = null;
        Assert.assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidId() throws IOException {
        String id = "InvalidId";
        DatabindContext context = null; // Fill in with appropriate context if needed
        classNameIdResolver.typeFromId(context, id);
    }

    @Test
    public void testTypeFromIdWithDeserializationContext() throws IOException {
        String id = "com.example.UnknownClass";
        DeserializationContext context = new DeserializationContext(null); // Fill in with appropriate context if needed
        JavaType expectedType = null; // Fill in with expected type
        Assert.assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void test_idFrom() {
        Object value = new Object();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String expectedId = cls.getName();
        Assert.assertEquals(expectedId, classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFromWithEnumClass() {
        Object value = TestEnum.ONE;
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String expectedId = cls.getSuperclass().getName();
        Assert.assertEquals(expectedId, classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFromWithEnumSubClass() {
        Object value = TestEnumSub.ONE;
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String expectedId = cls.getName();
        Assert.assertEquals(expectedId, classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFromWithJavaUtilPackage() {
        Object value = new ArrayList<>();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String expectedId = "java.util.ArrayList";
        Assert.assertEquals(expectedId, classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFromWithArraysInList() {
        Object value = Arrays.asList("one", "two", "three");
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String expectedId = "java.util.ArrayList";
        Assert.assertEquals(expectedId, classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFromWithInnerClass() {
        Object value = new InnerClass();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String expectedId = cls.getName();
        Assert.assertEquals(expectedId, classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        String expectedDesc = "class name used as type id";
        Assert.assertEquals(expectedDesc, classNameIdResolver.getDescForKnownTypeIds());
    }

    private enum TestEnum {
        ONE
    }

    private static class TestEnumSub extends TestEnum {
    }

    private static class InnerClass {
    }
}

