package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext context;

    @Mock
    private JsonParser parser;

    @Mock
    private TypeIdResolver typeIdResolver;

    @InjectMocks
    private TypeDeserializerBase typeDeserializerBase;

    private JavaType baseType;
    private BeanProperty property;

    @Before
    public void setUp() throws Exception {
        baseType = mock(JavaType.class);
        property = mock(BeanProperty.class);
        when(typeDeserializerBase.forProperty(property)).thenReturn(typeDeserializerBase);
        when(typeIdResolver.typeFromId(context, "typeId")).thenReturn(baseType);
        when(context.findContextualValueDeserializer(baseType, property)).thenReturn(mock(JsonDeserializer.class));
    }

    @Test
    public void testForProperty() throws JsonProcessingException {
        TypeDeserializer result = typeDeserializerBase.forProperty(property);
        assertNotNull(result);
        assertEquals(TypeDeserializerBase.class, result.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As result = typeDeserializerBase.getTypeInclusion();
        assertNull(result);
    }

    @Test
    public void testBaseTypeName() {
        String result = typeDeserializerBase.baseTypeName();
        assertNotNull(result);
    }

    @Test
    public void testGetPropertyName() {
        String result = typeDeserializerBase.getPropertyName();
        assertNotNull(result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(result);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> result = typeDeserializerBase.getDefaultImpl();
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        String result = typeDeserializerBase.toString();
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindDeserializerNullContext() throws IOException {
        typeDeserializerBase._findDeserializer(null, "typeId");
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerMissingDeserializer() throws IOException {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        typeDeserializerBase._findDeserializer(context, "missingTypeId");
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerNullTypeId() throws IOException {
        typeDeserializerBase._findDeserializer(context, null);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnregisteredDeserializer() throws IOException {
        typeDeserializerBase._deserializers.put("registeredTypeId", mock(JsonDeserializer.class));
        typeDeserializerBase._findDeserializer(context, "unregisteredTypeId");
    }

    @Test(expected = IOException.class)
    public void testFindContextualValueDeserializerNullType() throws IOException {
        typeDeserializerBase._findContextualValueDeserializer(context, null);
    }

    @Test(expected = IOException.class)
    public void testFindContextualValueDeserializerNullProperty() throws IOException {
        typeDeserializerBase._findContextualValueDeserializer(context, baseType);
    }

    @Test
    public void testFindContextualValueDeserializerRegisteredDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        typeDeserializerBase._deserializers.put("registeredTypeId", deserializer);
        JsonDeserializer<Object> result = typeDeserializerBase._findContextualValueDeserializer(context, baseType);
        assertNotNull(result);
        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        typeDeserializerBase._findDefaultImplDeserializer(null);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerNullDefaultImpl() throws IOException {
        typeDeserializerBase._findDefaultImplDeserializer(context);
    }

    @Test
    public void testFindDefaultImplDeserializerRegisteredDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        synchronized (typeDeserializerBase._defaultImpl) {
            typeDeserializerBase._defaultImplDeserializer = deserializer;
        }
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNotNull(result);
        assertEquals(deserializer, result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullParser() throws IOException {
        typeDeserializerBase._deserializeWithNativeTypeId(null, context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        typeDeserializerBase._deserializeWithNativeTypeId(parser, context);
    }

    @Test
    public void testDeserializeWithNativeTypeIdRegisteredDeserializer() throws IOException {
        String typeId = "registeredTypeId";
        when(parser.getTypeId()).thenReturn(typeId);
        when(context.findContextualValueDeserializer(baseType, property)).thenReturn(mock(JsonDeserializer.class));
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, context);
        assertNotNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        typeDeserializerBase._handleUnknownTypeId(null, "extraDesc");
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullTypeId() throws IOException {
        typeDeserializerBase._handleUnknownTypeId(context, null);
    }

    @Test
    public void testHandleUnknownTypeIdRegisteredDeserializer() throws IOException {
        String typeId = "registeredTypeId";
        when(context.handleMissingTypeId(baseType, typeIdResolver)).thenReturn(baseType);
        Object result = typeDeserializerBase._handleUnknownTypeId(context, "extraDesc");
        assertNotNull(result);
        assertEquals(baseType, result);
    }

    @Test
    public void testHandleMissingTypeIdNullContext() throws IOException {
        typeDeserializerBase._handleMissingTypeId(null, "extraDesc");
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeIdNullTypeId() throws IOException {
        typeDeserializerBase._handleMissingTypeId(context, null);
    }

    @Test
    public void testHandleMissingTypeIdRegisteredType() throws IOException {
        when(context.handleMissingTypeId(baseType, typeIdResolver)).thenReturn(baseType);
        Object result = typeDeserializerBase._handleMissingTypeId(context, "extraDesc");
        assertNotNull(result);
        assertEquals(baseType, result);
    }
}