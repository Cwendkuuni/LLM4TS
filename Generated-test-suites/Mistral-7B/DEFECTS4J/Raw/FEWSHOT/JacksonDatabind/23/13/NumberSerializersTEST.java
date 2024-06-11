package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        Short value = 123;
        JsonSerializer<Short> shortSerializer = new ShortSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
        shortSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer value = 123;
        JsonSerializer<Integer> intSerializer = new IntegerSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
        intSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        Integer value = 123;
        JsonSerializer<Number> intLikeSerializer = new IntLikeSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
        intLikeSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Long value = 123L;
        JsonSerializer<Long> longSerializer = new LongSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
        longSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Float value = 123.45f;
        JsonSerializer<Float> floatSerializer = new FloatSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
        floatSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-123.45", gen.getOutputBuffer().toString());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Double value = 123.45;
        JsonSerializer<Double> doubleSerializer = new DoubleSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
        doubleSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-123.45", gen.getOutputBuffer().toString());
    }

    @Test
    public void testContextualSerializer() throws JsonProcessingException {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        class MyClass {
            private Short myShort;

            public Short getMyShort() {
                return myShort;
            }

            public void setMyShort(Short myShort) {
                this.myShort = myShort;
            }
        }

        MyClass myClass = new MyClass();
        myClass.setMyShort((short) 123);
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<MyClass> contextualSerializer = mapper.findValueSerializer(MyClass.class, JsonSerializer.None.class);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
        contextualSerializer.serialize(myClass, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("\"-123\"", gen.getOutputBuffer().toString());
    }
}