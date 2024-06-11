package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.util.Map;

public class TypeDeserializerBaseTest extends BaseTest {

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase<Object, String> TEST_INSTANCE = new TypeDeserializerBase<Object, String>(
            null,
            new TypeIdResolverBase() {
                @Override
                public TypeIdResolverBase getInstance() {
                    return this;
                }

                @Override
                public String getDescForKnownTypeIds() {
                    return "desc for known type ids";
                }
            },
            "typePropertyName",
            false,
            String.class) {
                @Override
                public TypeDeserializer forProperty(BeanProperty prop) {
                    return null;
                }
            };

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testTypeDeserializerBaseConstruction() {
        assertNotNull(TEST_INSTANCE);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testForProperty() {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase<Object, String> forPropertyInstance = TEST_INSTANCE.forProperty(property);
        assertNotNull(forPropertyInstance);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As inclusion = TEST_INSTANCE.getTypeInclusion();
        assertNotNull(inclusion);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testBaseTypeName() {
        String baseTypeName = TEST_INSTANCE.baseTypeName();
        assertNotNull(baseTypeName);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetPropertyName() {
        String propertyName = TEST_INSTANCE.getPropertyName();
        assertNotNull(propertyName);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = TEST_INSTANCE.getTypeIdResolver();
        assertNotNull(idResolver);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = TEST_INSTANCE.getDefaultImpl();
        assertNotNull(defaultImpl);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testFindDeserializerNullTypeId() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        JsonDeserializer<Object> deserializer = TEST_INSTANCE._findDeserializer(ctxt, null);
        assertNull(deserializer);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testFindDeserializerValidTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String typeId = "StringType";
        JsonDeserializer<Object> deserializer = TEST_INSTANCE._findDeserializer(ctxt, typeId);
        assertNotNull(deserializer);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = TEST_INSTANCE._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        JsonParser jp = createMinimalParser();
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        try {
            TEST_INSTANCE._deserializeWithNativeTypeId(jp, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testDeserializeWithNativeTypeIdValidTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser(newStringInput("{\"typeId\":\"StringType\"}"));
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = TEST_INSTANCE._deserializeWithNativeTypeId(jp, ctxt);
        assertNotNull(result);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testHandleUnknownTypeIdNullTypeIdResolver() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String typeId = null;
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        try {
            TEST_INSTANCE._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testHandleUnknownTypeIdValidTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String typeId = "UnknownTypeId";
        TypeIdResolver idResolver = new TypeIdResolverBase() {};
        JavaType baseType = null;
        try {
            TEST_INSTANCE._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}