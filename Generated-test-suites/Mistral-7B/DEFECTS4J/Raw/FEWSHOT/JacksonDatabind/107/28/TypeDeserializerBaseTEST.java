package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
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
import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private DeserializationContext ctxt;
    private JsonParser jp;

    @Before
    public void setUp() throws Exception {
        typeDeserializerBase = new TestTypeDeserializerBase(null, null, null, false, null);
        ctxt = mock(DeserializationContext.class);
        jp = mock(JsonParser.class);
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertInstanceOf(TypeDeserializer.class, typeDeserializer);
        assertEquals(typeDeserializerBase.getClass(), typeDeserializer.getClass());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        when(ctxt.findContextualValueDeserializer(eq(String.class), any())).thenReturn(mock(JsonDeserializer.class));
        Object deser = typeDeserializerBase._findDeserializer(ctxt, "test");
        assertInstanceOf(JsonDeserializer.class, deser);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(ctxt.findContextualValueDeserializer(eq(Void.class), any())).thenReturn(NullifyingDeserializer.instance);
        JsonDeserializer<Object> deser = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertInstanceOf(NullifyingDeserializer.class, deser);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        when(ctxt.handleMissingTypeId(any(), any(), anyString())).thenThrow(JsonMappingException.class);
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, "test");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        when(ctxt.handleMissingTypeId(any(), any(), anyString())).thenReturn(mock(JavaType.class));
        JavaType javaType = typeDeserializerBase._handleMissingTypeId(ctxt, "test");
        assertNotNull(javaType);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        when(ctxt.findContextualValueDeserializer(eq(String.class), any())).thenReturn(mock(JsonDeserializer.class));
        Object deser = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, "test");
        assertInstanceOf(JsonDeserializer.class, deser);
    }

    @Test
    public void testDeserializeWithNullNativeTypeId() throws IOException {
        when(ctxt.findContextualValueDeserializer(eq(Void.class), any())).thenReturn(NullifyingDeserializer.instance);
        Object deser = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, null);
        assertInstanceOf(NullifyingDeserializer.class, deser);
    }

    static class TestTypeDeserializerBase extends TypeDeserializerBase {
        public TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idRes,
                String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        protected TestTypeDeserializerBase(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TestTypeDeserializerBase(this, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXISTING_PROPERTY;
        }
    }
}