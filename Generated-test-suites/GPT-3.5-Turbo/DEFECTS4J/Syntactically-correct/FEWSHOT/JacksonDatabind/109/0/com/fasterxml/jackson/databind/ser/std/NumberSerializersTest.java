
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

public class NumberSerializersTest {

    private NumberSerializers.Base<Object> createBase() {
        return new NumberSerializers.Base<Object>(Number.class, JsonParser.NumberType.INT, "number") {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
                return null;
            }
        };
    }

    @Test
    public void testBase() throws JsonMappingException {
        NumberSerializers.Base<Object> base = createBase();
        assertEquals(JsonParser.NumberType.INT, base._numberType);
        assertEquals("number", base._schemaType);
        assertEquals(false, base._isInt);
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        Short value = 10;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        Integer value = 10;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        Number value = 10;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        Long value = 10L;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        Float value = 10.5f;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        Double value = 10.5;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }
}
