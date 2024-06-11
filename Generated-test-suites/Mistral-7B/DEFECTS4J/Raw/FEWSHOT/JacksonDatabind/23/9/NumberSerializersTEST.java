package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        assertEquals(Short.parseShort("123"), shortSerializer.deserialize(shortSerializer.serialize(Short.parseShort("123")), null));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        assertEquals(Integer.parseInt("456"), integerSerializer.deserialize(integerSerializer.serialize(Integer.parseInt("456")), null));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        assertEquals(Integer.parseInt("789"), intLikeSerializer.deserialize(intLikeSerializer.serialize(Integer.valueOf(789)), null));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        assertEquals(Long.parseLong("9876543210"), longSerializer.deserialize(longSerializer.serialize(Long.parseLong("9876543210")), null));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        assertEquals(Float.parseFloat("123.45"), floatSerializer.deserialize(floatSerializer.serialize(Float.parseFloat("123.45")), null));
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        assertEquals(Double.parseDouble("123.456"), doubleSerializer.deserialize(doubleSerializer.serialize(Double.parseDouble("123.456")), null));
    }

    @Test
    public void testIntegerSerializerAcceptJsonFormatVisitor() throws JsonProcessingException {
        JsonParser jsonParser = new JsonFactory().createParser("123");
        JsonNode desiredNode = Json.value("123");
        JsonSerializer<?> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonFormatVisitorWrapper visitorWrapper = new JsonFactory().createJsonFormatVisitorWrapper(null, null, null, new ObjectMapper().getSerializationConfig());
        JsonNumberFormatVisitor jsonNumberFormatVisitor = visitorWrapper.expectNumberFormat(null);
        integerSerializer.acceptJsonFormatVisitor(visitorWrapper, null);
        JsonNode actualNode = jsonNumberFormatVisitor.getNumberNode();
        assertEquals(desiredNode, actualNode);
    }

    @Test
    public void testDoubleSerializerAcceptJsonFormatVisitor() throws JsonProcessingException {
        JsonParser jsonParser = new JsonFactory().createParser("123.45");
        JsonNode desiredNode = Json.value("123.45");
        JsonSerializer<?> doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        JsonFormatVisitorWrapper visitorWrapper = new JsonFactory().createJsonFormatVisitorWrapper(null, null, null, new ObjectMapper().getSerializationConfig());
        JsonNumberFormatVisitor jsonNumberFormatVisitor = visitorWrapper.expectNumberFormat(null);
        doubleSerializer.acceptJsonFormatVisitor(visitorWrapper, null);
        JsonNode actualNode = jsonNumberFormatVisitor.getNumberNode();
        assertEquals(desiredNode, actualNode);
    }

    @Test
    public void testShortSerializerContextualSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode inputNode = Json.value("123");
        Short expectedShort = Short.parseShort("123");
        JsonSerializer<?> shortSerializer = NumberSerializers.ShortSerializer.instance;
        JsonNode resultNode = objectMapper.readTree(new JsonFactory().createParser(new ByteArrayOutputStream().toByteArray(inputNode.toString().getBytes())), Short.class);
        assertEquals(expectedShort, resultNode.asInt());
        assertNotNull(resultNode.get("__type"));
        assertEquals(TypeSerializer.getEmptyValueSerializer().getClass().getName(), resultNode.get("__type").asText());
    }

    @Test
    public void testIntegerSerializerContextualSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode inputNode = Json.value("456");
        Integer expectedInteger = Integer.parseInt("456");
        JsonSerializer<?> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonNode resultNode = objectMapper.readTree(new ByteArrayOutputStream().toByteArray(inputNode.toString().getBytes()), Integer.class);
        assertEquals(expectedInteger, resultNode.asInt());
        assertNotNull(resultNode.get("__type"));
        assertEquals(TypeSerializer.getEmptyValueSerializer().getClass().getName(), resultNode.get("__type").asText());
    }

    @Test
    public void testJsonFormatAnnotation() throws JsonProcessingException {
        class TestBean {
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
            private Short shortField;

            public Short getShortField() {
                return shortField;
            }

            public void setShortField(Short shortField) {
                this.shortField = shortField;
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TestBean testBean = new TestBean();
        testBean.setShortField((short) 123);
        String jsonString = objectMapper.writeValueAsString(testBean);
        JsonParser jsonParser = new JsonFactory().createParser(new ByteArrayInputStream(jsonString.getBytes()));
        TestBean deserializedBean = objectMapper.readValue(jsonParser, TestBean.class);
        assertEquals(testBean.getShortField(), deserializedBean.getShortField());
    }
}