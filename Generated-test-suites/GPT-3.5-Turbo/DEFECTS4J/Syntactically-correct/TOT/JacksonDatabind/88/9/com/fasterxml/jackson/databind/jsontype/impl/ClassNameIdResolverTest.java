package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;

public class ClassNameIdResolverTest {

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, TypeFactory.defaultInstance());

        // Test that the registerSubtype method does not do anything
        // as it is not used with class name-based resolvers
        resolver.registerSubtype(Object.class, "object");

        // No assertion, just ensuring that the method does not throw an exception
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, TypeFactory.defaultInstance());

        // Test with a custom object
        Object value = new Object();
        String id = resolver.idFromValue(value);
        Assert.assertNotNull(id);
        Assert.assertEquals(value.getClass().getName(), id);

        // Test with an enum
        value = TestEnum.VALUE1;
        id = resolver.idFromValue(value);
        Assert.assertNotNull(id);
        Assert.assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, TypeFactory.defaultInstance());

        // Test with a custom object
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        Assert.assertNotNull(id);
        Assert.assertEquals(type.getName(), id);

        // Test with an enum
        value = TestEnum.VALUE1;
        type = value.getClass();
        id = resolver.idFromValueAndType(value, type);
        Assert.assertNotNull(id);
        Assert.assertEquals(type.getName(), id);
    }

    @Test
    public void testTypeFromId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, TypeFactory.defaultInstance());

        // Test with a known type id
        String id = String.class.getName();
        JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType actualType = resolver.typeFromId(null, id);
        Assert.assertNotNull(actualType);
        Assert.assertEquals(expectedType, actualType);

        // Test with an unknown type id
        id = "com.example.UnknownType";
        actualType = resolver.typeFromId(null, id);
        Assert.assertNull(actualType);
    }

    @Test
    public void test_idFrom() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, TypeFactory.defaultInstance());

        // Test with a custom object
        Object value = new Object();
        Class<?> cls = value.getClass();
        String expected = cls.getName();
        String actual = resolver._idFrom(value, cls, TypeFactory.defaultInstance());
        Assert.assertEquals(expected, actual);

        // Test with an enum
        value = TestEnum.VALUE1;
        cls = value.getClass();
        expected = cls.getName();
        actual = resolver._idFrom(value, cls, TypeFactory.defaultInstance());
        Assert.assertEquals(expected, actual);

        // Test with a java.util list
        value = Arrays.asList(1, 2, 3);
        cls = value.getClass();
        expected = "java.util.ArrayList";
        actual = resolver._idFrom(value, cls, TypeFactory.defaultInstance());
        Assert.assertEquals(expected, actual);

        // Test with an inner class
        value = new OuterClass.InnerClass();
        cls = value.getClass();
        expected = cls.getName();
        actual = resolver._idFrom(value, cls, TypeFactory.defaultInstance());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.class, TypeFactory.defaultInstance());

        // Test that the getDescForKnownTypeIds method returns the expected description
        String expected = "class name used as type id";
        String actual = resolver.getDescForKnownTypeIds();
        Assert.assertEquals(expected, actual);
    }

    private enum TestEnum {
        VALUE1,
        VALUE2
    }

}