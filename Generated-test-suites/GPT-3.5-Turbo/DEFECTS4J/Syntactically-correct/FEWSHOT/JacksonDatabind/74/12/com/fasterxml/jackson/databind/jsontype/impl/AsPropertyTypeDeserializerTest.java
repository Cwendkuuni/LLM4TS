package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class AsPropertyTypeDeserializerTest {
    @Mock
    private JsonParser jsonParser;
    private AsPropertyTypeDeserializer typeDeserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        typeDeserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl, inclusion);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer deserializer = typeDeserializer.forProperty(prop);

        assertNotEquals(typeDeserializer, deserializer);
        assertTrue(deserializer instanceof AsPropertyTypeDeserializer);
        assertEquals(prop, ((AsPropertyTypeDeserializer) deserializer)._property);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        TokenBuffer tb = mock(TokenBuffer.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = new Object();

        when(typeDeserializer._findDefaultImplDeserializer(ctxt)).thenReturn(deser);
        when(tb.asParser(parser)).thenReturn(parser);
        when(parser.nextToken()).thenReturn(mock(com.fasterxml.jackson.core.JsonToken.class));
        when(deser.deserialize(parser, ctxt)).thenReturn(result);

        Object deserializedObject = typeDeserializer._deserializeTypedUsingDefaultImpl(parser, ctxt, tb);

        assertEquals(result, deserializedObject);
        verify(deser).deserialize(parser, ctxt);
        verify(tb).asParser(parser);
        verify(parser).nextToken();
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        String typeId = "1";
        TokenBuffer tb = mock(TokenBuffer.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        Object result = new Object();
        when(typeDeserializer._typeIdVisible).thenReturn(true);
        when(parser.getCurrentName()).thenReturn("test");
        when(tb.asParser(parser)).thenReturn(parser);
        when(parser.getText()).thenReturn(typeId);
        when(typeDeserializer._findDeserializer(ctxt, typeId)).thenReturn(deser);
        when(deser.deserialize(parser, ctxt)).thenReturn(result);
        when(tb.writeString(typeId)).thenReturn(tb);

        Object deserializedObject = typeDeserializer._deserializeTypedForId(parser, ctxt, tb);

        assertEquals(result, deserializedObject);
        verify(typeDeserializer).deserializeTypedFromObject(parser, ctxt);
        verify(typeDeserializer).deserializeTypedFromAny(parser, ctxt);
        verify(typeDeserializer, never()).deserializeTypedFromScalar(parser, ctxt);
        verify(typeDeserializer, never()).deserializeTypedFromArray(parser, ctxt);
        verify(tb).asParser(parser);
        verify(parser).getText();
        verify(typeDeserializer._findDeserializer(ctxt, typeId)).deserialize(parser, ctxt);
    }

    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testDeserializeTypedForIdWithoutVisibleTypeId() throws IOException {
        String typeId = "1";
        TokenBuffer tb = mock(TokenBuffer.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(typeDeserializer._typeIdVisible).thenReturn(false);
        when(parser.getCurrentName()).thenReturn("test");
        when(tb.asParser(parser)).thenReturn(parser);
        when(parser.getText()).thenReturn(typeId);
        when(tb.writeString(typeId)).thenReturn(tb);

        typeDeserializer._deserializeTypedForId(parser, ctxt, tb);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        String typeId = "1";
        TokenBuffer tb = mock(TokenBuffer.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        Object result = new Object();
        when(parser.getCurrentToken()).thenReturn(com.fasterxml.jackson.core.JsonToken.START_OBJECT).thenReturn(com.fasterxml.jackson.core.JsonToken.FIELD_NAME);
        when(parser.nextToken()).thenReturn(com.fasterxml.jackson.core.JsonToken.FIELD_NAME).thenReturn(com.fasterxml.jackson.core.JsonToken.END_OBJECT);
        when(parser.getCurrentName()).thenReturn(typeId);
        when(tb.writeFieldName(typeId)).thenReturn(tb);
        when(deser.deserialize(parser, ctxt)).thenReturn(result);
        when(typeDeserializer._deserializeTypedForId(parser, ctxt, tb)).thenReturn(result);

        Object deserializedObject = typeDeserializer.deserializeTypedFromObject(parser, ctxt);

        assertEquals(result, deserializedObject);
        verify(parser, times(2)).getCurrentToken();
        verify(parser, times(2)).nextToken();
        verify(parser).getCurrentName();
        verify(tb).writeFieldName(typeId);
        verify(deser).deserialize(parser, ctxt);
        verify(typeDeserializer)._deserializeTypedForId(parser, ctxt, tb);
        verify(typeDeserializer, never())._deserializeUsingPropertyBased(com.fasterxml.jackson.core.JsonParser.class, DeserializationContext.class, tb);
    }
}