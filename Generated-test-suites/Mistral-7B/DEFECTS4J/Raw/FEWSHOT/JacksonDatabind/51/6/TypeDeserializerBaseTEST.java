package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;

/**
 * Test cases for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final TypeIdResolver idResolverMock = mock(TypeIdResolver.class);
    @SuppressWarnings("unchecked")
    private static final DeserializationContext contextMock = mock(DeserializationContext.class);
    @SuppressWarnings("unchecked")
    private static final JsonParser parserMock = mock(JsonParser.class);

    @Test
    public void testFindDeserializer() throws IOException {
        when(contextMock.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class)))
                .thenReturn(mock(JsonDeserializer.class));
        when(idResolverMock.typeFromId(contextMock, "typeId1"))
                .thenReturn(mock(JavaType.class));

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, idResolverMock, "", false, null) {
                    @Override
                    public JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                            throws IOException {
                        return super._findDeserializer(ctxt, typeId);
                    }
                };

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(contextMock, "typeId1");
        assertSame(mock(JsonDeserializer.class), deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(contextMock.findContextualValueDeserializer(eq(_defaultImpl), any(BeanProperty.class)))
                .thenReturn(mock(JsonDeserializer.class));

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, idResolverMock, "", false, _defaultImpl) {
                    @Override
                    public JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                            throws IOException {
                        return super._findDefaultImplDeserializer(ctxt);
                    }
                };

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(contextMock);
        assertSame(mock(JsonDeserializer.class), deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        when(contextMock.handleUnknownTypeId(any(JavaType.class), anyString(), idResolverMock, anyString()))
                .thenThrow(JsonMappingException.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, idResolverMock, "", false, null) {
                    @Override
                    public JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                            TypeIdResolver idResolver, String extraInfo) throws IOException {
                        return super._handleUnknownTypeId(ctxt, typeId, idResolver, extraInfo);
                    }
                };

        try {
            typeDeserializerBase._handleUnknownTypeId(contextMock, "typeId", idResolverMock, "");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);

        when(contextMock.findContextualValueDeserializer(eq(_defaultImpl), any(BeanProperty.class)))
                .thenReturn(deserializerMock);
        when(parserMock.getTypeId()).thenReturn("typeId");

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, idResolverMock, "", false, null) {
                    @Override
                    public Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt)
                            throws IOException {
                        return super._deserializeWithNativeTypeId(jp, ctxt);
                    }
                };

        typeDeserializerBase._deserializeWithNativeTypeId(parserMock, contextMock);
        verify(deserializerMock).deserialize(parserMock, contextMock);
    }

    @Test
    public void testDeserializeWithNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        NullifyingDeserializer nullifyingDeserializerMock = mock(NullifyingDeserializer.class);

        when(contextMock.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(contextMock.findContextualValueDeserializer(eq(JavaType.valueOf(Void.TYPE)), any(BeanProperty.class)))
                .thenReturn(nullifyingDeserializerMock);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, idResolverMock, "", false, null) {
                    @Override
                    public Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt)
                            throws IOException {
                        return super._deserializeWithNativeTypeId(jp, ctxt);
                    }
                };

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parserMock, contextMock);
        assertNull(result);
    }
}