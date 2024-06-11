package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DefaultParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class TypeDeserializerBaseTest extends TestBase {

    private static final JavaType BASE_TYPE = TypeFactory.defaultInstance().constructType(String.class);
    private static final String TYPE_PROPERTY_NAME = "type";

    private static class TestTypeDeserializerBase extends TypeDeserializerBase {
        public TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idRes,
                String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            // Do nothing in this test case
            return this;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt,
                String typeId) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId)
                throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc)
                throws IOException {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void testEmptyConstruction() {
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", deserializer.getClass().getName());
        assertNull(deserializer._idResolver);
        assertSame(BASE_TYPE, deserializer._baseType);
        assertNull(deserializer._property);
        assertNull(deserializer._defaultImpl);
        assertNull(deserializer._typePropertyName);
        assertFalse(deserializer._typeIdVisible);
        assertNotNull(deserializer._deserializers);
        assertTrue(deserializer._deserializers.isEmpty());
        assertNull(deserializer._defaultImplDeserializer);
    }

    @Test
    public void testWithIdResolver() {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(any(DeserializationContext.class), anyString())).thenReturn(null);
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, idResolver, TYPE_PROPERTY_NAME, false, null);
        assertEquals(idResolver, deserializer._idResolver);
        assertNull(deserializer._idResolver.typeFromId(mock(DeserializationContext.class), "test"));
        assertSame(BASE_TYPE, deserializer._baseType);
        assertNull(deserializer._property);
        assertNull(deserializer._defaultImpl);
        assertNull(deserializer._typePropertyName);
        assertFalse(deserializer._typeIdVisible);
        assertNotNull(deserializer._deserializers);
        assertTrue(deserializer._deserializers.isEmpty());
        assertNull(deserializer._defaultImplDeserializer);
    }

    @Test
    public void testWithProperty() {
        BeanProperty property = mock(BeanProperty.class);
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        TestTypeDeserializerBase deserializer2 = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        deserializer._property = property;
        deserializer2._property = property;
        assertSame(deserializer, deserializer2);
        assertEquals(property, deserializer._property);
        assertNull(deserializer._idResolver);
        assertSame(BASE_TYPE, deserializer._baseType);
        assertNull(deserializer._defaultImpl);
        assertNull(deserializer._typePropertyName);
        assertFalse(deserializer._typeIdVisible);
        assertNotNull(deserializer._deserializers);
        assertTrue(deserializer._deserializers.isEmpty());
        assertNull(deserializer._defaultImplDeserializer);
    }

    @Test
    public void testWithDefaultImpl() {
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Integer.class);
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, defaultImpl);
        assertSame(defaultImpl, deserializer._defaultImpl);
        assertNull(deserializer._idResolver);
        assertSame(BASE_TYPE, deserializer._baseType);
        assertNull(deserializer._property);
        assertSame(defaultImpl, deserializer._defaultImpl);
        assertNull(deserializer._typePropertyName);
        assertFalse(deserializer._typeIdVisible);
        assertNotNull(deserializer._deserializers);
        assertTrue(deserializer._deserializers.isEmpty());
        assertNull(deserializer._defaultImplDeserializer);
    }

    @Test
    public void testWithTypeIdVisible() {
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, true, null);
        assertTrue(deserializer._typeIdVisible);
        assertNull(deserializer._idResolver);
        assertSame(BASE_TYPE, deserializer._baseType);
        assertNull(deserializer._property);
        assertNull(deserializer._defaultImpl);
        assertNull(deserializer._typePropertyName);
        assertNotNull(deserializer._deserializers);
        assertTrue(deserializer._deserializers.isEmpty());
        assertNull(deserializer._defaultImplDeserializer);
    }

    @Test
    public void testSerialization() throws IOException, JsonProcessingException {
        DefaultParser parser = new DefaultParser();
        ObjectMapper mapper = new ObjectMapper();
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserializer);
        JsonParser jp = parser.parse(json);
        Object readValue = jp.readValueAs(TestTypeDeserializerBase.class);
        assertEquals(deserializer, readValue);
    }

    @Test
    public void testDeserializationWithDefaultImpl() throws IOException {
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        JsonParser jp = new DefaultParser().parse("null");
        Object readValue = deserializer.deserialize(jp, null);
        assertNull(readValue);
    }

    @Test
    public void testDeserializationWithIdResolver() throws IOException {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(any(DeserializationContext.class), anyString())).thenReturn(null);
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, idResolver, TYPE_PROPERTY_NAME, false, null);
        JsonParser jp = new DefaultParser().parse("{\"type\": \"someType\"}");
        Object readValue = deserializer.deserialize(jp, null);
        assertNull(readValue);
    }

    @Test
    public void testDeserializationWithProperty() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        TestTypeDeserializerBase deserializer2 = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        deserializer._property = property;
        deserializer2._property = property;
        JsonParser jp = new DefaultParser().parse("{\"type\": \"someType\"}");
        Object readValue = deserializer.deserialize(jp, null);
        Object readValue2 = deserializer2.deserialize(jp, null);
        assertSame(readValue, readValue2);
        assertNull(readValue);
    }

    @Test
    public void testDeserializationWithDefaultImplDeserializer() throws IOException {
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        deserializer._defaultImplDeserializer = deserializerMock;
        JsonParser jp = new DefaultParser().parse("null");
        Object readValue = deserializer.deserialize(jp, null);
        verify(deserializerMock).deserialize(jp, null);
        assertNull(readValue);
    }

    @Test
    public void testDeserializationWithTypeId() throws IOException {
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        JsonParser jp = new DefaultParser().parse("{\"type\": \"someType\"}");
        Object readValue = deserializer.deserialize(jp, null);
        assertNull(readValue);
    }

    @Test
    public void testDeserializationWithNativeTypeId() throws IOException {
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        JsonParser jp = new DefaultParser().parse("{\"type\": \"String\"}");
        Object readValue = deserializer._deserializeWithNativeTypeId(jp, null, "String");
        assertNull(readValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindDeserializer() throws IOException {
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        deserializer._findDeserializer(mock(DeserializationContext.class), "test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindDefaultImplDeserializer() throws IOException {
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        deserializer._findDefaultImplDeserializer(mock(DeserializationContext.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeserializeWithNativeTypeId() throws IOException {
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        deserializer._deserializeWithNativeTypeId(mock(JsonParser.class), mock(DeserializationContext.class), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHandleUnknownTypeId() throws IOException {
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        deserializer._handleUnknownTypeId(mock(DeserializationContext.class), "test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHandleMissingTypeId() throws IOException {
        TestTypeDeserializerBase deserializer = new TestTypeDeserializerBase(BASE_TYPE, null, TYPE_PROPERTY_NAME, false, null);
        deserializer._handleMissingTypeId(mock(DeserializationContext.class), "test");
    }
}