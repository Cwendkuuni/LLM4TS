package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Integer> integerSerializer = new StdSerializer<Integer>(Integer.class);
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonSerializer<Object> jacksonStdImplSerializer = new StdSerializer<Object>(Object.class, true);

        assertTrue(integerSerializer.isDefaultSerializer(integerSerializer));
        assertTrue(integerSerializer.isDefaultSerializer(stringSerializer));
        assertTrue(integerSerializer.isDefaultSerializer(jacksonStdImplSerializer));
        assertFalse(stringSerializer.isDefaultSerializer(integerSerializer));
        assertFalse(stringSerializer.isDefaultSerializer(stringSerializer));
        assertFalse(stringSerializer.isDefaultSerializer(jacksonStdImplSerializer));
        assertFalse(jacksonStdImplSerializer.isDefaultSerializer(integerSerializer));
        assertFalse(jacksonStdImplSerializer.isDefaultSerializer(stringSerializer));
        assertFalse(jacksonStdImplSerializer.isDefaultSerializer(jacksonStdImplSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                return createSchemaNode("string");
            }
        };
        BeanDescription desc = mapper.constructTypeDescription(String[].class);
        JsonSerializer<Object> expectedSerializer = mapper.findValueSerializer(JavaType.constructRawType(String[].class));

        JsonSerializer<Object> actualSerializer = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, new Object());

        assertEquals(expectedSerializer, actualSerializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindConvertingContentSerializerWithNullSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                return createSchemaNode("string");
            }
        };
        JsonSerializer<Object> expectedSerializer = null;

        serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, new Object());

        fail();
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                return createSchemaNode("string");
            }
        };
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        PropertyFilter filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), "filterId", node);

        assertNull(filter);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithNullProvider() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                return createSchemaNode("string");
            }
        };

        serializer.findPropertyFilter(null, "filterId", new Object());

        fail();
    }
}