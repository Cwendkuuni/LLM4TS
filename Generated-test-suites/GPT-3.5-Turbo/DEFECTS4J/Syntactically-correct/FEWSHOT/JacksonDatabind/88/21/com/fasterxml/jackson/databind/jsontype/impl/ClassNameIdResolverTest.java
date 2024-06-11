package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassNameIdResolverTest {
    private ClassNameIdResolver classNameIdResolver;

    @Before
    public void setUp() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // not used with class name - based resolvers
    }

    @Test
    public void testIdFromValue() {
        String id = classNameIdResolver.idFromValue("test");
        assertEquals("java.lang.String", id);

        id = classNameIdResolver.idFromValue(10);
        assertEquals("java.lang.Integer", id);

        id = classNameIdResolver.idFromValue(null);
        assertNull(id);
    }

    @Test
    public void testIdFromValueAndType() {
        String id = classNameIdResolver.idFromValueAndType("test", String.class);
        assertEquals("java.lang.String", id);

        id = classNameIdResolver.idFromValueAndType(10, Integer.class);
        assertEquals("java.lang.Integer", id);

        id = classNameIdResolver.idFromValueAndType(null, Object.class);
        assertNull(id);
    }

    @Test
    public void testTypeFromId() throws Exception {
        DatabindContext context = new DeserializationContext(null, null);

        JavaType type = classNameIdResolver.typeFromId(context, "java.lang.String");
        assertEquals(TypeFactory.defaultInstance().constructType(String.class), type);

        type = classNameIdResolver.typeFromId(context, "java.lang.Integer");
        assertEquals(TypeFactory.defaultInstance().constructType(Integer.class), type);

        type = classNameIdResolver.typeFromId(context, "com.example.UnknownClass");
        assertNull(type);
    }

    @Test
    public void testTypeFromIdWithGenerics() throws Exception {
        DatabindContext context = new DeserializationContext(null, null);

        JavaType type = classNameIdResolver.typeFromId(context, "java.util.List<java.lang.String>");
        assertEquals(TypeFactory.defaultInstance().constructCollectionType(java.util.List.class, String.class), type);

        type = classNameIdResolver.typeFromId(context, "java.util.Map<java.lang.Integer,java.lang.String>");
        assertEquals(TypeFactory.defaultInstance().constructMapType(java.util.Map.class, Integer.class, String.class), type);

        type = classNameIdResolver.typeFromId(context, "java.util.Set<com.example.UnknownClass>");
        assertNull(type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidId() throws Exception {
        DatabindContext context = new DeserializationContext(null, null);

        classNameIdResolver.typeFromId(context, "InvalidClassName");
    }

    @Test
    public void test_idFrom() {
        String id = classNameIdResolver._idFrom("test", String.class, TypeFactory.defaultInstance());
        assertEquals("java.lang.String", id);

        id = classNameIdResolver._idFrom(10, Integer.class, TypeFactory.defaultInstance());
        assertEquals("java.lang.Integer", id);

        id = classNameIdResolver._idFrom(null, Object.class, TypeFactory.defaultInstance());
        assertNull(id);
    }
}