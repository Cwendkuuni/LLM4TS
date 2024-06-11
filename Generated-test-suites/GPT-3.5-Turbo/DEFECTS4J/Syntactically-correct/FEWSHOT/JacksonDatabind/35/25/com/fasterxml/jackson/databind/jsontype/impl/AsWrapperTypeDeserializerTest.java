package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsWrapperTypeDeserializerTest {
    private AsWrapperTypeDeserializer asWrapperTypeDeserializer;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;

    @Before
    public void setup() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = Object.class;
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty beanProperty = mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = asWrapperTypeDeserializer.forProperty(beanProperty);
        assertEquals(asWrapperTypeDeserializer, typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, asWrapperTypeDeserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonToken jsonToken = mock(JsonToken.class);

        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(jsonToken);
        when(jsonToken == JsonToken.START_OBJECT).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        
        assertEquals(null, asWrapperTypeDeserializer.deserializeTypedFromObject(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        assertEquals(null, asWrapperTypeDeserializer.deserializeTypedFromArray(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        assertEquals(null, asWrapperTypeDeserializer.deserializeTypedFromScalar(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        assertEquals(null, asWrapperTypeDeserializer.deserializeTypedFromAny(jp, ctxt));
    }

    @Test
    public void test_deserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonToken jsonToken = mock(JsonToken.class);
        String typeId = "Type1";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        Class<?> defaultImpl = String.class;
        TokenBuffer tb = mock(TokenBuffer.class);
        JsonParserSequence jsonParserSequence = mock(JsonParserSequence.class);

        when(p.canReadTypeId()).thenReturn(true);
        when(p.getTypeId()).thenReturn(typeId);
        when(p.getCurrentToken()).thenReturn(jsonToken);
        when(jsonToken == JsonToken.START_OBJECT).thenReturn(false);
        when(_findDeserializer(ctxt, typeId)).thenReturn(deserializer);
        when(deserializer.deserialize(p, ctxt)).thenReturn("TestValue");
        when(p.getTypeId()).thenReturn(typeId);
        when(_typeIdVisible).thenReturn(true);
        when(p.getCurrentToken()).thenReturn(someToken);
        when(someToken == JsonToken.START_OBJECT).thenReturn(true);
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.START_OBJECT, JsonToken.END_OBJECT);
        when(tb.asParser(p)).thenReturn(p);
        when(JsonParserSequence.createFlattened(tb.asParser(p), p)).thenReturn(jsonParserSequence);
        when(p.nextToken()).thenReturn(JsonToken.END_OBJECT);

        assertEquals("TestValue", asWrapperTypeDeserializer._deserialize(p, ctxt));
    }
}