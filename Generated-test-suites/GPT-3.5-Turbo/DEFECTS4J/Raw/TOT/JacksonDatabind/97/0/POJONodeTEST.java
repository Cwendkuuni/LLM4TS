import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() {
        POJONode node = new POJONode(new byte[]{1,2,3});
        try {
            assertTrue(node.binaryValue().length > 0);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());
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
    public void testAsInt() {
        POJONode node = new POJONode(10);
        assertEquals(10, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(100L);
        assertEquals(100L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(3.14);
        assertEquals(3.14, node.asDouble(0.0), 0.0001);
    }

    @Test
    public void testSerialize() {
        // Assuming ObjectMapper is available
        POJONode node = new POJONode(new Object());
        try {
            ObjectMapper mapper = new ObjectMapper();
            StringWriter writer = new StringWriter();
            JsonGenerator generator = mapper.getFactory().createGenerator(writer);
            SerializerProvider provider = mapper.getSerializerProvider();
            node.serialize(generator, provider);
            generator.close();
            assertEquals("{}", writer.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(10);
        POJONode node2 = new POJONode(10);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode(10);
        assertEquals(10, node.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(new byte[]{1,2,3,4});
        assertEquals("(binary value of 4 bytes)", node.toString());
    }
}