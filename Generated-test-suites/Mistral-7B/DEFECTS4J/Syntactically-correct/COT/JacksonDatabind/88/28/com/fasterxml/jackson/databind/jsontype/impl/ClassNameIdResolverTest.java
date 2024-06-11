package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for ClassNameIdResolver
 */
public class ClassNameIdResolverTest {

    private static final TypeFactory typeFactory = new TypeFactory();

    @org.junit.Test
    public void testClassNameIdResolver_Constructor() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        assertNotNull(resolver);
    }

    @org.junit.Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @org.junit.Test
    public void testRegisterSubtype() {
        // Not applicable to class name based resolver
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        try {
            resolver.registerSubtype(String.class, "com.test.StringType");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testIdFromValue() throws Exception {
        Object value = new Object();
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        String actual = resolver.idFromValue(value);
        String expected = value.getClass().getName();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testIdFromValueAndType() throws Exception {
        Object value = new Object();
        Class<?> type = String.class;
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        String actual = resolver.idFromValueAndType(value, type);
        String expected = type.getName();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testTypeFromId() throws Exception {
        DatabindContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), context);
        String id = "java.lang.String";
        JavaType actual = resolver.typeFromId(context, id);
        JavaType expected = context.getTypeFactory().constructType(id);
        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = IOException.class)
    public void testTypeFromId_InvalidId() throws Exception {
        DatabindContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), context);
        String id = "invalidId";
        resolver.typeFromId(context, id);
        fail("Expected IOException");
    }

    @org.junit.Test
    public void testTypeFromId_UnknownClass() throws Exception {
        DatabindContext context = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), context);
        String id = "com.nonexistent.UnknownClass";
        JavaType actual = resolver.typeFromId(context, id);
        assertNull(actual);
    }

    @org.junit.Test
    public void testIdFrom_EnumSet() {
        EnumSet<String> value = EnumSet.allOf(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        String actual = resolver._idFrom(value, EnumSet.class, typeFactory);
        String expected = typeFactory.constructCollectionType(EnumSet.class, String.class).toCanonical();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testIdFrom_EnumMap() {
        Map<String, Integer> value = new HashMap<>();
        value.put("key1", 1);
        value.put("key2", 2);
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        String actual = resolver._idFrom(value, Map.class, typeFactory);
        String expected = typeFactory.constructMapType(EnumMap.class, String.class, Integer.class).toCanonical();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testIdFrom_Array() {
        int[] value = {1, 2, 3};
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        String actual = resolver._idFrom(value, int[].class, typeFactory);
        String expected = "java.lang.int[]";
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testIdFrom_InnerClass() {
        class Outer {
            static class Inner {
            }
        }
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        String actual = resolver._idFrom(new Outer.Inner(), Outer.Inner.class, typeFactory);
        String expected = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$Outer$Inner";
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        String actual = resolver.getDescForKnownTypeIds();
        String expected = "class name used as type id";
        assertEquals(expected, actual);
    }
}