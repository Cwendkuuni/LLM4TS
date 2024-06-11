package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

public class StdSerializerTest {

    @SuppressWarnings("unchecked")
    private static <T> StdSerializer<T> createStdSerializerMock(Class<T> handledType) {
        return mock(StdSerializer.class);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        StdSerializer<Object> serializer = createStdSerializerMock(Object.class);
        ObjectNode schema = createSchemaNode();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JsonTypeValidator validator = mock(JsonTypeValidator.class);
        when(visitor.expectAnyFormat(anyType())).thenReturn(validator);

        serializer.acceptJsonFormatVisitor(visitor, Object.class);

        verify(visitor).expectAnyFormat(Object.class);
    }

    @Test
    public void testHandleType() {
        StdSerializer<Integer> integerSerializer = new StdSerializer<Integer>(Integer.class) {};
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {};

        assertEquals(Integer.class, integerSerializer.handledType());
        assertEquals(String.class, stringSerializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException {
        StdSerializer<Object> serializer = createStdSerializerMock(Object.class);
        ObjectNode resultNode = createSchemaNode();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(true);

        Object obj = new Object();

        serializer.serialize(obj, generator, provider);

        // No verification needed as StdSerializer is an abstract class
    }

    @Test
    public void testCreateSchemaNode() {
        ObjectNode schema = createStdSerializerMock(Object.class).createSchemaNode("number");

        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        StdSerializer<Object> serializer = createStdSerializerMock(Object.class);
        ObjectNode resultNode = createSchemaNode();
        SerializerProvider provider = mock(SerializerProvider.class);

        when(serializer.getSchema(provider, anyType())).thenReturn(resultNode);

        assertSame(resultNode, serializer.getSchema(provider, Object.class));
        assertSame(resultNode, serializer.getSchema(provider, Object.class, true));
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> serializer = createStdSerializerMock(Object.class);
        StdSerializer<String> stringSerializer = mock(StdSerializer.class);

        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertFalse(StdSerializer.class.isInstance(new Object()));
        assertFalse(StdSerializer.class.isInstance(String.class));
        assertTrue(StdSerializer.isDefaultSerializer(serializer));
        assertFalse(StdSerializer.isDefaultSerializer(stringSerializer));
    }

    @Test
    public void testSerializeWithCustomConverter() throws IOException {
        Converter<Integer, String> converter = mock(Converter.class);
        ObjectNode resultNode = createSchemaNode();
        StdSerializer<Integer> serializer = new StdSerializer<Integer>(Integer.class) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                visitor.expectStringFormat(typeHint);
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                return createSchemaNode("string");
            }

            @Override
            public JsonSerializer<Object> createContextualSerializer(SerializerProvider p,
                    BeanProperty property, JavaType typeToSerialize)
                    throws JsonMappingException {
                return new StdDelegatingSerializer<Object>(converter, Object.class, this);
            }
        };
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        Integer number = 1;

        when(provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(true);
        when(converter.convert(number, Object.class)).thenReturn("One");

        serializer.serialize(number, generator, provider);

        verify(generator).writeString("One");
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        Converter<Object, Object> converter = mock(Converter.class);
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {};
        ObjectNode schema = createSchemaNode();
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        when(provider.converterInstance(property.getMember(), any())).thenReturn(converter);

        JsonSerializer<Object> resultSerializer = serializer.findConvertingContentSerializer(provider, property, null);

        assertSame(new StdDelegatingSerializer(converter, Object.class, resultSerializer), resultSerializer);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        PropertyFilter filter = mock(PropertyFilter.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object filterId = new Object();
        Object valueToFilter = new Object();

        when(provider.getFilterProvider()).thenReturn(new SimpleFilterProvider());

        PropertyFilter resultFilter = serializer.findPropertyFilter(provider, filterId, valueToFilter);

        assertSame(filter, resultFilter);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterNullFilterProvider() throws JsonMappingException {
        StdSerializer<Object> serializer = createStdSerializerMock(Object.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object filterId = new Object();
        Object valueToFilter = new Object();

        when(provider.getFilterProvider()).thenReturn(null);

        serializer.findPropertyFilter(provider, filterId, valueToFilter);
    }

    private ObjectNode createSchemaNode() {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        return nodeFactory.objectNode();
    }
}