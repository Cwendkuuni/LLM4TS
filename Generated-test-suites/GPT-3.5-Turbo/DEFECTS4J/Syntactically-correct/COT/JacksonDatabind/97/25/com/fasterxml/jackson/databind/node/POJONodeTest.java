import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] value = new byte[]{1, 2, 3};
        POJONode node = new POJONode(value);
        Assert.assertArrayEquals(value, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test", node.asText());

        node = new POJONode(null);
        Assert.assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode node = new POJONode("test");
        Assert.assertEquals("test", node.asText("default"));

        node = new POJONode(null);
        Assert.assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        Assert.assertEquals(true, node.asBoolean(false));

        node = new POJONode(null);
        Assert.assertEquals(false, node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(10);
        Assert.assertEquals(10, node.asInt(0));

        node = new POJONode(null);
        Assert.assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(100L);
        Assert.assertEquals(100L, node.asLong(0L));

        node = new POJONode(null);
        Assert.assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(10.5);
        Assert.assertEquals(10.5, node.asDouble(0.0), 0.01);

        node = new POJONode(null);
        Assert.assertEquals(0.0, node.asDouble(0.0), 0.01);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode(null);
        // Testing with a mock JsonGenerator and SerializerProvider would be needed
    }

    @Test
    public void testGetPojo() {
        Object obj = "test";
        POJONode node = new POJONode(obj);
        Assert.assertEquals(obj, node.getPojo());
    }

    @Test
    public void testEquals() {
        Object obj = "test";
        POJONode node1 = new POJONode(obj);
        POJONode node2 = new POJONode(obj);
        Assert.assertTrue(node1.equals(node2));

        node2 = new POJONode("test");
        Assert.assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        Object obj = "test";
        POJONode node = new POJONode(obj);
        Assert.assertEquals(obj.hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        byte[] value = new byte[]{1, 2, 3};
        POJONode node = new POJONode(value);
        Assert.assertEquals("(binary value of 3 bytes)", node.toString());

        RawValue rawValue = new RawValue("{ \"key\": \"value\" }");
        node = new POJONode(rawValue);
        Assert.assertEquals("(raw value '{ \"key\": \"value\" }')", node.toString());

        node = new POJONode("test");
        Assert.assertEquals("test", node.toString());
    }
}