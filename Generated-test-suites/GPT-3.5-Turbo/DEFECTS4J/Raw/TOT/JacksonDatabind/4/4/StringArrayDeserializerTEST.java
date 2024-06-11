
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer = new StringArrayDeserializer();

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = TestUtils.createJsonParser("[\"value1\", \"value2\", \"value3\"]");
        DeserializationContext context = TestUtils.createDeserializationContext();

        String[] result = deserializer.deserialize(parser, context);

        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        assertEquals("value3", result[2]);
    }
    
    @Test
    public void testDeserializeWithNullValue() throws IOException {
        JsonParser parser = TestUtils.createJsonParser("[\"value1\", null, \"value3\"]");
        DeserializationContext context = TestUtils.createDeserializationContext();

        String[] result = deserializer.deserialize(parser, context);

        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertNull(result[1]);
        assertEquals("value3", result[2]);
    }

    @Test
    public void testDeserializeWithEmptyArray() throws IOException {
        JsonParser parser = TestUtils.createJsonParser("[]");
        DeserializationContext context = TestUtils.createDeserializationContext();

        String[] result = deserializer.deserialize(parser, context);

        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeWithEmptyStringAndAcceptEmptyStringAsNullOptionEnabled() throws IOException {
        JsonParser parser = TestUtils.createJsonParser("[\"\"]");
        DeserializationContext context = TestUtils.createDeserializationContext();
        context.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        String[] result = deserializer.deserialize(parser, context);

        assertNull(result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeWithInvalidValueType() throws IOException {
        JsonParser parser = TestUtils.createJsonParser("[1, 2, 3]");
        DeserializationContext context = TestUtils.createDeserializationContext();

        deserializer.deserialize(parser, context);
    }

    @Test(expected = MissingTokenException.class)
    public void testDeserializeWithMissingArrayStartToken() throws IOException {
        JsonParser parser = TestUtils.createJsonParser("\"value\"");
        DeserializationContext context = TestUtils.createDeserializationContext();

        deserializer.deserialize(parser, context);
    }

    @Test(expected = MismatchedInputException.class)
    public void testDeserializeWithMismatchedArrayEndToken() throws IOException {
        JsonParser parser = TestUtils.createJsonParser("[\"value\"");
        DeserializationContext context = TestUtils.createDeserializationContext();

        deserializer.deserialize(parser, context);
    }
}

