package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.NumberType.INT;
import static com.fasterxml.jackson.core.JsonParser.NumberType.LONG;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

public class NumberSerializersTest {

    @Test
    public void testBaseClass() throws Exception {
        Base<Short> shortSerializer = new ShortSerializer();
        Base<Integer> intSerializer = new IntegerSerializer();
        Base<Number> intLikeSerializer = new IntLikeSerializer();
        Base<Long> longSerializer = new LongSerializer();
        Base<Float> floatSerializer = new FloatSerializer();
        Base<Double> doubleSerializer = new DoubleSerializer();

        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        assertEquals("number", shortSerializer._schemaType);
        assertTrue(shortSerializer._isInt);
        assertEquals(INT, shortSerializer.getSchema(null, null).getNumberType());

        assertEquals(JsonParser.NumberType.INT, intSerializer._numberType);
        assertEquals("integer", intSerializer._schemaType);
        assertTrue(intSerializer._isInt);
        assertEquals(INT, intSerializer.getSchema(null, null).getNumberType());

        assertEquals(JsonParser.NumberType.INT, intLikeSerializer._numberType);
        assertEquals("integer", intLikeSerializer._schemaType);
        assertTrue(intLikeSerializer._isInt);
        assertEquals(INT, intLikeSerializer.getSchema(null, null).getNumberType());

        assertEquals(JsonParser.NumberType.LONG, longSerializer._numberType);
        assertEquals("number", longSerializer._schemaType);
        assertFalse(longSerializer._isInt);
        assertEquals(LONG, longSerializer.getSchema(null, null).getNumberType());

        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer._numberType);
        assertEquals("number", floatSerializer._schemaType);
        assertFalse(floatSerializer._isInt);
        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer.getSchema(null, null).getNumberType());

        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer._numberType);
        assertEquals("number", doubleSerializer._schemaType);
        assertFalse(doubleSerializer._isInt);
        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer.getSchema(null, null).getNumberType());
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        IntegerSerializer integerSerializer = new IntegerSerializer();

        JsonNode result = mapper.writeValueAsTree(123, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.INT, result.getNodeType());
        assertEquals("123.0", result.asText());

        result = mapper.writeValueAsTree(Integer.MIN_VALUE, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.INT, result.getNodeType());
        assertEquals("-2147483648.0", result.asText());

        result = mapper.writeValueAsTree(Integer.MAX_VALUE, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.INT, result.getNodeType());
        assertEquals("2147483647.0", result.asText());
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();

        JsonNode result = mapper.writeValueAsTree(123, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.INT, result.getNodeType());
        assertEquals("123.0", result.asText());

        result = mapper.writeValueAsTree((byte) 123, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.INT, result.getNodeType());
        assertEquals("123.0", result.asText());

        result = mapper.writeValueAsTree((short) 123, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.INT, result.getNodeType());
        assertEquals("123.0", result.asText());

        result = mapper.writeValueAsTree(123L, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.LONG, result.getNodeType());
        assertEquals("123.0", result.asText());
    }

    @Test
    public void testLongSerializer() throws Exception {
        LongSerializer longSerializer = new LongSerializer();

        JsonNode result = mapper.writeValueAsTree(1234567890123L, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.LONG, result.getNodeType());
        assertEquals("1234567890123.0", result.asText());

        result = mapper.writeValueAsTree(-1234567890123L, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.LONG, result.getNodeType());
        assertEquals("-1234567890123.0", result.asText());
    }

    @Test
    public void testShortSerializer() throws Exception {
        ShortSerializer shortSerializer = new ShortSerializer();

        JsonNode result = mapper.writeValueAsTree((short) 123, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.INT, result.getNodeType());
        assertEquals("123.0", result.asText());

        result = mapper.writeValueAsTree((short) Short.MIN_VALUE, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.INT, result.getNodeType());
        assertEquals("-32768.0", result.asText());

        result = mapper.writeValueAsTree((short) Short.MAX_VALUE, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.INT, result.getNodeType());
        assertEquals("32767.0", result.asText());
    }

    @Test
    public void testFloatSerializer() throws Exception {
        FloatSerializer floatSerializer = new FloatSerializer();

        JsonNode result = mapper.writeValueAsTree(123.45f, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.FLOAT, result.getNodeType());
        assertEquals("123.45", result.asText());

        result = mapper.writeValueAsTree(-123.45f, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.FLOAT, result.getNodeType());
        assertEquals("-123.45", result.asText());
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        DoubleSerializer doubleSerializer = new DoubleSerializer();

        JsonNode result = mapper.writeValueAsTree(123.45, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.DOUBLE, result.getNodeType());
        assertEquals("123.45", result.asText());

        result = mapper.writeValueAsTree(-123.45, new ObjectMapper().disable(FAIL_ON_EMPTY_BEANS));
        assertEquals(JsonParser.NumberType.DOUBLE, result.getNodeType());
        assertEquals("-123.45", result.asText());
    }

    @Test
    public void testJsonFormatAnnotation() throws Exception {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        static class TestBean {
            private Short value;

            public Short getValue() {
                return value;
            }

            public void setValue(Short value) {
                this.value = value;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        bean.setValue((short) 123);

        JsonNode result = mapper.writeValueAsTree(bean);
        assertEquals(JsonParser.NumberType.STRING, result.getNodeType());
        assertEquals("123", result.asText());

        TestBean deserialized = mapper.readValue(result.toString(), TestBean.class);
        assertEquals(123, deserialized.getValue().shortValue());
    }

    @Test
    public void testCreateContextual() throws Exception {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        static class TestBean {
            private Short value;

            public Short getValue() {
                return value;
            }

            public void setValue(Short value) {
                this.value = value;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        bean.setValue((short) 123);

        JsonNode result = mapper.writeValueAsTree(bean);
        assertEquals(JsonParser.NumberType.STRING, result.getNodeType());
        assertEquals("123", result.asText());

        TestBean deserialized = mapper.readValue(result.toString(), TestBean.class);
        assertEquals(123, deserialized.getValue().shortValue());

        JsonSerializer<TestBean> serializer = mapper.findValueSerializer(TestBean.class);
        assertEquals(ToStringSerializer.instance, serializer);
    }
}