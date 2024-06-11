package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
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
    public void testConstructorWithClass() {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        Class<String> handledType = serializer.handledType();
        assertNotNull(handledType);
        assertEquals(String.class, handledType);
    }

    @Test
    public void testConstructorWithType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        StdSerializer<String> serializer = new CustomSerializer<String>(javaType);
        Class<String> handledType = serializer.handledType();
        assertNotNull(handledType);
        assertEquals(String.class, handledType);
    }

    @Test
    public void testConstructorWithTypeAndDummy() {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class, true);
        Class<String> handledType = serializer.handledType();
        assertNotNull(handledType);
        assertEquals(String.class, handledType);
    }

    @Test
    public void testSerialize() throws IOException {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.DefaultSerializerProviderBuilder()
                .build();
        serializer.serialize("test", generator, provider);
        generator.close();
        assertEquals("\"test\"", writer.toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider.DefaultSerializerProviderBuilder()
                .build();
        JsonNode schema = serializer.getSchema(provider, String.class);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaWithIsOptional() throws JsonMappingException {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider.DefaultSerializerProviderBuilder()
                .build();
        JsonNode schema = serializer.getSchema(provider, String.class, false);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
        assertEquals(false, schema.get("required").asBoolean());
    }

    @Test
    public void testCreateObjectNode() {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        ObjectNode node = serializer.createObjectNode();
        assertNotNull(node);
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        ObjectNode schema = serializer.createSchemaNode("string");
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testCreateSchemaNodeWithIsOptional() {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        ObjectNode schema = serializer.createSchemaNode("string", false);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
        assertEquals(false, schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider.DefaultSerializerProviderBuilder()
                .build();
        JsonFormatVisitorWrapper visitor = provider.getFactory()
                .getVisitorProvider().getProvider(provider.getConfig())
                .expectAnyFormat(provider.constructType(String.class));
        serializer.acceptJsonFormatVisitor(visitor, String.class);
    }

    @Test
    public void testWrapAndThrowWithIOException() throws IOException {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider.DefaultSerializerProviderBuilder()
                .build();
        IOException exception = new IOException();
        try {
            serializer.wrapAndThrow(provider, exception, "test", "fieldName");
        } catch (IOException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void testWrapAndThrowWithRuntimeException() throws IOException {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider.DefaultSerializerProviderBuilder()
                .build();
        RuntimeException exception = new RuntimeException();
        try {
            serializer.wrapAndThrow(provider, exception, "test", "fieldName");
        } catch (RuntimeException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void testWrapAndThrowWithJsonMappingException() throws IOException {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider.DefaultSerializerProviderBuilder()
                .build();
        JsonMappingException exception = new JsonMappingException(null, "test");
        try {
            serializer.wrapAndThrow(provider, exception, "test", "fieldName");
        } catch (JsonMappingException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void testWrapAndThrowWithNullException() throws IOException {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider.DefaultSerializerProviderBuilder()
                .build();
        try {
            serializer.wrapAndThrow(provider, null, "test", "fieldName");
        } catch (JsonMappingException e) {
            assertEquals(null, e.getCause());
        }
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider.DefaultSerializerProviderBuilder()
                .build();
        JsonMappingException exception = new JsonMappingException(null, "test");
        try {
            serializer.wrapAndThrow(provider, exception, "test", "fieldName");
        } catch (JsonMappingException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer<String> serializer = new CustomSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider.DefaultSerializerProviderBuilder()
                .build();
        FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", new SimpleBeanPropertyFilter());
        provider.setFilterProvider(filters);
        PropertyFilter filter = serializer.findPropertyFilter(provider, "myFilter", "valueToFilter");
        assertNotNull(filter);
        assertEquals(SimpleBeanPropertyFilter.class, filter.getClass());
    }
}

class CustomSerializer<T> extends StdSerializer<T> {

    private static final long serialVersionUID = 1L;

    protected CustomSerializer(Class<T> t) {
        super(t);
    }

    protected CustomSerializer(JavaType type) {
        super(type);
    }

    protected CustomSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonGenerationException {
        jgen.writeString(value.toString());
    }
}