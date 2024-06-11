import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.databind.JsonSerializer.StdSerializer.Base.INT;
import static com.fasterxml.jackson.databind.JsonSerializer.StdSerializer.Base.LONG;
import static com.fasterxml.jackson.databind.JsonSerializer.StdSerializer.Base.NUMBER;
import static com.fasterxml.jackson.databind.JsonSerializer.StdSerializer.Base.STRING;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonSerializer<Integer> intSerializer = mapper.serializationConfig().findValueSerializer(Integer.class);

        Assert.assertEquals(integerSerializer, intSerializer);
        Assert.assertEquals(NUMBER, intSerializer.getProvider().getXmlType());

        ObjectNode node = mapper.createObjectNode();
        node.put("integer", 123);
        IntNode intNode = (IntNode) mapper.readTree(node.toString());

        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(node.toString()));

        JsonSerializer<?> deserializer = mapper.readValue(node.toString(), JsonSerializer.class);
        Assert.assertEquals(integerSerializer.handledType(), deserializer.handledType());

        JsonNode deserializedNode = mapper.readTree(jsonParser);
        Assert.assertEquals(IntNode.class, deserializedNode.getClass());
        Assert.assertEquals(123, deserializedNode.asInt());

        byte[] serializedBytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(intNode);
        JsonNode serializedNode = mapper.readTree(new String(serializedBytes));
        Assert.assertEquals(TextNode.class, serializedNode.getClass());
        Assert.assertEquals("123", serializedNode.asText());

        JsonNode deserializedFromSerializedNode = mapper.readTree(serializedNode.toString());
        Assert.assertEquals(IntNode.class, deserializedFromSerializedNode.getClass());
        Assert.assertEquals(123, deserializedFromSerializedNode.asInt());
    }

    @Test
    public void testIntegerSerializerWithJsonFormatAnnotation() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;

        @JsonFormat(shape = shape)
        class WithJsonFormatAnnotation {
            private int intValue;

            public int getIntValue() {
                return intValue;
            }

            public void setIntValue(int intValue) {
                this.intValue = intValue;
            }
        }

        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        JsonSerializer<Integer> intSerializer = mapper.serializationConfig().findValueSerializer(Integer.class);

        Assert.assertEquals(integerSerializer, intSerializer);
        Assert.assertEquals(STRING, intSerializer.getProvider().getXmlType());

        WithJsonFormatAnnotation withJsonFormatAnnotation = new WithJsonFormatAnnotation();
        withJsonFormatAnnotation.setIntValue(123);
        ObjectNode node = mapper.createObjectNode();
        node.put("integer", withJsonFormatAnnotation);

        JsonNode deserializedNode = mapper.readTree(jsonParser);
        Assert.assertEquals(TextNode.class, deserializedNode.getClass());
        Assert.assertEquals("123", deserializedNode.asText());

        byte[] serializedBytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(node.get("integer"));
        JsonNode serializedNode = mapper.readTree(new String(serializedBytes));
        Assert.assertEquals(TextNode.class, serializedNode.getClass());
        Assert.assertEquals("123", serializedNode.asText());
    }

    @Test
    public void testLongSerializer() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        JsonSerializer<Long> longSerializerProvided = mapper.serializationConfig().findValueSerializer(Long.class);

        Assert.assertEquals(longSerializer, longSerializerProvided);
        Assert.assertEquals(NUMBER, longSerializer.getProvider().getXmlType());

        ObjectNode node = mapper.createObjectNode();
        node.put("long", 1234567890L);
        LongNode longNode = (LongNode) mapper.readTree(node.toString());

        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(node.toString()));

        JsonSerializer<?> deserializer = mapper.readValue(node.toString(), JsonSerializer.class);
        Assert.assertEquals(longSerializer.handledType(), deserializer.handledType());

        JsonNode deserializedNode = mapper.readTree(jsonParser);
        Assert.assertEquals(LongNode.class, deserializedNode.getClass());
        Assert.assertEquals(1234567890L, deserializedNode.asLong());

        byte[] serializedBytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(longNode);
        JsonNode serializedNode = mapper.readTree(new String(serializedBytes));
        Assert.assertEquals(TextNode.class, serializedNode.getClass());
        Assert.assertEquals("1234567890", serializedNode.asText());

        JsonNode deserializedFromSerializedNode = mapper.readTree(serializedNode.toString());
        Assert.assertEquals(LongNode.class, deserializedFromSerializedNode.getClass());
        Assert.assertEquals(1234567890L, deserializedFromSerializedNode.asLong());
    }

    @Test
    public void testShortSerializer() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        JsonSerializer<Short> shortSerializerProvided = mapper.serializationConfig().findValueSerializer(Short.class);

        Assert.assertEquals(shortSerializer, shortSerializerProvided);
        Assert.assertEquals(NUMBER, shortSerializer.getProvider().getXmlType());

        ObjectNode node = mapper.createObjectNode();
        node.put("short", Short.MIN_VALUE);
        ShortNode shortNode = (ShortNode) mapper.readTree(node.toString());

        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(node.toString()));

        JsonSerializer<?> deserializer = mapper.readValue(node.toString(), JsonSerializer.class);
        Assert.assertEquals(shortSerializer.handledType(), deserializer.handledType());

        JsonNode deserializedNode = mapper.readTree(jsonParser);
        Assert.assertEquals(ShortNode.class, deserializedNode.getClass());
        Assert.assertEquals(Short.MIN_VALUE, deserializedNode.asShort());

        byte[] serializedBytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(shortNode);
        JsonNode serializedNode = mapper.readTree(new String(serializedBytes));
        Assert.assertEquals(TextNode.class, serializedNode.getClass());
        Assert.assertEquals("-128", serializedNode.asText());

        JsonNode deserializedFromSerializedNode = mapper.readTree(serializedNode.toString());
        Assert.assertEquals(ShortNode.class, deserializedFromSerializedNode.getClass());
        Assert.assertEquals(Short.MIN_VALUE, deserializedFromSerializedNode.asShort());
    }

    @Test
    public void testFloatSerializer() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        JsonSerializer<Float> floatSerializerProvided = mapper.serializationConfig().findValueSerializer(Float.class);

        Assert.assertEquals(floatSerializer, floatSerializerProvided);
        Assert.assertEquals(NUMBER, floatSerializer.getProvider().getXmlType());

        ObjectNode node = mapper.createObjectNode();
        node.put("float", 123.456f);
        FloatNode floatNode = (FloatNode) mapper.readTree(node.toString());

        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(node.toString()));

        JsonSerializer<?> deserializer = mapper.readValue(node.toString(), JsonSerializer.class);
        Assert.assertEquals(floatSerializer.handledType(), deserializer.handledType());

        JsonNode deserializedNode = mapper.readTree(jsonParser);
        Assert.assertEquals(FloatNode.class, deserializedNode.getClass());
        Assert.assertEquals(123.456f, deserializedNode.asFloat(), 0.01f);

        byte[] serializedBytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(floatNode);
        JsonNode serializedNode = mapper.readTree(new String(serializedBytes));
        Assert.assertEquals(TextNode.class, serializedNode.getClass());
        Assert.assertEquals("123.45599999999998", serializedNode.asText());

        JsonNode deserializedFromSerializedNode = mapper.readTree(serializedNode.toString());
        Assert.assertEquals(FloatNode.class, deserializedFromSerializedNode.getClass());
        Assert.assertEquals(123.456f, deserializedFromSerializedNode.asFloat(), 0.01f);
    }

    @Test
    public void testDoubleSerializer() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        JsonSerializer<Double> doubleSerializerProvided = mapper.serializationConfig().findValueSerializer(Double.class);

        Assert.assertEquals(doubleSerializer, doubleSerializerProvided);
        Assert.assertEquals(NUMBER, doubleSerializer.getProvider().getXmlType());

        ObjectNode node = mapper.createObjectNode();
        node.put("double", 123.456789);
        DoubleNode doubleNode = (DoubleNode) mapper.readTree(node.toString());

        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(node.toString()));

        JsonSerializer<?> deserializer = mapper.readValue(node.toString(), JsonSerializer.class);
        Assert.assertEquals(doubleSerializer.handledType(), deserializer.handledType());

        JsonNode deserializedNode = mapper.readTree(jsonParser);
        Assert.assertEquals(DoubleNode.class, deserializedNode.getClass());
        Assert.assertEquals(123.456789, deserializedNode.asDouble(), 0.0001);

        byte[] serializedBytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(doubleNode);
        JsonNode serializedNode = mapper.readTree(new String(serializedBytes));
        Assert.assertEquals(TextNode.class, serializedNode.getClass());
        Assert.assertEquals("123.45678900000001", serializedNode.asText());

        JsonNode deserializedFromSerializedNode = mapper.readTree(serializedNode.toString());
        Assert.assertEquals(DoubleNode.class, deserializedFromSerializedNode.getClass());
        Assert.assertEquals(123.456789, deserializedFromSerializedNode.asDouble(), 0.0001);
    }

    @Test
    public void testIntLikeSerializer() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        JsonSerializer<Number> numberSerializerProvided = mapper.serializationConfig().findValueSerializer(Number.class);

        Assert.assertEquals(intLikeSerializer, numberSerializerProvided);
        Assert.assertEquals(NUMBER, intLikeSerializer.getProvider().getXmlType());

        Number number = 123;
        ObjectNode node = mapper.createObjectNode();
        node.put("number", number);
        NumberNode numberNode = (NumberNode) mapper.readTree(node.toString());

        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(node.toString()));

        JsonSerializer<?> deserializer = mapper.readValue(node.toString(), JsonSerializer.class);
        Assert.assertEquals(intLikeSerializer.handledType(), deserializer.handledType());

        JsonNode deserializedNode = mapper.readTree(jsonParser);
        Assert.assertEquals(IntNode.class, deserializedNode.getClass());
        Assert.assertEquals(123, deserializedNode.asInt());

        byte[] serializedBytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(numberNode);
        JsonNode serializedNode = mapper.readTree(new String(serializedBytes));
        Assert.assertEquals(TextNode.class, serializedNode.getClass());
        Assert.assertEquals("123", serializedNode.asText());

        JsonNode deserializedFromSerializedNode = mapper.readTree(serializedNode.toString());
        Assert.assertEquals(IntNode.class, deserializedFromSerializedNode.getClass());
        Assert.assertEquals(123, deserializedFromSerializedNode.asInt());
    }
}