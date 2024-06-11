package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class StdSerializerTest {

    @Test
    public void testDefaultConstructor() {
        StdSerializer<?> serializer = new StdSerializer<>(Object.class);
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testConstructorWithType() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testConstructorWithTypeAndDummy() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class, true);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testGetSchema() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectNode schema = serializer.getSchema(null, Object.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaWithIsOptional() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectNode schema = serializer.getSchema(null, Object.class, true);
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.hasNonNull("required"));
        assertEquals(false, schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper(), null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullProvider() {
        StdSerializer<?> serializer = new StdSerializer<>(Object.class);
        serializer.wrapAndThrow(null, new RuntimeException("Boom"), null, null);
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        assertTrue(StdSerializer.class.isInstance(new StdSerializer<>(String.class)));
        assertFalse(StdSerializer.class.isInstance(new MyCustomSerializer<>()));
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertFalse(StdSerializer.class.isInstance(new MyCustomSerializer<>()));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<List<Integer>> listSerializer = new StdSerializer<>(List.class);
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        StdSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
        StdSerializer<Object> defaultSerializer = mapper.findValueSerializer(Object.class);

        StdSerializer<List<Integer>> result = listSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, javaType);
        assertTrue(StdSerializer.class.isInstance(result));
        assertEquals(List.class, result.handledType());
        assertEquals(intSerializer, result.delegate);

        result = listSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, javaType);
        assertTrue(StdSerializer.class.isInstance(result));
        assertEquals(List.class, result.handledType());
        assertEquals(intSerializer, result.delegate);

        StdSerializer<List<String>> stringListSerializer = new StdSerializer<>(List.class);
        result = stringListSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, javaType);
        assertTrue(StdSerializer.class.isInstance(result));
        assertEquals(List.class, result.handledType());
        assertEquals(defaultSerializer, result.delegate);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        Object filterId = new Object();
        Object valueToFilter = "filter value";
        PropertyFilter filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), filterId, valueToFilter);
        assertNull(filter);

        mapper.getSerializerProvider().setFilterProvider(new SimpleFilterProvider().setFilter("test", new MyPropertyFilter()));
        filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), filterId, valueToFilter);
        assertNotNull(filter);
    }
}

class MyCustomSerializer<T> extends StdSerializer<T> {}

class MyPropertyFilter implements PropertyFilter {}