package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {
    private ClassNameIdResolver idResolver;
    private JavaType baseType;
    private TypeFactory typeFactory;
    
    @Before
    public void setUp() {
        baseType = TypeFactory.defaultInstance().constructType(Object.class);
        typeFactory = TypeFactory.defaultInstance();
        idResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, idResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // registerSubtype() method is not used in ClassNameIdResolver, so verify that it does not throw an exception
        idResolver.registerSubtype(Object.class, "");
    }

    @Test
    public void testIdFromValue() {
        assertEquals("java.lang.Object", idResolver.idFromValue(new Object()));
        assertEquals("java.lang.String", idResolver.idFromValue(""));
    }

    @Test
    public void testIdFromValueAndType() {
        assertEquals("java.lang.Object", idResolver.idFromValueAndType(new Object(), Object.class));
        assertEquals("java.lang.String", idResolver.idFromValueAndType("", String.class));
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext ctxt = new ObjectMapper().reader().createDeserializationContext(null, null);
        
        JavaType expectedType = typeFactory.constructType(Object.class);
        JavaType actualType = idResolver.typeFromId(ctxt, "java.lang.Object");
        assertEquals(expectedType, actualType);
        
        expectedType = typeFactory.constructType(EnumSet.class, MyEnum.class);
        actualType = idResolver.typeFromId(ctxt, "java.util.EnumSet<com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$MyEnum>");
        assertEquals(expectedType, actualType);
        
        expectedType = typeFactory.constructMapType(EnumMap.class, MyEnum.class, String.class);
        actualType = idResolver.typeFromId(ctxt, "java.util.EnumMap<com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$MyEnum,java.lang.String>");
        assertEquals(expectedType, actualType);
        
        try {
            idResolver.typeFromId(ctxt, "unknown.class.Unknown");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id 'unknown.class.Unknown' (for id type 'Id.class'): Class not found", e.getMessage());
        }
    }

    protected static enum MyEnum {
        VALUE1, VALUE2;
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testTypeFromId_handleUnknownTypeId() throws IOException {
        ClassNameIdResolver idResolver = new ClassNameIdResolver(BaseType.class, TypeFactory.defaultInstance()) {
            @Override
            protected JavaType _typeFromId(String id, DatabindContext ctxt) throws IOException {
                return null;
            }
        };
        
        DeserializationContext dctxt = new ObjectMapper().reader().createDeserializationContext(null, null);
        
        JavaType expectedType = TypeFactory.unknownType();
        JavaType actualType = idResolver.typeFromId(dctxt, "unknown.class.Unknown");
        assertEquals(expectedType, actualType);
    }

    protected static class BaseType {
    }
}