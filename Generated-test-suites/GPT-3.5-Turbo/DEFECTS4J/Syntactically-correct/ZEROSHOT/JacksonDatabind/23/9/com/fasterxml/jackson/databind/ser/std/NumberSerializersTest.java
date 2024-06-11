package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
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

    @SuppressWarnings("serial")
    public class MockJsonGenerator extends JsonGenerator {
        // Mock JsonGenerator implementation
        // ...

        @Override
        public void close() throws IOException {}

        @Override
        public void writeNumber(int v) throws IOException {}

        // Override other necessary methods
        // ...
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> deserializers = new HashMap<String, JsonSerializer<?>>();
        NumberSerializers.addAll(deserializers);

        assertNotNull(deserializers.get(Integer.class.getName()));
        assertNotNull(deserializers.get(Integer.TYPE.getName()));
        assertNotNull(deserializers.get(Long.class.getName()));
        assertNotNull(deserializers.get(Long.TYPE.getName()));
        assertNotNull(deserializers.get(Byte.class.getName()));
        assertNotNull(deserializers.get(Byte.TYPE.getName()));
        assertNotNull(deserializers.get(Short.class.getName()));
        assertNotNull(deserializers.get(Short.TYPE.getName()));
        assertNotNull(deserializers.get(Float.class.getName()));
        assertNotNull(deserializers.get(Float.TYPE.getName()));
        assertNotNull(deserializers.get(Double.class.getName()));
        assertNotNull(deserializers.get(Double.TYPE.getName()));
    }

    @JacksonStdImpl
    public final static class ShortSerializerTest extends Base<Short> {
        public ShortSerializerTest() { super(Short.class, JsonParser.NumberType.INT, "number"); }

        @Override
        public void serialize(Short value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value.shortValue());
        }
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializerTest serializer = new ShortSerializerTest();
        MockJsonGenerator gen = new MockJsonGenerator();

        serializer.serialize((short) 10, gen, null);

        // Verify the result
        // ...
    }

    @JacksonStdImpl
    public final static class IntegerSerializerTest extends Base<Object> {
        public IntegerSerializerTest() { super(Integer.class, JsonParser.NumberType.INT, "integer"); }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Integer) value).intValue());
        }
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializerTest serializer = new IntegerSerializerTest();
        MockJsonGenerator gen = new MockJsonGenerator();

        serializer.serialize(10, gen, null);

        // Verify the result
        // ...
    }

    @JacksonStdImpl
    public final static class IntLikeSerializerTest extends Base<Number> {
        public IntLikeSerializerTest() {
            super(Number.class, JsonParser.NumberType.INT, "integer");
        }

        @Override
        public void serialize(Number value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value.intValue());
        }
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializerTest serializer = new IntLikeSerializerTest();
        MockJsonGenerator gen = new MockJsonGenerator();

        serializer.serialize(10, gen, null);

        // Verify the result
        // ...
    }

    @JacksonStdImpl
    public final static class LongSerializerTest extends Base<Object> {
        public LongSerializerTest() { super(Long.class, JsonParser.NumberType.LONG, "number"); }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Long) value).longValue());
        }
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializerTest serializer = new LongSerializerTest();
        MockJsonGenerator gen = new MockJsonGenerator();

        serializer.serialize(10L, gen, null);

        // Verify the result
        // ...
    }

    @JacksonStdImpl
    public final static class FloatSerializerTest extends Base<Object> {

        public FloatSerializerTest() { super(Float.class, JsonParser.NumberType.FLOAT, "number"); }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Float) value).floatValue());
        }
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializerTest serializer = new FloatSerializerTest();
        MockJsonGenerator gen = new MockJsonGenerator();

        serializer.serialize(1.23f, gen, null);

        // Verify the result
        // ...
    }

    @JacksonStdImpl
    public final static class DoubleSerializerTest extends Base<Object> {

        public DoubleSerializerTest() { super(Double.class, JsonParser.NumberType.DOUBLE, "number"); }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Double) value).doubleValue());
        }
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializerTest serializer = new DoubleSerializerTest();
        MockJsonGenerator gen = new MockJsonGenerator();

        serializer.serialize(1.23, gen, null);

        // Verify the result
        // ...
    }

    // Other test cases for remaining classes...

}