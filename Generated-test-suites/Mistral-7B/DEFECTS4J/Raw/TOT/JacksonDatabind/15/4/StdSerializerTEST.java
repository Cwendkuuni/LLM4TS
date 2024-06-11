package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializerTests;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdSerializerTest {

    @Test
    public void testDefaultSerializer() throws IOException {
        // Testing if the default serializer is an instance of StdSerializer
        JsonSerializer<Object> defaultSerializer = StdSerializerTests.stdSerializer(Object.class);
        assertTrue(defaultSerializer instanceof StdSerializer);
    }

    @Test
    public void testSerializeNull() throws IOException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        stringSerializer.serialize(null, jsonGenerator, new ObjectMapper().getSerializerProvider());
        ObjectNode jsonNode = JsonParser.parseNumbericString(writer.toString());
        assertEquals(ValueWrapped.nullValue, jsonNode.get("value"));
    }

    @Test
    public void testSerializeEmptyString() throws IOException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        stringSerializer.serialize("", jsonGenerator, new ObjectMapper().getSerializerProvider());
        ObjectNode jsonNode = JsonParser.parseNumbericString(writer.toString());
        assertEquals("\"\"", jsonNode.get("value").asText());
    }

    @Test
    public void testSerializeString() throws IOException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        stringSerializer.serialize("Hello", jsonGenerator, new ObjectMapper().getSerializerProvider());
        ObjectNode jsonNode = JsonParser.parseNumbericString(writer.toString());
        assertEquals("\"Hello\"", jsonNode.get("value").asText());
    }

    @Test
    public void testGetSchema() throws IOException {
        StdSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode schema = intSerializer.getSchema(mapper.getSerializerProvider(), Integer.TYPE);
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.expectAnyFormat(String.class)).thenReturn(null);
        stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithFilter() throws IOException {
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class);
        FilterProvider filterProvider = mock(FilterProvider.class);
        ObjectNode filterNode = createFilterNode();
        when(filterProvider.findPropertyFilter("MyFilterId", Object.class)).thenReturn(new PropertyFilter() {
            @Override
            public void serializeAsValue(SerializerProvider p, Type typeHint, Object value, ObjectNode node) throws IOException {
                node.putPOJO("filterValue", value);
            }
        });

        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setFilterProvider(filterProvider);
        jsonGenerator = mapper.getFactory().createGenerator(writer);
        when(filterProvider.findPropertyFilter("MyFilterId", Object.class)).thenReturn(null);

        objectSerializer.serialize(new Object(), jsonGenerator, mapper.getSerializerProvider());
        ObjectNode jsonNode = JsonParser.parseNumbericString(writer.toString());
        assertNotNull(jsonNode.get("filterValue"));

        objectSerializer.serialize(new Object(), jsonGenerator, mapper.getSerializerProvider(), filterNode);
        ObjectNode filteredJsonNode = JsonParser.parseNumbericString(writer.toString());
        assertEquals(filterNode.toString(), filteredJsonNode.toString());
    }

    private ObjectNode createFilterNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode filterNode = mapper.createObjectNode();
        filterNode.put("filterId", "MyFilterId");
        return filterNode;
    }
}