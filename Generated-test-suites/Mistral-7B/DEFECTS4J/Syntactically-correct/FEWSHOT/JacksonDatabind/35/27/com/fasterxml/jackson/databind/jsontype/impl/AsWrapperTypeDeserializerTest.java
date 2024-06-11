package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"String\",\"value\":\"Hello\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(ctxt.findValueDeserializer(eq(String.class), anyObject()))
                .thenReturn(mock(JsonDeserializer.class));

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
        assertEquals("Hello", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[{\"type\":\"String\",\"value\":\"Hello\"}]");
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(ctxt.findValueDeserializer(eq(String.class), anyObject()))
                .thenReturn(mock(JsonDeserializer.class));

        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);
        assertEquals("[Hello]", result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"Hello\"");
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(ctxt.findValueDeserializer(eq(String.class), anyObject()))
                .thenReturn(mock(JsonDeserializer.class));

        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);
        assertEquals("Hello", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"Number\",\"value\":42}");
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(ctxt.findValueDeserializer(eq(Number.class), anyObject()))
                .thenReturn(mock(JsonDeserializer.class));

        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
        assertEquals(42, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"String\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(ctxt.findValueDeserializer(eq(String.class), anyObject()))
                .thenReturn(mock(JsonDeserializer.class));

        when(ctxt.findTypedValueDeserializer(eq("String"), eq(String.class)))
                .thenReturn(mock(JsonDeserializer.class));

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
        assertEquals("", result);
    }

    @Test(expected = IOException.class)
    public void testWrongTokenException() throws Exception {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"Hello\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
    }

    @Test(expected = IOException.class)
    public void testMissingTypeId() throws Exception {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"value\":\"Hello\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
    }

    @Test
    public void testTypeIdVisible() throws Exception {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mock(JavaType.class), mock(TypeIdResolver.class), "type", true, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"type\":\"String\",\"value\":\"Hello\"}");
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(ctxt.findValueDeserializer(eq(String.class), anyObject()))
                .thenReturn(mock(JsonDeserializer.class));

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
        assertEquals("Hello", result);
        assertEquals("String", deserializer._property.getAnnotation(JsonTypeInfo.As.class).value());
    }
}