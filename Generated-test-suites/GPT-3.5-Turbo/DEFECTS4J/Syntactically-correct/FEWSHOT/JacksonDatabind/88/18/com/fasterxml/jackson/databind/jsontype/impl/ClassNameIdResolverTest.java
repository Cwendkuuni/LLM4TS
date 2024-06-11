package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // No need to test this method as it is not used with class name - based resolvers
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        assertEquals(value.getClass().getName(), resolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> type = value.getClass();
        assertEquals(type.getName(), resolver.idFromValueAndType(value, type));
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());

        // Test with valid id
        String id = "java.util.ArrayList";
        JavaType expectedType = TypeFactory.defaultInstance().constructSpecializedType(null, java.util.ArrayList.class);
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getDeserializationContext();
        JavaType actualType = resolver.typeFromId(context, id);
        assertEquals(expectedType, actualType);

        // Test with invalid id
        id = "com.example.InvalidClass";
        actualType = resolver.typeFromId(context, id);
        assertNull(actualType);
    }

    @Test
    public void test_typeFromId_withUnknownTypeIdHandler() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());

        // Test with unknown type id handler
        String id = "com.example.UnknownClass";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        String expectedId = "no such class found";
        JavaType actualType = resolver._typeFromId(id, context);
        assertNotNull(actualType);
        assertEquals(baseType, actualType.getBindings().getTypeParameters().get(0));
        assertEquals(expectedId, actualType.getBindings().getTypeArguments().get(0).toCanonical());
    }

    @Test
    public void test_typeFromId_withClassNotFoundException() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());

        // Test with ClassNotFoundException
        String id = "com.example.UnknownClass";
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getDeserializationContext();
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType actualType = resolver._typeFromId(id, context);
        assertNull(actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_typeFromId_withException() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());

        // Test with IllegalArgumentException
        String id = "java.util.ArrayList<>";
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getDeserializationContext();
        resolver._typeFromId(id, context);
    }

    @Test
    public void test_idFrom() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());

        // Test with Enum
        Object value = MyEnum.VAL1;
        String expectedId = MyEnum.class.getName();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        String actualId = resolver._idFrom(value, cls, typeFactory);
        assertEquals(expectedId, actualId);

        // Test with subclass of Enum
        value = MySubEnum.VAL2;
        cls = value.getClass();
        actualId = resolver._idFrom(value, cls, typeFactory);
        assertEquals(expectedId, actualId);

        // Test with other classes
        value = new java.util.ArrayList<>();
        expectedId = java.util.ArrayList.class.getName();
        cls = value.getClass();
        actualId = resolver._idFrom(value, cls, typeFactory);
        assertEquals(expectedId, actualId);

        value = new EnumSet<MySubEnum>(MySubEnum.class);
        expectedId = typeFactory.constructCollectionType(EnumSet.class, MySubEnum.class).toCanonical();
        cls = value.getClass();
        actualId = resolver._idFrom(value, cls, typeFactory);
        assertEquals(expectedId, actualId);

        value = new EnumMap<MyEnum, MySubEnum>(MyEnum.class);
        expectedId = typeFactory.constructMapType(EnumMap.class, MyEnum.class, MySubEnum.class).toCanonical();
        cls = value.getClass();
        actualId = resolver._idFrom(value, cls, typeFactory);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }

    private enum MyEnum {
        VAL1, VAL2
    }

    private enum MySubEnum {
        VAL1, VAL2
    }
}