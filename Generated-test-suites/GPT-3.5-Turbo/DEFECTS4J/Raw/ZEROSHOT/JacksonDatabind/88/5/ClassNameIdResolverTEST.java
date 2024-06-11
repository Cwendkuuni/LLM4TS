package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // test not used with class name - based resolvers
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertEquals(type.getName(), id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // create dummy DatabindContext object
        DatabindContext context = new DatabindContext() {};

        String id = "java.lang.Object";
        JavaType expectedType = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType actualType = resolver.typeFromId(context, id);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testTypeFromId_NoSuchClassFound() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // dummy DatabindContext object to trigger handleUnknownTypeId
        DeserializationContext context = new DeserializationContext(null, null, null);
        String id = "nonexistent.class.Name";
        JavaType actualType = resolver.typeFromId(context, id);
        assertNull(actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromId_InvalidTypeId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DatabindContext() {};
        String id = "invalid type id";
        resolver.typeFromId(context, id);
    }

    @Test
    public void testTypeFromId_EnumSet() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // create dummy DatabindContext object
        DatabindContext context = new DatabindContext() {};

        EnumSet<TestClass> value = EnumSet.allOf(TestClass.class);
        String id = "java.util.EnumSet";
        JavaType expectedType = TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, TestClass.class);
        JavaType actualType = resolver.typeFromId(context, id);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testTypeFromId_EnumMap() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // create dummy DatabindContext object
        DatabindContext context = new DatabindContext() {};

        EnumMap<TestClass, Object> value = new EnumMap<>(TestClass.class);
        String id = "java.util.EnumMap";
        JavaType expectedType = TypeFactory.defaultInstance().constructMapType(EnumMap.class, TestClass.class, Object.class);
        JavaType actualType = resolver.typeFromId(context, id);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testTypeFromId_ArraysList() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // create dummy DatabindContext object
        DatabindContext context = new DatabindContext() {};

        List<TestClass> value = new ArrayList<>();
        String id = "java.util.ArrayList";
        JavaType expectedType = TypeFactory.defaultInstance().constructParametricType(ArrayList.class, TestClass.class);
        JavaType actualType = resolver.typeFromId(context, id);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void test_idFrom() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Class<?> cls = TestClass.class;
        String expectedId = cls.getName();
        String actualId = resolver._idFrom(null, cls, null);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testIdFrom_EnumClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Class<?> cls = TestEnum.class;
        String expectedId = cls.getName();
        String actualId = resolver._idFrom(null, cls, null);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testIdFrom_EnumSubclass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Class<?> cls = TestEnumSubclass.class;
        String expectedId = cls.getSuperclass().getName();
        String actualId = resolver._idFrom(null, cls, null);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testIdFrom_InnerClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Class<?> cls = InnerClass.class;
        String expectedId = cls.getName();
        String actualId = resolver._idFrom(null, cls, null);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }

    enum TestEnum {
        VALUE1, VALUE2
    }

    enum TestEnumSubclass {
        VALUE1, VALUE2
    }

    enum InnerClass {
        ;
        enum InnerEnum {
            ;
        }
    }

    static class TestClass {
    }
}