package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testBaseGetSchema() {
        NumberSerializers.Base base = new NumberSerializers.Base() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

            }
        };
        JsonNode schema = base.getSchema(Mockito.mock(SerializerProvider.class), Mockito.mock(Type.class));

        assertNotNull(schema);
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() {
        NumberSerializers.Base base = new NumberSerializers.Base() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

            }
        };
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        base.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).expectNumberFormat(typeHint, base._numberType);
    }

    @Test
    public void testBaseContextual() throws JsonMappingException {
        NumberSerializers.Base base = new NumberSerializers.Base() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

            }
        };
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        assertNull(base.createContextual(prov, property));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize((short) 1, gen, provider);

        Mockito.verify(gen).writeNumber(1);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(1, gen, provider);

        Mockito.verify(gen).writeNumber(1);
    }

    @Test
    public void testIntegerSerializerWithType() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        serializer.serializeWithType(1, gen, provider, typeSer);

        Mockito.verify(gen).writeNumber(1);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(1, gen, provider);

        Mockito.verify(gen).writeNumber(1);
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(1L, gen, provider);

        Mockito.verify(gen).writeNumber(1L);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(1.0f, gen, provider);

        Mockito.verify(gen).writeNumber(1.0f);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        serializer.serialize(1.0, gen, provider);

        Mockito.verify(gen).writeNumber(1.0);
    }

    @Test
    public void testDoubleSerializerWithType() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        serializer.serializeWithType(1.0, gen, provider, typeSer);

        Mockito.verify(gen).writeNumber(1.0);
    }
}