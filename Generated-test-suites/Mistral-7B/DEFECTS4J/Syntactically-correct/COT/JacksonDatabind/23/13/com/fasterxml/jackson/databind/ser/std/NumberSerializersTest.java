package com.example.tests;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonGeneratorFactory;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {
    private ObjectMapper objectMapper;
    private JsonGeneratorFactory jsonGeneratorFactory;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonGeneratorFactory = objectMapper.getFactory();
    }

    @Test
    public void testAddAll() {
        // Create a mock Map for testing number serializer registration
        Map<String, JsonSerializer<?>> allDeserializers = Mockito.mock(Map.class);

        // Test method call
        NumberSerializers.addAll(allDeserializers);

        // Verify that all expected serializers were registered
        for (Class<?> numberType : new Class[] { Integer.class, Integer.TYPE, Long.class, Long.TYPE,
                Byte.class, Byte.TYPE, Short.class, Short.TYPE, Float.class, Float.TYPE, Double.class, Double.TYPE }) {
            JsonSerializer<?> serializer = allDeserializers.get(numberType.getName());
            Assert.assertNotNull(serializer);
        }
    }

    @Test
    public void testShortSerializer_intValue() throws IOException {
        Short number = Short.MIN_VALUE;

        // Test method call
        JsonGenerator gen = jsonGeneratorFactory.createGenerator(System.out);
        NumberSerializers.ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        shortSerializer.serialize(number, gen, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return gen;
            }

            @Override
            public void close() throws IOException {
                gen.close();
            }
        });

        // Verify that the correct value was serialized
        String actualOutput = gen.getOutputBuffer().getString();
        String expectedOutput = String.format("%c%d", (char) 0x60, number.shortValue());
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIntegerSerializer_intValue() throws IOException {
        Integer number = Integer.MIN_VALUE;

        // Test method call
        JsonGenerator gen = jsonGeneratorFactory.createGenerator(System.out);
        NumberSerializers.IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        integerSerializer.serialize(number, gen, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return gen;
            }

            @Override
            public void close() throws IOException {
                gen.close();
            }
        });

        // Verify that the correct value was serialized
        String actualOutput = gen.getOutputBuffer().getString();
        String expectedOutput = String.format("%d", number.intValue());
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIntLikeSerializer_intValue() throws IOException {
        Byte number = Byte.MIN_VALUE;

        // Test method call
        JsonGenerator gen = jsonGeneratorFactory.createGenerator(System.out);
        NumberSerializers.IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        intLikeSerializer.serialize(number, gen, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return gen;
            }

            @Override
            public void close() throws IOException {
                gen.close();
            }
        });

        // Verify that the correct value was serialized
        String actualOutput = gen.getOutputBuffer().getString();
        String expectedOutput = String.format("%d", number.intValue());
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLongSerializer_longValue() throws IOException {
        Long number = Long.MIN_VALUE;

        // Test method call
        JsonGenerator gen = jsonGeneratorFactory.createGenerator(System.out);
        NumberSerializers.LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        longSerializer.serialize(number, gen, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return gen;
            }

            @Override
            public void close() throws IOException {
                gen.close();
            }
        });

        // Verify that the correct value was serialized
        String actualOutput = gen.getOutputBuffer().getString();
        String expectedOutput = String.format("%d", number.longValue());
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFloatSerializer_floatValue() throws IOException {
        Float number = Float.NEGATIVE_INFINITY;

        // Test method call
        JsonGenerator gen = jsonGeneratorFactory.createGenerator(System.out);
        NumberSerializers.FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        floatSerializer.serialize(number, gen, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return gen;
            }

            @Override
            public void close() throws IOException {
                gen.close();
            }
        });

        // Verify that the correct value was serialized
        String actualOutput = gen.getOutputBuffer().getString();
        String expectedOutput = "-INF";
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDoubleSerializer_doubleValue() throws IOException {
        Double number = Double.NaN;

        // Test method call
        JsonGenerator gen = jsonGeneratorFactory.createGenerator(System.out);
        NumberSerializers.DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        doubleSerializer.serialize(number, gen, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return gen;
            }

            @Override
            public void close() throws IOException {
                gen.close();
            }
        });

        // Verify that the correct value was serialized
        String actualOutput = gen.getOutputBuffer().getString();
        String expectedOutput = "NaN";
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}