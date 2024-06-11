package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_INSTANCE = new TestTypeDeserializerBase();

    private static class TestTypeDeserializerBase extends TypeDeserializerBase {

        public TestTypeDeserializerBase() {
            super(null, null, null, false, null);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            // no-op in this case
            return this;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXISTING_PROPERTY;
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                TypeIdResolver idResolver, JavaType baseType) throws IOException {
            throw new IOException("Unknown type id: " + typeId);
        }
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer deserializer = TEST_INSTANCE.forProperty(prop);
        assertEquals(TEST_INSTANCE.getClass(), deserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, TEST_INSTANCE.getTypeInclusion());
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(ctxt, "test-type-id")).thenReturn(null);
        TypeDeserializerBase base = new TestTypeDeserializerBase();
        base.handleUnknownTypeId(ctxt, "test-type-id", idResolver, null);
    }

    @Test
    public void testDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TestTypeDeserializerBase testBase = new TestTypeDeserializerBase();
        JsonDeserializer<Object> deserializer = testBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(ctxt, "test-type-id")).thenReturn(null);
        TestTypeDeserializerBase testBase = new TestTypeDeserializerBase();
        JsonDeserializer<Object> deserializer = testBase._findDeserializer(ctxt, "test-type-id");
        assertNull(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TestTypeDeserializerBase testBase = new TestTypeDeserializerBase();
        JsonDeserializer<Object> deserializer = testBase._findDefaultImplDeserializer(ctxt);
        when(testBase._deserializeWithNativeTypeId(jp, ctxt, "test-type-id")).thenReturn("test");
        Object result = testBase._deserializeWithNativeTypeId(jp, ctxt, "test-type-id");
        assertEquals("test", result);
    }
}