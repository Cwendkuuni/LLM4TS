package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClassNameIdResolverTest {

    @Mock
    private JavaType baseType;

    @Mock
    private TypeFactory typeFactory;

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        // Method not used with class name - based resolvers, Mock object is just for testing purpose
        resolver.registerSubtype(Object.class, "ObjectName");
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        Object value = "test";
        String id = resolver.idFromValue(value);
        assertNotNull(id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        Object value = "test";
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertNotNull(id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        DatabindContext context = mock(DatabindContext.class);
        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(context, id);
        assertNotNull(javaType);
    }

    @Test
    public void test_typeFromId_unknownTypeId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        DatabindContext context = mock(DatabindContext.class);
        String id = "unknown.type.Identifier";
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(context instanceof DeserializationContext).thenReturn(true);
        when((DeserializationContext)context).thenReturn(deserializationContext);
        JavaType javaType = resolver.typeFromId(context, id);
        assertNotNull(javaType);
        verify(deserializationContext).handleUnknownTypeId(baseType, id, resolver, "no such class found");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_typeFromId_invalidTypeId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        DatabindContext context = mock(DatabindContext.class);
        String id = "invalid_type";
        resolver.typeFromId(context, id);
    }

    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        Object value = "test";
        Class<?> cls = value.getClass();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertNotNull(id);
    }

    @Test
    public void test_idFrom_enumClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        Object value = EnumTest.TEST;
        Class<?> cls = value.getClass();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertNotNull(id);
    }

    @Test
    public void test_idFrom_enumSubClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        Object value = EnumTest.TEST_SUB;
        Class<?> cls = value.getClass();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertNotNull(id);
    }

    @Test
    public void test_idFrom_javaUtil() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        Object value = new ArrayList<>();
        Class<?> cls = value.getClass();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertNotNull(id);
    }

    @Test
    public void test_idFrom_enumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        Object value = EnumSet.of(EnumTest.TEST);
        Class<?> cls = value.getClass();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertNotNull(id);
    }

    @Test
    public void test_idFrom_enumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        Object value = new EnumMap<>(EnumTest.class);
        Class<?> cls = value.getClass();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertNotNull(id);
    }

    @Test
    public void test_idFrom_innerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        Object value = new TestClass.InnerClass();
        Class<?> cls = value.getClass();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertNotNull(id);
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }

    // Enum for testing purposes
    enum EnumTest {
        TEST,
        TEST_SUB
    }

    // Inner class for testing purposes
    public static class TestClass {
        public static class InnerClass {
        }
    }

}