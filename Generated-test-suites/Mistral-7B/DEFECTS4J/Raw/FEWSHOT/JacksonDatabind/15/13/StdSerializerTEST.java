package com.fasterxml.jackson.databind.ser.std;

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

import org.junit.Test;

public class StdSerializerTest {

    @Test
    public void testDefaultConstructor() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testTypeConstructor() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        StdSerializer<String> serializer = new StdSerializer<String>(javaType);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testAlternateConstructor() {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = new StdSerializer<String>(stringClass, false);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testGetSchema() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertEquals(createSchemaNode("string"), serializer.getSchema(null, null));
    }

    @Test
    public void testGetSchemaWithIsOptional() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        ObjectNode schema = serializer.getSchema(null, null, true);
        assertEquals("string", schema.get("type").asText());
        assertEquals(true, schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullProvider() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        serializer.wrapAndThrow(null, new IOException(), null, "");
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullBean() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        serializer.wrapAndThrow(null, new IOException(), null, "");
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullFieldName() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        serializer.wrapAndThrow(null, new IOException(), null, "");
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertTrue(StdSerializer.class.isInstance(new Object() {}));
        assertFalse(StdSerializer.class.isInstance(new Object() {}));
        assertFalse(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isInstance(serializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> existingSerializer = mapper.findValueSerializer(Object.class);
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        PropertyFilter filter = new SimpleFilterProvider().findPropertyFilter(null, null);
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonSerializer<Object> convertingSerializer = serializer.findConvertingContentSerializer(provider, null, existingSerializer);
        assertNotNull(convertingSerializer);
        assertSame(filter, convertingSerializer.getProvider().getFilterProvider().findPropertyFilter(null, null));
    }

    @Test
    public void testFindConvertingContentSerializerNullExistingSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonSerializer<Object> convertingSerializer = serializer.findConvertingContentSerializer(provider, null, null);
        assertNull(convertingSerializer);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        PropertyFilter filter = serializer.findPropertyFilter(provider, "testFilterId", null);
        assertNull(filter);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterNullProvider() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        serializer.findPropertyFilter(null, "testFilterId", null);
    }

    private ObjectNode createSchemaNode(String type) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", type);
        return schema;
    }
}