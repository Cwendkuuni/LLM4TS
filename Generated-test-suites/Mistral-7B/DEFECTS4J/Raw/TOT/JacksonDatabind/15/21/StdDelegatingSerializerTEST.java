import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializer.ValueType;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerializationWithConverter() throws JsonMappingException, JsonGenerationException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                new StringToLongConverter(),
                Object.class.getType(),
                new StdSerializer<>(Long.class) {});

        SerializerProvider serializerProvider = new DefaultSerializerProvider();
        serializer.serialize(42L, objectMapper.getJsonGenerator(serializerProvider), serializerProvider);
        assertEquals("42", objectMapper.writeValueAsString(serializer.getDelegateSerializer().getValueType()));
    }

    @Test
    public void testSerializationWithContextualizer() throws JsonMappingException, JsonGenerationException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                StringToLongConverter.class,
                Object.class,
                new StdSerializer<>(Long.class) {});

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = new DefaultSerializerProvider();

        // Test serialization with no contextualization
        serializer.serialize(42L, objectMapper.getJsonGenerator(serializerProvider), serializerProvider);
        assertEquals("42", objectMapper.writeValueAsString(serializer.getDelegateSerializer().getValueType()));

        // Test serialization with contextualization
        ObjectMapper contextualObjectMapper = new ObjectMapper();
        contextualObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        serializer.createContextual(contextualObjectMapper.getSerializerProvider(), null);
        serializer.serialize(null, contextualObjectMapper.getJsonGenerator(serializerProvider), serializerProvider);
        assertNull(contextualObjectMapper.writeValueAsString(serializer.getDelegateSerializer().getValueType()));

        // Test serialization with contextualization and property
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("value", 42L);
        JsonNode node = objectMapper.readTree(objectMapper.writerWithRootName("root").writeValueAsString(map));
        serializer = new StdDelegatingSerializer(
                StringToLongConverter.class,
                Object.class,
                new StdSerializer<Object>(Map.class) {
                    @Override
                    public void serialize(Map<String, Object> map, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        // Do nothing
                    }
                });
        serializer.createContextual(contextualObjectMapper.getSerializerProvider(), new BeanProperty("value"));
        serializer.serialize(map, contextualObjectMapper.getJsonGenerator(serializerProvider), serializerProvider);
        assertEquals("42", contextualObjectMapper.writeValueAsString(serializer.getDelegateSerializer().getValueType()));
    }

    @Test
    public void testSerializationWithDynamicType() throws JsonMappingException, JsonGenerationException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                Object.class,
                new StringToLongConverter(),
                new StdSerializer<>(Long.class) {});

        SerializerProvider serializerProvider = new DefaultSerializerProvider();
        serializer.serialize(42L, objectMapper.getJsonGenerator(serializerProvider), serializerProvider);
        assertEquals("42", objectMapper.writeValueAsString(serializer.getDelegateSerializer().getValueType()));

        // Test serialization with dynamic type lookup
        ObjectMapper dynamicObjectMapper = new ObjectMapper();
        dynamicObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        serializer = new StdDelegatingSerializer(
                Object.class,
                new StringToLongConverter(),
                null);
        serializer.createContextual(dynamicObjectMapper.getSerializerProvider(), null);
        serializer.serialize(42L, dynamicObjectMapper.getJsonGenerator(serializerProvider), serializerProvider);
        assertEquals("42", dynamicObjectMapper.getSerializerProvider().findValueSerializer(Long.class).getClass().getName());
    }

    @Test
    public void testSerializationWithNullValue() throws JsonMappingException, JsonGenerationException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                Object.class,
                new StringToLongConverter(),
                new StdSerializer<Object>(Integer.class) {
                    @Override
                    public void serialize(int value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        jsonGenerator.writeNull();
                    }
                });

        SerializerProvider serializerProvider = new DefaultSerializerProvider();
        ObjectMapper objectMapper = new ObjectMapper();
        serializer.serialize(null, objectMapper.getJsonGenerator(serializerProvider), serializerProvider);
        assertNull(objectMapper.writeValueAsString(serializer.getDelegateSerializer().getValueType()));
    }

    @Test
    public void testEmptySerialization() throws JsonMappingException, JsonGenerationException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                new StringToLongConverter(),
                Object.class.getType(),
                new StdSerializer<Object>(Collections.emptyList().getClass()) {
                    @Override
                    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        jsonGenerator.writeNull();
                    }
                });

        SerializerProvider serializerProvider = new DefaultSerializerProvider();
        ObjectMapper objectMapper = new ObjectMapper();
        serializer.serialize(null, objectMapper.getJsonGenerator(serializerProvider), serializerProvider);
        assertNull(objectMapper.writeValueAsString(serializer.getDelegateSerializer().getValueType()));
    }

    private static class StringToLongConverter implements Converter<String, Long> {
        @Override
        public Long convert(String text) {
            return Long.parseLong(text);
        }

        @Override
        public String convert(Long value) {
            return String.valueOf(value);
        }
    }
}