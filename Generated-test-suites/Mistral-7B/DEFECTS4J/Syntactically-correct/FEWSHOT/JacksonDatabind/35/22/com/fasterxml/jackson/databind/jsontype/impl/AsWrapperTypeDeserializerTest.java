package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsWrapperTypeDeserializerTest {
    private static final String TYPE_ID = "TestTypeId";

    @Test
    public void testDeserializeTypedFromObjectWithTypeId() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mock(JavaType.class), mock(TypeIdResolver.class), null, false, Object.class);
        ObjectTypeIdResolver typeIdResolverMock = mock(ObjectTypeIdResolver.class);
        when(typeIdResolverMock.idFromValueAndType(Mockito.any(), Mockito.eq(TYPE_ID))).thenReturn(new TypeId("TestTypeId", Object.class));
        DeserializationContext contextMock = mock(DeserializationContext.class);
        when(contextMock.findValueDeserializer(Mockito.eq(Object.class))).thenReturn(mock(JsonDeserializer.class));
        when(contextMock.getAnnotationIntrospector()).thenReturn(mock(AnnotatedClass.class));

        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.canReadTypeId()).thenReturn(true);
        when(parserMock.getTypeId()).thenReturn(TYPE_ID);

        Object result = deserializer.deserializeTypedFromObject(parserMock, contextMock);
        Assert.assertNotNull(result);
        Assert.assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromObjectWithoutTypeId() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mock(JavaType.class), mock(TypeIdResolver.class), null, false, Object.class);

        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.canReadTypeId()).thenReturn(false);
        when(parserMock.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parserMock.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parserMock.getText()).thenReturn("typeId");
        when(parserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parserMock.getText()).thenReturn(TYPE_ID);
        when(parserMock.nextToken()).thenReturn(JsonToken.END_OBJECT);

        ObjectTypeIdResolver typeIdResolverMock = mock(ObjectTypeIdResolver.class);
        when(typeIdResolverMock.idFromValueAndType(Mockito.any(), Mockito.eq(TYPE_ID))).thenReturn(new TypeId("TestTypeId", Object.class));
        DeserializationContext contextMock = mock(DeserializationContext.class);
        when(contextMock.findValueDeserializer(Mockito.eq(Object.class))).thenReturn(mock(JsonDeserializer.class));
        when(contextMock.getAnnotationIntrospector()).thenReturn(mock(AnnotatedClass.class));

        Object result = deserializer._deserialize(parserMock, contextMock);
        Assert.assertNotNull(result);
        Assert.assertEquals(Object.class, result.getClass());
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObjectWithInvalidJson() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mock(JavaType.class), mock(TypeIdResolver.class), null, false, Object.class);

        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parserMock.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parserMock.getText()).thenReturn("typeId");
        when(parserMock.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);
        when(parserMock.nextIntValue()).thenReturn(1);
        when(parserMock.nextToken()).thenReturn(JsonToken.END_OBJECT);

        ObjectTypeIdResolver typeIdResolverMock = mock(ObjectTypeIdResolver.class);
        DeserializationContext contextMock = mock(DeserializationContext.class);

        Object result = deserializer._deserialize(parserMock, contextMock);
        Assert.fail();
    }
}