package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

/**
 * Test cases for {@link NullifyingDeserializer}.
 */
public class NullifyingDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDeserializeNullifyingDeserializer() throws IOException {
        JsonParser parser = MAPPER.getFactory().createParser("{}");
        deserializeTest(parser, NullifyingDeserializer.instance);
        assertNull(parser.getValueAsTree());
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithType() throws IOException {
        JsonParser parser = MAPPER.getFactory().createParser("[]");
        deserializeWithTypeTest(parser, NullifyingDeserializer.instance, null);
        assertNull(parser.getValueAsTree());
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithTypeObject() throws IOException {
        JsonParser parser = MAPPER.getFactory().createParser(MAPPER.writeValueAsString(new ObjectNode(MAPPER.createObjectNode())));
        deserializeWithTypeTest(parser, NullifyingDeserializer.instance, Object.class);
        assertNull(parser.getValueAsTree());
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithTypeArray() throws IOException {
        JsonParser parser = MAPPER.getFactory().createParser("[1,2,3]");
        deserializeWithTypeTest(parser, NullifyingDeserializer.instance, int[].class);
        assertNull(parser.getValueAsTree());
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithTypeString() throws IOException {
        JsonParser parser = MAPPER.getFactory().createParser("\"Hello World\"");
        deserializeWithTypeTest(parser, NullifyingDeserializer.instance, String.class);
        assertNull(parser.getValueAsTree());
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullifyingDeserializerWithInvalidToken() throws IOException {
        JsonParser parser = MAPPER.getFactory().createParser("false");
        deserializeTest(parser, NullifyingDeserializer.instance);
    }

    private void deserializeTest(JsonParser parser, DeserializerProvider deserializerProvider) throws IOException {
        Object readValue = deserializerProvider.getDeserializer(NullifyingDeserializer.class).deserialize(parser, null);
        assertNull(readValue);
    }

    private void deserializeWithTypeTest(JsonParser parser, DeserializerProvider deserializerProvider, Class<?> type) throws IOException {
        Object readValue = deserializerProvider.getDeserializer(NullifyingDeserializer.class)
                .deserializeWithType(parser, deserializerProvider.findValueDeserializer(type), null);
        assertNull(readValue);
    }
}