import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Test class for NumberSerializers.
 */
public class NumberSerializersTest {

    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setup() {
        objectMapper = JsonMapperFactory.getMapper();
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException, IOException {
        Integer number = 123;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("type", "number");
        expectedNode.put("number", number);
        Assert.assertEquals(expectedNode, jsonNode);

        JsonNode deserializedNode = objectMapper.readTree("{\"type\":\"number\",\"number\":123}");
        Integer deserializedNumber = objectMapper.treeToValue(deserializedNode, Integer.class);
        Assert.assertEquals(number, deserializedNumber);
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException, IOException {
        Short number = 123;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("type", "integer");
        expectedNode.put("number", number.shortValue());
        Assert.assertEquals(expectedNode, jsonNode);

        JsonNode deserializedNode = objectMapper.readTree("{\"type\":\"integer\",\"number\":123}");
        Short deserializedNumber = objectMapper.treeToValue(deserializedNode, Short.class);
        Assert.assertEquals(number, deserializedNumber);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException, IOException {
        Long number = 123L;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("type", "number");
        expectedNode.put("number", number);
        Assert.assertEquals(expectedNode, jsonNode);

        JsonNode deserializedNode = objectMapper.readTree("{\"type\":\"number\",\"number\":123}");
        Long deserializedNumber = objectMapper.treeToValue(deserializedNode, Long.class);
        Assert.assertEquals(number, deserializedNumber);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException, IOException {
        Float number = 123.45f;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("type", "number");
        expectedNode.put("number", number);
        Assert.assertEquals(expectedNode, jsonNode);

        JsonNode deserializedNode = objectMapper.readTree("{\"type\":\"number\",\"number\":123.45}");
        Float deserializedNumber = objectMapper.treeToValue(deserializedNode, Float.class);
        Assert.assertEquals(number, deserializedNumber, 0.001f);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException, IOException {
        Double number = 123.45;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("type", "number");
        expectedNode.put("number", number);
        Assert.assertEquals(expectedNode, jsonNode);

        JsonNode deserializedNode = objectMapper.readTree("{\"type\":\"number\",\"number\":123.45}");
        Double deserializedNumber = objectMapper.treeToValue(deserializedNode, Double.class);
        Assert.assertEquals(number, deserializedNumber, 0.001);
    }
}