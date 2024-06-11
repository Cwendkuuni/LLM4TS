package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() {
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class) {
            @Override
            protected Integer _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Integer.parseInt(value);
            }
        };

        try {
            JsonParser parser = new JsonParser();
            DeserializationContext ctxt = new DeserializationContext();

            // Test positive case
            JsonToken token = new JsonToken();
            token.setValue("5");
            parser.setCurrentToken(token);
            Integer result = deserializer.deserialize(parser, ctxt);
            assertEquals(5, result.intValue());

            // Test negative case
            token.setValue("abc");
            parser.setCurrentToken(token);
            try {
                deserializer.deserialize(parser, ctxt);
                fail("Expected JsonMappingException");
            } catch (JsonMappingException e) {
                // Expected exception thrown
            }
        } catch (IOException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testDeserializeFromEmptyString() {
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class) {
            @Override
            protected Integer _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Integer.parseInt(value);
            }
        };

        try {
            JsonParser parser = new JsonParser();
            DeserializationContext ctxt = new DeserializationContext();

            // Test positive case
            JsonToken token = new JsonToken();
            token.setValue("");
            parser.setCurrentToken(token);
            Integer result = deserializer.deserialize(parser, ctxt);
            assertNull(result);
        } catch (IOException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testDeserializeEmbedded() {
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class) {
            @Override
            protected Integer _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Integer.parseInt(value);
            }

            @Override
            protected Integer _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
                if (ob instanceof Integer) {
                    return (Integer) ob;
                } else {
                    throw new JsonMappingException("Invalid embedded object type");
                }
            }
        };

        try {
            JsonParser parser = new JsonParser();
            DeserializationContext ctxt = new DeserializationContext();

            // Test positive case
            JsonToken token = new JsonToken();
            token.setValue(10);
            parser.setCurrentToken(token);
            Integer result = deserializer.deserialize(parser, ctxt);
            assertEquals(10, result.intValue());

            // Test negative case
            token.setValue("abc");
            parser.setCurrentToken(token);
            try {
                deserializer.deserialize(parser, ctxt);
                fail("Expected JsonMappingException");
            } catch (JsonMappingException e) {
                // Expected exception thrown
            }
        } catch (IOException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testFindDeserializer() {
        Class<?> rawType = Integer.class;
        Std deserializer = FromStringDeserializer.findDeserializer(rawType);

        assertNotNull(deserializer);
        assertEquals(Integer.class, deserializer.getValueType());
        assertEquals(FromStringDeserializer.Std.STD_CLASS, deserializer.getKind());
    }

}