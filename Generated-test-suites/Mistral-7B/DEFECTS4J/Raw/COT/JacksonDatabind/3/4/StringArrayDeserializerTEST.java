package com.example.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.Types;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

public class StringArrayDeserializerTest {

    private DeserializationContext context;
    private JsonParser parser;

    @Before
    public void setup() {
        context = new TestDeserializationContext();
        parser = new TestJsonParser(context);
    }

    @Test
    public void testDeserializeStandard() throws IOException {
        parser.setValue(JsonToken.START_ARRAY);
        parser.nextToken(); // skip START_ARRAY
        parser.setValue(JsonToken.VALUE_STRING, "test1");
        parser.nextToken(); // skip VALUE_STRING
        parser.setValue(JsonToken.VALUE_STRING, "test2");
        parser.nextToken(); // skip VALUE_STRING
        parser.setValue(JsonToken.END_ARRAY);

        String[] result = new StringArrayDeserializer().deserialize(parser, context);
        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        parser.setValue(JsonToken.START_ARRAY);
        parser.nextToken(); // skip START_ARRAY

        // Use a custom deserializer for the elements
        JsonDeserializer<String> customDeserializer = new TestStringDeserializer();
        new StringArrayDeserializer(customDeserializer).deserialize(parser, context);

        parser.setValue(JsonToken.END_ARRAY);

        String[] result = context.underlyingObject() instanceof ObjectBuffer ?
                ((ObjectBuffer) context.underlyingObject()).currentChunk() : new String[0];

        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        parser.setValue(JsonToken.START_ARRAY);
        parser.nextToken(); // skip START_ARRAY
        parser.setValue(JsonToken.VALUE_STRING, "test1");
        parser.nextToken(); // skip VALUE_STRING
        parser.setValue(JsonToken.VALUE_NUMBER, 123); // invalid type for String array
        parser.setValue(JsonToken.END_ARRAY);

        new StringArrayDeserializer().deserialize(parser, context);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        parser.setValue(JsonToken.START_ARRAY);
        parser.nextToken(); // skip START_ARRAY
        parser.setValue(JsonToken.END_ARRAY);

        String[] result = new StringArrayDeserializer().deserialize(parser, context);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSingleValue() throws IOException {
        parser.setValue(JsonToken.VALUE_STRING, "test");

        String[] result = new StringArrayDeserializer().deserialize(parser, context);
        assertArrayEquals(new String[]{"test"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullValue() throws IOException {
        parser.setValue(JsonToken.VALUE_NULL);

        new StringArrayDeserializer().deserialize(parser, context);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = new TestTypeDeserializer();
        parser.setValue(JsonToken.START_ARRAY);
        parser.nextToken(); // skip START_ARRAY
        parser.setValue(JsonToken.VALUE_STRING, "test1");
        parser.nextToken(); // skip VALUE_STRING
        parser.setValue(JsonToken.VALUE_STRING, "test2");
        parser.nextToken(); // skip VALUE_STRING
        parser.setValue(JsonToken.END_ARRAY);

        String[] result = new StringArrayDeserializer().deserializeWithType(parser, context, typeDeserializer);
        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @Test
    public void testCreateContextual() {
        JsonDeserializer<String> customDeserializer = new TestStringDeserializer();

        JsonDeserializer<?> deserializer = new StringArrayDeserializer().createContextual(context, null);
        assertNotNull(deserializer);

        JsonDeserializer<?> deserializerWithCustom = new StringArrayDeserializer(customDeserializer).createContextual(context, null);
        assertEquals(StringArrayDeserializer.class, deserializerWithCustom.getClass());
        assertEquals(customDeserializer, deserializerWithCustom.getDelegate());
    }

    @Test
    public void testIsDefaultDeserializer() {
        JsonDeserializer<String> customDeserializer = new TestStringDeserializer();
        JsonDeserializer<String> defaultDeserializer = new StandardDeserializer<String>(String.class);

        assertTrue(new StringArrayDeserializer.isDefaultDeserializer(defaultDeserializer));
        assertFalse(new StringArrayDeserializer.isDefaultDeserializer(customDeserializer));
    }
}

class TestDeserializationContext extends DeserializationContext {
    @Override
    public ObjectBuffer newObjectBuffer(int size) {
        return new ObjectBuffer(size);
    }

    @Override
    public ObjectBuffer currentObjectBuffer() {
        return (ObjectBuffer) super.currentObjectBuffer();
    }

    @Override
    public void returnObjectBuffer(ObjectBuffer buffer) {
        // Do nothing
    }
}

class TestJsonParser extends JsonParser {
    private Object currentValue;
    private JsonToken currentToken;

    public TestJsonParser(DeserializationContext ctxt) {
        super(new TestStreamContext());
        this.context = ctxt;
    }

    public void setValue(JsonToken token) throws IOException {
        this.currentToken = token;
        currentValue = null;
    }

    public void nextToken() throws IOException {
        if (currentValue != null) {
            this.valueAs(String.class);
            currentValue = null;
        }
        currentToken = this.nextToken();
    }

    public JsonToken getCurrentToken() {
        return currentToken;
    }

    public String getText() throws IOException {
        if (currentToken == JsonToken.VALUE_STRING) {
            String text = (String) currentValue;
            currentToken = null;
            currentValue = null;
            return text;
        }
        throw new IOException("Expected VALUE_STRING but got " + currentToken);
    }
}

class TestStringDeserializer extends StdDeserializer<String> {
    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return "test";
    }
}

class TestTypeDeserializer implements TypeDeserializer {
    @Override
    public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new String[0];
    }
}