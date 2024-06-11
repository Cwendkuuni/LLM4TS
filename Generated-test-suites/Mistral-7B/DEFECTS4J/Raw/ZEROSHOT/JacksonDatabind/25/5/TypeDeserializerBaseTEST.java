package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Unit tests for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    private static final String TEST_CLASS_NAME = "com.example.TestClass";
    private static final String TEST_TYPE_ID = "testTypeId";
    private static final String TEST_TYPE_PROPERTY_NAME = "typePropertyName";

    private TypeDeserializerBase _testee;
    private DeserializationContext _context;
    private JsonParser _parser;

    @Before
    public void setUp() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(TEST_CLASS_NAME);
        TypeIdResolver idResolver = new StubTypeIdResolver();
        _testee = new TypeDeserializerBase(javaType, idResolver, TEST_TYPE_PROPERTY_NAME, false, null) {
            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
        _context = new TestDeserializationContext();
        _parser = _context.getFactory().createParser("{}");
    }

    @Test
    public void testForProperty() {
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializer deserializer = _testee.forProperty(property);
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(_testee.getClass(), deserializer.getClass());
    }

    @Test
    public void testTypeInclusion() {
        JsonTypeInfo.As typeInclusion = _testee.getTypeInclusion();
        Assert.assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = _testee.baseTypeName();
        Assert.assertEquals(TEST_CLASS_NAME, baseTypeName);
    }

    @Test
    public void testPropertyName() {
        String propertyName = _testee.getPropertyName();
        Assert.assertEquals(TEST_TYPE_PROPERTY_NAME, propertyName);
    }

    @Test
    public void testToString() {
        String toString = _testee.toString();
        Assert.assertEquals("[TypeDeserializerBase; base-type:" + TEST_CLASS_NAME + "; id-resolver:]", toString);
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        JsonDeserializer<Object> deserializer = _testee._findDeserializer(_context, null);
        Assert.assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerUnknownTypeId() throws IOException {
        String typeId = "unknownTypeId";
        _testee._deserializers.put(typeId, new StubJsonDeserializer<>(String.class));
        JsonDeserializer<Object> deserializer = _testee._findDeserializer(_context, typeId);
        Assert.assertEquals(StubJsonDeserializer.class, deserializer.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerUnknownTypeIdException() throws IOException {
        String typeId = "unknownTypeId";
        _testee._deserializers.put(typeId, null);
        _testee._findDeserializer(_context, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = _testee._findDefaultImplDeserializer(_context);
        Assert.assertNull(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNull() throws IOException {
        Object result = _testee._deserializeWithNativeTypeId(_parser, _context);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdString() throws IOException {
        _testee._deserializers.put(TEST_TYPE_ID, new StubJsonDeserializer<>(String.class));
        Object result = _testee._deserializeWithNativeTypeId(_parser, _context, TEST_TYPE_ID);
        Assert.assertEquals(String.class, result.getClass());
    }

    private static class StubTypeIdResolver implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }
    }

    private static class StubJsonDeserializer<T>
            extends JsonDeserializer<Object> {

        private final Class<T> _valueClass;

        StubJsonDeserializer(Class<T> valueClass) {
            _valueClass = valueClass;
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return (T) new Object();
        }

        @Override
        public Class<?> getValueClass() {
            return _valueClass;
        }
    }

    private static class TestDeserializationContext
            extends DeserializationContext {

        public TestDeserializationContext() {
            super(null, null, null, null);
        }

        @Override
        public JsonFactory getFactory() {
            return null;
        }

        @Override
        public DeserializationFeature[] getActiveFeatures() {
            return new DeserializationFeature[0];
        }

        @Override
        public JavaType findValueType(Class<?> type, JsonParser p, BeanProperty property)
                throws IOException {
            return null;
        }

        @Override
        public Object findValueWithTypeId(JsonParser p, String typeIdStr, DeserializationContext ctxt)
                throws IOException {
            return null;
        }

        @Override
        public Object findContextualValueDeserializer(JavaType type, BeanProperty property) {
            return null;
        }

        @Override
        public JsonDeserializer<?> findBeanDeserializer(Class<?> valueType, DeserializationContext ctxt)
                throws IOException {
            return null;
        }

        @Override
        public Object findValueWithTypeId(String typeId, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object findContextualValueDeserializer(Class<?> valueType) {
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findValueDeserializer(Class<T> valueType) {
            return null;
        }
    }
}