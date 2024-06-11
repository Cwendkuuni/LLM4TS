
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class POJONodeTest {
    
    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode pojoNode = new POJONode(new byte[]{1, 2, 3});
        assertArrayEquals(new byte[]{1, 2, 3}, pojoNode.binaryValue());

        POJONode pojoNode2 = new POJONode("");
        assertNull(pojoNode2.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());

        POJONode pojoNode2 = new POJONode("test");
        assertEquals("test", pojoNode2.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("default", pojoNode.asText("default"));

        POJONode pojoNode2 = new POJONode("test");
        assertEquals("test", pojoNode2.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(null);
        assertFalse(pojoNode.asBoolean(false));

        POJONode pojoNode2 = new POJONode(true);
        assertTrue(pojoNode2.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(123, pojoNode.asInt(123));

        POJONode pojoNode2 = new POJONode(456);
        assertEquals(456, pojoNode2.asInt(123));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(123L, pojoNode.asLong(123L));

        POJONode pojoNode2 = new POJONode(456L);
        assertEquals(456L, pojoNode2.asLong(123L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(1.23, pojoNode.asDouble(1.23), 0.0);

        POJONode pojoNode2 = new POJONode(4.56);
        assertEquals(4.56, pojoNode2.asDouble(1.23), 0.0);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode pojoNode = new POJONode(null);
        JsonGenerator gen = null; // replace with a mock generator
        SerializerProvider ctxt = null; // replace with a mock provider
        pojoNode.serialize(gen, ctxt);
        // Verify the serialization behavior
    }

    @Test
    public void testGetPojo() {
        String pojo = "test";
        POJONode pojoNode = new POJONode(pojo);
        assertEquals(pojo, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        assertTrue(pojoNode.equals(pojoNode2));

        POJONode pojoNode3 = new POJONode("test2");
        assertFalse(pojoNode.equals(pojoNode3));

        assertFalse(pojoNode.equals(null));
        assertFalse(pojoNode.equals("test"));
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        assertEquals(pojoNode.hashCode(), pojoNode2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode pojoNode = new POJONode(new byte[]{1, 2, 3});
        assertEquals("(binary value of 3 bytes)", pojoNode.toString());

        POJONode pojoNode2 = new POJONode("test");
        assertEquals("test", pojoNode2.toString());

        POJONode pojoNode3 = new POJONode(new RawValue("test"));
        assertEquals("(raw value 'test')", pojoNode3.toString());
    }
}
