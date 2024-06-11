package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class StdSerializerTest {

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        StdSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class);
        JsonSerializer<Object> objSerializer = new StdSerializer<Object>(Object.class, false, null);
        assertTrue(stringSerializer.isDefaultSerializer(stringSerializer));
        assertFalse(stringSerializer.isDefaultSerializer(intSerializer));
        assertFalse(stringSerializer.isDefaultSerializer(objSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> delegateSerializer = mapper.findValueSerializer(Object.class);
        StdSerializer<List<String>> listSerializer = new StdListSerializer(String.class, delegateSerializer);
        ObjectNode node = mapper.createObjectNode();
        node.put("items", "foo");
        JsonNode jsonNode = mapper.readTree(node.toString());
        Object content = jsonNode.get("items");
        StdSerializer<List<String>> resultSerializer = new StdSerializer<List<String>>(List.class, null, null);
        resultSerializer = resultSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, content);
        assertEquals(delegateSerializer, resultSerializer.getDelegateSerializer());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        FilterProvider filterProvider = mapper.getFilterProvider();
        PropertyFilter filter = filterProvider.findPropertyFilter("testFilter", "testValue");
        assertNull(filter);
        filter = filterProvider.findPropertyFilter("anotherFilter", new Object());
        assertNotNull(filter);
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        String value = "test";
        ObjectNode rootNode = mapper.createObjectNode();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        stringSerializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();
        String result = new String(gen.getOutputBuffer().toByteArray());
        assertEquals("\"test\"", result);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        ObjectNode schemaNode = stringSerializer.getSchema(mapper.getSerializerProvider(), String.class);
        assertEquals("{\"type\":\"string\"}", schemaNode.toString());
    }
}