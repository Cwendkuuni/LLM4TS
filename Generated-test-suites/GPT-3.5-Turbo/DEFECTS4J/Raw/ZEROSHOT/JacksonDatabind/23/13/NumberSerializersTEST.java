
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
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

public class NumberSerializersTest {

    private NumberSerializers.ShortSerializer shortSerializer;
    private NumberSerializers.IntegerSerializer integerSerializer;
    private NumberSerializers.IntLikeSerializer intLikeSerializer;
    private NumberSerializers.LongSerializer longSerializer;
    private NumberSerializers.FloatSerializer floatSerializer;
    private NumberSerializers.DoubleSerializer doubleSerializer;

    @Before
    public void setUp() {
        shortSerializer = new NumberSerializers.ShortSerializer();
        integerSerializer = new NumberSerializers.IntegerSerializer();
        intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        longSerializer = new NumberSerializers.LongSerializer();
        floatSerializer = new NumberSerializers.FloatSerializer();
        doubleSerializer = new NumberSerializers.DoubleSerializer();
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        shortSerializer.serialize((short) 10, gen, provider);
        gen.close();
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        integerSerializer.serialize(10, gen, provider);
        gen.close();
    }

    @Test
    public void testSerializeIntLike() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        intLikeSerializer.serialize(10, gen, provider);
        gen.close();
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        longSerializer.serialize(10L, gen, provider);
        gen.close();
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        floatSerializer.serialize(10.5f, gen, provider);
        gen.close();
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        doubleSerializer.serialize(10.5, gen, provider);
        gen.close();
    }

}
