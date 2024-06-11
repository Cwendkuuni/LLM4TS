import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class StdSerializerTest {

    @Test
    public void testStringSerializer() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);

        String value = "Hello, World!";
        ObjectNode node = mapper.createObjectNode();
        stringSerializer.serialize(value, node.staticStreamProvider(), new SerializerProvider());

        String serializedValue = node.get("value").asText();
        Assert.assertEquals(value, serializedValue);

        // Edge case: null value
        String nullValue = null;
        ObjectNode nullNode = mapper.createObjectNode();
        stringSerializer.serialize(nullValue, nullNode.statricStreamProvider(), new SerializerProvider());
        Assert.assertNull(nullNode.get("value"));

        // Error scenario: IOException
        try {
            throw new IOException();
        } catch (IOException e) {
            ObjectNode errorNode = mapper.createObjectNode();
            stringSerializer.serialize(value, errorNode.statricStreamProvider(), new SerializerProvider());
            String errorSerializedValue = errorNode.get("value").asText();
            Assert.fail("Expected IOException to be thrown during serialization, but got: " + errorSerializedValue);
        }
    }

    @Test
    public void testIntSerializer() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);

        Integer value = 42;
        ObjectNode node = mapper.createObjectNode();
        intSerializer.serialize(value, node.statricStreamProvider(), new SerializerProvider());

        int serializedValue = node.get("value").asInt();
        Assert.assertEquals(value, serializedValue);

        // Edge case: minimum int value
        Integer minIntValue = Integer.MIN_VALUE;
        ObjectNode minIntNode = mapper.createObjectNode();
        intSerializer.serialize(minIntValue, minIntNode.statricStreamProvider(), new SerializerProvider());
        int minIntSerializedValue = minIntNode.get("value").asInt();
        Assert.assertEquals(minIntValue, minIntSerializedValue);

        // Error scenario: JsonProcessingException
        try {
            throw new JsonProcessingException("Test error");
        } catch (JsonProcessingException e) {
            ObjectNode errorNode = mapper.createObjectNode();
            intSerializer.serialize(value, errorNode.statricStreamProvider(), new SerializerProvider());
            int errorSerializedValue = errorNode.get("value").asInt();
            Assert.fail("Expected JsonProcessingException to be thrown during serialization, but got: " + errorSerializedValue);
        }
    }
}