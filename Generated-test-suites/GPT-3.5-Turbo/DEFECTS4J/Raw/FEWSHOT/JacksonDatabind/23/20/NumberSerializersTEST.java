package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testAddAll() {
        // Test if all deserializers are added successfully
        NumberSerializers numberSerializers = new NumberSerializers();
        numberSerializers.addAll(allDeserializers);
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.TYPE.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.class.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.class.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.TYPE.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()).getClass());
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();

        // Test if schema is created successfully
        assertNotNull(shortSerializer.getSchema(provider, typeHint));

        // Test if number type is set correctly
        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        assertEquals("number", shortSerializer._schemaType);

        // Test if serialize method is implemented correctly
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator gen = new JsonFactory().createGenerator(out);
        shortSerializer.serialize(Short.valueOf("5"), gen, provider);
        gen.close();
        assertEquals("5", new String(out.toByteArray()));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();

        // Test if schema is created successfully
        assertNotNull(integerSerializer.getSchema(provider, typeHint));

        // Test if number type is set correctly
        assertEquals(JsonParser.NumberType.INT, integerSerializer._numberType);
        assertEquals("integer", integerSerializer._schemaType);

        // Test if serialize method is implemented correctly
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator gen = new JsonFactory().createGenerator(out);
        integerSerializer.serialize(Integer.valueOf("10"), gen, provider);
        gen.close();
        assertEquals("10", new String(out.toByteArray()));

        // Test if serialize method with type is implemented correctly
        out = new ByteArrayOutputStream();
        gen = new JsonFactory().createGenerator(out);
        integerSerializer.serializeWithType(Integer.valueOf("15"), gen, provider, typeSer);
        gen.close();
        assertEquals("15", new String(out.toByteArray()));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();

        // Test if schema is created successfully
        assertNotNull(intLikeSerializer.getSchema(provider, typeHint));

        // Test if number type is set correctly
        assertEquals(JsonParser.NumberType.INT, intLikeSerializer._numberType);
        assertEquals("integer", intLikeSerializer._schemaType);

        // Test if serialize method is implemented correctly
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator gen = new JsonFactory().createGenerator(out);
        intLikeSerializer.serialize(Byte.valueOf("2"), gen, provider);
        gen.close();
        assertEquals("2", new String(out.toByteArray()));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();

        // Test if schema is created successfully
        assertNotNull(longSerializer.getSchema(provider, typeHint));

        // Test if number type is set correctly
        assertEquals(JsonParser.NumberType.LONG, longSerializer._numberType);
        assertEquals("number", longSerializer._schemaType);

        // Test if serialize method is implemented correctly
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator gen = new JsonFactory().createGenerator(out);
        longSerializer.serialize(Long.valueOf("100"), gen, provider);
        gen.close();
        assertEquals("100", new String(out.toByteArray()));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();

        // Test if schema is created successfully
        assertNotNull(floatSerializer.getSchema(provider, typeHint));

        // Test if number type is set correctly
        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer._numberType);
        assertEquals("number", floatSerializer._schemaType);

        // Test if serialize method is implemented correctly
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator gen = new JsonFactory().createGenerator(out);
        floatSerializer.serialize(Float.valueOf("1.5"), gen, provider);
        gen.close();
        assertEquals("1.5", new String(out.toByteArray()));
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();

        // Test if schema is created successfully
        assertNotNull(doubleSerializer.getSchema(provider, typeHint));

        // Test if number type is set correctly
        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer._numberType);
        assertEquals("number", doubleSerializer._schemaType);

        // Test if serialize method is implemented correctly
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator gen = new JsonFactory().createGenerator(out);
        doubleSerializer.serialize(Double.valueOf("3.14"), gen, provider);
        gen.close();
        assertEquals("3.14", new String(out.toByteArray()));

        // Test if serialize method with type is implemented correctly
        out = new ByteArrayOutputStream();
        gen = new JsonFactory().createGenerator(out);
        doubleSerializer.serializeWithType(Double.valueOf("2.718"), gen, provider, typeSer);
        gen.close();
        assertEquals("2.718", new String(out.toByteArray()));
    }
}