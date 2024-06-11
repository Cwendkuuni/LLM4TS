package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter.ConverterFactory;
import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {

    private SerializerProvider provider;
    private ObjectNode node;
    private JsonNodeFactory factory;

    @Before
    public void setup() {
        provider = mock(SerializerProvider.class);
        factory = JsonNodeFactory.instance;
        node = factory.objectNode();
    }

    @Test
    public void testIsDefaultSerializer() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {}
        };
        JsonSerializer<Object> mockSerializer = mock(JsonSerializer.class);
        when(provider.findValueSerializer(Object.class)).thenReturn(mockSerializer);
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isInstance(mockSerializer));
        assertFalse(StdSerializer.class.isInstance(new ObjectSerializer()));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<List<String>> existingSerializer = mapper.findValueSerializer(JavaType.constructParametricType(List.class, String.class));
        Converter.ConverterFactory<Object, Object> convFactory = mock(Converter.ConverterFactory.class);
        Converter<Object, Object> converter = mock(Converter.class);
        when(convFactory.getConverterInstance(Object.class, null)).thenReturn(converter);
        when(provider.getConverterInstances(Object.class, String[].class)).thenReturn(new Converter[]{converter});
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(JavaType.constructRawType(Object.class));
        StdSerializer<List<String>> serializer = new StdSerializer<List<String>>(List.class) {
            @Override
            public void serialize(List<String> value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {}

            @Override
            public JsonSerializer<?> findConvertingContentSerializer(SerializerProvider provider, BeanProperty prop, JsonSerializer<?> existingSerializer)
                    throws JsonMappingException {
                return new StdDelegatingSerializer<>(converter, JavaType.constructRawType(Object.class), existingSerializer);
            }
        };
        JsonSerializer<?> result = serializer.findConvertingContentSerializer(provider, null, null);
        assertNotNull(result);
        assertEquals(delegateSerializer.getClass(), result.getClass());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        FilterProvider filterProvider = mock(FilterProvider.class);
        when(provider.getFilterProvider()).thenReturn(filterProvider);
        PropertyFilter filter = mock(PropertyFilter.class);
        when(filterProvider.findPropertyFilter("testId", null)).thenReturn(filter);
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {}
        };
        PropertyFilter result = serializer.findPropertyFilter(provider, "testId", node);
        assertNotNull(result);
        assertEquals(filter.getClass(), result.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterNullFilterProvider() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {}
        };
        serializer.findPropertyFilter(null, "testId", node);
    }

    private static class ObjectSerializer extends StdSerializer<Object> {
        public ObjectSerializer() {
            super(Object.class);
        }
    }
}