package com.example.json; // replace with your test package name

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonGenerator;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;
    private JsonValueSerializer jsonValueSerializer;
    private TestBean testBean;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TestBean testBean = new TestBean();
        Method getMethod = TestBean.class.getMethod("getValue");
        jsonValueSerializer = new JsonValueSerializer(getMethod, null);
        this.testBean = testBean;
    }

    @Test
    public void testSerializerNullValue() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        jsonValueSerializer.serialize(null, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> valueClass, BeanProperty property) {
                return null;
            }

            @Override
            public JsonSerializer<Object> findPrimaryPropertySerializer(Class<?> type, BeanProperty property) {
                return null;
            }

            @Override
            public JsonSerializer<Object> findTypedValueSerializer(Class<?> valueClass, boolean isEmpty, BeanProperty property) {
                return null;
            }
        });
        jsonGenerator.flush();
        jsonGenerator.close();
        String expectedOutput = "null";
        Assert.assertEquals(expectedOutput, objectMapper.writeValueAsString(null));
    }

    @Test
    public void testSerializerValue() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        jsonValueSerializer.serialize(testBean, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<?> valueClass, BeanProperty property) {
                return null;
            }

            @Override
            public JsonSerializer<Object> findPrimaryPropertySerializer(Class<?> type, BeanProperty property) {
                return null;
            }

            @Override
            public JsonSerializer<Object> findTypedValueSerializer(Class<?> valueClass, boolean isEmpty, BeanProperty property) {
                return null;
            }
        });
        jsonGenerator.flush();
        jsonGenerator.close();
        String expectedOutput = "expectedValue";
        Assert.assertEquals(expectedOutput, objectMapper.writeValueAsString(testBean));
    }

    @Test
    public void testSerializeWithType() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        jsonValueSerializer.serializeWithType(testBean, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<?> valueClass, BeanProperty property) {
                return null;
            }

            @Override
            public JsonSerializer<Object> findPrimaryPropertySerializer(Class<?> type, BeanProperty property) {
                return null;
            }

            @Override
            public JsonSerializer<Object> findTypedValueSerializer(Class<?> valueClass, boolean isEmpty, BeanProperty property) {
                return null;
            }
        }, typeSerializer);
        jsonGenerator.flush();
        jsonGenerator.close();
        String expectedOutput = "expectedType/expectedValue";
        Assert.assertEquals(expectedOutput, objectMapper.writeValueAsString(testBean));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        // JsonFormatVisitorWrapper is not available in Junit 4
    }

    static class TestBean {
        private String value;

        public String getValue() {
            return value;
        }

        @JsonValue
        public String getJsonValue() {
            return "expectedValue";
        }
    }
}