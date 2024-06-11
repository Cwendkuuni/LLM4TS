import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        String[] expected = {"apple", "banana", null};
        String json = "[\"apple\", \"banana\", null]";
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        DeserializationContext context = new DeserializationContext(factory, null, null);
        String[] result = deserializer.deserialize(parser, context);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_EmptyArray() throws IOException {
        String[] expected = {};
        String json = "[]";
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        DeserializationContext context = new DeserializationContext(factory, null, null);
        String[] result = deserializer.deserialize(parser, context);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_SingleValueArray() throws IOException {
        String[] expected = {"apple"};
        String json = "\"apple\"";
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        DeserializationContext context = new DeserializationContext(factory, null, null);
        String[] result = deserializer.deserialize(parser, context);

        assertArrayEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_InvalidJson() throws IOException {
        String json = "\"apple\"";
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        DeserializationContext context = new DeserializationContext(factory, null, null);
        deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        String[] expected = {"apple", "banana", null};
        String json = "[\"apple\", \"banana\", null]";
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        DeserializationContext context = new DeserializationContext(factory, null, null);
        Object result = deserializer.deserializeWithType(parser, context, null);

        assertTrue(result instanceof String[]);
        assertArrayEquals(expected, (String[]) result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        String[] expected = {"apple", "banana", null};
        String json = "[\"apple\", \"banana\", null]";
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);

        JsonDeserializer<Object> elementDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString().toUpperCase();
            }
        };

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);

        DeserializationContext context = new DeserializationContext(factory, null, null);
        String[] result = deserializer.deserialize(parser, context);

        assertArrayEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeCustom_InvalidJson() throws IOException {
        String json = "[\"apple\", \"banana\", null]";
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);

        JsonDeserializer<Object> elementDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString().toUpperCase();
            }
        };

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);

        DeserializationContext context = new DeserializationContext(factory, null, null);
        deserializer.deserialize(parser, context);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeCustom_WithType() throws IOException {
        String[] expected = {"APPLE", "BANANA", null};
        String json = "[\"apple\", \"banana\", null]";
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);

        JsonDeserializer<Object> elementDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString().toUpperCase();
            }
        };

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);

        DeserializationContext context = new DeserializationContext(factory, null, null);
        Object result = deserializer.deserializeWithType(parser, context, null);

        assertTrue(result instanceof String[]);
        assertArrayEquals(expected, (String[]) result);
    }
}