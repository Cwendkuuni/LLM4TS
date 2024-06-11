
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonFactory jsonFactory = new JsonFactory();
    private ObjectWriter objectWriter = objectMapper.writer();

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        Writer writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);

        serializer.serialize(100, generator, null);
        generator.flush();

        Assert.assertEquals("100", writer.toString());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        Writer writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);

        serializer.serialize(100, generator, null);
        generator.flush();

        Assert.assertEquals("100", writer.toString());
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        Writer writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);

        serializer.serialize((short) 100, generator, null);
        generator.flush();

        Assert.assertEquals("100", writer.toString());
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        Writer writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);

        serializer.serialize(100L, generator, null);
        generator.flush();

        Assert.assertEquals("100", writer.toString());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        Writer writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);

        serializer.serialize(100.0f, generator, null);
        generator.flush();

        Assert.assertEquals("100.0", writer.toString());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        Writer writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);

        serializer.serialize(100.0, generator, null);
        generator.flush();

        Assert.assertEquals("100.0", writer.toString());
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Assert.assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }
}
