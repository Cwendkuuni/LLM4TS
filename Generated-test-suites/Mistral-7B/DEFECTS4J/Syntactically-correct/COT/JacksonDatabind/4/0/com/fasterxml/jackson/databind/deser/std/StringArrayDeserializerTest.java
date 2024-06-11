package com.example.stringarraydeserializertest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ObjectBuffer;

/**
 * Class StringArrayDeserializerTest is used to test the StringArrayDeserializer class.
 */
public class StringArrayDeserializerTest {

    /**
     * Test method for deserialize method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext.Builder().buildContext());
        Assertions.assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
    }

    /**
     * Test method for _deserializeCustom method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void test_deserializeCustom() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StandardDeserializer<String>(String.class));
        String[] result = deserializer._deserializeCustom(jp, new DeserializationContext.Builder().buildContext());
        Assertions.assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
    }

    /**
     * Test method for deserializeWithType method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructTypeDeserializer(String[].class);
        String[] result = new StringArrayDeserializer(typeDeserializer).deserializeWithType(jp, new DeserializationContext.Builder().buildContext(), typeDeserializer);
        Assertions.assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
    }

    /**
     * Test method for handleNonArray method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testHandleNonArray_SingleValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"abc\"");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        if (DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY.isEnabled(new DeserializationContext.Builder().buildContext())) {
            String[] result = deserializer.handleNonArray(jp, new DeserializationContext.Builder().buildContext());
            Assertions.assertArrayEquals(new String[]{"abc"}, result);
        } else {
            String str = jp.getText();
            Assertions.assertEquals("abc", str);
            Assertions.assertThrows(JsonMappingException.class, () -> deserializer.handleNonArray(jp, new DeserializationContext.Builder().buildContext()));
        }
    }

    /**
     * Test method for handleNonArray method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testHandleNonArray_EmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        if (DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT.isEnabled(new DeserializationContext.Builder().buildContext())) {
            String[] result = deserializer.handleNonArray(jp, new DeserializationContext.Builder().buildContext());
            Assertions.assertNull(result);
        } else {
            String[] result = deserializer.handleNonArray(jp, new DeserializationContext.Builder().buildContext());
            Assertions.assertArrayEquals(new String[0], result);
            Assertions.assertThrows(JsonMappingException.class, () -> deserializer.handleNonArray(jp, new DeserializationContext.Builder().buildContext()));
        }
    }

    /**
     * Test method for createContextual method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testCreateContextual_DefaultDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        JsonDeserializer<?> result = deserializer.createContextual(
                new DeserializationContext.Builder().withContextualizer(new StandardContextualizer()).buildContext(), null);
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(StringArrayDeserializer.class, result);
    }

    /**
     * Test method for createContextual method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testCreateContextual_StringDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StandardDeserializer<String>(String.class));
        JsonDeserializer<?> result = deserializer.createContextual(
                new DeserializationContext.Builder().withContextualizer(new StandardContextualizer()).buildContext(), null);
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(StdDeserializer.class.cast(StringArrayDeserializer.class.cast(deserializer)._elementDeserializer.getClass()), result);
    }

    /**
     * Test method for createContextual method.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testCreateContextual_NullDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        JsonDeserializer<?> result = deserializer.createContextual(
                new DeserializationContext.Builder().withContextualizer(new StandardContextualizer()).buildContext(), null);
        Assertions.assertNull(result);
    }
}