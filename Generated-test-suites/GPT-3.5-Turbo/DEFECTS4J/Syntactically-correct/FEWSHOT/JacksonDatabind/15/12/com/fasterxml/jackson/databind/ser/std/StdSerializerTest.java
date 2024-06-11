package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {

    private StdSerializer<Object> serializer;

    @Before
    public void setup() {
        serializer = new StdSerializer<>(Object.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                // Empty implementation
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                // Empty implementation
            }
        };
    }

    @Test
    public void testConstructedSerializer() {
        assertNotNull(serializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(new Object(), jgen, provider);
        Mockito.verify(jgen).writeObject(Mockito.any());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Object.class;
        assertNull(serializer.getSchema(provider, typeHint));
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Object.class;
        boolean isOptional = true;

        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", "string");
        schema.put("required", !isOptional);

        assertEquals(schema, serializer.getSchema(provider, typeHint, isOptional));
    }

    @Test
    public void testCreateObjectNode() {
        assertNotNull(serializer.createObjectNode());
    }

    @Test
    public void testCreateSchemaNode() {
        ObjectNode schema = serializer.createSchemaNode("string");
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testCreateSchemaNodeWithOptional() {
        ObjectNode schema = serializer.createSchemaNode("string", true);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
        assertEquals(false, schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, mock(JavaType.class));
        Mockito.verify(visitor).expectAnyFormat(Mockito.any());
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Throwable t = new RuntimeException();

        try {
            serializer.wrapAndThrow(provider, t, new Object(), "fieldName");
        } catch (RuntimeException e) {
            assertEquals(t, e);
        }
    }

    @Test
    public void testWrapAndThrowWithJsonMappingException() throws IOException {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Throwable t = new JsonMappingException("Test");

        try {
            serializer.wrapAndThrow(provider, t, new Object(), "fieldName");
        } catch (JsonMappingException e) {
            assertEquals(t, e);
        }
    }

    @Test
    public void testWrapAndThrowWithIOException() throws IOException {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Throwable t = new IOException("Test");

        try {
            serializer.wrapAndThrow(provider, t, new Object(), "fieldName");
        } catch (IOException e) {
            assertEquals(t, e);
        }
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty prop = Mockito.mock(BeanProperty.class);
        @SuppressWarnings("unchecked")
        JsonSerializer<Object> existingSerializer = Mockito.mock(JsonSerializer.class);
        Converter<Object,Object> conv = Mockito.mock(Converter.class);

        Mockito.when(provider.getTypeFactory()).thenReturn(mock(JavaType.class));
        Mockito.when(conv.getOutputType(provider.getTypeFactory())).thenReturn(mock(JavaType.class));

        JsonSerializer<?> result = serializer.findConvertingContentSerializer(provider, prop, existingSerializer);
        assertNotNull(result);
        assertEquals(conv, ((StdDelegatingSerializer) result).getConverter());
        assertEquals(existingSerializer, ((StdDelegatingSerializer) result).getDelegatee());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Object filterId = "filterId";
        Object valueToFilter = new Object();
        FilterProvider filters = Mockito.mock(FilterProvider.class);
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);

        Mockito.when(provider.getFilterProvider()).thenReturn(filters);
        Mockito.when(filters.findPropertyFilter(filterId, valueToFilter)).thenReturn(filter);

        PropertyFilter result = serializer.findPropertyFilter(provider, filterId, valueToFilter);
        assertEquals(filter, result);
    }

    @Test
    public void testIsDefaultSerializer() {
        assertFalse(serializer.isDefaultSerializer(mock(JsonSerializer.class)));
    }
}