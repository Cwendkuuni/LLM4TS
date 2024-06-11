package com.fasterxml.jackson.databind.ser.std;

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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testStdSerializerDefaultConstructor() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testStdSerializerTypeConstructor() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        StdSerializer<String> serializer = new StdSerializer<String>(javaType);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testStdSerializerAlternateConstructor() throws Exception {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = new StdSerializer<String>(stringClass, false);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        ObjectNode node = mapper.createObjectNode();
        serializer.serialize(null, mapper.getFactory().createGenerator(node), mapper.getSerializerProvider());
        assertTrue(node.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetSchemaNullProvider() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        serializer.getSchema(null, null);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testGetSchema() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        ObjectNode schemaNode = serializer.getSchema(mapper.getSerializerProvider(), null);
        assertEquals("string", schemaNode.get("type").asText());
        assertTrue(schemaNode.isEmpty());
    }

    @Test
    public void testCreateSchemaNode() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        ObjectNode schemaNode = serializer.createSchemaNode("number");
        assertEquals("number", schemaNode.get("type").asText());
        assertTrue(schemaNode.isEmpty());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        JsonFormatVisitorWrapper visitor = mapper.getFactory().createJsonFormatVisitor(null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testWrapAndThrowNullProvider() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        throw serializer.wrapAndThrow(null, new Exception("Test Exception"), null, null);
        fail("Expected error or checked exception");
    }

    @Test
    public void testWrapAndThrow() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        Object node = mapper.createObjectNode();
        Throwable exception = new IOException("Test IOException");
        serializer.wrapAndThrow(mapper.getSerializerProvider(), exception, node, null);
        fail("Expected error or checked exception");
    }

    @Test
    public void testIsDefaultSerializer() throws Exception {
        StdSerializer<String> serializer1 = new StdSerializer<String>(String.class);
        StdSerializer<Integer> serializer2 = new StdSerializer<Integer>(Integer.class);
        assertTrue(StdSerializer.class.isInstance(serializer1));
        assertTrue(StdSerializer.class.isInstance(serializer2));
        assertFalse(StdSerializer.class.isInstance(new ObjectMapper()));
        assertFalse(StdSerializer.class.isInstance(new JsonFactory()));
        assertTrue(StdSerializer.class.isInstance(serializer1));
        assertFalse(StdSerializer.class.isInstance(serializer2));
    }

    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        StdSerializer<List<String>> serializer = new StdSerializer<List<String>>(List.class) {
            @Override
            public void serialize(List<String> value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }
        };
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, String.class);
        StdSerializer<List<String>> existingSerializer = new StdListSerializer(String.class);
        JsonSerializer<List<String>> convertingSerializer = mapper.findValueSerializer(javaType);
        JsonSerializer<?> result = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, null);
        assertTrue(result instanceof StdDelegatingSerializer);
        assertEquals(convertingSerializer, result.delegate);
    }

    @Test
    public void testFindPropertyFilter() throws Exception {
        FilterProvider filterProvider = mapper.getSerializerProvider().getFilterProvider();
        PropertyFilter filter = filterProvider.findPropertyFilter("testFilterId", new Object());
        assertNull(filter);
    }
}