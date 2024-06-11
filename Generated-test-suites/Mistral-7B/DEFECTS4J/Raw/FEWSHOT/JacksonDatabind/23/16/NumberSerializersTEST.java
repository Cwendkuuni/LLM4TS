package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(11, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        JsonSerializer<?> jsonSerializer = new JsonSerializer<Short>() {
            @Override
            public void serialize(Short value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.shortValue());
            }
        };
        assertEquals(jsonSerializer, shortSerializer);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonSerializer<?> jsonSerializer = new JsonSerializer<Integer>() {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.intValue());
            }
        };
        assertEquals(jsonSerializer, integerSerializer);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        JsonSerializer<?> jsonSerializer = new JsonSerializer<Number>() {
            @Override
            public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.intValue());
            }
        };
        assertEquals(jsonSerializer, intLikeSerializer);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        JsonSerializer<?> jsonSerializer = new JsonSerializer<Long>() {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.longValue());
            }
        };
        assertEquals(jsonSerializer, longSerializer);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        JsonSerializer<?> jsonSerializer = new JsonSerializer<Float>() {
            @Override
            public void serialize(Float value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.floatValue());
            }
        };
        assertEquals(jsonSerializer, floatSerializer);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        JsonSerializer<?> jsonSerializer = new JsonSerializer<Double>() {
            @Override
            public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.doubleValue());
            }
        };
        assertEquals(jsonSerializer, doubleSerializer);
    }

    @Test
    public void testSerializeWithType() throws JsonProcessingException {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        Object i = 1;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializerTestHelper(integerSerializer, i, gen, null);
        JsonParser parser = new JsonFactory().createParser(new StringReader(gen.getOutputStream().toString()));
        Object deserialized = mapperTestHelper.readValue(parser, Integer.class);
        assertEquals(i, deserialized);
    }

    @Test
    public void testSerializeWithType_IntLike() throws JsonProcessingException {
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        Number n = 1;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializerTestHelper(intLikeSerializer, n, gen, null);
        JsonParser parser = new JsonFactory().createParser(new StringReader(gen.getOutputStream().toString()));
        Object deserialized = mapperTestHelper.readValue(parser, Integer.class);
        assertEquals(1, deserialized);
    }

    @Test
    public void testSerializeWithType_Long() throws JsonProcessingException {
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        Object l = 1L;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializerTestHelper(longSerializer, l, gen, null);
        JsonParser parser = new JsonFactory().createParser(new StringReader(gen.getOutputStream().toString()));
        Object deserialized = mapperTestHelper.readValue(parser, Long.class);
        assertEquals(l, deserialized);
    }

    @Test
    public void testSerializeWithType_Float() throws JsonProcessingException {
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        Object f = 1.0f;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializerTestHelper(floatSerializer, f, gen, null);
        JsonParser parser = new JsonFactory().createParser(new StringReader(gen.getOutputStream().toString()));
        Object deserialized = mapperTestHelper.readValue(parser, Float.class);
        assertEquals(f, deserialized);
    }

    @Test
    public void testSerializeWithType_Double() throws JsonProcessingException {
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        Object d = 1.0;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializerTestHelper(doubleSerializer, d, gen, null);
        JsonParser parser = new JsonFactory().createParser(new StringReader(gen.getOutputStream().toString()));
        Object deserialized = mapperTestHelper.readValue(parser, Double.class);
        assertEquals(d, deserialized);
    }

    @Test
    public void testAcceptJsonFormatVisitor_Integer() throws JsonProcessingException {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonParser parser = new JsonFactory().createParser(new StringReader("1"));
        JsonFormatVisitorWrapper visitorWrapper = new JsonFormatVisitorWrapper(parser, null, null, null, null);
        JsonNumberFormatVisitor visitor = visitorWrapper.expectNumberFormat(null);
        assertEquals(JsonParser.NumberType.INT, visitor.numberType(integerSerializer));
    }

    @Test
    public void testAcceptJsonFormatVisitor_Long() throws JsonProcessingException {
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        JsonParser parser = new JsonFactory().createParser(new StringReader("1"));
        JsonFormatVisitorWrapper visitorWrapper = new JsonFormatVisitorWrapper(parser, null, null, null, null);
        JsonNumberFormatVisitor visitor = visitorWrapper.expectNumberFormat(null);
        assertEquals(JsonParser.NumberType.LONG, visitor.numberType(longSerializer));
    }

    @Test
    public void testAcceptJsonFormatVisitor_Float() throws JsonProcessingException {
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        JsonParser parser = new JsonFactory().createParser(new StringReader("1.0"));
        JsonFormatVisitorWrapper visitorWrapper = new JsonFormatVisitorWrapper(parser, null, null, null, null);
        JsonNumberFormatVisitor visitor = visitorWrapper.expectNumberFormat(null);
        assertEquals(JsonParser.NumberType.FLOAT, visitor.numberType(floatSerializer));
    }

    @Test
    public void testAcceptJsonFormatVisitor_Double() throws JsonProcessingException {
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        JsonParser parser = new JsonFactory().createParser(new StringReader("1.0"));
        JsonFormatVisitorWrapper visitorWrapper = new JsonFormatVisitorWrapper(parser, null, null, null, null);
        JsonNumberFormatVisitor visitor = visitorWrapper.expectNumberFormat(null);
        assertEquals(JsonParser.NumberType.DOUBLE, visitor.numberType(doubleSerializer));
    }

    @Test
    public void testCreateContextual() throws JsonProcessingException {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<?> jsonSerializer = mapper.findContextualSerializer(Integer.class, null);
        assertEquals(integerSerializer, jsonSerializer);
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        Schema schema = mapper.schemaForValue(1);
        assertEquals("integer", schema.getSchemaName());
        assertEquals(schema, integerSerializer.getSchema(new ObjectMapper(), null));
    }

    private static void serializerTestHelper(JsonSerializer<?> serializer, Object value, JsonGenerator gen, TypeSerializer typeSerializer) throws IOException {
        serializer.serialize(value, gen, new ObjectMapper().getDefaultSerializerProvider());
        if (typeSerializer != null) {
            typeSerializer.serialize(value.getClass(), value, gen, new ObjectMapper().getDefaultSerializerProvider());
        }
    }

    private static ObjectMapper mapperTestHelper = new ObjectMapper();

}