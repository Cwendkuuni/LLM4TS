package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test class for TypeDeserializerBase
 */
@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private JsonParser jp;

    @Mock
    private JavaType typeIdResolver;

    @Mock
    private BeanProperty property;

    @Mock
    private JsonDeserializer<Object> jsonDeserializer;

    @InjectMocks
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        when(ctxt.findContextualValueDeserializer(any(JavaType.class), eq(property))).thenReturn(jsonDeserializer);
        when(typeIdResolver.typeFromId(ctxt, anyString())).thenReturn(mock(JavaType.class));
    }

    @Test
    public void testConstructorWithBaseTypeEmpty() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase",
                typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testConstructorWithBaseType() {
        JavaType baseType = mock(JavaType.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        assertEquals(baseType.getRawClass().getName(), typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testForProperty() {
        when(typeIdResolver.typeFromId(any(DeserializationContext.class), eq("typeId"))).thenReturn(mock(JavaType.class));
        typeDeserializerBase = new TypeDeserializerBase(mock(JavaType.class), typeIdResolver, "typePropertyName", false, null);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        verify(typeDeserializerBase)._deserializers.put("typeId", jsonDeserializer);
        assertEquals(typeDeserializerBase, typeDeserializer);
    }

    @Test(expected = IOException.class)
    public void testGetTypeInclusionWhenNull() {
        typeDeserializerBase = new TypeDeserializerBase(mock(JavaType.class), null, null, false, null);
        typeDeserializerBase.getTypeInclusion();
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        when(typeIdResolver.typeFromId(ctxt, anyString())).thenReturn(null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        when(typeIdResolver.typeFromId(ctxt, "typeId")).thenReturn(mock(JavaType.class));
        deserializer = typeDeserializerBase._findDeserializer(ctxt, "typeId");
        verify(ctxt).findContextualValueDeserializer(mock(JavaType.class), property);
        assertEquals(jsonDeserializer, deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerWhenNullDefaultImpl() throws IOException {
        typeDeserializerBase._findDefaultImplDeserializer(ctxt);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerWithNullType() throws IOException {
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        verify(ctxt).findContextualValueDeserializer(mock(JavaType.class), property);
        verify(ctxt).findContextualValueDeserializer(eq(Void.TYPE), property);
        verify(typeDeserializerBase)._deserializers.put(eq("java.lang.Void"), NullifyingDeserializer.instance);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        JavaType defaultImpl = mock(JavaType.class);
        when(defaultImpl.getRawClass()).thenReturn(Object.class);
        typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        verify(ctxt).findContextualValueDeserializer(defaultImpl, property);
    }

    @Test
    public void testHandleUnknownTypeIdWithUnknownTypeId() throws IOException {
        when(typeIdResolver.typeFromId(ctxt, "unknownTypeId")).thenReturn(null);
        typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId", typeIdResolver, mock(JavaType.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdWithNullTypeIdResolver() throws IOException {
        typeDeserializerBase._handleUnknownTypeId(ctxt, null, null, mock(JavaType.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdWithNullBaseType() throws IOException {
        typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId", null, mock(JavaType.class));
    }

    @Test
    public void testToString() {
        String expected = "[" + TypeDeserializerBase.class.getName() + "; base-type:java.lang.String; id-resolver:class com.fasterxml.jackson.databind.TypeIdResolver]";
        assertEquals(expected, typeDeserializerBase.toString());
    }
}