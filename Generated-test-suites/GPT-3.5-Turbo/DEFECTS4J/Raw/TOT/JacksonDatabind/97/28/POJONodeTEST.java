import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    @Test
    public void testConstructor() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj, node.getPojo());
    }

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() {
        POJONode node = new POJONode("test");
        try {
            node.binaryValue();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(new Object());
        assertEquals(node.getPojo().toString(), node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertTrue(node.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(10);
        assertEquals(10, node.asInt(0));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(100000000000L);
        assertEquals(100000000000L, node.asLong(0));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(3.14);
        assertEquals(3.14, node.asDouble(0), 0);
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(0.0, node.asDouble(0.0), 0);
    }

    @Test
    public void testSerialize() {
        POJONode node = new POJONode(new Object());
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        try {
            node.serialize(generator, provider);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode("test");
        POJONode node2 = new POJONode("test");
        assertEquals(node1, node2);
    }

    @Test
    public void testEqualsWithNullValue() {
        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        assertEquals(node1, node2);
    }

    @Test
    public void testEqualsWithDifferentValue() {
        POJONode node1 = new POJONode("test1");
        POJONode node2 = new POJONode("test2");
        assertNotEquals(node1, node2);
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode("test1");
        POJONode node2 = new POJONode("test1");
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode("test");
        assertEquals(node.getPojo().toString(), node.toString());
    }

    @Test
    public void testToStringWithBinaryValue() {
        byte[] byteArray = {1, 2, 3};
        POJONode node = new POJONode(byteArray);
        assertEquals("(binary value of 3 bytes)", node.toString());
    }

    @Test
    public void testToStringWithRawValue() {
        RawValue rawValue = new RawValue("test");
        POJONode node = new POJONode(rawValue);
        assertEquals("(raw value 'test')", node.toString());
    }
}