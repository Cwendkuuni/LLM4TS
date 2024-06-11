package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.TestSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtil;

public class StdSerializerTest extends BaseMapTest {

    private static class TestSerializer<T> extends StdSerializer<T> {
        public TestSerializer(Class<T> t) {
            super(t);
        }

        @Override
        public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {}
    }

    @SuppressWarnings("unchecked")
    private static <T> TestSerializer<T> createTestSerializer(Class<T> handledType) {
        return new TestSerializer<>(handledType);
    }

    /**
     * Test for constructor with Class<T> parameter.
     */
    @Test
    public void testStdSerializerWithClassParameter() {
        TestSerializer<String> stringSerializer = createTestSerializer(String.class);
        assertEquals(String.class, stringSerializer.handledType());
    }

    /**
     * Test for constructor with JavaType parameter.
     */
    @Test
    public void testStdSerializerWithJavaTypeParameter() {
        TestSerializer<String> stringSerializer = new TestSerializer<>(String.class.getType());
        assertEquals(String.class, stringSerializer.handledType());
    }

    /**
     * Test for constructor with Class<T> and boolean dummy parameter.
     */
    @Test
    public void testStdSerializerWithClassAndBooleanDummyParameter() {
        TestSerializer<String> stringSerializer = new TestSerializer<>(String.class, true);
        assertEquals(String.class, stringSerializer.handledType());
    }

    /**
     * Test for getSchema method which returns a JSON Schema node for the serializer.
     */
    @Test
    public void testGetSchema() throws JsonMappingException {
        TestSerializer<String> stringSerializer = createTestSerializer(String.class);
        JsonNode schema = stringSerializer.getSchema(new ObjectMapper(), String.class);
        ObjectNode schemaNode = (ObjectNode) schema;
        assertEquals("string", schemaNode.get("type").asText());
    }

    /**
     * Test for acceptJsonFormatVisitor method which is used to accept JSON format visitor for schema generation.
     */
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        TestSerializer<String> stringSerializer = createTestSerializer(String.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new JsonSerializer.StdSerializerProvider()) {
            @Override
            public void expectAnyFormat(JavaType typeHint) {
                fail("expectAnyFormat should not be called");
            }
        };
        stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
    }

    /**
     * Test for wrapAndThrow method which wraps an exception and throws a JsonMappingException.
     */
    @Test
    public void testWrapAndThrow() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestSerializer<String> stringSerializer = createTestSerializer(String.class);
        String value = "test";

        try {
            stringSerializer.serialize(value, null, mapper.getSerializerProvider());
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            Exception wrappedException = null;
            try {
                wrappedException = stringSerializer.wrapAndThrow(mapper.getSerializerProvider(), e, null, null);
            } catch (JsonMappingException e1) {
                assertEquals("Nested exception: "+e.getMessage(), e.getCause(), wrappedException);
            }
        }
    }

    /**
     * Test for wrapAndThrow method with Object as the third argument.
     */
    @Test
    public void testWrapAndThrowWithObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestSerializer<String> stringSerializer = createTestSerializer(String.class);
        String value = "test";

        try {
            stringSerializer.serialize(value, null, mapper.getSerializerProvider());
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            try {
                stringSerializer.wrapAndThrow(mapper.getSerializerProvider(), e, new Object(), null);
                fail("Expected a JsonMappingException to be thrown");
            } catch (JsonMappingException e1) {
                assertEquals("Nested exception: "+e.getMessage(), e.getCause(), e1.getCause());
            }
        }
    }

    /**
     * Test for wrapAndThrow method with int as the third argument.
     */
    @Test
    public void testWrapAndThrowWithInt() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestSerializer<String> stringSerializer = createTestSerializer(String.class);
        String value = "test";

        try {
            stringSerializer.serialize(value, null, mapper.getSerializerProvider());
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            try {
                stringSerializer.wrapAndThrow(mapper.getSerializerProvider(), e, new Object(), 0);
                fail("Expected a JsonMappingException to be thrown");
            } catch (JsonMappingException e1) {
                assertEquals("Nested exception: "+e.getMessage(), e.getCause(), e1.getCause());
            }
        }
    }

    /**
     * Test for isDefaultSerializer method which checks if given serializer is the default serializer.
     */
    @Test
    public void testIsDefaultSerializer() {
        TestSerializer<String> stringSerializer = createTestSerializer(String.class);
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertTrue(StdSerializer.class.isAssignableFrom(stringSerializer.getClass()));
        assertTrue(StdSerializer.isDefaultSerializer(stringSerializer));

        JsonSerializer<Integer> intSerializer = new TestSerializer<Integer>(Integer.class);
        assertFalse(StdSerializer.isDefaultSerializer(intSerializer));
    }

    /**
     * Test for findConvertingContentSerializer method which locates a converter for serializing contents
     * of structured types.
     */
    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        TestSerializer<MyClass> serializer = new TestSerializer<>(MyClass.class);

        Converter<Object, Object> converter = ConverterUtil.createConverter(String.class, Integer.class);
        JsonSerializer<Integer> integerSerializer = mapper.findValueSerializer(Integer.class);
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeValueAsString(value);
            }
        };

        JsonSerializer<MyClass> expectedSerializer = new StdDelegatingSerializer<>(converter, Integer.class, integerSerializer);
        JsonSerializer<MyClass> actualSerializer = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), MyClass.class, null);

        assertEquals(expectedSerializer, actualSerializer);
    }

    /**
     * Test for findPropertyFilter method which locates a property filter based on the filter id.
     */
    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        FilterProvider filterProvider = mapper.getFilterProvider();
        TestSerializer<MyClass> serializer = new TestSerializer<>(MyClass.class);

        MyClass value = new MyClass();
        PropertyFilter filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), "filterId", value);

        assertNull(filter);

        filterProvider.addFilter("filterId", new TestPropertyFilter());
        filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), "filterId", value);

        assertEquals(TestPropertyFilter.class, filter.getClass());
    }

    static class MyClass {
        // Empty class for testing purposes
    }

    static class TestPropertyFilter extends PropertyFilter {
        @Override
        public boolean filterOut(SerializerProvider provider, BeanProperty property, Object value) {
            return false;
        }
    }
}