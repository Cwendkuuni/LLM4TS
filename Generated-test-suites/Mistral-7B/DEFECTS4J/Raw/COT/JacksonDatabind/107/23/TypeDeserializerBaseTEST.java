package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class TypeDeserializerBaseTest extends TestBase {

    private static final Class<TypeDeserializerBase> TYPE_DESERIALIZER_BASE = TypeDeserializerBase.class;

    private TypeDeserializerBase _typeDeserializerBase;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        _typeDeserializerBase = new TypeDeserializerBase(mapper.getTypeFactory().constructType(TYPE_DESERIALIZER_BASE), idResolver, "typePropertyName", true, null);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty beanProperty = new BeanProperty("testProperty", String.class);
        TypeDeserializer typeDeserializer = _typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As typeInclusion = _typeDeserializerBase.getTypeInclusion();
        assertNotNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String baseTypeName = _typeDeserializerBase.baseTypeName();
        assertNotNull(baseTypeName);
    }

    @Test
    public void testPropertyName() throws Exception {
        String propertyName = _typeDeserializerBase.getPropertyName();
        assertEquals("typePropertyName", propertyName);
    }

    @Test
    public void testgetTypeIdResolver() throws Exception {
        TypeIdResolver typeIdResolver = _typeDeserializerBase.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testDefaultImpl() throws Exception {
        Class<?> defaultImpl = _typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testFindDeserializer_NullTypeId() throws Exception {
        DeserializationContext context = new TestContext();
        assertNull(_typeDeserializerBase._findDeserializer(context, null));
    }

    @Test
    public void testFindDeserializer_TypeId() throws Exception {
        DeserializationContext context = new TestContext();
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        _typeDeserializerBase._deserializers.put("stubTypeId", deserializer);

        String typeId = "stubTypeId";
        JsonDeserializer<Object> foundDeserializer = _typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(deserializer, foundDeserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializer_UnknownTypeId() throws Exception {
        DeserializationContext context = new TestContext();
        _typeDeserializerBase._handleUnknownTypeId(context, "unknownTypeId");
    }

    @Test
    public void testFindDefaultImplDeserializer_NullContext() throws Exception {
        assertNull(_typeDeserializerBase._findDefaultImplDeserializer(null));
    }

    @Test
    public void testFindDefaultImplDeserializer_Context() throws Exception {
        DeserializationContext context = new TestContext();
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        _typeDeserializerBase._defaultImplDeserializer = deserializer;

        JsonDeserializer<Object> foundDeserializer = _typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(deserializer, foundDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullContext() throws Exception {
        Object result = _typeDeserializerBase._deserializeWithNativeTypeId(null, new TestContext(), null);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeId_UnknownTypeId() throws Exception {
        JsonParser jsonParser = createParser("");
        DeserializationContext context = new TestContext();
        _typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, "unknownTypeId");
    }

    @Test
    public void testDeserializeWithNativeTypeId_KnownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("{\"typeId\": \"knownTypeId\"}");
        DeserializationContext context = new TestContext();
        _typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, "knownTypeId");
        assertTrue(jsonParser.nextToken() == JsonToken.END_OBJECT);
        jsonParser.close();
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext context = new TestContext();
        Class<?> type = _typeDeserializerBase._handleUnknownTypeId(context, "unknownTypeId");
        assertNull(type);
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        DeserializationContext context = new TestContext();
        Class<?> type = _typeDeserializerBase._handleMissingTypeId(context, "");
        assertNull(type);
    }

    static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "stub";
        }
    }

    static class TestContext extends DeserializationContext {
        public TestContext() {
            super(new ObjectMapper(), new TestSerializerProvider(), new StandardNameTransformer(true));
            configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
    }

    static class TestSerializerProvider extends SerializerProvider {
        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type) {
            return null;
        }

        @Override
        public JsonSerializer<?> findFilterSerializer(Class<?> type, BeanProperty property) {
            return null;
        }

        @Override
        public JsonSerializer<?> findContentSerializer(Class<?> type) {
            return null;
        }

        @Override
        public JsonSerializer<?> findSerializer(Class<?> type) {
            return null;
        }

        @Override
        public JsonSerializer<?> findValueSerializer(TypeSerializer typeSerializer) {
            return null;
        }

        @Override
        public JsonSerializer<?> findFilterSerializer(Class<?> type, BeanProperty property, TypeSerializer valueSerializer) {
            return null;
        }

        @Override
        public JsonSerializer<?> findContentSerializer(TypeSerializer typeSerializer) {
            return null;
        }

        @Override
        public JsonSerializer<?> findSerializer(TypeSerializer typeSerializer) {
            return null;
        }
    }
}