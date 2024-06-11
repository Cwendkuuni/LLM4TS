package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databinding.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer deserializer;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private DeserializationContext context;
    private JsonParser parser;
    private JsonDeserializer<Object> subDeserializer;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "type", true, null);
        context = mock(DeserializationContext.class);
        parser = mock(JsonParser.class);
        subDeserializer = mock(JsonDeserializer.class);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer result = deserializer.forProperty(prop);

        assertNotNull(result);
        assertSame(deserializer, result);
        assertSame(prop, result.getProperty());
    }

    @Test
    public void testGetTypeInclusion() {
        assertSame(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObjectWithWrongToken() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        deserializer.deserializeTypedFromObject(parser, context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObjectWithWrongFieldNameToken() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);

        deserializer.deserializeTypedFromObject(parser, context);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getText()).thenReturn("type");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(subDeserializer);
        when(subDeserializer.deserialize(parser, context)).thenReturn("value");
        when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromObject(parser, context);

        assertEquals("value", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromArrayWithWrongToken() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

        deserializer.deserializeTypedFromArray(parser, context);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getText()).thenReturn("type");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(subDeserializer);
        when(subDeserializer.deserialize(parser, context)).thenReturn("value");
        when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromArray(parser, context);

        assertEquals("value", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromScalarWithWrongToken() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);

        deserializer.deserializeTypedFromScalar(parser, context);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getText()).thenReturn("type");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(subDeserializer);
        when(subDeserializer.deserialize(parser, context)).thenReturn("value");
        when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromScalar(parser, context);

        assertEquals("value", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromAnyWithWrongToken() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.END_OBJECT);

        deserializer.deserializeTypedFromAny(parser, context);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getText()).thenReturn("type");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(subDeserializer);
        when(subDeserializer.deserialize(parser, context)).thenReturn("value");
        when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromAny(parser, context);

        assertEquals("value", result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        when(parser.getTypeId()).thenReturn("typeId");
        when(idResolver.idFromBaseType()).thenReturn("resolvedTypeId");
        when(idResolver.typeFromId(any(), any())).thenReturn(baseType);
        when(context.getTypeFactory()).thenReturn(mock(TypeFactory.class));
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(subDeserializer);
        when(subDeserializer.deserialize(parser, context)).thenReturn("value");

        Object result = deserializer.deserializeWithNativeTypeId(parser, context, "typeId");

        assertEquals("value", result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWhenTypeIdIsNull() throws IOException {
        when(parser.getTypeId()).thenReturn(null);
        when(subDeserializer.deserialize(parser, context)).thenReturn("value");

        Object result = deserializer.deserializeWithNativeTypeId(parser, context, "typeId");

        assertEquals("value", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWhenSubDeserializerIsNull() throws IOException {
        when(parser.getTypeId()).thenReturn("typeId");
        when(idResolver.idFromBaseType()).thenReturn("resolvedTypeId");
        when(idResolver.typeFromId(any(), any())).thenReturn(baseType);
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(null);

        deserializer.deserializeWithNativeTypeId(parser, context, "typeId");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWhenTypeIsNull() throws IOException {
        when(parser.getTypeId()).thenReturn("typeId");
        when(idResolver.idFromBaseType()).thenReturn("resolvedTypeId");
        when(idResolver.typeFromId(any(), any())).thenReturn(null);

        deserializer.deserializeWithNativeTypeId(parser, context, "typeId");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWhenTypeCannotBeResolved() throws IOException {
        when(parser.getTypeId()).thenReturn("typeId");
        when(idResolver.idFromBaseType()).thenReturn("resolvedTypeId");
        when(idResolver.typeFromId(any(), any())).thenThrow(new IllegalArgumentException("Type cannot be resolved"));

        deserializer.deserializeWithNativeTypeId(parser, context, "typeId");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWhenTypeCannotBeResolvedAndTypeIdVisibilityIsFalse() throws IOException {
        deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "type", false, null);

        when(parser.getTypeId()).thenReturn("typeId");
        when(idResolver.idFromBaseType()).thenReturn("resolvedTypeId");
        when(idResolver.typeFromId(any(), any())).thenThrow(new IllegalArgumentException("Type cannot be resolved"));

        deserializer.deserializeWithNativeTypeId(parser, context, "typeId");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWhenNextFieldNameTokenIsInvalid() throws IOException {
        when(parser.getTypeId()).thenReturn("typeId");
        when(idResolver.idFromBaseType()).thenReturn("resolvedTypeId");
        when(idResolver.typeFromId(any(), any())).thenReturn(baseType);
        when(context.getTypeFactory()).thenReturn(mock(TypeFactory.class));
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(subDeserializer);
        when(subDeserializer.deserialize(parser, context)).thenReturn("value");
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);

        deserializer.deserializeWithNativeTypeId(parser, context, "typeId");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWhenWrongToken() throws IOException {
        when(parser.getTypeId()).thenReturn("typeId");
        when(idResolver.idFromBaseType()).thenReturn("resolvedTypeId");
        when(idResolver.typeFromId(any(), any())).thenReturn(baseType);
        when(context.getTypeFactory()).thenReturn(mock(TypeFactory.class));
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(subDeserializer);
        when(subDeserializer.deserialize(parser, context)).thenReturn("value");
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        deserializer.deserializeWithNativeTypeId(parser, context, "typeId");
    }
}