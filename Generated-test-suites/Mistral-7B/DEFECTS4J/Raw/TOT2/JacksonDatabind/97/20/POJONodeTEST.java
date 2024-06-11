
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.POJONode;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        Assert.assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsText() {
        POJONode nodeText = new POJONode("Hello World");
        Assert.assertEquals("Hello World", nodeText.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode nodeNull = null;
        Assert.assertEquals("null", nodeNull.asText(null));
    }

    @Test
    public void testAsBoolean() {
        POJONode nodeTrue = new POJONode(true);
        POJONode nodeFalse = new POJONode(false);
        Assert.assertTrue(nodeTrue.asBoolean(false));
        Assert.assertFalse(nodeFalse.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode nodeInt = new POJONode(123);
        Assert.assertEquals(123, nodeInt.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode nodeLong = new POJONode(1234567890L);
        Assert.assertEquals(1234567890L, nodeLong.asLong(0));
    }

    @Test
    public void testAsDouble() {
        POJONode nodeDouble = new POJONode(123.45);
        Assert.assertEquals(123.45, nodeDouble.asDouble(0), 0.01);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode("Test Object");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        Assert.assertEquals("{\"value\":\"Test Object\"}", json);
    }

    @Test
    public void testGetPojo() {
        String testString = "Test String";
        POJONode node = new POJONode(testString);
        Assert.assertEquals(testString, node.getPojo());
    }

    @Test
    public void testBinaryValueWithByteArray() throws IOException {
        byte[] testByteArray = {1, 2, 3};
        POJONode nodeByteArray = new POJONode(testByteArray);
        byte[] result = nodeByteArray.binaryValue();
        Assert.assertArrayEquals(testByteArray, result);
    }

    @Test
    public void testBinaryValueWithEmptyPOJONode() throws IOException {
        POJONode emptyNode = new POJONode(null);
        try {
            emptyNode.binaryValue();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testEqualsWithNullPOJONode() {
        POJONode node1 = new POJONode("Test String 1");
        POJONode node2 = null;
        Assert.assertFalse(node1.equals(node2));
    }

    @Test
    public void testEqualsWithDifferentPOJONodes() {
        POJONode node1 = new POJONode("Test String 1");
        POJONode node2 = new POJONode("Test String 2");
        Assert.assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCodeWithDifferentPOJONodes() {
        POJONode node1 = new POJONode("Test String 1");
        POJONode node2 = new POJONode("Test String 2");
        Assert.assertNotEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testToStringWithRawValue() {
        POJONode node = new POJONode(new RawValue("Test String"));
        Assert.assertEquals("(raw value 'Test String')", node.toString());
    }

    @Test
    public void testToStringWithBinaryValue() {
        byte[] testByteArray = {1, 2, 3};
        POJONode nodeByteArray = new POJONode(testByteArray);
        Assert.assertEquals("(binary value of 3 bytes)", nodeByteArray.toString());
    }

    @Test
    public void testSerializeWithJsonSerializable() throws IOException {
        MyJsonSerializable jsonSerializable = new MyJsonSerializable();
        POJONode nodeJsonSerializable = new POJONode(jsonSerializable);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodeJsonSerializable);
        Assert.assertEquals("{\"value\":{\"name\":\"Test String\", \"age\":42}}", json);
    }

    @Test
    public void testSerializeWithNull() throws IOException {
        POJONode nodeNull = new POJONode(null);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodeNull);
        Assert.assertEquals("{}", json);
    }
}

class MyJsonSerializable {
    String name;
    int age;
}
