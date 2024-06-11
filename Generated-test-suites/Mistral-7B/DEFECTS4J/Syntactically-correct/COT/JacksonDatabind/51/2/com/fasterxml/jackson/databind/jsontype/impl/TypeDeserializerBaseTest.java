package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

public class TypeDeserializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_INSTANCE = new TypeDeserializerBase() {
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TestTypeDeserializer();
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXISTING_PROPERTY;
        }

        @Override
        protected Map<String, JsonDeserializer<Object>> _deserializers = new ConcurrentHashMap<>(16, 0.75f, 2);

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
            return null;
        }

        @Override
        protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
            return null;
        }
    };

    @SuppressWarnings("unchecked")
    private static final TestTypeDeserializer TEST_TYPE_DESERIALIZER = new TestTypeDeserializer();

    @SuppressWarnings("unchecked")
    private static final TypeIdResolver TEST_TYPE_ID_RESOLVER = new TypeIdResolver() {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        @Override
        public String idFromValue(DeserializationContext ctxt, Object value) {
            return null;
        }
    };

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testForProperty() throws Exception {
        TypeDeserializer actual = TEST_INSTANCE.forProperty(new BeanProperty.Standard("testProperty"));
        assertEquals(TestTypeDeserializer.class, actual.getClass());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As actual = TEST_INSTANCE.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, actual);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testBaseTypeName() throws Exception {
        String actual = TEST_INSTANCE.baseTypeName();
        fail("baseTypeName should not be called directly");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetPropertyName() throws Exception {
        String actual = TEST_INSTANCE.getPropertyName();
        fail("getPropertyName should not be called directly");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetTypeIdResolver() throws Exception {
        TypeIdResolver actual = TEST_INSTANCE.getTypeIdResolver();
        assertNotNull(actual);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetDefaultImpl() throws Exception {
        Class<?> actual = TEST_INSTANCE.getDefaultImpl();
        assertNull(actual);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testToString() throws Exception {
        String actual = TEST_INSTANCE.toString();
        fail("toString should not be called directly");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testFindDeserializer() throws Exception {
        DeserializationContext ctxt = new TestContext();
        JsonDeserializer<Object> actual = TEST_INSTANCE._findDeserializer(ctxt, "testTypeId");
        assertNull(actual);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext ctxt = new TestContext();
        JsonDeserializer<Object> actual = TEST_INSTANCE._findDefaultImplDeserializer(ctxt);
        assertNull(actual);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeId() throws Exception {
        JsonParser jp = new TestParser();
        Object actual = TEST_INSTANCE._deserializeWithNativeTypeId(jp, new TestContext(), null);
        fail("_deserializeWithNativeTypeId should not be called directly");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = new TestContext();
        JavaType actual = TEST_INSTANCE._handleUnknownTypeId(ctxt, "testTypeId", TEST_TYPE_ID_RESOLVER, TEST_INSTANCE._baseType);
        fail("_handleUnknownTypeId should not be called directly");
    }

    // Edge cases and exception handling tests:

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IllegalStateException.class)
    public void testForPropertyNull() throws Exception {
        TypeDeserializerBase instance = new TypeDeserializerBase() {
            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers = new ConcurrentHashMap<>(16, 0.75f, 2);

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        assertNull(instance.forProperty(null));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindDeserializerNullContext() throws Exception {
        TypeDeserializerBase instance = TEST_INSTANCE;
        instance._findDeserializer(null, "testTypeId");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindDeserializerNullTypeId() throws Exception {
        TypeDeserializerBase instance = TEST_INSTANCE;
        instance._findDeserializer(new TestContext(), null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullContext() throws Exception {
        TypeDeserializerBase instance = TEST_INSTANCE;
        instance._handleUnknownTypeId(null, "testTypeId", TEST_TYPE_ID_RESOLVER, TEST_INSTANCE._baseType);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullTypeId() throws Exception {
        TypeDeserializerBase instance = TEST_INSTANCE;
        instance._handleUnknownTypeId(new TestContext(), null, TEST_TYPE_ID_RESOLVER, TEST_INSTANCE._baseType);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullBaseType() throws Exception {
        TypeDeserializerBase instance = TEST_INSTANCE;
        instance._handleUnknownTypeId(new TestContext(), "testTypeId", TEST_TYPE_ID_RESOLVER, null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = NullPointerException.class)
    public void testFindDeserializerNullDeserializer() throws Exception {
        TypeDeserializerBase instance = TEST_INSTANCE;
        Map<String, JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<>(16, 0.75f, 2);
        instance._deserializers = deserializers;
        deserializers.put("testTypeId", null);
        instance._findDeserializer(new TestContext(), "testTypeId");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeIdUnknownTypeId() throws Exception {
        TypeDeserializerBase instance = TEST_INSTANCE;
        instance._handleUnknownTypeId(new TestContext(), "unknownTypeId", TEST_TYPE_ID_RESOLVER, TEST_INSTANCE._baseType);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public DeserializationConfig getConfig() {
            return new TestDeserializationConfig();
        }

        @Override
        public JsonFactory getFactory() {
            return new TestJsonFactory();
        }

        @Override
        public ObjectMapper getObjectMapper() {
            return new TestObjectMapper();
        }

        @Override
        public JsonParser _constructParser(StreamParser p, DefaultSerializerProvider prov, Object mc) throws IOException {
            return new TestParser();
        }

        @Override
        public Object _createContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
            return new TestContextualValueDeserializer();
        }
    }

    private static class TestDeserializationConfig extends DeserializationConfig {
        @Override
        public DeserializationFeature[] getActiveFeatures() {
            return new DeserializationFeature[0];
        }
    }

    private static class TestJsonFactory extends JsonFactory {
        @Override
        public JsonParser createParser(InputStream stream) throws IOException {
            return new TestParser();
        }
    }

    private static class TestObjectMapper extends ObjectMapper {
        // Empty implementation
    }

    private static class TestParser extends JsonParser {
        @Override
        public Object parseValue(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserialize(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public String getText(int len) {
            return getText();
        }

        @Override
        public int nextToken() throws IOException {
            return 0;
        }

        @Override
        public int nextValue() throws IOException {
            return 0;
        }

        @Override
        public void close() throws IOException {
            // Empty implementation
        }
    }

    private static class TestContextualValueDeserializer
            extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class TestTypeDeserializer extends TypeDeserializer {
        // Empty implementation
    }
}