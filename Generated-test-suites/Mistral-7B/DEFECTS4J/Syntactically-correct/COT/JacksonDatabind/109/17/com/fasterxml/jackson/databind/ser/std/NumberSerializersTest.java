import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddAll() throws IOException {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);
        Assert.assertEquals(11, allDeserializers.size());
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

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<Integer> serializer = objectMapper.findValueSerializer(new TypeReference<Integer>() {});
        Assert.assertSame(IntegerSerializer.class, serializer.getClass());
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        Integer value = 123;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        Assert.assertEquals(value, parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        Integer value = 123;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonSerializer<Integer> serializer = objectMapper.findValueSerializer(new TypeReference<Integer>() {});
        JsonGenerator gen = objectMapper.createGenerator(new java.io.File("test.json"));
        serializer.serializeWithType(value, gen, objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
        JsonParser parser2 = objectMapper.getFactory().createParser(new java.io.File("test.json"));
        Assert.assertEquals(value, parser2.readValueAs(Integer.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testIntegerSerializerInvalid() throws IOException {
        Integer value = new Integer(123);
        String json = "{\"value\": \"not a number\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonSerializer<Integer> serializer = objectMapper.findValueSerializer(new TypeReference<Integer>() {});
        serializer.deserialize(parser, null);
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<Long> serializer = objectMapper.findValueSerializer(new TypeReference<Long>() {});
        Assert.assertSame(LongSerializer.class, serializer.getClass());
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        Long value = 123L;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        Assert.assertEquals(value, parser.readValueAs(Long.class));
    }

    @Test
    public void testLongSerializerSerializeWithType() throws IOException {
        Long value = 123L;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonSerializer<Long> serializer = objectMapper.findValueSerializer(new TypeReference<Long>() {});
        JsonGenerator gen = objectMapper.createGenerator(new java.io.File("test.json"));
        serializer.serializeWithType(value, gen, objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
        JsonParser parser2 = objectMapper.getFactory().createParser(new java.io.File("test.json"));
        Assert.assertEquals(value, parser2.readValueAs(Long.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testLongSerializerInvalid() throws IOException {
        Long value = new Long(123);
        String json = "{\"value\": \"not a number\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonSerializer<Long> serializer = objectMapper.findValueSerializer(new TypeReference<Long>() {});
        serializer.deserialize(parser, null);
    }

    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<Short> serializer = objectMapper.findValueSerializer(new TypeReference<Short>() {});
        Assert.assertSame(ShortSerializer.class, serializer.getClass());
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        Short value = (short) 123;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        Assert.assertEquals(value, parser.readValueAs(Short.class));
    }

    @Test
    public void testShortSerializerSerializeWithType() throws IOException {
        Short value = (short) 123;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonSerializer<Short> serializer = objectMapper.findValueSerializer(new TypeReference<Short>() {});
        JsonGenerator gen = objectMapper.createGenerator(new java.io.File("test.json"));
        serializer.serializeWithType(value, gen, objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
        JsonParser parser2 = objectMapper.getFactory().createParser(new java.io.File("test.json"));
        Assert.assertEquals(value, parser2.readValueAs(Short.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testShortSerializerInvalid() throws IOException {
        Short value = (short) 123;
        String json = "{\"value\": \"not a number\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonSerializer<Short> serializer = objectMapper.findValueSerializer(new TypeReference<Short>() {});
        serializer.deserialize(parser, null);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<Float> serializer = objectMapper.findValueSerializer(new TypeReference<Float>() {});
        Assert.assertSame(FloatSerializer.class, serializer.getClass());
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        Float value = 123.4f;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        Assert.assertEquals(value, parser.readValueAs(Float.class));
    }

    @Test
    public void testFloatSerializerSerializeWithType() throws IOException {
        Float value = 123.4f;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonSerializer<Float> serializer = objectMapper.findValueSerializer(new TypeReference<Float>() {});
        JsonGenerator gen = objectMapper.createGenerator(new java.io.File("test.json"));
        serializer.serializeWithType(value, gen, objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
        JsonParser parser2 = objectMapper.getFactory().createParser(new java.io.File("test.json"));
        Assert.assertEquals(value, parser2.readValueAs(Float.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testFloatSerializerInvalid() throws IOException {
        Float value = 123.4f;
        String json = "{\"value\": \"not a number\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonSerializer<Float> serializer = objectMapper.findValueSerializer(new TypeReference<Float>() {});
        serializer.deserialize(parser, null);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<Double> serializer = objectMapper.findValueSerializer(new TypeReference<Double>() {});
        Assert.assertSame(DoubleSerializer.class, serializer.getClass());
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        Double value = 123.4;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        Assert.assertEquals(value, parser.readValueAs(Double.class));
    }

    @Test
    public void testDoubleSerializerSerializeWithType() throws IOException {
        Double value = 123.4;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonSerializer<Double> serializer = objectMapper.findValueSerializer(new TypeReference<Double>() {});
        JsonGenerator gen = objectMapper.createGenerator(new java.io.File("test.json"));
        serializer.serializeWithType(value, gen, objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
        JsonParser parser2 = objectMapper.getFactory().createParser(new java.io.File("test.json"));
        Assert.assertEquals(value, parser2.readValueAs(Double.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDoubleSerializerInvalid() throws IOException {
        Double value = 123.4;
        String json = "{\"value\": \"not a number\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        JsonSerializer<Double> serializer = objectMapper.findValueSerializer(new TypeReference<Double>() {});
        serializer.deserialize(parser, null);
    }
}