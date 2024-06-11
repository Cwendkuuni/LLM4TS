package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    ClassNameIdResolver resolver;

    @Before
    public void setUp() {
        resolver = new ClassNameIdResolver(JavaType.DEFAULT, TypeFactory.defaultInstance());
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        Class<?> type = Object.class;
        String name = "object";
        resolver.registerSubtype(type, name);
        // No assertion needed, should not throw exception
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertNotNull(id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertNotNull(id);
    }

    @Test
    public void testTypeFromId() {
        DatabindContext context = new DeserializationContext();
        String id = "java.lang.Object";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNotNull(javaType);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testTypeFromIdUnknown() {
        DatabindContext context = new DeserializationContext();
        String id = "unknown";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNull(javaType);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testTypeFromIdException() {
        DatabindContext context = new DeserializationContext();
        String id = "exception";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNull(javaType);
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Expected, do nothing
        }
    }

    @Test
    public void testPrivateMethod() {
        Object value = new Object();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String id = resolver.privateMethod(value, cls, typeFactory);
        assertNotNull(id);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        String desc = resolver.getDescForKnownTypeIds();
        assertNotNull(desc);
    }
}