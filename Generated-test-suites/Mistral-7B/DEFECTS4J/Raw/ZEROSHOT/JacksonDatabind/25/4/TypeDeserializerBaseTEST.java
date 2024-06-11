package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * This class contains test methods for all methods in TypeDeserializerBase class.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstruction() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertNotNull(typeDeserializer);
        assertEquals(String.class, typeDeserializer.baseType());
        assertEquals(typePropertyName, typeDeserializer.getPropertyName());
        assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
        assertEquals(typeIdVisible, typeDeserializer.isTypeIdVisible());
        assertNull(typeDeserializer.getProperty());
    }

    @Test
    public void testTypeDeserializerBaseForProperty() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        TypeDeserializerBase forProperty = typeDeserializer.forProperty(property);
        assertNotNull(forProperty);
        assertEquals(String.class, forProperty.baseType());
        assertEquals(typePropertyName, forProperty.getPropertyName());
        assertEquals(idResolver, forProperty.getTypeIdResolver());
        assertEquals(typeIdVisible, forProperty.isTypeIdVisible());
        assertEquals(property, forProperty.getProperty());
    }

    @Test
    public void testGetTypeInclusion() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        JsonTypeInfo.As include = typeDeserializer.getTypeInclusion();
        assertNull(include);
    }

    @Test
    public void testBaseTypeName() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals("class java.lang.String", typeDeserializer.baseTypeName());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Map.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = new TestingDeserializationContext();
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(context, typeId);
        assertNotNull(deserializer);
        // This test assumes that there is a JsonDeserializer for Map registered in TestingDeserializationContext
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnknownTypeId() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = new TestingDeserializationContext();
        String typeId = "unknownTypeId";
        typeDeserializer._findDeserializer(context, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = new TestingDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(context);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullifyingDeserializer() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = new TestingDeserializationContext();
        context.enable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
        context.enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(context);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Map.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{\"typeId\":\"HashMap\",\"data\":[1,2,3]}");
        DeserializationContext context = new TestingDeserializationContext();
        Object result = typeDeserializer._deserializeWithNativeTypeId(parser, context);
        // This test assumes that there is a JsonDeserializer for Map registered in TestingDeserializationContext
        assertTrue(result instanceof Map);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Map.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{\"typeId\":\"UnknownTypeId\",\"data\":[1,2,3]}");
        DeserializationContext context = new TestingDeserializationContext();
        Object result = typeDeserializer._deserializeWithNativeTypeId(parser, context);
        fail("Should have thrown an exception");
    }

    /**
     * Helper class for testing.
     */
    static class TestingDeserializationContext extends DeserializationContext {
        public TestingDeserializationContext() {
            super(new TestingAnnotationIntrospector(), new ObjectMapper());
            this.setContextInitializer(new DeserializationConfig.Feature[]{DeserializationFeature.FAIL_ON_EMPTY_BEANS, DeserializationFeature.FAIL_ON_INVALID_SUBTYPE});
        }

        @Override
        public Object instanceCreationHandler() {
            return new Object() {};
        }
    }
}