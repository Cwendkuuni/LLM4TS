package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    private static final String TEST_BASE_TYPE = "com.example.TestBaseType";
    private static final String TEST_SUB_TYPE1 = "com.example.TestSubType1";
    private static final String TEST_SUB_TYPE2 = "com.example.TestSubType2";
    private static final String TEST_DEFAULT_IMPL = "com.example.TestDefaultImpl";
    private static final String TEST_MISSING_ID = "missingTypeId";

    private TypeDeserializerBase typeDeserializer;
    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;

    @Before
    public void setup() throws Exception {
        JsonTypeInfo.As typeInclusion = JsonTypeInfo.As.EXISTING_PROPERTY;
        TypeIdResolver idResolver = new StubTypeIdResolver();
        JavaType baseType = objectMapper.getTypeFactory().constructType(TEST_BASE_TYPE);

        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return typeInclusion;
            }
        };

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonFactory = objectMapper.getFactory();
    }

    @Test
    public void testForProperty() throws IOException, JsonProcessingException {
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializer deserializer = typeDeserializer.forProperty(property);
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyWithNullProperty() {
        typeDeserializer.forProperty(null);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws IOException {
        assertEquals(TEST_BASE_TYPE, typeDeserializer.baseTypeName());
    }

    @Test
    public void testPropertyName() {
        assertEquals("typePropertyName", typeDeserializer.getPropertyName());
    }

    @Test
    public void testTypeIdResolver() {
        assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testDefaultImpl() throws IOException {
        assertEquals(null, typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        assertNull(typeDeserializer._findDeserializer(objectMapper.getDeserializationContext(), null));
    }

    @Test
    public void testFindDeserializerKnownTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSubtypes(new TestSubType1(), new TestSubType2());
        JsonTypeInfo.As typeInclusion = JsonTypeInfo.As.WRAP_ONLY;
        TypeIdResolver idResolver = mapper.getTypeIdResolver();

        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(mapper.getDeserializationContext(), TEST_SUB_TYPE1);
        assertNotNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnknownTypeId() throws IOException {
        typeDeserializer._findDeserializer(objectMapper.getDeserializationContext(), TEST_MISSING_ID);
    }

    @Test
    public void testFindDefaultImplDeserializerNull() throws IOException {
        assertNull(typeDeserializer._findDefaultImplDeserializer(objectMapper.getDeserializationContext()));
    }

    @Test
    public void testFindDefaultImplDeserializerNotNull() throws IOException {
        typeDeserializer._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        JsonParser parser = jsonFactory.getParser(new ByteArrayInputStream(new byte[0]));
        Object result = typeDeserializer._deserializeWithNativeTypeId(parser, objectMapper.getDeserializationContext());
        fail("Expected IOException but got: " + result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdKnownTypeId() throws IOException {
        String json = "{\"typePropertyName\": \"com.example.TestSubType1\"}";
        JsonParser parser = jsonFactory.getParser(new ByteArrayInputStream(json.getBytes()));
        Object result = typeDeserializer._deserializeWithNativeTypeId(parser, objectMapper.getDeserializationContext());
        assertTrue(result instanceof TestSubType1);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws IOException {
        String json = "{\"typePropertyName\": \"missingTypeId\"}";
        JsonParser parser = jsonFactory.getParser(new ByteArrayInputStream(json.getBytes()));
        typeDeserializer._deserializeWithNativeTypeId(parser, objectMapper.getDeserializationContext());
        fail("Expected IOException but got: null");
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "unknownTypeId";
        Object result = typeDeserializer._handleUnknownTypeId(objectMapper.getDeserializationContext(), typeId);
        assertNull(result);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        Object result = typeDeserializer._handleMissingTypeId(objectMapper.getDeserializationContext(), "missingTypeId");
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testFindContextualValueDeserializerNullContext() throws IOException {
        typeDeserializer.findContextualValueDeserializer(null, null);
    }

    @Test
    public void testFindContextualValueDeserializerNullType() throws IOException {
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerDeserializer(Object.class, deserializer);

        JsonDeserializer<Object> result = typeDeserializer.findContextualValueDeserializer(mapper.getDeserializationContext(), null);
        assertNull(result);
    }

    @Test
    public void testFindContextualValueDeserializerNotNull() throws IOException {
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerDeserializer(Object.class, deserializer);

        JsonDeserializer<Object> result = typeDeserializer.findContextualValueDeserializer(mapper.getDeserializationContext(), String.class);
        assertNotNull(result);
        assertEquals(deserializer, result);
    }

    private static class StubTypeIdResolver implements TypeIdResolver {
        @Override
        public TypeId idFromValue(DeserializationContext ctxt, Object value) {
            return null;
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
            if (TEST_SUB_TYPE1.equals(id)) {
                return objectMapper.getTypeFactory().constructType(TEST_SUB_TYPE1);
            } else if (TEST_SUB_TYPE2.equals(id)) {
                return objectMapper.getTypeFactory().constructType(TEST_SUB_TYPE2);
            } else if (TEST_DEFAULT_IMPL.equals(id)) {
                return objectMapper.getTypeFactory().constructType(TEST_DEFAULT_IMPL);
            }
            return null;
        }
    }

    private static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}