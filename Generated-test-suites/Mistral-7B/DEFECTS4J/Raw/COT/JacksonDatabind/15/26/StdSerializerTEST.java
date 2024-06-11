package com.fasterxml.jackson.databind.ser.std; // Import necessary packages and classes

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {

    private StdSerializer<String> stringSerializer;
    private StdSerializer<Integer> intSerializer;
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        stringSerializer = mapper.findValueSerializer(String.class);
        intSerializer = mapper.findValueSerializer(Integer.class);
    }

    

    // Test for handledType() method
    @Test
    public void testHandledType() {
        assertEquals(String.class, stringSerializer.handledType());
        assertEquals(Integer.class, intSerializer.handledType());
    }

    // Test for serialize() method (basic functionality)
    @Test
    public void testSerializeBasic() throws Exception {
        String value = "hello";
        JsonGenerator generator = new TestJsonGenerator();

        stringSerializer.serialize(value, generator, null);

        ObjectNode actual = generator.getOutput();
        ObjectNode expected = mapper.readTree("{\"@class\":\"String\",\"value\":\"hello\"}");

        assertEquals(expected, actual);
    }

    // Test for serialize() method (edge cases and exceptions)
    @Test(expected = IOException.class)
    public void testSerializeIOException() throws Exception {
        JsonGenerator generator = new TestJsonGenerator();
        generator.setFailOnEmpty();

        stringSerializer.serialize(null, generator, null);
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerializeJsonGenerationException() throws Exception {
        JsonGenerator generator = new TestJsonGenerator();
        generator.setFailOnEmpty();
        generator.setFailOnEmptyObject();

        stringSerializer.serialize(new String(""), generator, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeJsonMappingException() throws Exception {
        JsonGenerator generator = new TestJsonGenerator();
        generator.setFailOnEmpty();
        generator.setFailOnEmptyObject();

        stringSerializer.serialize(new Object(), generator, null);
    }

    @Test(expected = RuntimeException.class)
    public void testSerializeRuntimeException() throws Exception {
        JsonGenerator generator = new TestJsonGenerator();
        generator.setFailOnEmpty();
        generator.setFailOnEmptyObject();

        stringSerializer.serialize(new Throwable(), generator, null);
    }

    @Test
    public void testSerializeSchemaGeneration() throws Exception {
        SchemaAware schemaAware = stringSerializer;
        ObjectNode schemaNode = schemaAware.getSchema(null, String.class);
        ObjectNode expectedSchema = mapper.readTree("{\"type\":\"string\"}");

        assertEquals(expectedSchema, schemaNode);
    }

    @Test
    public void testSerializeSchemaGenerationOptional() throws Exception {
        SchemaAware schemaAware = stringSerializer;
        ObjectNode schemaNode = schemaAware.getSchema(null, String.class, true);
        ObjectNode expectedSchema = mapper.readTree("{\"type\":\"string\",\"required\":false}");

        assertEquals(expectedSchema, schemaNode);
    }

    @Test
    public void testAcceptJsonFormatVisitorWrapper() throws Exception {
        JsonSerializer<Object> visitor = new TestJsonSerializerVisitor();
        stringSerializer.acceptJsonFormatVisitorWrapper(visitor, null);
        assertEquals(JsonFormatFeature.ANY_FORMAT, visitor.getFormatFeature());
    }

    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = new AnnotatedClass(String[].class).findProperty("0");

        JsonSerializer<?> existingSerializer = intSerializer;
        JsonSerializer<?> contentSerializer = stringSerializer.findConvertingContentSerializer(provider, property, existingSerializer);

        assertNotNull(contentSerializer);
        assertTrue(contentSerializer instanceof StdDelegatingSerializer);
    }

    @Test
    public void testFindPropertyFilter() throws Exception {
        SerializerProvider provider = mapper.getSerializerProvider();
        Object filterId = new Object();
        Object valueToFilter = new Object();

        PropertyFilter filter = intSerializer.findPropertyFilter(provider, filterId, valueToFilter);

        assertNull(filter);
    }

    
}