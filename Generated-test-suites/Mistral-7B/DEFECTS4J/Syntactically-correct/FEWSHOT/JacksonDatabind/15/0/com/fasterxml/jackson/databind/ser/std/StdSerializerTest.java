package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class StdSerializerTest {

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        JsonSerializer<String> otherSerializer = new CustomStringSerializer();

        assertNotNull(serializer);
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isAssignableFrom(serializer.getClass()));
        assertTrue(serializer instanceof SchemaAware);
        assertTrue(serializer instanceof JsonFormatVisitable);
        assertTrue(serializer instanceof JsonSerializer);
        assertTrue(serializer instanceof Serializable);

        assertTrue(StdSerializer.class.isInstance(otherSerializer));
        assertFalse(StdSerializer.class.isAssignableFrom(otherSerializer.getClass()));
        assertFalse(otherSerializer instanceof SchemaAware);
        assertFalse(otherSerializer instanceof JsonFormatVisitable);
        assertFalse(otherSerializer instanceof JsonSerializer);
        assertFalse(otherSerializer instanceof Serializable);

        assertFalse(StdSerializer.class.isInstance(null));
        assertFalse(StdSerializer.class.isAssignableFrom(null));
        assertFalse(null instanceof SchemaAware);
        assertFalse(null instanceof JsonFormatVisitable);
        assertFalse(null instanceof JsonSerializer);
        assertFalse(null instanceof Serializable);

        assertTrue(StdSerializer.class.isInstance(new StdSerializer<Object>(Object.class)));
        assertTrue(StdSerializer.class.isAssignableFrom(StdSerializer.class));
        assertTrue(StdSerializer.class.isInstance(new StdSerializer<Object>(Object.class.getType())));
        assertTrue(StdSerializer.class.isInstance(new StdSerializer<Object>(Object.class, false)));

        assertTrue(StdSerializer.class.isInstance(new StdSerializer<String[]>(String[].class)));
        assertTrue(StdSerializer.class.isAssignableFrom(StdSerializer[].class));
        assertTrue(StdSerializer.class.isInstance(new StdSerializer<String[]>(String[].class.getType())));
        assertFalse(StdSerializer.class.isInstance(new StdSerializer<String[]>(String[].class, false)));

        // Test with a custom serializer
        assertFalse(StdSerializer.class.isInstance(serializer));
        assertFalse(StdSerializer.class.isAssignableFrom(serializer.getClass()));
        assertTrue(serializer instanceof CustomStringSerializer);
        assertFalse(serializer instanceof SchemaAware);
        assertFalse(serializer instanceof JsonFormatVisitable);
        assertFalse(serializer instanceof JsonSerializer);
        assertFalse(serializer instanceof Serializable);

        // Test with a null serializer
        assertNull(StdSerializer.class.cast(null));

        // Test with a non-StdSerializer
        JsonSerializer<String> nonStdSerializer = new NonStdSerializer<>();
        assertFalse(StdSerializer.class.isInstance(nonStdSerializer));
        assertFalse(StdSerializer.class.isAssignableFrom(nonStdSerializer.getClass()));
        assertFalse(nonStdSerializer instanceof SchemaAware);
        assertFalse(nonStdSerializer instanceof JsonFormatVisitable);
        assertFalse(nonStdSerializer instanceof JsonSerializer);
        assertFalse(nonStdSerializer instanceof Serializable);
    }

    @Test
    public void testHandledType() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        String value = "Hello, World!";
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(stringWriter);
        serializer.serialize(value, jgen, new SerializerProvider());
        jgen.flush();

        String result = stringWriter.toString();
        assertEquals("\"Hello, World!\"", result);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        ObjectNode schema = serializer.getSchema(new SerializerProvider(), String.class);
        assertEquals("\"string\"", schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StandardSerializerFactory(ObjectNode.class));
        serializer.acceptJsonFormatVisitor(visitor, String.class);
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        try {
            throw new RuntimeException("Test exception");
        } catch (RuntimeException e) {
            serializer.wrapAndThrow(new SerializerProvider(), e, new Object(), "test");
        }

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);

        ObjectNode objectNode = serializer.getSchema(new SerializerProvider(), Object.class);
        assertEquals("{\"type\":\"object\"}", objectNode.toString());

        SerializerProvider provider = new SerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<List<String>> listSerializer = mapper.findValueSerializer(List.class);
        JsonSerializer<Map<String, String>> mapSerializer = mapper.findValueSerializer(Map.class);

        JsonSerializer<Object> existingSerializer = serializer;
        JsonSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonSerializer<List<String>> listStringSerializer = new StdDelegatingSerializer<List<String>>(listSerializer, String[].class, stringSerializer);
        JsonSerializer<Map<String, String>> mapStringSerializer = new StdDelegatingSerializer<Map<String, String>>(mapSerializer, Map.class, stringSerializer);

        JsonSerializer<Object> foundSerializer = serializer.findConvertingContentSerializer(provider, null, new Object());
        assertNotNull(foundSerializer);
        assertSame(existingSerializer, foundSerializer);

        ObjectNode listSchema = serializer.getSchema(provider, List[].class);
        assertEquals("{ \"type\": \"array\", \"items\": { \"type\": \"string\" } }", listSchema.toString());

        ObjectNode mapSchema = serializer.getSchema(provider, Map.class);
        assertEquals("{ \"type\": \"object\", \"properties\": { }, \"additionalProperties\": { \"type\": \"string\" } }", mapSchema.toString());

       foundSerializer = serializer.findConvertingContentSerializer(provider, List[].class, null);
        assertNotNull(foundSerializer);
        assertSame(listSerializer, foundSerializer);

        foundSerializer = serializer.findConvertingContentSerializer(provider, Map.class, null);
        assertNotNull(foundSerializer);
        assertSame(mapSerializer, foundSerializer);

        foundSerializer = serializer.findConvertingContentSerializer(provider, String[].class, null);
        assertNotNull(foundSerializer);
        assertSame(stringSerializer, foundSerializer);

        foundSerializer = serializer.findConvertingContentSerializer(provider, String[].class, new Object[]{});
        assertNotNull(foundSerializer);
        assertSame(listStringSerializer, foundSerializer);

        foundSerializer = serializer.findConvertingContentSerializer(provider, Map.class, new Object[]{});
        assertNotNull(foundSerializer);
        assertSame(mapStringSerializer, foundSerializer);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = serializer.findPropertyFilter(provider, "testFilterId", new Object());
        assertNull(filter);
    }
}

class CustomStringSerializer extends StdSerializer<String> {
    public CustomStringSerializer() {
        this(String.class);
    }

    public CustomStringSerializer(Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString("custom-" + value);
    }
}

class NonStdSerializer<T> implements JsonSerializer<T> {
    // Empty class
}