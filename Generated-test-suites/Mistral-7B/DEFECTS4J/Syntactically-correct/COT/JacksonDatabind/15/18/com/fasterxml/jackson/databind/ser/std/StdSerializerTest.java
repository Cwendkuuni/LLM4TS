package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdSerializerTest {

    @InjectMocks
    private StdSerializer<Object> stdSerializer;
    private ObjectMapper objectMapper;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testDefaultConstructor() {
        StdSerializer<Object> serializer = new StdSerializer<>();
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testConstructorWithType() {
        StdSerializer<Object> serializer = new StdSerializer(Object.class);
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testConstructorWithTypeAndDummy() {
        StdSerializer<Object> serializer = new StdSerializer(Object.class, false);
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException {
        Object value = new Object();
        ObjectNode jsonNode = createJsonNode();
        when(serializerProvider.createObjectCodec()).thenReturn(objectMapper.getCodec());
        when(serializerProvider.createGenerator(jsonNode)).thenReturn(objectMapper.getFactory().createGenerator(jsonNode));

        stdSerializer.serialize(value, serializerProvider.getGenerator(jsonNode), serializerProvider);

        assertNotNull(jsonNode);
        assertNull(jsonNode.path(""));
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWhenError() throws JsonMappingException {
        stdSerializer.wrapAndThrow(serializerProvider, new Error(), null, "");
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWhenIOException() throws JsonMappingException {
        stdSerializer.wrapAndThrow(serializerProvider, new IOException(), null, "");
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWhenJsonMappingException() throws JsonMappingException {
        stdSerializer.wrapAndThrow(serializerProvider, new JsonMappingException(), null, "");
    }

    @Test
    public void testWrapAndThrowWhenRuntimeException() throws JsonMappingException {
        stdSerializer.wrapAndThrow(serializerProvider, new RuntimeException(), null, "");
        assertNull(serializerProvider);
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        BeanProperty property = createBeanProperty();
        JsonSerializer<Object> existingSerializer = mock(JsonSerializer.class);
        Converter<Object, Object> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        StdSerializer<Object> stdSerializer = new StdSerializer<>(converter, delegateType, existingSerializer);

        when(serializerProvider.getFilterProvider()).thenReturn(null);
        when(serializerProvider.converterInstance(property.getMember(), Converter.class)).thenReturn(converter);
        when(delegateType.getRawClass()).thenReturn(String.class);

        JsonSerializer<String> result = stdSerializer.findConvertingContentSerializer(serializerProvider, property, existingSerializer);

        assertEquals(String.class, result.handledType());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        Object filterId = "testFilterId";
        Object valueToFilter = new Object();
        PropertyFilter propertyFilter = mock(PropertyFilter.class);

        when(serializerProvider.getFilterProvider()).thenReturn(mock(FilterProvider.class));
        when(serializerProvider.getFilterProvider().findPropertyFilter(filterId, valueToFilter)).thenReturn(propertyFilter);

        PropertyFilter result = stdSerializer.findPropertyFilter(serializerProvider, filterId, valueToFilter);

        assertEquals(propertyFilter, result);
    }

    private ObjectNode createJsonNode() {
        return JsonNodeFactory.instance.objectNode();
    }

    private BeanProperty createBeanProperty() {
        return mock(BeanProperty.class);
    }
}