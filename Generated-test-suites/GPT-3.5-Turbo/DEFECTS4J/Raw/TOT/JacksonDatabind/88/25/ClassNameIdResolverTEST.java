
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

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
        // Testing that the method does nothing
        classNameIdResolver.registerSubtype(ClassNameIdResolverTest.class, "subtype");
    }
    
    @Test
    public void testIdFromValue() {
        // Testing with a basic class
        String expected = "java.lang.String";
        Object value = "test";
        String actual = classNameIdResolver.idFromValue(value);
        assertEquals(expected, actual);
        
        // Testing with an enum
        expected = "java.lang.Enum";
        value = TestEnum.VALUE1;
        actual = classNameIdResolver.idFromValue(value);
        assertEquals(expected, actual);
        
        // Testing with an enum sub-class
        expected = "java.lang.Enum";
        value = TestSubEnum.VALUE1;
        actual = classNameIdResolver.idFromValue(value);
        assertEquals(expected, actual);
        
        // Testing with an enum set
        expected = "java.util.EnumSet<com.fasterxml.jackson.databind.jsontype.impl.TestEnum>";
        value = EnumSet.of(TestEnum.VALUE1, TestEnum.VALUE2);
        actual = classNameIdResolver.idFromValue(value);
        assertEquals(expected, actual);
        
        // Testing with an enum map
        expected = "java.util.EnumMap<com.fasterxml.jackson.databind.jsontype.impl.TestEnum,java.lang.Object>";
        value = new EnumMap<>(TestEnum.class);
        actual = classNameIdResolver.idFromValue(value);
        assertEquals(expected, actual);
        
        // Testing with a java.util.ArrayList
        expected = "java.util.ArrayList";
        value = new ArrayList<>();
        actual = classNameIdResolver.idFromValue(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIdFromValueAndType() {
        // Testing with a basic class
        String expected = "java.lang.String";
        Object value = "test";
        Class<?> type = String.class;
        String actual = classNameIdResolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
        
        // Testing with an enum
        expected = "java.lang.Enum";
        value = TestEnum.VALUE1;
        type = TestEnum.class;
        actual = classNameIdResolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
        
        // Testing with an enum sub-class
        expected = "java.lang.Enum";
        value = TestSubEnum.VALUE1;
        type = TestSubEnum.class;
        actual = classNameIdResolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
        
        // Testing with an enum set
        expected = "java.util.EnumSet<com.fasterxml.jackson.databind.jsontype.impl.TestEnum>";
        value = EnumSet.of(TestEnum.VALUE1, TestEnum.VALUE2);
        type = EnumSet.class;
        actual = classNameIdResolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
        
        // Testing with an enum map
        expected = "java.util.EnumMap<com.fasterxml.jackson.databind.jsontype.impl.TestEnum,java.lang.Object>";
        value = new EnumMap<>(TestEnum.class);
        type = EnumMap.class;
        actual = classNameIdResolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
        
        // Testing with a java.util.ArrayList
        expected = "java.util.ArrayList";
        value = new ArrayList<>();
        type = ArrayList.class;
        actual = classNameIdResolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new DefaultDeserializationContext.Impl(null, null);
        
        // Testing with a valid class name
        String id = "java.lang.String";
        JavaType expected = TypeFactory.defaultInstance().constructType(String.class);
        JavaType actual = classNameIdResolver.typeFromId(context, id);
        assertEquals(expected, actual);
        
        // Testing with an invalid class name
        id = "com.example.InvalidClassName";
        assertNull(classNameIdResolver.typeFromId(context, id));
    }
    
    @Test
    public void test_typeFromId_shouldThrowIOException_whenContextIsNotDeserializationContext() throws IOException {
        DatabindContext context = new DefaultSerializationContext.Impl(null, null);
        String id = "java.lang.String";
        
        try {
            classNameIdResolver.typeFromId(context, id);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid type id 'java.lang.String' (for id type 'Id.class'): no such class found", e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_shouldThrowIllegalArgumentException_whenInvalidTypeIdIsProvided() throws IOException {
        DatabindContext context = new DefaultDeserializationContext.Impl(null, null);
        String id = "InvalidTypeId";

        try {
            classNameIdResolver.typeFromId(context, id);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id 'InvalidTypeId' (for id type 'Id.class'): null", e.getMessage());
        }
    }
    
    @Test
    public void test_typeFromId_shouldThrowIllegalArgumentException_whenTypeFactoryThrowsException() throws IOException {
        DatabindContext context = new DefaultDeserializationContext.Impl(null, null);
        String id = "java.lang.String";
        
        try {
            classNameIdResolver.typeFromId(context, id);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id 'java.lang.String' (for id type 'Id.class'): null", e.getMessage());
        }
    }
    
    @Test
    public void test_typeFromId_shouldThrowIOException_whenTypeFactoryThrowsClassNotFoundException() throws IOException {
        DatabindContext context = new DefaultDeserializationContext.Impl(null, null);
        String id = "java.lang.InvalidClass";
        
        try {
            classNameIdResolver.typeFromId(context, id);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid type id 'java.lang.InvalidClass' (for id type 'Id.class'): no such class found", e.getMessage());
        }
    }
    
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
    
    private static enum TestSubEnum {
        VALUE1, VALUE2
    }
}
