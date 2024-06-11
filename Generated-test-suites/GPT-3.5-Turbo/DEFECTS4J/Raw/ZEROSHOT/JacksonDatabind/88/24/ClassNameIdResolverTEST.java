package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.*;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver resolver;
    private JavaType baseType;
    
    @Before
    public void setUp() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        baseType = typeFactory.constructType(Object.class);
        resolver = new ClassNameIdResolver(baseType, typeFactory);
    }
    
    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        // Test that this method does nothing
        resolver.registerSubtype(String.class, "String");
    }
    
    @Test
    public void testIdFromValue() {
        Object value = "Hello";
        String expected = "java.lang.String";
        String actual = resolver.idFromValue(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIdFromValueAndType() {
        Object value = "Hello";
        Class<?> type = String.class;
        String expected = "java.lang.String";
        String actual = resolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.String";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType expected = mapper.constructType(String.class);
        JavaType actual = resolver.typeFromId(context, id);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTypeFromIdUnknown() throws IOException {
        String id = "com.example.UnknownClass";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType actual = resolver.typeFromId(context, id);
        assertNull(actual);
    }
    
    @Test
    public void testTypeFromIdInvalid() throws IOException {
        String id = "com.example.InvalidClass";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        try {
            resolver.typeFromId(context, id);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id 'com.example.InvalidClass' (for id type 'Id.class'): null", e.getMessage());
        }
    }
    
    @Test
    public void testTypeFromIdCollections() throws IOException {
        String id = "java.util.ArrayList";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType expected = mapper.constructCollectionType(ArrayList.class, Object.class);
        JavaType actual = resolver.typeFromId(context, id);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTypeFromIdEnums() throws IOException {
        String id = "java.util.EnumSet";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType expected = mapper.constructCollectionType(EnumSet.class, Object.class);
        JavaType actual = resolver.typeFromId(context, id);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTypeFromIdEnumMap() throws IOException {
        String id = "java.util.EnumMap";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType expected = mapper.constructMapType(EnumMap.class, Object.class, Object.class);
        JavaType actual = resolver.typeFromId(context, id);
        assertEquals(expected, actual);
    }
       
    @Test
    public void testTypeFromIdInnerClass() throws IOException {
        String id = "com.example.OuterClass$InnerClass";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType expected = mapper.constructType(OuterClass.InnerClass.class);
        JavaType actual = resolver.typeFromId(context, id);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTypeFromIdNonStaticInnerClass() throws IOException {
        String id = "com.example.OuterClass$1";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType expected = mapper.constructType(OuterClass.class);
        JavaType actual = resolver.typeFromId(context, id);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIdFromValueAndTypeEnum() {
        Object value = TestEnum.VALUE1;
        Class<?> type = TestEnum.class;
        String expected = TestEnum.class.getName();
        String actual = resolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIdFromValueAndTypeSubClass() {
        Object value = new SubClass();
        Class<?> type = SubClass.class;
        String expected = SubClass.class.getName();
        String actual = resolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDescForKnownTypeIds() {
        String expected = "class name used as type id";
        String actual = resolver.getDescForKnownTypeIds();
        assertEquals(expected, actual);
    }
    
    private enum TestEnum {
        VALUE1,
        VALUE2
    }
    
    private static class OuterClass {
        
        private static class InnerClass {
        
        }
    }
    
    private static class SubClass extends OuterClass {
    
    }
}