package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, true, null);

        Object result = deserializer.deserializeTypedFromObject(parser, context);

        assertNull(result);
        Mockito.verify(parser, Mockito.times(1)).canReadTypeId();
        Mockito.verify(parser, Mockito.times(1)).getTypeId();
        Mockito.verify(parser, Mockito.times(0)).getCurrentToken();
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, true, null);

        Object result = deserializer.deserializeTypedFromArray(parser, context);

        assertNull(result);
        Mockito.verify(parser, Mockito.times(1)).canReadTypeId();
        Mockito.verify(parser, Mockito.times(1)).getTypeId();
        Mockito.verify(parser, Mockito.times(0)).getCurrentToken();
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, true, null);

        Object result = deserializer.deserializeTypedFromScalar(parser, context);

        assertNull(result);
        Mockito.verify(parser, Mockito.times(1)).canReadTypeId();
        Mockito.verify(parser, Mockito.times(1)).getTypeId();
        Mockito.verify(parser, Mockito.times(0)).getCurrentToken();
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, true, null);

        Object result = deserializer.deserializeTypedFromAny(parser, context);

        assertNull(result);
        Mockito.verify(parser, Mockito.times(1)).canReadTypeId();
        Mockito.verify(parser, Mockito.times(1)).getTypeId();
        Mockito.verify(parser, Mockito.times(0)).getCurrentToken();
    }

    @Test
    public void test_deserialize() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, true, null);

        Object result = deserializer.deserializeTypedFromObject(parser, context);

        assertNull(result);
        Mockito.verify(parser, Mockito.times(1)).canReadTypeId();
        Mockito.verify(parser, Mockito.times(1)).getTypeId();
        Mockito.verify(parser, Mockito.times(0)).getCurrentToken();
    }
}