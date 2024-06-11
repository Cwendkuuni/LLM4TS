package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class StdSerializerTest extends BaseTest {

    private StdSerializer<Object> serializer;
    private ObjectNode schemaNode;

    @Before
    public void setUp() throws Exception {
        serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {}
        };
        schemaNode = JsonNodeFactory.instance.objectNode();
    }

    @Test
    public void testGetSchema() throws Exception {
        assertEquals("string", serializer.getSchema(null, Object.class).path("type").asText());
    }

    @Test
    public void testGetSchemaOptional() throws Exception {
        schemaNode = serializer.getSchema(null, Object.class, true);
        assertTrue(schemaNode.get("required").booleanValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = createMock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
        verify(visitor).expectAnyFormat(Object.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullProvider() throws Exception {
        serializer.wrapAndThrow(null, new IOException(), null, null);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testIsDefaultSerializer() throws Exception {
        assertTrue(StdSerializerTest.class.getDeclaredField("serializer").get(this) instanceof StdSerializer);
        assertFalse(StdSerializerTest.class.getDeclaredField("serializer").get(this) instanceof JacksonStdImpl);

        Object jacksonStdSerializer = new JacksonStdImpl() {};
        assertTrue(jacksonStdSerializer instanceof StdSerializer);
        assertTrue(jacksonStdSerializer instanceof JacksonStdImpl);
    }

    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        ObjectNode delegateTypeNode = JsonNodeFactory.instance.objectNode();
        delegateTypeNode.put("type", "java.lang.String");

        SerializerProvider provider = new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(JavaType type) {
                return new StdSerializer<String>(String.class) {
                    @Override
                    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                            throws IOException, JsonGenerationException {
                    }
                };
            }

            @Override
            public FilterProvider getFilterProvider() {
                return null;
            }
        };

        JsonSerializer<Object> existingSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {}
        };

        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.getOutputType(any(TypeFactory.class))).thenReturn(String.class);
        when(provider.converterInstance(any(AnnotatedMember.class), eq(Converter.class))).thenReturn(converter);

        JsonSerializer<Object> result = serializer.findConvertingContentSerializer(provider, null, null);
        assertEquals(existingSerializer.getClass(), result.getClass());
        JsonSerializer<Object> delegateSerializer = result.getDelegate();
        assertEquals(String.class, delegateSerializer.handledType());
    }

    @Test
    public void testFindPropertyFilter() throws Exception {
        FilterProvider filterProvider = new FilterProvider() {
            @Override
            public PropertyFilter findPropertyFilter(String filterId, Object valueToFilter) {
                if (filterId.equals("filterId")) {
                    return new PropertyFilter() {};
                }
                return null;
            }
        };

        PropertyFilter result = serializer.findPropertyFilter(new SerializerProvider() {
            @Override
            public FilterProvider getFilterProvider() {
                return filterProvider;
            }
        }, "filterId", null);
        assertNotNull(result);
    }
}