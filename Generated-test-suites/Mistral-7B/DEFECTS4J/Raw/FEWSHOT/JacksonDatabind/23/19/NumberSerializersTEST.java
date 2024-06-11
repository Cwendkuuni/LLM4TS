package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        Short value = Short.parseShort("123");

        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), getObjectMapper().getSerializerProvider());
            shortSerializer.serialize(value, gen, getObjectMapper().getSerializerProvider());
            assertEquals("123", gen.getOutputText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        Integer value = Integer.valueOf(456);

        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), getObjectMapper().getSerializerProvider());
            integerSerializer.serialize(value, gen, getObjectMapper().getSerializerProvider());
            assertEquals("456", gen.getOutputText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        Number value = 789;

        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), getObjectMapper().getSerializerProvider());
            intLikeSerializer.serialize(value, gen, getObjectMapper().getSerializerProvider());
            assertEquals("789", gen.getOutputText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        Long value = Long.valueOf(1011121314L);

        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), getObjectMapper().getSerializerProvider());
            longSerializer.serialize(value, gen, getObjectMapper().getSerializerProvider());
            assertEquals("1011121314", gen.getOutputText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        Float value = Float.valueOf(3.14159265f);

        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), getObjectMapper().getSerializerProvider());
            floatSerializer.serialize(value, gen, getObjectMapper().getSerializerProvider());
            assertEquals("3.14159265", gen.getOutputText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        Double value = Double.valueOf(1.23456789);

        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), getObjectMapper().getSerializerProvider());
            doubleSerializer.serialize(value, gen, getObjectMapper().getSerializerProvider());
            assertEquals("1.23456789", gen.getOutputText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIntegerSerializerWithType() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        Integer value = Integer.valueOf(21);

        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), getObjectMapper().getSerializerProvider());
            TypeSerializer typeSerializer = getObjectMapper().getTypeSerializer();
            integerSerializer.serializeWithType(value, gen, getObjectMapper().getSerializerProvider(), typeSerializer);
            assertEquals("\"21\"", gen.getOutputText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLongSerializerWithType() {
        LongSerializer longSerializer = new LongSerializer();
        Long value = Long.valueOf(321);

        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), getObjectMapper().getSerializerProvider());
            TypeSerializer typeSerializer = getObjectMapper().getTypeSerializer();
            longSerializer.serializeWithType(value, gen, getObjectMapper().getSerializerProvider(), typeSerializer);
            assertEquals("\"321\"", gen.getOutputText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testContextualSerializer() {
        ObjectMapper objectMapper = getObjectMapper();
        MyClass myClass = new MyClass();
        MyClassWithFormat myClassWithFormat = new MyClassWithFormat();

        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider());
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(gen, myClass);
            assertEquals("{\"intValue\":1,\"longValue\":1,\"floatValue\":1.0,\"doubleValue\":1.0}", gen.getOutputText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider());
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(gen, myClassWithFormat);
            assertEquals("{\"intValue\":\"intValue\",\"longValue\":\"longValue\"," +
                         "\"floatValue\":\"floatValue\",\"doubleValue\":\"doubleValue\"}", gen.getOutputText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNullValue() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        assertNull(integerSerializer.createContextual(null, null));
    }

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    static class MyClass {
        private int intValue = 1;
        private long longValue = 1;
        private float floatValue = 1.0F;
        private double doubleValue = 1.0;
    }

    static class MyClassWithFormat {
        @JsonFormat(shape = JsonFormat.Shape.STRING, serializableAs = Integer.class)
        private Integer intValue;
        @JsonFormat(shape = JsonFormat.Shape.STRING, serializableAs = Long.class)
        private Long longValue;
        @JsonFormat(shape = JsonFormat.Shape.STRING, serializableAs = Float.class)
        private Float floatValue;
        @JsonFormat(shape = JsonFormat.Shape.STRING, serializableAs = Double.class)
        private Double doubleValue;
    }
}