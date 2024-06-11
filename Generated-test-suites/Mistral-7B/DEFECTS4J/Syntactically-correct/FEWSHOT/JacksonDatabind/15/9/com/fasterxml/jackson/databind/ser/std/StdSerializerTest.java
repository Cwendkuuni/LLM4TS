package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

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

/**
 * Unit tests for {@link StdSerializer}.
 */
public class StdSerializerTest {

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonNode schema = stringSerializer.getSchema(new JsonFactory(), String.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        StdSerializer<Boolean> booleanSerializer = new StdSerializer<Boolean>(Boolean.class);
        JsonNode schema = booleanSerializer.getSchema(new JsonFactory(), Boolean.class, true);
        assertEquals("boolean", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper());
        objectSerializer.acceptJsonFormatVisitor(visitor, Object.class);
        assertEquals("any", visitor.getFormatType().getSchemaName());
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        SerializerProvider provider = new SerializerProvider();
        Throwable exception = new RuntimeException("Test exception");
        Object bean = new Object();
        int index = 0;

        serializer.wrapAndThrow(provider, exception, bean, index);
        throw exception;
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> serializer1 = new StdSerializer<Object>(Object.class);
        JsonSerializer<Object> serializer2 = new StdSerializer<Object>(Object.class) {
            @JacksonStdImpl
            public boolean isEmpty() {
                return false;
            }
        };
        assertTrue(StdSerializer.class.isInstance(serializer1));
        assertTrue(StdSerializer.class.isInstance(serializer2));
        assertTrue(StdSerializer.class.isInstance(serializer1));
        assertFalse(StdSerializer.class.isInstance(new ObjectSerializer() {}));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("list", "[1, 2, 3]");
        SerializerProvider provider = new SerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty prop = mapper.findBeanProperty("list", List.class);

        StdSerializer<Object> existingSerializer = provider.findValueSerializer(JsonType. constructValueType(List.class, Integer.class));
        JsonSerializer<Object> customSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeValueAsString("[1, 2, 3]");
            }
        };

        JsonSerializer<Object> result = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                customSerializer.serialize(value, jgen, provider);
            }
        }.findConvertingContentSerializer(provider, prop, existingSerializer);

        assertEquals(customSerializer.getClass(), result.getClass());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        Object filterId = new Object();
        Object valueToFilter = new Object();
        PropertyFilter result = new StdSerializer<Object>(Object.class).findPropertyFilter(provider, filterId, valueToFilter);
        assertNull(result);
    }
}