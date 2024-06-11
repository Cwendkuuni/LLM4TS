import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NumberSerializersTest {

    private ObjectMapper objectMapper = JsonMapper.builder().build();

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer number = 123;
        String json = "123";

        JsonSerializer<Object> serializer = NumberSerializers.IntegerSerializer.instance;
        JsonNode node = objectMapper.treeParser().readValue(json);
        assertEquals(node.asInt(), number.intValue());

        byte[] bytes = objectMapper.writeValueAsBytes(number);
        JsonNode deserializedNode = objectMapper.readTree(bytes);
        assertEquals(deserializedNode.asInt(), number.intValue());

        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(number, gen, new SerializerProvider());
        gen.flush();
        assertEquals("123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Float number = 123.45f;
        String json = "123.45";

        JsonSerializer<Object> serializer = NumberSerializers.FloatSerializer.instance;
        JsonNode node = objectMapper.treeParser().readValue(json);
        assertEquals(node.asFloat(), number.floatValue(), 0.001f);

        byte[] bytes = objectMapper.writeValueAsBytes(number);
        JsonNode deserializedNode = objectMapper.readTree(bytes);
        assertEquals(deserializedNode.asFloat(), number.floatValue(), 0.001f);

        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(number, gen, new SerializerProvider());
        gen.flush();
        assertEquals("123.45", gen.getOutputBuffer().toString());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Long number = 1234567890123L;
        String json = "1234567890123";

        JsonSerializer<Object> serializer = NumberSerializers.LongSerializer.instance;
        JsonNode node = objectMapper.treeParser().readValue(json);
        assertEquals(node.asLong(), number.longValue());

        byte[] bytes = objectMapper.writeValueAsBytes(number);
        JsonNode deserializedNode = objectMapper.readTree(bytes);
        assertEquals(deserializedNode.asLong(), number.longValue());

        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(number, gen, new SerializerProvider());
        gen.flush();
        assertEquals("1234567890123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        Short number = (short) 123;
        String json = "123";

        JsonSerializer<Object> serializer = NumberSerializers.ShortSerializer.instance;
        JsonNode node = objectMapper.treeParser().readValue(json);
        assertEquals(node.asInt(), number.shortValue());

        byte[] bytes = objectMapper.writeValueAsBytes(number);
        JsonNode deserializedNode = objectMapper.readTree(bytes);
        assertEquals(deserializedNode.asInt(), number.shortValue());

        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(number, gen, new SerializerProvider());
        gen.flush();
        assertEquals("123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Double number = 123.456789;
        String json = "123.456789";

        JsonSerializer<Object> serializer = NumberSerializers.DoubleSerializer.instance;
        JsonNode node = objectMapper.treeParser().readValue(json);
        assertEquals(node.asDouble(), number.doubleValue(), 0.0001);

        byte[] bytes = objectMapper.writeValueAsBytes(number);
        JsonNode deserializedNode = objectMapper.readTree(bytes);
        assertEquals(deserializedNode.asDouble(), number.doubleValue(), 0.0001);

        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(number, gen, new SerializerProvider());
        gen.flush();
        assertEquals("123.456789", gen.getOutputBuffer().toString());
    }
}