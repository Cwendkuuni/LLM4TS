package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

public class NumberSerializersTest {

    @SuppressWarnings("unchecked")
    public static void addAll(Map<String, JsonSerializer<?>> allDeserializers) {
        final JsonSerializer<?> intS = new IntegerSerializer();
        allDeserializers.put(Integer.class.getName(), intS);
        allDeserializers.put(Integer.TYPE.getName(), intS);
        allDeserializers.put(Long.class.getName(), LongSerializer.instance);
        allDeserializers.put(Long.TYPE.getName(), LongSerializer.instance);
        allDeserializers.put(Byte.class.getName(), IntLikeSerializer.instance);
        allDeserializers.put(Byte.TYPE.getName(), IntLikeSerializer.instance);
        allDeserializers.put(Short.class.getName(), ShortSerializer.instance);
        allDeserializers.put(Short.TYPE.getName(), ShortSerializer.instance);

        // Numbers, limited length floating point
        allDeserializers.put(Float.class.getName(), FloatSerializer.instance);
        allDeserializers.put(Float.TYPE.getName(), FloatSerializer.instance);
        allDeserializers.put(Double.class.getName(), DoubleSerializer.instance);
        allDeserializers.put(Double.TYPE.getName(), DoubleSerializer.instance);
    }

    @Test
    public void testBase() {
        Base<?> base = new Base<Object>(Integer.class, JsonParser.NumberType.INT, "number") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {}

            @Override
            public JsonNode getSchema(SerializerProvider provider, JavaType typeHint) { return null; }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {}

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
                return null;
            }
        };
        base.serialize(null, null, null);

        // Add more tests here
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer ss = new ShortSerializer();
        ss.serialize(null, null, null);

        // Add more tests here
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer is = new IntegerSerializer();
        is.serialize(null, null, null);

        // Add more tests here
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer ils = new IntLikeSerializer();
        ils.serialize(null, null, null);

        // Add more tests here
    }

    @Test
    public void testLongSerializer() {
        LongSerializer ls = new LongSerializer();
        ls.serialize(null, null, null);

        // Add more tests here
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer fs = new FloatSerializer();
        fs.serialize(null, null, null);

        // Add more tests here
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer ds = new DoubleSerializer();
        ds.serialize(null, null, null);

        // Add more tests here
    }
    
}