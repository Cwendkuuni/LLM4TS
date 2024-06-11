package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

/**
 * Test cases for NumberSerializers class.
 */
@SuppressWarnings("unchecked")
public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
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
    public void testBaseClass() throws JsonProcessingException {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals("number", shortSerializer.getSchema(null, null).getSchemaName());
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        shortSerializer.serialize(Short.parseShort("123"), gen, new DefaultSerializerProvider());
        String serialized = gen.getOutputBuffer().toString();
        assertEquals("123", serialized);

        IntegerSerializer intSerializer = new IntegerSerializer();
        assertEquals("integer", intSerializer.getSchema(null, null).getSchemaName());
        gen.reset();
        intSerializer.serialize(Integer.valueOf(456), gen, new DefaultSerializerProvider());
        serialized = gen.getOutputBuffer().toString();
        assertEquals("456", serialized);

        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertEquals("integer", intLikeSerializer.getSchema(null, null).getSchemaName());
        gen.reset();
        intLikeSerializer.serialize(Byte.valueOf((byte) 789), gen, new DefaultSerializerProvider());
        serialized = gen.getOutputBuffer().toString();
        assertEquals("789", serialized);

        LongSerializer longSerializer = new LongSerializer();
        assertEquals("number", longSerializer.getSchema(null, null).getSchemaName());
        gen.reset();
        longSerializer.serialize(Long.parseLong("9876543210"), gen, new DefaultSerializerProvider());
        serialized = gen.getOutputBuffer().toString();
        assertEquals("9876543210", serialized);

        FloatSerializer floatSerializer = new FloatSerializer();
        assertEquals("number", floatSerializer.getSchema(null, null).getSchemaName());
        gen.reset();
        floatSerializer.serialize(Float.parseFloat("3.14"), gen, new DefaultSerializerProvider());
        serialized = gen.getOutputBuffer().toString();
        assertEquals("3.14", serialized);

        DoubleSerializer doubleSerializer = new DoubleSerializer();
        assertEquals("number", doubleSerializer.getSchema(null, null).getSchemaName());
        gen.reset();
        doubleSerializer.serialize(Double.parseDouble("2.71828"), gen, new DefaultSerializerProvider());
        serialized = gen.getOutputBuffer().toString();
        assertEquals("2.71828", serialized);
    }

    @Test
    public void testContextualSerializer() throws JsonProcessingException {
        JsonFormat format = new JsonFormat(new Annotation() {
            @JsonFormat.Value
            public String shape() {
                return "string";
            }
        });
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonSerializer<?> stringSerializer = shortSerializer.createContextual(new DefaultSerializerProvider(), null);
        assertEquals(ToStringSerializer.instance, stringSerializer);
    }
}