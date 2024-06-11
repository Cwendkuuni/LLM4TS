package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    private AsPropertyTypeDeserializer typeDeserializer;

    private JsonParser jsonParser;

    private DeserializationContext deserializationContext;

    private TokenBuffer tokenBuffer;

    @Before
    public void setup() {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;

        typeDeserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl, inclusion);

        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        tokenBuffer = mock(TokenBuffer.class);
    }

    @Test
    public void testDeserializeTypedFromObject_Success() throws IOException {
        when(jsonParser.canReadTypeId()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.END_OBJECT);

        Object result = typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedFromObject_WithTypedId() throws IOException {
        when(jsonParser.canReadTypeId()).thenReturn(true);
        when(jsonParser.getTypeId()).thenReturn("typeIdValue");

        Object result = typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedFromObject_WithFieldNames() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedForId_Success() throws IOException {
        when(jsonParser.getText()).thenReturn("typeIdValue");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(tokenBuffer.asParser(jsonParser)).thenReturn(jsonParser);

        Object result = typeDeserializer._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);

        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_Success() throws IOException {
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deser.deserialize(jsonParser, deserializationContext)).thenReturn("result");

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(tokenBuffer.asParser(jsonParser)).thenReturn(jsonParser);

        Object result = typeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);

        assertEquals("result", result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_NullResult() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        Object result = typeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);

        assertEquals(null, result);
    }

    @Test(expected = SomeCustomException.class)
    public void testDeserializeTypedUsingDefaultImpl_CustomException() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getCurrentName()).thenReturn("fieldName");

        typeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
    }

    @Test
    public void testDeserializeTypedFromAny_Success() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        Object result = typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);

        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedFromAny_DeserializeTypedFromArray() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        Object result = typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);

        assertEquals(null, result);
    }

}