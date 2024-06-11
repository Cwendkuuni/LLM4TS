package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(singleThreaded = true)
public class StdSerializerTest {

    @Test
    public void testConstructor() {
        Class<String> stringType = String.class;
        StdSerializer<String> serializer1 = new StdSerializer<>(stringType);
        StdSerializer<String> serializer2 = new StdSerializer<>(stringType.asSubTypeOf(Object.class));
        StdSerializer<String> serializer3 = new StdSerializer<>(stringType.asSubTypeOf(Object.class), false);

        assertEquals(serializer1.handledType(), stringType);
        assertEquals(serializer2.handledType(), stringType);
        assertEquals(serializer3.handledType(), stringType);
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        String value = "Hello World";
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);
        generator.setEncoder(StandardEncoders.STRING);

        serializer.serialize(value, generator, mapper.getSerializationConfig());

        String result = outputStream.toString();
        assertEquals(result, "\"" + value + "\"");
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);

        ObjectNode schema = serializer.getSchema(mapper.getSerializationConfig(), String.class);

        assertEquals(schema.getType(), JsonNodeType.OBJECT);
        assertEquals(schema.get("type"), "string");
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);

        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getDefaultFormatVisitor(String.class);

        serializer.acceptJsonFormatVisitor(visitor, null);

        assertTrue(visitor.isAccepted(JsonFormat.Feature.AUTO_CLOSE_TARGET));
        assertTrue(visitor.isAccepted(JsonFormat.Feature.WRITE_NULL_FOR_EMPTY));
        assertFalse(visitor.isAccepted(JsonFormat.Feature.WRITE_BIGDECIMAL_AS_PLAIN));
    }

    @Test
    public void testWrapAndThrow() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Object bean = new Object();

        try {
            mapper.findValueSerializer(int[].class).serialize(null, null, mapper.getSerializationConfig());
            fail("Expected IOException");
        } catch (IOException e) {
            StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
            serializer.wrapAndThrow(mapper, e, bean, "testField");
            fail("Expected JsonMappingException");
        }

        try {
            mapper.findValueSerializer(int[].class).serialize(new int[0], null, mapper.getSerializationConfig());
            fail("Expected IOException");
        } catch (IOException e) {
            StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
            serializer.wrapAndThrow(mapper, e, bean, 0);
            fail("Expected JsonMappingException");
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<Object> serializer1 = new StdSerializer<>(Object.class);
        JsonSerializer<Object> serializer2 = new CustomSerializer<>();

        assertTrue(StdSerializer.class.isInstance(serializer1));
        assertTrue(StdSerializer.class.isInstance(serializer1));
        assertFalse(StdSerializer.class.isInstance(serializer2));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        // No custom content serializer provided, should use default
        JsonSerializer<List<Integer>> serializer1 = mapper.findValueSerializer(new TypeFactory().constructCollectionType(List.class, Integer.class));
        assertEquals(serializer1.getClass(), StdListSerializer.class);

        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        BeanDescription description = mapper.findBeanDescription(List.class);
        PropertyDescriptor propertyDescriptor = description.findProperty("0");

        // Custom content serializer provided, should use it
        JsonSerializer<List<String>> serializer2 = mapper.findValueSerializer(new TypeFactory().constructCollectionType(List.class, String.class));
        Converter<Object,Object> converter = new StringToIntegerConverter();
        StdSerializer<List<String>> customSerializer = new StdDelegatingSerializer<>(converter, IntType.INSTANCE, serializer1);
        assertEquals(customSerializer.getClass(), customSerializer.getClass());

        assertEquals(customSerializer.findConvertingContentSerializer(mapper, propertyDescriptor, null).getClass(), CustomContentSerializer.class);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        FilterProvider filterProvider = mapper.getSerializationConfig().getFilterProvider();

        String filterId = "testFilter";
        PropertyFilter filter = new TestPropertyFilter();
        filterProvider.addFilter(filterId, filter);

        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);

        assertEquals(serializer.findPropertyFilter(mapper, filterId, null), filter);
        assertEquals(serializer.findPropertyFilter(mapper, "nonExistentFilterId", null), null);
    }

    private static class CustomSerializer<T> extends StdSerializer<T> {
        public CustomSerializer() {
            super(Object.class);
        }

        @Override
        public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
            jgen.writeString("Custom");
        }
    }

    private static class CustomContentSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
            jgen.writeString("CustomContent");
        }
    }

    private static class StringToIntegerConverter extends StdConverter<String, Integer> {
        @Override
        public Integer convert(String value, DeserializationContext ctxt) throws IOException {
            return Integer.parseInt(value);
        }
    }

    private static class TestPropertyFilter implements PropertyFilter {
        @Override
        public void serializeAsProperty(Object bean, JsonGenerator jgen, SerializerProvider provider, String name)
                throws IOException {
            jgen.writeString("Filtered");
        }

        @Override
        public void serializeAsField(SerializerProvider provider, JsonGenerator jgen, Object bean, String fieldName)
                throws IOException {
            jgen.writeString("Filtered");
        }
    }
}