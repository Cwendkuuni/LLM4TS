package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();

        // Create a mock JsonParser
        JsonParser parser = Mockito.mock(JsonParser.class);

        // Create a mock DeserializationContext
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        // Invoke the deserialize method
        Object result = deserializer.deserialize(parser, context);

        // Verify that p.skipChildren() is called
        Mockito.verify(parser).skipChildren();

        // Verify that the result is null
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();

        // Create a mock JsonParser
        JsonParser parser = Mockito.mock(JsonParser.class);

        // Create a mock DeserializationContext
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        // Create a mock TypeDeserializer
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        // Set the current token id of the parser
        Mockito.when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);

        // Invoke the deserializeWithType method
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);

        // Verify that typeDeserializer.deserializeTypedFromAny() is called
        Mockito.verify(typeDeserializer).deserializeTypedFromAny(Mockito.eq(parser), Mockito.eq(context));

        // Verify that the result is not null
        Assert.assertNotNull(result);
    }
}