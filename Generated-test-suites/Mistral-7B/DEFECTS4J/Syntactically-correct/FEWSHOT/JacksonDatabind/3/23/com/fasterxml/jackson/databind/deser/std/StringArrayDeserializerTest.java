package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TestBase;

public class StringArrayDeserializerTest extends TestBase {

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        JsonParser p = createParser("[]");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).readValue(p);
        assertEquals(new String[0], result);
    }

    @Test
    public void testDeserializeArrayWithDefaultElementDeserializer() throws Exception {
        JsonParser p = createParser("['foo', 'bar']");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).readValue(p);
        assertArrayEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testDeserializeArrayWithCustomElementDeserializer() throws Exception {
        JsonParser p = createParser("['foo', 'bar']");
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText().toUpperCase();
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = mapper.readerForArrayOf(String.class).readValue(p);
        assertArrayEquals(new String[]{"FOO", "BAR"}, result);
    }

    @Test
    public void testDeserializeArrayWithNullElements() throws Exception {
        JsonParser p = createParser("['foo', null, 'bar']");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).readValue(p);
        assertArrayEquals(new String[]{"foo", null, "bar"}, result);
    }

    @Test
    public void testDeserializeArrayWithEmptyStringElements() throws Exception {
        JsonParser p = createParser("['foo', '', 'bar']");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).readValue(p);
        assertArrayEquals(new String[]{"foo", "", "bar"}, result);
    }

    @Test
    public void testDeserializeArrayWithEmptyJsonArray() throws Exception {
        JsonParser p = createParser("[]");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).readValue(p);
        assertEquals(new String[0], result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJsonArray() throws Exception {
        JsonParser p = createParser("{'foo': 'bar'");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        mapper.readerForArrayOf(String.class).readValue(p);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullJsonArray() throws Exception {
        JsonParser p = createParser(null);
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        mapper.readerForArrayOf(String.class).readValue(p);
    }

    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText().toUpperCase();
            }
        };
        JsonDeserializer<String> deser = mapper.findValueDeserializer(String.class, null);
        JsonDeserializer<String> deserWithContext = mapper.handleSecondaryContextualization(deser, null);
        assertEquals(deser, deserWithContext);
        JsonDeserializer<String[]> deserializer = mapper.findContextualValueDeserializer(String[].class, null);
        JsonDeserializer<String[]> contextualDeserializer = mapper.handleSecondaryContextualization(deserializer, null);
        assertNotSame(deserializer, contextualDeserializer);
        assertInstanceOf(StringArrayDeserializer.class, contextualDeserializer);
        JsonDeserializer<String[]> customDeserializerContextual = mapper.handleSecondaryContextualization(deserializer, new PropertyName("test"));
        assertInstanceOf(StringArrayDeserializer.class, customDeserializerContextual);
        assertNotNull(customDeserializerContextual.getContentDeserializer());
        JsonDeserializer<String[]> customDeserializerContextualWithNull = mapper.handleSecondaryContextualization(deserializer, new PropertyName("test"), null);
        assertNull(customDeserializerContextualWithNull.getContentDeserializer());
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser p = createParser("['foo', 'bar']");
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(JsonDeserialize.withType(String[].class));
        Object result = mapper.readerForValueAsTree(p).deserialize(mapper, typeDeserializer);
        assertEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        JsonParser p = createParser("'foo'");
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(p, mapper.getDeserializationContext());
        assertEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testIsDefaultDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> deser = mapper.findValueDeserializer(String.class, null);
        assertTrue(deserializer.isDefaultDeserializer(deser));
        JsonDeserializer<String> deserWithContext = mapper.handleSecondaryContextualization(deser, null);
        assertTrue(deserializer.isDefaultDeserializer(deserWithContext));
        JsonDeserializer<String[]> deserializer1 = mapper.findContextualValueDeserializer(String[].class, null);
        assertFalse(deserializer.isDefaultDeserializer(deserializer1));
    }
}