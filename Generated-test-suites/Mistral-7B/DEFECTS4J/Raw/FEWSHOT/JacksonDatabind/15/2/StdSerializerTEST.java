package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

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

import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {

    private ObjectNode schemaNode;
    private StdSerializer<String> stringSerializer;

    @Before
    public void setup() {
        schemaNode = new JsonNodeFactory(StandardSerializerFeatures.STANDARD_FEATURES).objectNode();
        stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }
        };
    }

    @Test
    public void testIsDefaultSerializer() {
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertTrue(StdSerializer.class.isInstance(new StdSerializer<Object>(Object.class)));
        assertFalse(StdSerializer.class.isInstance(new CustomSerializer<>()));
    }

    @Test
    public void testGetSchema() {
        assertEquals(schemaNode, stringSerializer.getSchema(new SerializerProvider(), String.class));
    }

    @Test
    public void testGetSchemaWithIsOptional() {
        ObjectNode schema = stringSerializer.getSchema(new SerializerProvider(), String.class, true);
        assertTrue(schema.has("required"));
        assertFalse(schema.getBoolean("required"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        // Default implementation
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAcceptJsonFormatVisitorWithNullTypeHint() {
        stringSerializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testFindConvertingContentSerializer() {
        JsonSerializer<Object> existingSerializer = new StdSerializer<Object>(Object.class);
        assertEquals(existingSerializer, stringSerializer.findConvertingContentSerializer(new SerializerProvider(), null, null));

        ObjectNode converterAnnotation = schemaNode.objectNode();
        converterAnnotation.put("converter.impl", CustomConverter.class.getName());
        schemaNode = schemaNode.set("properties", converterAnnotation);

        ObjectNode propertyNode = schemaNode.objectNode();
        propertyNode.put("type", "string");
        propertyNode.set("properties", schemaNode);
        schemaNode = schemaNode.set("type", "object");
        schemaNode.put("properties", propertyNode);

        JsonSerializer<Object> customConverter = new CustomSerializer<>();
        JavaType delegateType = TypeFactory.defaultInstance().constructType(String.class);
        StdSerializer<String> delegatingSerializer = new StdDelegatingSerializer<>(customConverter, delegateType, existingSerializer);

        JsonSerializer<Object> result = stringSerializer.findConvertingContentSerializer(new SerializerProvider(), null, null);
        assertInstanceOf(StdDelegatingSerializer.class, result);
        assertSame(customConverter, ((StdDelegatingSerializer<?>) result).getDelegate());
    }

    @Test
    public void testFindPropertyFilter() {
        FilterProvider filterProvider = new SerializerProvider() {
            @Override
            public PropertyFilter findPropertyFilter(String id, Object valueToFilter)
                    throws JsonMappingException {
                return new PropertyFilter() {
                    @Override
                    public boolean shouldFilterProperty(MapperContext ctxt, Iterator<String> names, Object bean)
                            throws JsonProcessingException {
                        return true;
                    }
                };
            }
        };

        PropertyFilter result = stringSerializer.findPropertyFilter(new SerializerProvider(), "test", null);
        assertNull(result);

        result = stringSerializer.findPropertyFilter(filterProvider, "test", null);
        assertNotNull(result);
    }
}

class CustomConverter extends Converter<Object, Object> {
    @Override
    public Object convert(Object value, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return null;
    }

    @Override
    public Object convertAndAddBinding(Object value, AnnotatedMember member, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return null;
    }
}

class CustomSerializer<T> extends StdSerializer<T> {
    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonGenerationException {
        // no implementation
    }
}