package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    // Test Base class methods
    @Test
    public void testBaseClassMethods() {
        // Test getSchema method
        Base<Short> shortSerializer = new ShortSerializer();
        assertEquals("number", shortSerializer.getSchema(null, null).getTextValue());

        // Test acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper visitor = null;
        ShortSerializer shortSerializerInstance = ShortSerializer.instance;
        shortSerializerInstance.acceptJsonFormatVisitor(visitor, null);
        assertNull(visitor);

        JsonIntegerFormatVisitor intVisitor = null;
        IntegerSerializer intSerializer = IntegerSerializer.instance;
        intSerializer.acceptJsonFormatVisitor(visitor = new JsonFormatVisitorWrapper(), null);
        assertEquals(IntegerSerializer.class, visitor.getSerializationConfig().getClass());
        intVisitor = visitor.expectIntegerFormat(null);
        assertNull(intVisitor);

        JsonNumberFormatVisitor numberVisitor = null;
        DoubleSerializer doubleSerializer = DoubleSerializer.instance;
        doubleSerializer.acceptJsonFormatVisitor(visitor = new JsonFormatVisitorWrapper(), null);
        assertEquals(DoubleSerializer.class, visitor.getSerializationConfig().getClass());
        numberVisitor = visitor.expectNumberFormat(null);
        assertNull(numberVisitor);

        // Test createContextual method
        Object value = new Object() {
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            String testString;
        };
        SerializerProvider serializerProvider = null;
        Base<Object> baseSerializer = IntegerSerializer.instance;
        JsonSerializer<?> jsonSerializer = baseSerializer.createContextual(serializerProvider, null);
        assertNull(jsonSerializer);

        JsonSerializer<?> stringSerializer = ToStringSerializer.instance;
        jsonSerializer = baseSerializer.createContextual(serializerProvider, new BeanProperty());
        assertEquals(stringSerializer.getClass(), jsonSerializer.getClass());
    }

    // Test ShortSerializer
    @Test
    public void testShortSerializer() {
        Base<Short> shortSerializer = new ShortSerializer();
        Short value = Short.MIN_VALUE;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        shortSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = Short.MAX_VALUE;
        shortSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = (short) 0;
        shortSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        short valueToSerialize = 123;
        String expectedOutput = String.valueOf(valueToSerialize);
        gen = new MockJsonGenerator();
        provider = new MockSerializerProvider();
        shortSerializer.serialize(valueToSerialize, gen, provider);
        assertEquals(expectedOutput, gen.getOutputContent());
    }

    // Test IntegerSerializer
    @Test
    public void testIntegerSerializer() {
        Base<Object> intSerializer = IntegerSerializer.instance;
        Integer value = Integer.MIN_VALUE;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        intSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = Integer.MAX_Value;
        intSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = 0;
        intSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        int valueToSerialize = 456;
        String expectedOutput = String.valueOf(valueToSerialize);
        gen = new MockJsonGenerator();
        provider = new MockSerializerProvider();
        intSerializer.serialize(valueToSerialize, gen, provider);
        assertEquals(expectedOutput, gen.getOutputContent());

        // Test serializeWithType method
        int valueToSerializeWithType = 789;
        String expectedOutputWithType = "789";
        gen = new MockJsonGenerator();
        provider = new MockSerializerProvider();
        intSerializer.serializeWithType(valueToSerializeWithType, gen, provider, TypeSerializer.instance);
        assertEquals(expectedOutputWithType, gen.getOutputContent());
    }

    // Test IntLikeSerializer
    @Test
    public void testIntLikeSerializer() {
        Base<Number> intLikeSerializer = IntLikeSerializer.instance;
        Number value = Byte.MIN_VALUE;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        intLikeSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = Byte.MAX_VALUE;
        intLikeSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = (byte) 0;
        intLikeSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        byte valueToSerialize = 123;
        String expectedOutput = String.valueOf(valueToSerialize);
        gen = new MockJsonGenerator();
        provider = new MockSerializerProvider();
        intLikeSerializer.serialize(valueToSerialize, gen, provider);
        assertEquals(expectedOutput, gen.getOutputContent());

        // Test serialize method with integer value
        int intValueToSerialize = 456;
        String expectedOutputInt = String.valueOf(intValueToSerialize);
        gen = new MockJsonGenerator();
        provider = new MockSerializerProvider();
        intLikeSerializer.serialize(intValueToSerialize, gen, provider);
        assertEquals(expectedOutputInt, gen.getOutputContent());

        // Test serialize method with long value
        long longValueToSerialize = 789;
        String expectedOutputLong = String.valueOf(longValueToSerialize);
        gen = new MockJsonGenerator();
        provider = new MockSerializerProvider();
        intLikeSerializer.serialize(longValueToSerialize, gen, provider);
        assertEquals(expectedOutputLong, gen.getOutputContent());
    }

    // Test LongSerializer
    @Test
    public void testLongSerializer() {
        Base<Object> longSerializer = LongSerializer.instance;
        Long value = Long.MIN_VALUE;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        longSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = Long.MAX_VALUE;
        longSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = 0L;
        longSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        long valueToSerialize = 123456L;
        String expectedOutput = String.valueOf(valueToSerialize);
        gen = new MockJsonGenerator();
        provider = new MockSerializerProvider();
        longSerializer.serialize(valueToSerialize, gen, provider);
        assertEquals(expectedOutput, gen.getOutputContent());
    }

    // Test FloatSerializer
    @Test
    public void testFloatSerializer() {
        Base<Object> floatSerializer = FloatSerializer.instance;
        Float value = Float.MIN_VALUE;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        floatSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = Float.MAX_VALUE;
        floatSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = 0.0F;
        floatSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        float valueToSerialize = 123.45F;
        String expectedOutput = String.valueOf(valueToSerialize);
        gen = new MockJsonGenerator();
        provider = new MockSerializerProvider();
        floatSerializer.serialize(valueToSerialize, gen, provider);
        assertEquals(expectedOutput, gen.getOutputContent());
    }

    // Test DoubleSerializer
    @Test
    public void testDoubleSerializer() {
        Base<Object> doubleSerializer = DoubleSerializer.instance;
        Double value = Double.MIN_VALUE;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        doubleSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = Double.MAX_VALUE;
        doubleSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        value = 0.0;
        doubleSerializer.serialize(value, gen, provider);
        assertNull(gen);
        assertNull(provider);

        double valueToSerialize = 123456.789;
        String expectedOutput = String.valueOf(valueToSerialize);
        gen = new MockJsonGenerator();
        provider = new MockSerializerProvider();
        doubleSerializer.serialize(valueToSerialize, gen, provider);
        assertEquals(expectedOutput, gen.getOutputContent());
    }

    // Test addAll method
    @Test
    public void testAddAllMethod() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(10, allDeserializers.size());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.class.getName()));
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()));
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()));
        assertEquals(LongSerializer.class, allDeserializers.get(Long.TYPE.getName()));
        assertEquals(Byte.class, allDeserializers.get(Byte.class.getName()));
        assertEquals(Byte.TYPE, allDeserializers.get(Byte.TYPE.getName()));
        assertEquals(ShortSerializer.class, allDeserializers.get(Integer.class.getName()));
        assertEquals(ShortSerializer.class, allDeserializers.get(Integer.TYPE.getName()));
        assertEquals(IntegerSerializer.class, allDeserializers.get(Long.class.getName()));
        assertEquals(IntegerSerializer.class, allDeserializers.get(Long.TYPE.getName()));
    }
}