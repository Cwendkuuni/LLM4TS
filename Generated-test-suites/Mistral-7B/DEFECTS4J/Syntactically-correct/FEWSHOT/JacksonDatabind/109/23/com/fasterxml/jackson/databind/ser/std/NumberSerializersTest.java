package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    @JacksonStdImpl
    static class TestUtil {
        static ObjectMapper mapper;

        @BeforeClass
        public static void setup() {
            mapper = new ObjectMapper();
        }

        static <T> void testSerialization(Class<T> clazz, Object input)
                throws IOException {
            String json = mapper.writeValueAsString(input);
            Object deserialized = mapper.readValue(json, clazz);
            assertEquals(input, deserialized);
        }
    }

    @Test
    public void testIntegerSerializer() {
        TestUtil.testSerialization(Integer.class, 123);
        TestUtil.testSerialization(Integer.TYPE, 123);
    }

    @Test
    public void testIntLikeSerializer() {
        TestUtil.testSerialization(Number.class, 123);
        TestUtil.testSerialization(Number.class, 123d);
        TestUtil.testSerialization(Number.class, 123f);
    }

    @Test
    public void testShortSerializer() {
        TestUtil.testSerialization(Short.class, (short) 123);
        TestUtil.testSerialization(Short.TYPE, (short) 123);
    }

    @Test
    public void testLongSerializer() {
        TestUtil.testSerialization(Long.class, 123L);
        TestUtil.testSerialization(Long.TYPE, 123L);
    }

    @Test
    public void testFloatSerializer() {
        TestUtil.testSerialization(Float.class, 123.0f);
        TestUtil.testSerialization(Float.TYPE, 123.0f);
    }

    @Test
    public void testDoubleSerializer() {
        TestUtil.testSerialization(Double.class, 123.0);
        TestUtil.testSerialization(Double.TYPE, 123.0);
    }
}