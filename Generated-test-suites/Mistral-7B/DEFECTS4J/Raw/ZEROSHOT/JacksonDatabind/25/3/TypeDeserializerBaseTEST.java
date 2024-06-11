package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, String.class);
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = new BeanProperty("propertyName", String.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(forProperty);
        assertTrue(forProperty instanceof TypeDeserializerBase);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals(String.class.getName(), baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("typePropertyName", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertEquals(String.class, defaultImpl.getName());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = objectMapper.getFactory().getPolymorphicTypeIdResolver();
        JsonDeserializer<Object> jsonDeserializer = objectMapper.findValueDeserializer(String[].class, null);

        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        String typeId = "com.example.MyArray";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof JsonDeserializer);
        assertEquals(JsonDeserializer.class, deserializer.getClass());
        assertEquals(jsonDeserializer, deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithInvalidTypeId() throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = objectMapper.getFactory().getPolymorphicTypeIdResolver();
        JsonDeserializer<Object> jsonDeserializer = objectMapper.findValueDeserializer(String[].class, null);

        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        String typeId = "com.example.MyInvalidTypeId";
        typeDeserializerBase._findDeserializer(ctxt, typeId);
        fail("Expected IOException");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = objectMapper.getFactory().getPolymorphicTypeIdResolver();

        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(defaultImplDeserializer);
        assertTrue(defaultImplDeserializer instanceof NullifyingDeserializer);
        assertEquals(NullifyingDeserializer.class, defaultImplDeserializer.getClass());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = objectMapper.getFactory().getPolymorphicTypeIdResolver();

        JsonParser jsonParser = objectMapper.getFactory().createParser("\"[1, 2, 3]\"");
        jsonParser.setCodec(objectMapper);
        jsonParser.setTypeIdHandler(idResolver);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), JsonTypeId.ID_ARRAY);
        assertEquals(Object[].class, result.getClass());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidNativeTypeId() throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = objectMapper.getFactory().getPolymorphicTypeIdResolver();

        JsonParser jsonParser = objectMapper.getFactory().createParser("\"[1, 2, 3]\"");
        jsonParser.setCodec(objectMapper);
        jsonParser.setTypeIdHandler(idResolver);

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), "InvalidTypeId");
        fail("Expected IOException");
    }

    @Test
    public void testHandleUnknownTypeIdWithDefaultTypeIdResolver() throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = objectMapper.getFactory().getPolymorphicTypeIdResolver();

        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        String typeId = "com.example.MyUnknownTypeId";
        Object result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, Object.class);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithCustomTypeIdResolver() throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = new CustomTypeIdResolver();

        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        String typeId = "com.example.MyUnknownTypeId";
        Object result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, Object.class);
        fail("Expected IOException");
    }
}

class CustomTypeIdResolver extends TypeIdResolverBase {
    @Override
    public Map<String, TypeId> getTypeIds() {
        return new HashMap<>();
    }
}