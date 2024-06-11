package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {
    
    ClassNameIdResolver resolver;
    ObjectMapper mapper;
    TypeFactory typeFactory;
        
    @Before
    public void setUp() {
        JavaType baseType = TypeFactory.defaultInstance().constructSimpleType(ClassNameIdResolver.class, null);
        typeFactory = TypeFactory.defaultInstance();
        resolver = new ClassNameIdResolver(baseType, typeFactory);
        mapper = new ObjectMapper();
        mapper.registerModule(new com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverModule());
    }
    
    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        Class<?> type = String.class;
        String name = "string";
        // Method not used with class name-based resolvers, so no assertions here
    }
    
    @Test
    public void testIdFromValue() {
        Object value = "test";
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.String", id);
    }
    
    @Test
    public void testIdFromValueAndType() {
        Object value = "test";
        Class<?> type = String.class;
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.String", id);
    }
    
    @Test
    public void testTypeFromId() throws Exception {
        String id = "java.lang.String";
        DatabindContext context = mapper.getDeserializationContext();
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(String.class, javaType.getRawClass());
    }
    
    @Test
    public void test_typeFromId_invalidId() throws Exception {
        String id = "com.example.InvalidType";
        DatabindContext context = mapper.getDeserializationContext();
        JavaType javaType = resolver.typeFromId(context, id);
        assertNull(javaType);
    }
    
    @Test
    public void test_typeFromId_unknownTypeId() throws Exception {
        String id = "com.example.UnknownType";
        DatabindContext context = mapper.getDeserializationContext();
        JavaType javaType = resolver.typeFromId(context, id);
        assertNotNull(javaType);
        assertEquals(ClassNameIdResolver.class, javaType.getRawClass());
    }
    
    @Test
    public void test_typeFromId_invalidDatabindContext() throws Exception {
        String id = "java.lang.String";
        DatabindContext context = new CustomDatabindContext();
        JavaType javaType = resolver.typeFromId(context, id);
        assertNotNull(javaType);
        assertEquals(ClassNameIdResolver.class, javaType.getRawClass());
    }
    
    @Test
    public void test_typeFromId_invalidIdAndDatabindContext() throws Exception {
        String id = "com.example.InvalidType";
        DatabindContext context = new CustomDatabindContext();
        JavaType javaType = resolver.typeFromId(context, id);
        assertNull(javaType);
    }
    
    @Test
    public void test_typeFromId_genericId() throws Exception {
        String id = "java.util.List<java.lang.String>";
        DatabindContext context = mapper.getDeserializationContext();
        JavaType javaType = resolver.typeFromId(context, id);
        assertNotNull(javaType);
        assertEquals(java.util.List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType().getRawClass());
    }
    
    @Test
    public void test_typeFromId_innerClass() throws Exception {
        String id = "com.example.OuterClass$InnerClass";
        DatabindContext context = mapper.getDeserializationContext();
        JavaType javaType = resolver.typeFromId(context, id);
        assertNotNull(javaType);
        assertEquals(InnerClass.class, javaType.getRawClass());
    }

    private static class CustomDatabindContext implements DatabindContext {
        public JavaType getBaseType() {
            return null;
        }
    }
}