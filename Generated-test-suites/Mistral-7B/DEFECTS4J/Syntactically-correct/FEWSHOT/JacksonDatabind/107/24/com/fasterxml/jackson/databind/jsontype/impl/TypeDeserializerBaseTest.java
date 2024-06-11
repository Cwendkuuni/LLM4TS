package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() {
        // Test default constructor
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null);
        assertNotNull(base);

        // Test constructor with parameters
        JavaType baseType = Object.class.getTypeName().getClass();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(baseType, typeDeserializer.baseType());
        assertEquals(idRes, typeDeserializer.getTypeIdResolver());
        assertEquals(typePropertyName, typeDeserializer.getPropertyName());
        assertEquals(defaultImpl, typeDeserializer.getDefaultImpl());
        assertEquals(typeIdVisible, typeDeserializer.isTypeIdVisible());
        assertNull(typeDeserializer.forProperty(null));
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = Object.class.getTypeName().getClass();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return String.class;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer deserializer = typeDeserializer.forProperty(property);
        assertEquals(String.class, deserializer.getType());
        assertEquals(idRes, deserializer.getTypeIdResolver());
        assertEquals(typePropertyName, deserializer.getPropertyName());
        assertEquals(typeDeserializer, deserializer.getTypeDeserializer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyNullProperty() throws Exception {
        JavaType baseType = Object.class.getTypeName().getClass();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return String.class;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        TypeDeserializer deserializer = typeDeserializer.forProperty(null);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                if ("StringDeserializer".equals(typeId)) {
                    return StringDeserializer.class;
                }
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(Object.class.getTypeName().getClass(), idRes, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(context, "StringDeserializer");
        assertEquals(StringDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializerNullTypeId() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(Object.class.getTypeName().getClass(), idRes, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(context, null);
        assertNull(deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class;
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(Object.class.getTypeName().getClass(), idRes, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType result = typeDeserializer._handleUnknownTypeId(context, "UnknownTypeId");
        assertNotNull(result);
        assertEquals(NullifyingDeserializer.class, result.getRawClass());
    }
}