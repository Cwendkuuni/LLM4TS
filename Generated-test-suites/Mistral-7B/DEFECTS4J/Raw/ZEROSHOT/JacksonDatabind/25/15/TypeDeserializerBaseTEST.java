package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.test.BaseTest;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest extends BaseTest {

    @SuppressWarnings("unchecked")
    private static final TypeIdResolver idResolverMock = mock(TypeIdResolver.class);
    @SuppressWarnings("unchecked")
    private static final JavaType baseType = mock(JavaType.class);
    @SuppressWarnings("unchecked")
    private static final BeanProperty propertyMock = mock(BeanProperty.class);
    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase typeDeserializerBaseMock = mock(TypeDeserializerBase.class);
    @SuppressWarnings("unchecked")
    private static final JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);

    private static class DummyTypeDeserializer extends TypeDeserializerBase {
        DummyTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName,
                boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                throws IOException {
            return deserializerMock;
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                throws IOException {
            return null;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                throws IOException {
            return null;
        }

        @Override
        protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver,
                JavaType baseType) throws IOException {
            throw new IOException();
        }
    }

    @Before
    public void setUp() throws Exception {
        when(baseType.getTypeIdVersion()).thenReturn(Long.MIN_VALUE);
        when(idResolverMock.typeFromId(any(DeserializationContext.class), anyString())).thenCallRealMethod();
        when(idResolverMock.typeFromId(any(DeserializationContext.class), eq("CustomTypeId"))).thenReturn(any(JavaType.class));
        when(propertyMock.getName()).thenReturn("testPropertyName");
        when(propertyMock.getBeanClass()).thenReturn(Object.class);

        DummyTypeDeserializer typeDeserializer = new DummyTypeDeserializer(baseType, idResolverMock, "testPropertyName", true,
                Object.class);
        TypeDeserializerBase testObject = new DummyTypeDeserializer(typeDeserializer, propertyMock);
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializer forProperty = testObject.forProperty(propertyMock);
        assertEquals(TypeDeserializerBase.class, forProperty.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = testObject.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String baseTypeName = testObject.baseTypeName();
        assertNull(baseTypeName);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        String propertyName = testObject.getPropertyName();
        assertEquals("testPropertyName", propertyName);
    }

    @Test
    public void testTypeIdResolver() throws Exception {
        TypeIdResolver typeIdResolver = testObject.getTypeIdResolver();
        assertEquals(idResolverMock, typeIdResolver);
    }

    @Test
    public void testDefaultImpl() throws Exception {
        Class<?> defaultImpl = testObject.getDefaultImpl();
        assertEquals(Object.class, defaultImpl);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "CustomTypeId";
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.findContextualValueDeserializer(eq(Object.class), any(BeanProperty.class))).thenReturn(deserializerMock);

        JsonDeserializer<Object> deserializer = testObject._findDeserializer(ctxt, typeId);
        assertEquals(deserializerMock, deserializer);
        verify(idResolverMock).typeFromId(ctxt, typeId);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        testObject._findDefaultImplDeserializer(ctxt);
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = testObject._findDeserializer(ctxt, null);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(parser.getTypeId()).thenReturn("CustomTypeId");

        Object result = testObject._deserializeWithNativeTypeId(parser, ctxt, "CustomTypeId");
        verifyZeroInteractions(parser, ctxt, idResolverMock, deserializerMock);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "UnknownTypeId";
        testObject._handleUnknownTypeId(ctxt, typeId, idResolverMock, baseType);
    }

    @Test
    public void testFindDeserializerWithEmptyMap() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(idResolverMock.getDeserializers()).thenReturn(new HashMap<>());

        JsonDeserializer<Object> deserializer = testObject._findDeserializer(ctxt, "CustomTypeId");
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test
    public void testFindDeserializerWithDeserializerInMap() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(idResolverMock.getDeserializers()).thenReturn(map(String.class, JsonDeserializer.class));
        when(idResolverMock.typeFromId(ctxt, "CustomTypeId")).thenReturn(any(JavaType.class));

        JsonDeserializer<Object> deserializer = testObject._findDeserializer(ctxt, "CustomTypeId");
        assertEquals(deserializerMock, deserializer);
        verify(idResolverMock).typeFromId(ctxt, "CustomTypeId");
    }

    @Test
    public void testFindDeserializerWithNullDeserializerInMap() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(idResolverMock.getDeserializers()).thenReturn(map(String.class, null));

        JsonDeserializer<Object> deserializer = testObject._findDeserializer(ctxt, "CustomTypeId");
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    private Map<String, JsonDeserializer<Object>> map(Class<?> keyType, Class<?> valueType) {
        Map<String, JsonDeserializer<Object>> map = new HashMap<>();
        map.put("CustomTypeId", mock(JsonDeserializer.class));
        return map;
    }
}