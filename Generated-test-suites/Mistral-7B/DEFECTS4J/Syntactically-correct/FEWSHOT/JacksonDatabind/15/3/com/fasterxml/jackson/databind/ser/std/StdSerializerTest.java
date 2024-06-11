package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {

    @Test
    public void testUtilIsDefaultSerializer() {
        StdSerializer<?> serializer = new StdSerializer<Object>(Object.class) {};
        assertTrue(StdSerializer.isDefaultSerializer(serializer));
    }

    @Test
    public void testFindConvertingContentSerializerNull() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(Object.class);
        assertNull(serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, null));
    }

    @Test
    public void testFindConvertingContentSerializerPresent() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<List<Integer>> listSerializer = mapper.findValueSerializer(List.class);
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
        Converter<Object, Object> converter = mapper.converterInstance(Object.class, JsonNode.class);
        PropertyFilter filter = mapper.getFilterProvider().findPropertyFilter("customFilterId", null);

        assertEquals(intSerializer, listSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, null));
        assertEquals(converter, listSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, JsonNode.class));
        assertEquals(filter, listSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), Object.class, null));
    }

    @Test
    public void testFindConvertingContentSerializerNullContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<List<Integer>> listSerializer = mapper.findValueSerializer(List.class);
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
        Converter<Object, Object> converter = mapper.converterInstance(Object.class, JsonNode.class);
        PropertyFilter filter = mapper.getFilterProvider().findPropertyFilter("customFilterId", null);

        assertNull(listSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, Integer.class));
        assertNull(listSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, Object.class));
        assertNull(listSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, null));
    }

    @Test
    public void testFindPropertyFilterNullId() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(Object.class);
        FilterProvider filters = mapper.getFilterProvider();
        Object valueToFilter = new Object();

        assertNull(serializer.findPropertyFilter(mapper.getSerializerProvider(), null, valueToFilter));
    }

    @Test
    public void testFindPropertyFilterPresent() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(Object.class);
        FilterProvider filters = mapper.getFilterProvider();
        Object valueToFilter = new Object();
        PropertyFilter filter = filters.findPropertyFilter("customFilterId", valueToFilter);

        assertEquals(filter, serializer.findPropertyFilter(mapper.getSerializerProvider(), "customFilterId", valueToFilter));
    }

    @Test
    public void testGetSchemaNull() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(Object.class);

        assertNull(serializer.getSchema(mapper.getSerializerProvider(), null));
    }

    @Test
    public void testGetSchemaString() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(Object.class);

        ObjectNode schema = serializer.getSchema(mapper.getSerializerProvider(), String.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaBoolean() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Boolean> serializer = mapper.findValueSerializer(Boolean.class);

        ObjectNode schema = serializer.getSchema(mapper.getSerializerProvider(), Boolean.class);
        assertEquals("boolean", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorNull() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(Object.class);

        serializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorString() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(Object.class);

        JsonFormatVisitorWrapper visitor = mapper.getPolymorphicVisitor();
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
    }

    @Test
    public void testAcceptJsonFormatVisitorBoolean() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Boolean> serializer = mapper.findValueSerializer(Boolean.class);

        JsonFormatVisitorWrapper visitor = mapper.getPolymorphicVisitor();
        serializer.acceptJsonFormatVisitor(visitor, Boolean.class);
    }
}