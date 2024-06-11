
package com.example;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {
    
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<String, JsonSerializer<?>>();
        NumberSerializers.addAll(allDeserializers);

        // Check if all required serializers are added
        assert(allDeserializers.containsKey(Integer.class.getName()));
        assert(allDeserializers.containsKey(Integer.TYPE.getName()));
        assert(allDeserializers.containsKey(Long.class.getName()));
        assert(allDeserializers.containsKey(Long.TYPE.getName()));
        assert(allDeserializers.containsKey(Byte.class.getName()));
        assert(allDeserializers.containsKey(Byte.TYPE.getName()));
        assert(allDeserializers.containsKey(Short.class.getName()));
        assert(allDeserializers.containsKey(Short.TYPE.getName()));
        assert(allDeserializers.containsKey(Float.class.getName()));
        assert(allDeserializers.containsKey(Float.TYPE.getName()));
        assert(allDeserializers.containsKey(Double.class.getName()));
        assert(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize((short) 10, gen, provider);

        gen.close();
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(10, gen, provider);

        gen.close();
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(10, gen, provider);

        gen.close();
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        LongSerializer serializer = new LongSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize((long) 10, gen, provider);

        gen.close();
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(10.5f, gen, provider);

        gen.close();
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(10.5, gen, provider);

        gen.close();
    }
}

