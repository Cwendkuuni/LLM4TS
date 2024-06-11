package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;

import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = JsonMapperFactory.builder().build();
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                // Overridden for testing
            }
        };

        JsonSerializer<Object> defaultSerializer = objectMapper.findValueSerializer(Object.class);

        assertTrue(StdSerializer.class.isInstance(defaultSerializer));
        assertTrue(StdSerializer.class.isInstance(stdSerializer));
        assertTrue(StdSerializer.class.isAssignableFrom(defaultSerializer.getClass()));
        assertTrue(StdSerializer.class.isAssignableFrom(stdSerializer.getClass()));
        assertFalse(defaultSerializer instanceof StdSerializer);
        assertEquals(StdSerializer.class, defaultSerializer.getClass().getSuperclass());
        assertFalse(stdSerializer instanceof StdSerializer);
        assertEquals(StdSerializer.class, stdSerializer.getClass().getSuperclass());
        assertTrue(StdSerializer.class.isInstance(StdSerializer.class.cast(defaultSerializer)));
        assertTrue(StdSerializer.class.isInstance(StdSerializer.class.cast(stdSerializer)));
        assertTrue(StdSerializer.class.isInstance(StdSerializer.class.cast(defaultSerializer).getHandler()));
        assertTrue(StdSerializer.class.isInstance(StdSerializer.class.cast(stdSerializer).getHandler()));
        assertTrue(StdSerializer.class.isInstance(StdSerializer.class.cast(defaultSerializer)._handledType));
        assertTrue(StdSerializer.class.isInstance(StdSerializer.class.cast(stdSerializer)._handledType));
        assertEquals(Object.class, StdSerializer.class.cast(defaultSerializer)._handledType);
        assertEquals(Object.class, StdSerializer.class.cast(stdSerializer)._handledType);
        assertEquals(StdSerializer.class, StdSerializer.class.cast(defaultSerializer).getClass());
        assertEquals(StdSerializer.class, StdSerializer.class.cast(stdSerializer).getClass());

        assertTrue(StdSerializer.class.isAssignableFrom(defaultSerializer.getClass()));
        assertTrue(StdSerializer.class.isAssignableFrom(stdSerializer.getClass()));

        assertTrue(StdSerializer.class.isInstance(StdSerializer.class.cast(defaultSerializer).getHandler()));
        assertTrue(StdSerializer.class.isInstance(StdSerializer.class.cast(stdSerializer).getHandler()));

        assertEquals(Object.class, StdSerializer.class.cast(defaultSerializer)._handledType);
        assertEquals(Object.class, StdSerializer.class.cast(stdSerializer)._handledType);

        assertTrue(isDefaultSerializer(defaultSerializer));
        assertFalse(isDefaultSerializer(stdSerializer));
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public ObjectNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                // Overridden for testing
                return null;
            }
        };

        ObjectNode schema = stdSerializer.getSchema(objectMapper.getSerializerProvider(), Object.class);
        assertNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // Overridden for testing
            }
        };

        JsonFormatVisitorWrapper visitor = objectMapper.getPolymorphicSerializerProvider().findFormatVisitor(Object.class);
        stdSerializer.acceptJsonFormatVisitor(visitor, Object.class);
    }

    @Test
    public void testFindConvertingContentSerializer() {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                // Overridden for testing
            }
        };

        SerializerProvider provider = objectMapper.getSerializerProvider();
        Object property = new Object();
        PropertyFilter propertyFilter = new PropertyFilter() {
            @Override
            public void serializeAsSchema(Object value, JsonSerializer<Object> s, ObjectNode schema, SerializerProvider p)
                    throws IOException {
                // Overridden for testing
            }

            @Override
            public Boolean filterProperty(SerializerProvider provider, Object value, String name) throws JsonMappingException {
                // Overridden for testing
                return null;
            }
        };

        FilterProvider filters = new FilterProvider() {
            @Override
            public PropertyFilter findPropertyFilter(String id, Object value) throws JsonMappingException {
                if (id.equals("id")) {
                    return propertyFilter;
                }
                return null;
            }
        };

        provider.setFilterProvider(filters);

        JsonSerializer<Object> existingSerializer = objectMapper.findValueSerializer(Object[].class);

        JsonSerializer<Object> convertingSerializer = stdSerializer.findConvertingContentSerializer(provider,
                property, existingSerializer);

        assertNotNull(convertingSerializer);
        assertTrue(convertingSerializer instanceof StdDelegatingSerializer);
    }

    @Test
    public void testFindPropertyFilter() {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void findPropertyFilter(Object filterId, Object valueToFilter, SerializerProvider provider,
                    BeanProperty property) throws JsonMappingException {
                // Overridden for testing
                return null;
            }
        };

        SerializerProvider provider = objectMapper.getSerializerProvider();
        Object filterId = "id";
        Object valueToFilter = new Object();

        PropertyFilter propertyFilter = stdSerializer.findPropertyFilter(provider, filterId, valueToFilter);
        assertNull(propertyFilter);
    }
}