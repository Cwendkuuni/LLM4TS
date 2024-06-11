package com.example.json; // replace with your test package if needed

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonNode;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

public class JsonValueSerializerTest {

    private ObjectMapper mapper;
    private JsonValueSerializer serializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        Class<?> testClass = TestClass.class;
        try {
            Method getValueMethod = testClass.getMethod("getValue");
            serializer = new JsonValueSerializer(getValueMethod, null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        TestClass testObject = new TestClass();
        Object result;

        try {
            result = serializer.getValue(testObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        JsonSerializer<Object> valueSerializer = serializer.getValueSerializer();

        JsonNode expectedNode = mapper.valueToTree(new Object() {
            @JsonValue
            public String getValue() {
                return "test";
            }
        });

        JsonNode actualNode = mapper.valueToTree(result);

        Assert.assertEquals(expectedNode, actualNode);
        Assert.assertEquals(valueSerializer.getClass(), JsonSerializer.class);
    }

    @Test
    public void testSerializeWithType() throws JsonMappingException, JsonProcessingException, IOException {
        TestClass testObject = new TestClass();
        Object result;

        try {
            result = serializer.getValue(testObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        JsonSerializer<Object> valueSerializer = serializer.getValueSerializer();

        SerializerProvider provider = mapper.getSerializerProvider();
        TypeSerializer typeSerializer = provider.findTypeSerializer(result.getClass(), null);

        byte[] serialized = mapper.writeValueAsBytes(result);
        JsonNode expectedNode = mapper.readTree(new String(serialized));

        JsonNode actualNode = mapper.valueToTree(result);

        JsonNode expectedWithTypeNode = mapper.readTree(String.format(
                "[%s \"%s\"]",
                BeanUtil.getClassName(result.getClass()),
                result.toString()
        ));

        Assert.assertEquals(expectedNode, actualNode);
        Assert.assertEquals(expectedWithTypeNode, mapper.readTree(String.format(
                "[%s \"%s\"]%s",
                BeanUtil.getClassName(result.getClass()),
                result.toString(),
                typeSerializer.writeTypePrefixForScalar(new Object(), null)
        )));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        TestClass testObject = new TestClass();
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getSchemaProvider()
                .findSchemaVisitor(mapper.getTypeFactory().constructValueType(Object.class));

        serializer.acceptJsonFormatVisitor(visitor, null);

        JsonNode actualNode = visitor.asPrefixTree();

        JsonNode expectedNode = mapper.valueToTree(new Object() {
            @JsonValue
            public String getValue() {
                return "test";
            }
        });

        expectedNode.put("type", "string");

        Assert.assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        TestClass testObject = new TestClass();
        JsonSerializer<Object> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);

        Assert.assertNotNull(contextualSerializer);
        Assert.assertSame(contextualSerializer, serializer);

        TestClassWithContextual contextualTestObject = new TestClassWithContextual();
        contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);

        Assert.assertNotSame(contextualSerializer, serializer);
        Assert.assertSame(contextualSerializer.getClass(), JsonValueSerializer.class);
        Assert.assertSame(contextualSerializer.getValueSerializer().getClass(), JsonSerializer.class);
    }

    static class TestClass {
        @JsonValue
        public Object getValue() {
            return new Object() {
                @JsonValue
                public String getValue() {
                    return "test";
                }
            };
        }
    }

    static class TestClassWithContextual {
        @JsonValue
        public Object getValue() {
            return new String("test");
        }
    }
}