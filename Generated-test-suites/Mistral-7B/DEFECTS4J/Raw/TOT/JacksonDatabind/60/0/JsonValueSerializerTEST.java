package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class JsonValueSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerializingSimpleJsonValueAnnotation() throws IOException, JsonProcessingException {
        class TestBean {
            @JsonValue
            public String getValue() {
                return "test";
            }
        }

        TestBean bean = new TestBean();

        JsonValueSerializer serializer = mapper.findValueSerializer(TestBean.class, null);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serializeWithType(bean, gen, mapper.getSerializerProvider(), mapper.getTypeSerializer());

        String result = gen.toString();
        assertEquals("\"test\"", result);
    }

    @Test
    public void testSerializingJsonValueAnnotationOnStaticMethod() throws IOException, JsonProcessingException {
        class TestBean {
            public static String getValue() {
                return "test";
            }
        }

        JsonValueSerializer serializer = mapper.findValueSerializer(TestBean.class, null);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serializeWithType(null, gen, mapper.getSerializerProvider(), mapper.getTypeSerializer());

        String result = gen.toString();
        assertEquals("\"test\"", result);
    }

    @Test
    public void testSerializingJsonValueAnnotationOnEnum() throws IOException, JsonProcessingException {
        class TestEnum {
            @JsonValue
            public String getValue() {
                return name();
            }

            public String name() {
                return "TestEnum_" + this.hashCode();
            }
        }

        TestEnum enumValue = TestEnum.values()[0];

        JsonValueSerializer serializer = mapper.findValueSerializer(TestEnum.class, null);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serializeWithType(enumValue, gen, mapper.getSerializerProvider(), mapper.getTypeSerializer());

        String result = gen.toString();
        assertEquals("\"TestEnum_0\"", result);
    }

    @Test
    public void testSerializingJsonValueAnnotationWithNullValue() throws IOException, JsonProcessingException {
        class TestBean {
            @JsonValue
            public String getValue() {
                return null;
            }
        }

        TestBean bean = new TestBean();

        JsonValueSerializer serializer = mapper.findValueSerializer(TestBean.class, null);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serializeWithType(bean, gen, mapper.getSerializerProvider(), mapper.getTypeSerializer());

        String result = gen.toString();
        assertTrue(result.equals("null") || result.equals("\"null\""));
    }

    @Test
    public void testSerializingJsonValueAnnotationWithException() throws IOException, JsonProcessingException {
        class TestBean {
            @JsonValue
            public String getValue() throws IOException {
                throw new IOException("TestException");
            }
        }

        TestBean bean = new TestBean();

        try {
            JsonValueSerializer serializer = mapper.findValueSerializer(TestBean.class, null);
            JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
            serializer.serializeWithType(bean, gen, mapper.getSerializerProvider(), mapper.getTypeSerializer());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertTrue(e.getMessage().contains("Could not serialize"));
        }
    }
}