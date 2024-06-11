package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    private final JsonSerializer<Object> integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
    private final JsonSerializer<Object> shortSerializer = new NumberSerializers.ShortSerializer();
    private final JsonSerializer<Object> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
    private final JsonSerializer<Object> longSerializer = new NumberSerializers.LongSerializer(Long.class);
    private final JsonSerializer<Object> floatSerializer = NumberSerializers.FloatSerializer.instance;
    private final JsonSerializer<Object> doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<Object> serializer = integerSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(1, gen, provider);
        gen.flush();
    }

    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<Object> serializer = shortSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize((short)1, gen, provider);
        gen.flush();
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonSerializer<Object> serializer = intLikeSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(1, gen, provider);
        gen.flush();
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<Object> serializer = longSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(1L, gen, provider);
        gen.flush();
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<Object> serializer = floatSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(1.0F, gen, provider);
        gen.flush();
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<Object> serializer = doubleSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(1.0, gen, provider);
        gen.flush();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(7, allDeserializers.size());
    }

    @Test
    public void testShortSerializerSerializeWithType() throws IOException {
        JsonSerializer<Object> serializer = shortSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        TypeSerializer typeSer = provider.findTypeSerializer(Short.class);
        serializer.serializeWithType((short)1, gen, provider, typeSer);
        gen.flush();
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        JsonSerializer<Object> serializer = integerSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        TypeSerializer typeSer = provider.findTypeSerializer(Integer.class);
        serializer.serializeWithType(1, gen, provider, typeSer);
        gen.flush();
    }

    @Test
    public void testIntLikeSerializerSerializeWithType() throws IOException {
        JsonSerializer<Object> serializer = intLikeSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        TypeSerializer typeSer = provider.findTypeSerializer(Number.class);
        serializer.serializeWithType(1, gen, provider, typeSer);
        gen.flush();
    }

    @Test
    public void testLongSerializerSerializeWithType() throws IOException {
        JsonSerializer<Object> serializer = longSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        TypeSerializer typeSer = provider.findTypeSerializer(Long.class);
        serializer.serializeWithType(1L, gen, provider, typeSer);
        gen.flush();
    }

    @Test
    public void testFloatSerializerSerializeWithType() throws IOException {
        JsonSerializer<Object> serializer = floatSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        TypeSerializer typeSer = provider.findTypeSerializer(Float.class);
        serializer.serializeWithType(1.0F, gen, provider, typeSer);
        gen.flush();
    }

    @Test
    public void testDoubleSerializerSerializeWithType() throws IOException {
        JsonSerializer<Object> serializer = doubleSerializer;
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        TypeSerializer typeSer = provider.findTypeSerializer(Double.class);
        serializer.serializeWithType(1.0, gen, provider, typeSer);
        gen.flush();
    }

    @Test
    public void testBaseGetSchema() {
        NumberSerializers.Base<?> base = shortSerializer;
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        base.getSchema(provider, Short.class);
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializers.Base<?> base = shortSerializer;
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getJsonFactory().getCodec().visitorWrapper(null);
        base.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testBaseCreateContextual() throws JsonMappingException {
        NumberSerializers.Base<?> base = shortSerializer;
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        BeanProperty property = new SimpleBeanProperty("test");
        base.createContextual(provider, property);
    }
}