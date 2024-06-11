package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private DeserializationContext mockDeserializationContext;
    private JsonParser mockJsonParser;
    private JavaType mockJavaType;
    private TypeIdResolver mockResolver;

    @Before
    public void setup() {
        mockDeserializationContext = mock(DeserializationContext.class);
        mockJsonParser = mock(JsonParser.class);
        mockJavaType = mock(JavaType.class);
        mockResolver = mock(TypeIdResolver.class);

        typeDeserializerBase = new TypeDeserializerBase(mockJavaType, mockResolver, "", false, null) {
            @Override
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        when(mockJavaType.getRawClass()).thenReturn(String.class);
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(mockResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest$1; base-type:null; id-resolver: null]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializerSuccess() throws IOException {
        String typeId = "typeId";
        JsonDeserializer<Object> mockDeserializer = mock(JsonDeserializer.class);

        Map<String, JsonDeserializer<Object>> mockDeserializers = new ConcurrentHashMap<>();
        mockDeserializers.put(typeId, mockDeserializer);

        typeDeserializerBase._deserializers = mockDeserializers;

        when(mockJsonParser.getTypeId()).thenReturn(typeId);
        when(mockResolver.typeFromId(mockDeserializationContext, typeId)).thenReturn(mock(JavaType.class));

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(mockDeserializationContext, typeId);

        assertEquals(mockDeserializer, result);
    }

    @Test
    public void testFindDeserializerWithoutType() throws IOException {
        String typeId = null;
        JsonDeserializer<Object> mockDeserializer = mock(JsonDeserializer.class);

        typeDeserializerBase._defaultImplDeserializer = mockDeserializer;

        when(mockJsonParser.getTypeId()).thenReturn(typeId);
        when(mockDeserializationContext.isEnabled(any())).thenReturn(true);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(mockDeserializationContext, typeId);

        assertEquals(mockDeserializer, result);
    }

    @Test
    public void testFindDeserializerWithTypeNotFound() throws IOException {
        String typeId = "typeId";

        typeDeserializerBase._defaultImpl = mock(JavaType.class);

        when(mockJsonParser.getTypeId()).thenReturn(typeId);
        when(mockResolver.typeFromId(mockDeserializationContext, typeId)).thenReturn(null);
        when(mockDeserializationContext.isEnabled(any())).thenReturn(true);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(mockDeserializationContext, typeId);

        assertNull(result);
    }

    @Test
    public void testFindDeserializerWithException() throws IOException {
        String typeId = "typeId";
        JsonDeserializer<Object> mockDeserializer = mock(JsonDeserializer.class);

        typeDeserializerBase._defaultImplDeserializer = mockDeserializer;

        when(mockJsonParser.getTypeId()).thenReturn(typeId);
        when(mockDeserializationContext.isEnabled(any())).thenReturn(true);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(mockDeserializationContext, typeId);

        assertEquals(mockDeserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(mockDeserializationContext));
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithoutTypeId() throws IOException {
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext));
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWithNullDefaultImpl() throws IOException {
        typeDeserializerBase._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext, "typeId");
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        assertNull(typeDeserializerBase._handleUnknownTypeId(mockDeserializationContext, "typeId", mockResolver, mockJavaType));
    }
}