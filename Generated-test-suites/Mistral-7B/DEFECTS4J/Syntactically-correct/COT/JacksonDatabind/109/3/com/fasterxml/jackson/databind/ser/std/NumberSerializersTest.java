import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.Integer;
import java.lang.Long;
import java.util.Map;

import static com.fasterxml.jackson.databind.JsonTestUtils.mapper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        ObjectMapper objectMapper = mapper();
        ObjectNode actual = objectMapper.createObjectNode();
        NumberSerializers.addAll(actual);

        ObjectNode expected = objectMapper.createObjectNode();
        expected.put("java.lang.Integer", mapper().getSerializerProvider().findValueSerializer(Integer.class));
        expected.put("java.lang.Integer", mapper().getSerializerProvider().findValueSerializer(Integer.TYPE));
        expected.put("java.lang.Long", mapper().getSerializerProvider().findValueSerializer(Long.class));
        expected.put("java.lang.Long", mapper().getSerializerProvider().findValueSerializer(Long.TYPE));
        expected.put("java.lang.Byte", IntLikeSerializer.instance);
        expected.put("java.lang.Byte", IntLikeSerializer.instance);
        expected.put("java.lang.Short", ShortSerializer.instance);
        expected.put("java.lang.Short", ShortSerializer.instance);
        expected.put("java.lang.Double", new DoubleSerializer(Double.class));
        expected.put("java.lang.Double", new DoubleSerializer(Double.TYPE));
        expected.put("java.lang.Float", FloatSerializer.instance);
        expected.put("java.lang.Float", FloatSerializer.instance);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = mapper();
        JsonNode input = objectMapper.createObjectNode();
        input.put("number", Short.parseShort("123"));

        ShortSerializer shortSerializer = new ShortSerializer();
        JsonNode expected = mapper().readTree("123");

        JsonNode actual = objectMapper.treeToJson(shortSerializer, objectMapper.getSerializerProvider(), input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = mapper();
        JsonNode input = objectMapper.createObjectNode();
        input.put("integer", Integer.parseInt("456"));

        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonNode expected = mapper().readTree("456");

        JsonNode actual = objectMapper.treeToJson(integerSerializer, objectMapper.getSerializerProvider(), input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = mapper();
        JsonNode input = objectMapper.createObjectNode();
        input.put("integer", new Integer(567));

        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonNode expected = mapper().readTree("567");

        JsonNode actual = objectMapper.treeToJson(intLikeSerializer, objectMapper.getSerializerProvider(), input);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithType_IntegerSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = mapper();
        JsonNode input = objectMapper.createObjectNode();
        input.put("integer", Integer.valueOf(789));

        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonNode actual = objectMapper.treeToJson(integerSerializer, objectMapper.getSerializerProvider(), input);

        ObjectMapper mapperForTest = mapper();
        JsonNode deserializedNode = mapperForTest.readTree(actual.toString());
        Object deserializedObject = mapperForTest.readValue(deserializedNode, Object.class);

        IntegerSerializer deserializedSerializer = new IntegerSerializer(Integer.class);
        deserializedSerializer.deserialize(mapperForTest.getFactory().createParser(actual.toString()), deserializedObject);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = mapper();
        JsonNode input = objectMapper.createObjectNode();
        input.put("number", Long.parseLong("9012345678901"));

        LongSerializer longSerializer = new LongSerializer(Long.class);
        JsonNode expected = mapper().readTree("9012345678901");

        JsonNode actual = objectMapper.treeToJson(longSerializer, objectMapper.getSerializerProvider(), input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = mapper();
        JsonNode input = objectMapper.createObjectNode();
        input.put("number", Float.parseFloat("3.141592"));

        FloatSerializer floatSerializer = new FloatSerializer();
        JsonNode expected = mapper().readTree("3.141592");

        JsonNode actual = objectMapper.treeToJson(floatSerializer, objectMapper.getSerializerProvider(), input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = mapper();
        JsonNode input = objectMapper.createObjectNode();
        input.put("number", Double.parseDouble("1.23456789"));

        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        JsonNode expected = mapper().readTree("1.23456789");

        JsonNode actual = objectMapper.treeToJson(doubleSerializer, objectMapper.getSerializerProvider(), input);
        Assert.assertEquals(expected, actual);
    }
}