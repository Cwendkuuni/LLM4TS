package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test cases for StringArrayDeserializer class.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final String JSON_STRING = "[\n" +
            "  \"foo\",\n" +
            "  \"bar\",\n" +
            "  \"baz\",\n" +
            "  null,\n" +
            "  \"qux\"\n" +
            "]";

    @Test
    public void testDeserializeStandard() throws IOException {
        JsonParser jp = createParser(JSON_STRING);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{"foo", "bar", "baz", null, "qux"}, result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        JsonParser jp = createParser(JSON_STRING);
        JsonDeserializer<String> elementDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getText();
                System.out.println("Deserialized String: " + value);
                return value;
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = deserializer.deserialize(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{"foo", "bar", "baz", null, "qux"}, result);
    }

    @Test
    public void testDeserializeNullElementDeserializer() throws IOException {
        JsonParser jp = createParser(JSON_STRING);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{"foo", "bar", "baz", null, "qux"}, result);
    }

    @Test
    public void testDeserializeTypeDeserializer() throws IOException {
        JsonParser jp = createParser(JSON_STRING);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectTypeDescriptor typeDesc = new ObjectTypeDescriptor();
        typeDesc.setName("com.example.StringArray");
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonToken token = p.nextToken();
                if (token == JsonToken.END_ARRAY) {
                    return new Object[] {};
                }
                Object value = deserializer.deserialize(p, ctxt);
                p.nextToken(JsonToken.COMMA);
                return new Object[]{value};
            }
        };
        String[] result = deserializer.deserializeWithType(jp, new DeserializationContext(new ObjectMapper()), typeDeserializer);
        assertArrayEquals(new Object[]{new String[]{"foo", "bar", "baz", null, "qux"}}, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jp = createParser("\"foo\"");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        JsonParser jp = createParser(JSON_STRING);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        StringArrayDeserializer customDeserializer = deserializer.createContextual(new DeserializationContext(new ObjectMapper()), new StdFieldProperty("foo"));
        String[] result = customDeserializer.deserialize(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{"foo", "bar", "baz", null, "qux"}, result);
    }

    @Test
    public void testCreateContextualNullDeserializer() throws IOException {
        JsonParser jp = createParser(JSON_STRING);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        StringArrayDeserializer customDeserializer = deserializer.createContextual(new DeserializationContext(new ObjectMapper()), null);
        String[] result = customDeserializer.deserialize(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{"foo", "bar", "baz", null, "qux"}, result);
    }

    @Test
    public void testCreateContextualNullElement() throws IOException {
        JsonParser jp = createParser("[null]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        StringArrayDeserializer customDeserializer = deserializer.createContextual(new DeserializationContext(new ObjectMapper()), new StdFieldProperty("foo"));
        String[] result = customDeserializer.deserialize(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{"null"}, result);
    }

    @Test
    public void testCreateContextualEmptyArray() throws IOException {
        JsonParser jp = createParser("[]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        StringArrayDeserializer customDeserializer = deserializer.createContextual(new DeserializationContext(new ObjectMapper()), new StdFieldProperty("foo"));
        String[] result = customDeserializer.deserialize(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testCreateContextualEmptyCustomDeserializer() throws IOException {
        JsonParser jp = createParser("[null]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonDeserializer<String> emptyDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        StringArrayDeserializer customDeserializer = deserializer.createContextual(new DeserializationContext(new ObjectMapper()), new StdFieldProperty("foo"), emptyDeserializer);
        String[] result = customDeserializer.deserialize(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{"null"}, result);
    }

    @Test
    public void testCreateContextualWithEmptyStringAsNull() throws IOException {
        JsonParser jp = createParser("[\"\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        boolean nullEnabled = new DeserializationContext(new ObjectMapper()).isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        StringArrayDeserializer customDeserializer = deserializer.createContextual(new DeserializationContext(new ObjectMapper(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, new ObjectMapper().disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)), new StdFieldProperty("foo"));
        String[] result = customDeserializer.deserialize(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{"null"}, result);
        assertTrue(nullEnabled);
    }

    @Test
    public void testCreateContextualWithSingleValueAsArray() throws IOException {
        JsonParser jp = createParser("\"foo\"");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        boolean acceptSingleValueAsArray = new DeserializationContext(new ObjectMapper()).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        StringArrayDeserializer customDeserializer = deserializer.createContextual(new DeserializationContext(new ObjectMapper(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, new ObjectMapper().disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)), new StdFieldProperty("foo"));
        String[] result = customDeserializer.deserialize(jp, new DeserializationContext(new ObjectMapper()));
        assertArrayEquals(new String[]{"foo"}, result);
        assertTrue(acceptSingleValueAsArray);
    }

    private JsonParser createParser(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        return factory.createParser(jsonString);
    }
}