import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] byteArray = {1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);
        Assert.assertArrayEquals(byteArray, pojoNode.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValueNull() {
        POJONode pojoNode = new POJONode(null);
        Assert.assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        Assert.assertEquals("defaultValue", pojoNode.asText("defaultValue"));
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(true);
        Assert.assertTrue(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        Assert.assertTrue(pojoNode.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(10);
        Assert.assertEquals(10, pojoNode.asInt(0));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        Assert.assertEquals(5, pojoNode.asInt(5));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(100L);
        Assert.assertEquals(100L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        Assert.assertEquals(50L, pojoNode.asLong(50L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(3.14);
        Assert.assertEquals(3.14, pojoNode.asDouble(0.0), 0.001);
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        Assert.assertEquals(2.71, pojoNode.asDouble(2.71), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        POJONode pojoNode = new POJONode("test");
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        pojoNode.serialize(jsonGenerator, serializerProvider);
        // Verify the serialization result manually
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode pojoNode = new POJONode(pojo);
        Assert.assertEquals(pojo, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        Assert.assertEquals(pojoNode1, pojoNode2);
    }

    @Test
    public void testEqualsWithNull() {
        POJONode pojoNode1 = new POJONode(null);
        POJONode pojoNode2 = new POJONode(null);
        Assert.assertEquals(pojoNode1, pojoNode2);
    }

    @Test
    public void testEqualsWithDifferentValue() {
        POJONode pojoNode1 = new POJONode("test1");
        POJONode pojoNode2 = new POJONode("test2");
        Assert.assertNotEquals(pojoNode1, pojoNode2);
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        Assert.assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode pojoNode = new POJONode("test");
        Assert.assertEquals("test", pojoNode.toString());
    }

    @Test
    public void testToStringWithByteArray() {
        byte[] byteArray = {1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);
        Assert.assertEquals("(binary value of 3 bytes)", pojoNode.toString());
    }

    @Test
    public void testToStringWithRawValue() {
        RawValue rawValue = new RawValue("raw");
        POJONode pojoNode = new POJONode(rawValue);
        Assert.assertEquals("(raw value 'raw')", pojoNode.toString());
    }

}