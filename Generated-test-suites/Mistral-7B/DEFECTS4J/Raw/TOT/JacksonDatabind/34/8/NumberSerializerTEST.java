import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.Types;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class NumberSerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new BigDecimal("123.45");
        serializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "\"123.45\"";
        String actualJson = objectMapper.writeValueAsString(number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new BigInteger("12345678901234567890");
        serializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "\"12345678901234567890\"";
        String actualJson = objectMapper.writeValueAsString(number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeInt() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = 123;
        serializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "123";
        String actualJson = objectMapper.writeValueAsString(number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = 123456789012345L;
        serializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "123456789012345";
        String actualJson = objectMapper.writeValueAsString(number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = 123.45;
        serializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "123.45";
        String actualJson = objectMapper.writeValueAsString(number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = 123.45f;
        serializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "123.45";
        String actualJson = objectMapper.writeValueAsString(number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeByte() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = (byte) 127;
        serializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "127";
        String actualJson = objectMapper.writeValueAsString(number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeShort() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = (short) 12345;
        serializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "12345";
        String actualJson = objectMapper.writeValueAsString(number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = "123";
        serializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "\"123\"";
        String actualJson = objectMapper.writeValueAsString(number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = null;
        serializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "null";
        String actualJson = objectMapper.writeValueAsString(number);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"123.45\"";
        Number number = testObjectMapper.readValue(json, Number.class);

        Assert.assertNotNull(number);
        Assert.assertEquals(new BigDecimal("123.45"), number);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"12345678901234567890\"";
        Number number = testObjectMapper.readValue(json, Number.class);

        Assert.assertNotNull(number);
        Assert.assertEquals(new BigInteger("12345678901234567890"), number);
    }

    @Test
    public void testDeserializeInt() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "123";
        Number number = testObjectMapper.readValue(json, Number.class);

        Assert.assertNotNull(number);
        Assert.assertEquals(123, number.intValue());
    }

    @Test
    public void testDeserializeLong() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "123456789012345";
        Number number = testObjectMapper.readValue(json, Number.class);

        Assert.assertNotNull(number);
        Assert.assertEquals(123456789012345L, number.longValue());
    }

    @Test
    public void testDeserializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "123.45";
        Number number = testObjectMapper.readValue(json, Number.class);

        Assert.assertNotNull(number);
        Assert.assertEquals(123.45, number.doubleValue(), 0.01);
    }

    @Test
    public void testDeserializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "123.45";
        Number number = testObjectMapper.readValue(json, Number.class);

        Assert.assertNotNull(number);
        Assert.assertEquals(123.45f, number.floatValue(), 0.01f);
    }

    @Test
    public void testDeserializeByte() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "127";
        Number number = testObjectMapper.readValue(json, Number.class);

        Assert.assertNotNull(number);
        Assert.assertEquals((byte) 127, number.byteValue());
    }

    @Test
    public void testDeserializeShort() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "12345";
        Number number = testObjectMapper.readValue(json, Number.class);

        Assert.assertNotNull(number);
        Assert.assertEquals((short) 12345, number.shortValue());
    }

    @Test
    public void testDeserializeString() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"123\"";
        Number number = testObjectMapper.readValue(json, Number.class);

        Assert.assertNotNull(number);
        Assert.assertEquals(123, number.intValue());
    }

    @Test
    public void testDeserializeInvalidJson() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "\"123\"a";

        try {
            Number number = testObjectMapper.readValue(json, Number.class);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeNull() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper testObjectMapper = new ObjectMapper(objectMapper);
        testObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "null";
        Number number = testObjectMapper.readValue(json, Number.class);

        Assert.assertNull(number);
    }
}