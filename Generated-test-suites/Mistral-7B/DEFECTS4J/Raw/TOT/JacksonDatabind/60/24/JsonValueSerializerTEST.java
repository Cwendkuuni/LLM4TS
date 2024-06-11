package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializerSimple() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(String.class, null);
        assertTrue(serializer instanceof JsonValueSerializer);
        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
        assertEquals(String.class, jsonValueSerializer.handledType());
        assertEquals("valueMethod", jsonValueSerializer.getName());
        JsonSerializer<String> valueSerializer = jsonValueSerializer.getValueSerializer();
        assertEquals(String.class, valueSerializer.handledType());
        BeanPropertyWriter writer = mapper.getSerializerProvider().findBeanPropertyWriter(String.class, null);
        assertEquals(writer, jsonValueSerializer._property);
    }

    @Test
    public void testJsonValueSerializerWithMethod() throws Exception {
        class TestClass {
            @JsonValue
            public int getValue() {
                return 123;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TestClass.class, "getValue");
        assertTrue(serializer instanceof JsonValueSerializer);
        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
        assertEquals(TestClass.class, jsonValueSerializer.handledType());
        assertEquals("getValue", jsonValueSerializer.getName());
        JsonSerializer<Integer> valueSerializer = jsonValueSerializer.getValueSerializer();
        assertEquals(Integer.class, valueSerializer.handledType());
    }

    @Test
    public void testJsonValueSerializerWithMethodAnnotation() throws Exception {
        class TestClass {
            @JsonValue("customSerializer")
            public int getValue() {
                return 123;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TestClass.class, "getValue");
        assertTrue(serializer instanceof JsonValueSerializer);
        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
        assertEquals(TestClass.class, jsonValueSerializer.handledType());
        assertEquals("getValue", jsonValueSerializer.getName());
        JsonSerializer<Integer> valueSerializer = jsonValueSerializer.getValueSerializer();
        assertNotNull(valueSerializer);
        assertEquals("customSerializer", valueSerializer.getClass().getSimpleName());
    }

    @Test
    public void testJsonValueSerializerWithNullValue() throws Exception {
        class TestClass {
            @JsonValue
            public String getValue() {
                return null;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TestClass.class, "getValue");
        assertTrue(serializer instanceof JsonValueSerializer);
        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
        assertEquals(TestClass.class, jsonValueSerializer.handledType());
        assertEquals("getValue", jsonValueSerializer.getName());
        JsonSerializer<String> valueSerializer = jsonValueSerializer.getValueSerializer();
        assertEquals(String.class, valueSerializer.handledType());

        try {
            serializer.serialize(new TestClass(), new DefaultSerializerProvider(), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("No serializer found for class java.lang.String and no properties discovered to serialize", e.getMessage());
        }
    }

    @Test
    public void testJsonValueSerializerWithCustomValueSerializer() throws Exception {
        class TestClass {
            @JsonValue("customSerializer")
            public int getValue() {
                return 123;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Integer> customSerializer = mapper.getSerializerProvider().findValueSerializer(Integer.class, null);
        JsonValueSerializer serializer = mapper.findValueSerializer(TestClass.class, "getValue");
        assertTrue(serializer instanceof JsonValueSerializer);
        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
        assertEquals(TestClass.class, jsonValueSerializer.handledType());
        assertEquals("getValue", jsonValueSerializer.getName());
        JsonSerializer<Integer> valueSerializer = jsonValueSerializer.getValueSerializer();
        assertSame(customSerializer, valueSerializer);
    }

    @Test
    public void testJsonValueSerializerWithNaturalType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(int.class, null);
        assertTrue(serializer instanceof JsonValueSerializer);
        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
        assertTrue(jsonValueSerializer._forceTypeInformation);
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonValueSerializerWithException() throws Exception {
        class TestClass {
            @JsonValue
            public void getValue() throws IOException {
                throw new IOException();
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TestClass.class, "getValue");
        assertTrue(serializer instanceof JsonValueSerializer);
        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
        assertEquals(TestClass.class, jsonValueSerializer.handledType());
        assertEquals("getValue", jsonValueSerializer.getName());

        try {
            JsonNode node = mapper.readerForTree(new TestClass()).getValueAsTree();
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Method 'getValue' in class TestClass throws an exception (class org.codehaus.jackson.io.IOException)", e.getMessage());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonValueSerializerWithExceptionAndCustomSerializer() throws Exception {
        class TestClass {
            @JsonValue("customSerializer")
            public void getValue() throws IOException {
                throw new IOException();
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Throwable> customSerializer = mapper.getSerializerProvider().findValueSerializer(Throwable.class, null);
        JsonValueSerializer serializer = mapper.findValueSerializer(TestClass.class, "getValue");
        assertTrue(serializer instanceof JsonValueSerializer);
        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
        assertEquals(TestClass.class, jsonValueSerializer.handledType());
        assertEquals("getValue", jsonValueSerializer.getName());

        try {
            JsonNode node = mapper.readerForTree(new TestClass()).getValueAsTree();
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Method 'getValue' in class TestClass throws an exception (class org.codehaus.jackson.io.IOException)", e.getMessage());
        }

        JsonNode node = mapper.readerForTree(new TestClass()).getValueAsTree();
        JsonNode expectedNode = mapper.readerForTree(new IOException()).getValueAsTree();
        JsonNodeFactory factory = mapper.getNodeFactory();
        assertTrue(node.isValueNode());
        assertSame(expectedNode, node);
    }

    @Test
    public void testJsonValueSerializerWithNestedValueSerializer() throws Exception {
        class TestClass {
            @JsonValue
            public TestInner getValue() {
                return new TestInner();
            }

            static class TestInner {
                @JsonValue
                public String getValue() {
                    return "innerValue";
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TestClass.class, "getValue");
        assertTrue(serializer instanceof JsonValueSerializer);
        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
        assertEquals(TestClass.class, jsonValueSerializer.handledType());
        assertEquals("getValue", jsonValueSerializer.getName());
        JsonSerializer<TestClass.TestInner> innerSerializer = jsonValueSerializer.getValueSerializer();
        assertEquals(TestClass.TestInner.class, innerSerializer.handledType());
        JsonSerializer<String> stringSerializer = innerSerializer.getValueSerializer();
        assertEquals(String.class, stringSerializer.handledType());
    }
}