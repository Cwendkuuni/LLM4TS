package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonValueSerializerTest extends BaseTest {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = mapper.findValueSerializer(String.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new MethodTest(), serializer, false);

        String json = mapper.writerWithTypeValidator(false).writeValueAsString(new TestClass("Hello World"));
        System.out.println(json); // {"value":"Hello World"}
    }

    static class TestClass {
        private final String value;

        TestClass(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    static class MethodTest {
        Method getValueMethod() {
            return TestClass.class.getMethod("getValue");
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerialize() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new MethodTest().getValueMethod(), valueSerializer, false);

        String json = mapper.writerWithTypeValidator(false).writeValueAsString(new TestClass("Hello World"));
        JsonNode node = mapper.readTree(json);
        assertEquals("value", node.fieldNames().next());
        assertEquals("Hello World", node.get("value").asText());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new MethodTest().getValueMethod(), valueSerializer, false);

        JsonSerializer<Object> contextualSerializer = jsonValueSerializer.createContextual(mapper.getSerializationConfig(), null);
        assertSame(TestClass.class, contextualSerializer.handledType().getRawClass());
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class, null);
        assertEquals(stringSerializer, contextualSerializer);
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new MethodTest().getValueMethod(), valueSerializer, false);

        boolean isNaturalTypeWithStdHandling = jsonValueSerializer.isNaturalTypeWithStdHandling(String.class, valueSerializer);
        assertTrue(isNaturalTypeWithStdHandling);
    }

    @Test
    public void testIsNaturalTypeWithStdHandling_NonString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(Integer.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new MethodTest().getValueMethod(), valueSerializer, false);

        boolean isNaturalTypeWithStdHandling = jsonValueSerializer.isNaturalTypeWithStdHandling(Integer.class, valueSerializer);
        assertFalse(isNaturalTypeWithStdHandling);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new MethodTest().getValueMethod(), valueSerializer, false);

        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new JsonFormatVisitorWrapper() {
            @Override
            public JsonNode visitArrayFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                fail();
                return null;
            }

            @Override
            public JsonNode visitContainerFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                fail();
                return null;
            }

            @Override
            public void acceptJsonStringFormatVisitor(JsonStringFormatVisitor visitor, JavaType typeHint) throws JsonMappingException {
                assertSame(String.class, typeHint.getRawClass());
                visitor.stringSerializable(JsonValueSerializer.class.getName() + "#value");
            }
        };

        jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
    }
}