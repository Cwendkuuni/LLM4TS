package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Unit tests for {@link NumberSerializers} class.
 */
public class NumberSerializersTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAddAll() throws IOException {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Assert.assertEquals(11, allDeserializers.size());
        Assert.assertNotNull(allDeserializers.get(Integer.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Long.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Byte.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Short.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Float.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Float.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Double.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<Integer> intSerializer = NumberSerializers.getInstance(IntegerSerializer.class);

        Integer number = 123;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        Assert.assertEquals("123", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Integer deserializedNumber = intSerializer.deserialize(parser, Integer.class);
        Assert.assertEquals(number, deserializedNumber);
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<Long> longSerializer = NumberSerializers.getInstance(LongSerializer.class);

        Long number = 456L;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        Assert.assertEquals("456", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Long deserializedNumber = longSerializer.deserialize(parser, Long.class);
        Assert.assertEquals(number, deserializedNumber);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonSerializer<Number> intLikeSerializer = NumberSerializers.getInstance(IntLikeSerializer.class);

        Byte byteNumber = 7;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(byteNumber);
        Assert.assertEquals("7", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = intLikeSerializer.deserialize(parser, Number.class);
        Assert.assertEquals(byteNumber, deserializedNumber);

        Short shortNumber = 11;
        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(shortNumber);
        Assert.assertEquals("11", json);

        parser = mapper.getFactory().createParser(json);
        deserializedNumber = intLikeSerializer.deserialize(parser, Number.class);
        Assert.assertEquals(shortNumber, deserializedNumber);

        Integer intNumber = 22;
        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(intNumber);
        Assert.assertEquals("22", json);

        parser = mapper.getFactory().createParser(json);
        deserializedNumber = intLikeSerializer.deserialize(parser, Number.class);
        Assert.assertEquals(intNumber, deserializedNumber);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<Float> floatSerializer = NumberSerializers.getInstance(FloatSerializer.class);

        Float number = 3.14F;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        Assert.assertEquals("3.14", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Float deserializedNumber = floatSerializer.deserialize(parser, Float.class);
        Assert.assertEquals(number, deserializedNumber);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<Double> doubleSerializer = NumberSerializers.getInstance(DoubleSerializer.class);

        Double number = 1.23;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        Assert.assertEquals("1.23", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Double deserializedNumber = doubleSerializer.deserialize(parser, Double.class);
        Assert.assertEquals(number, deserializedNumber);
    }

    /**
     * Helper method to get an instance of a serializer class.
     *
     * @param clazz the serializer class
     * @param <T>    the type of the serializer
     * @return an instance of the serializer class
     */
    private static <T extends JsonSerializer<?>> T getInstance(Class<T> clazz) {
        return mapper.getSerializationConfig()
                .findValueSerializer(clazz);
    }
}