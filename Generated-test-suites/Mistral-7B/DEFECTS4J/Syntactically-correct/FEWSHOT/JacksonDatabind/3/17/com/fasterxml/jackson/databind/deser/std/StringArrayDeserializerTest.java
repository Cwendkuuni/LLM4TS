package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactoryWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for StringArrayDeserializer class.
 */
public class StringArrayDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private DeserializationContext context;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        jsonFactory = new JsonFactoryWrapper(objectMapper.getFactory());
        context = objectMapper.getDeserializationContext();
    }

    @Test
    public void testEmptyArray() throws Exception {
        JsonParser jp = jsonFactory.createParser("[]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, context);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testArrayWithOneElement() throws Exception {
        JsonParser jp = jsonFactory.createParser("[\"foo\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, context);
        assertArrayEquals(new String[] {"foo"}, result);
    }

    @Test
    public void testArrayWithMultipleElements() throws Exception {
        JsonParser jp = jsonFactory.createParser("[\"foo\", \"bar\", \"baz\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, context);
        assertArrayEquals(new String[] {"foo", "bar", "baz"}, result);
    }

    @Test
    public void testArrayWithNullElement() throws Exception {
        JsonParser jp = jsonFactory.createParser("[\"foo\", null, \"baz\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, context);
        assertArrayEquals(new String[] {"foo", null, "baz"}, result);
    }

    @Test
    public void testCustomElementDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText() + "_custom";
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        JsonParser jp = jsonFactory.createParser("[\"foo\", \"bar\"]");
        String[] result = deserializer.deserialize(jp, context);
        assertArrayEquals(new String[] {"foo_custom", "bar_custom"}, result);
    }

    @Test
    public void testTypeDeserialization() throws Exception {
        JsonParser jp = jsonFactory.createParser("[\"foo\", \"bar\"]");
        TypeDeserializer typeDeserializer = context.findValueDeserializer(new TypeReference<String[]>() {});
        String[] result = typeDeserializer.deserializeTypedFromArray(jp, context);
        assertArrayEquals(new String[] {"foo", "bar"}, result);
    }

    @Test
    public void testNullInput() throws Exception {
        JsonParser jp = jsonFactory.createParser("null");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, context);
        assertNull(result);
    }

    @Test
    public void testNonArrayInput() throws Exception {
        JsonParser jp = jsonFactory.createParser("{\"foo\": \"bar\"}");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        try {
            deserializer.deserialize(jp, context);
            fail("Expected MappingException");
        } catch (IOException e) {
            assertInstanceOf(MappingException.class, e);
        }
    }

    @Test
    public void testCustomDeserializationContext() throws Exception {
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText() + "_custom";
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        DeserializationContext customContext = context.withValueDeserializer("String", customDeserializer);
        JsonParser jp = jsonFactory.createParser("[\"foo\", \"bar\"]");
        String[] result = deserializer.createContextual(customContext, null).deserialize(jp, customContext);
        assertArrayEquals(new String[] {"foo_custom", "bar_custom"}, result);
    }
}