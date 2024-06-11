package com.example.junit; // replace with your package name

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.DoubleSerializer;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializersTest {

    @InjectMocks
    private NumberSerializers numberSerializers;

    private JsonFactory jsonFactory = Mockito.spy(new JsonFactory());
    private ObjectMapper objectMapper = Mockito.spy(new ObjectMapper());

    @Test
    public void testAddAll() throws Exception {
        Map<String, JsonSerializer<?>> allDeserializers = Mockito.mock(Map.class);
        numberSerializers.addAll(allDeserializers);
        Mockito.verify(allDeserializers).put(Integer.class.getName(), numberSerializers.new IntegerSerializer());
        Mockito.verify(allDeserializers).put(Integer.TYPE.getName(), numberSerializers.new IntegerSerializer());
        Mockito.verify(allDeserializers).put(Long.class.getName(), numberSerializers.new LongSerializer());
        Mockito.verify(allDeserializers).put(Long.TYPE.getName(), numberSerializers.new LongSerializer());
        Mockito.verify(allDeserializers).put(Byte.class.getName(), numberSerializers.new IntLikeSerializer());
        Mockito.verify(allDeserializers).put(Byte.TYPE.getName(), numberSerializers.new IntLikeSerializer());
        Mockito.verify(allDeserializers).put(Short.class.getName(), numberSerializers.new ShortSerializer());
        Mockito.verify(allDeserializers).put(Short.TYPE.getName(), numberSerializers.new ShortSerializer());
        Mockito.verify(allDeserializers).put(Float.class.getName(), numberSerializers.new FloatSerializer());
        Mockito.verify(allDeserializers).put(Float.TYPE.getName(), numberSerializers.new FloatSerializer());
        Mockito.verify(allDeserializers).put(Double.class.getName(), numberSerializers.new DoubleSerializer());
        Mockito.verify(allDeserializers).put(Double.TYPE.getName(), numberSerializers.new DoubleSerializer());
    }

    @Test
    public void testShortSerializer_serialize() throws Exception {
        ShortSerializer shortSerializer = numberSerializers.new ShortSerializer();
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.spy(new SerializerProvider() {});

        shortSerializer.serialize(Short.valueOf("127"), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(127);
    }

    @Test
    public void testIntegerSerializer_serialize() throws Exception {
        IntegerSerializer integerSerializer = numberSerializers.new IntegerSerializer();
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.spy(new SerializerProvider() {});

        integerSerializer.serialize(Integer.valueOf("255"), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(255);
    }

    @Test
    public void testIntLikeSerializer_serialize() throws Exception {
        IntLikeSerializer intLikeSerializer = numberSerializers.new IntLikeSerializer();
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.spy(new SerializerProvider() {});

        intLikeSerializer.serialize(Byte.valueOf("128"), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(128);
    }

    @Test
    public void testLongSerializer_serialize() throws Exception {
        LongSerializer longSerializer = numberSerializers.new LongSerializer();
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.spy(new SerializerProvider() {});

        longSerializer.serialize(Long.valueOf("9223372036854775807L"), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(9223372036854775807L);
    }

    @Test
    public void testFloatSerializer_serialize() throws Exception {
        FloatSerializer floatSerializer = numberSerializers.new FloatSerializer();
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.spy(new SerializerProvider() {});

        floatSerializer.serialize(Float.valueOf("3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955"), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955F);
    }

    @Test
    public void testDoubleSerializer_serialize() throws Exception {
        DoubleSerializer doubleSerializer = numberSerializers.new DoubleSerializer();
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.spy(new SerializerProvider() {});

        doubleSerializer.serialize(Double.valueOf("6.02214076e23"), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeNumber(6.02214076e23D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSerializeWithNullValue() throws Exception {
        Base<Object> baseSerializer = numberSerializers.new Base<>(Object.class, JsonParser.NumberType.INT, "number");
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.spy(new SerializerProvider() {});

        baseSerializer.serialize(null, jsonGenerator, serializerProvider);
    }
}