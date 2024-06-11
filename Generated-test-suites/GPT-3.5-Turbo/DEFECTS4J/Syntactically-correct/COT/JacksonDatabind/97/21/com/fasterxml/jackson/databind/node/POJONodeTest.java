
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
    public void testBinaryValue() {
        // Test when value is byte[]
        byte[] bytes = new byte[10];
        POJONode pojoNode = new POJONode(bytes);
        try {
            assertArrayEquals(bytes, pojoNode.binaryValue());
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }

        // Test when value is not byte[]
        POJONode pojoNode2 = new POJONode(null);
        try {
            assertNull(pojoNode2.binaryValue());
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());

        POJONode pojoNode2 = new POJONode("test");
        assertEquals("test", pojoNode2.asText());
    }

    @Test
    public void testAsText_withDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("default", pojoNode.asText("default"));

        POJONode pojoNode2 = new POJONode("test");
        assertEquals("test", pojoNode2.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(null);
        assertTrue(pojoNode.asBoolean(true));

        POJONode pojoNode2 = new POJONode(true);
        assertTrue(pojoNode2.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(123, pojoNode.asInt(123));

        POJONode pojoNode2 = new POJONode(456);
        assertEquals(456, pojoNode2.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(123L, pojoNode.asLong(123L));

        POJONode pojoNode2 = new POJONode(456L);
        assertEquals(456L, pojoNode2.asLong(0));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(1.23, pojoNode.asDouble(1.23), 0);

        POJONode pojoNode2 = new POJONode(4.56);
        assertEquals(4.56, pojoNode2.asDouble(0), 0);
    }

    @Test
    public void testSerialize() {
        // Test when value is null
        POJONode pojoNode = new POJONode(null);
        try {
            pojoNode.serialize(null, null);
            // If no exception is thrown, the test is considered passed
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }

        // Test when value is JsonSerializable
        // Note: Since there is no implementation of JsonSerializable in this test case, we cannot perform a functional test here.
        POJONode pojoNode2 = new POJONode(new Object());
        try {
            pojoNode2.serialize(null, null);
            // If no exception is thrown, the test is considered passed
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }

        // Test when value is neither null nor JsonSerializable
        // Note: We cannot directly test the behavior of writing the value with the generator, as the code calls gen.writeObject(_value) using the context instead.
        // Hence, we can only test that the serialization method does not throw an exception.
        POJONode pojoNode3 = new POJONode("test");
        try {
            pojoNode3.serialize(null, null);
            // If no exception is thrown, the test is considered passed
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode pojoNode = new POJONode(obj);
        assertEquals(obj, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode = new POJONode("test");
        POJONode pojoNode2 = new POJONode(null);
        assertFalse(pojoNode.equals(null));
        assertFalse(pojoNode.equals("test"));
        assertFalse(pojoNode.equals(pojoNode2));

        POJONode pojoNode3 = new POJONode("test");
        assertTrue(pojoNode.equals(pojoNode3));
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode = new POJONode("test");
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        // Test when the value is byte[]
        byte[] bytes = new byte[10];
        POJONode pojoNode = new POJONode(bytes);
        assertEquals("(binary value of 10 bytes)", pojoNode.toString());

        // Test when the value is RawValue
        RawValue rawValue = new RawValue("test");
        POJONode pojoNode2 = new POJONode(rawValue);
        assertEquals("(raw value 'test')", pojoNode2.toString());

        // Test when the value is neither byte[] nor RawValue
        POJONode pojoNode3 = new POJONode("test");
        assertEquals("test", pojoNode3.toString());
    }
}
