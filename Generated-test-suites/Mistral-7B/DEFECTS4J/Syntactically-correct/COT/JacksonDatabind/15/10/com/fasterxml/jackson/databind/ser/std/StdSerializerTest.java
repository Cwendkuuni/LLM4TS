package com.fasterxml.jackson.databind.ser.std; // import necessary packages here

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {
    private ObjectMapper mapper;
    private JsonNodeFactory nodeFactory;
    private SerializerProvider provider;
    private StdSerializer<String> serializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        nodeFactory = mapper.getNodeFactory();
        provider = mock(SerializerProvider.class);
        serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                jgen.writeString(value);
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testConstructorWithType() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(mapper.constructType(String.class));
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testConstructorWithTypeAndDummy() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class, false);
        assertEquals(String.class, serializer.handledType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowIllegalArgumentExceptionWhenInvalidTypeIsPassed() {
        new StdSerializer<Object>(String.class);
    }

    @Test
    public void testSerializeMethod() throws JsonMappingException, IOException {
        String value = "Hello World!";
        ObjectNode result = JsonNodeFactory.instance.objectNode();

        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeString(value)).thenReturn(result);

        when(provider.getActiveContext()).thenReturn(null);
        serializer.serialize(value, gen, provider);

        assertEquals("Hello World!", result.asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowMethodWithJsonMappingException() throws IOException {
        Throwable t = new JsonMappingException("Test Mapping Exception");
        serializer.wrapAndThrow(provider, t, null, null);
    }

    @Test(expected = IOException.class)
    public void testWrapAndThrowMethodWithIOException() throws IOException {
        Throwable t = new IOException("Test IOException");
        serializer.wrapAndThrow(provider, t, null, null);
    }

    @Test(expected = RuntimeException.class)
    public void testWrapAndThrowMethodWithRuntimeException() throws IOException {
        Throwable t = new RuntimeException("Test Runtime Exception");
        serializer.wrapAndThrow(provider, t, null, null);
    }

    @Test
    public void testIsDefaultSerializerMethod() {
        StdSerializer<String> serializer1 = new StdSerializer<String>(String.class);
        StdSerializer<Integer> serializer2 = new StdSerializer<Integer>(Integer.class);
        StdSerializer<Object> serializer3 = new StdSerializer<Object>(Object.class);

        assertTrue(serializer1.isDefaultSerializer(serializer1));
        assertFalse(serializer1.isDefaultSerializer(serializer2));
        assertFalse(serializer1.isDefaultSerializer(serializer3));
    }

    @Test
    public void testFindConvertingContentSerializerMethod() throws JsonMappingException {
        Converter<Object,Object> converter = mock(Converter.class);
        when(converter.getOutputType(provider.getTypeFactory())).thenReturn(String.class);
        SerializerProvider provider2 = mock(SerializerProvider.class);
        when(provider2.getFilterProvider()).thenReturn(null);
        when(provider2.converterInstance(any(), any())).thenReturn(converter);

        JsonSerializer<Object> existingSerializer = mock(JsonSerializer.class);
        when(provider.getFilterProvider()).thenReturn(provider2);

        JsonSerializer<String> serializer = serializer.findConvertingContentSerializer(provider, null, existingSerializer);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testFindConvertingContentSerializerMethodWithNullSerializer() throws JsonMappingException {
        Converter<Object,Object> converter = mock(Converter.class);
        when(converter.getOutputType(provider.getTypeFactory())).thenReturn(String.class);
        SerializerProvider provider2 = mock(SerializerProvider.class);
        when(provider2.getFilterProvider()).thenReturn(null);
        when(provider2.converterInstance(any(), any())).thenReturn(converter);

        JsonSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        when(provider.getFilterProvider()).thenReturn(provider2);

        JsonSerializer<String> result = serializer.findConvertingContentSerializer(provider, null, null);
        assertNull(result);
    }

    @Test
    public void testFindPropertyFilterMethod() throws JsonMappingException {
        FilterProvider filterProvider = mock(FilterProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        when(filterProvider.findPropertyFilter(eq("test"), any())).thenReturn(filter);

        when(provider.getFilterProvider()).thenReturn(filterProvider);

        PropertyFilter result = serializer.findPropertyFilter(provider, "test", null);
        assertEquals(filter, result);
    }
}