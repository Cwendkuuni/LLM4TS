
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

import java.io.IOException;

import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        assertEquals(node.getNodeType(), JsonNodeType.POJO);
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        assertEquals(node.asToken(), JsonToken.VALUE_EMBEDDED_OBJECT);
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(null);
        assertNull(node.binaryValue());

        byte[] bytes = new byte[]{1, 2, 3};
        POJONode binaryNode = new POJONode(bytes);
        assertArrayEquals(binaryNode.binaryValue(), bytes);
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals(node.asText(), "test");

        POJONode nullNode = new POJONode(null);
        assertEquals(nullNode.asText(), "null");
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode node = new POJONode("test");
        assertEquals(node.asText("default"), "test");

        POJONode nullNode = new POJONode(null);
        assertEquals(nullNode.asText("default"), "default");
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertEquals(node.asBoolean(false), true);

        POJONode falseNode = new POJONode(false);
        assertFalse(falseNode.asBoolean(true));

        POJONode nullNode = new POJONode(null);
        assertFalse(nullNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(42);
        assertEquals(node.asInt(0), 42);

        POJONode nullNode = new POJONode(null);
        assertEquals(nullNode.asInt(0), 0);
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(42L);
        assertEquals(node.asLong(0L), 42L);

        POJONode nullNode = new POJONode(null);
        assertEquals(nullNode.asLong(0L), 0L);
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(3.14);
        assertEquals(node.asDouble(0.0), 3.14, 0.0);

        POJONode nullNode = new POJONode(null);
        assertEquals(nullNode.asDouble(0.0), 0.0, 0.0);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode nullNode = new POJONode(null);
        JsonGenerator generator = null; // Initialize the generator
        SerializerProvider provider = null; // Initialize a mock provider
        // Perform the serialization
        nullNode.serialize(generator, provider);
        // Verify the serialization results
        // (mock the verification steps)
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode("test");
        assertEquals(node.getPojo(), "test");

        POJONode nullNode = new POJONode(null);
        assertNull(nullNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        POJONode node3 = new POJONode("other");

        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode("test");
        assertEquals(node.hashCode(), "test".hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        assertEquals(node.toString(), "test");

        POJONode binaryNode = new POJONode(new byte[]{1, 2, 3});
        assertEquals(node.toString(), "(binary value of 3 bytes)");

        POJONode rawValueNode = new POJONode(new RawValue("raw"));
        assertEquals(node.toString(), "(raw value 'raw')");
    }
}

