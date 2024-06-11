import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ValueNode;

public class POJONodeTest {

    private POJONode pojoNode;

    @Before
    public void setUp() {
        pojoNode = new POJONode("test");
    }

    @Test
    public void testGetNodeType() {
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        assertNull(pojoNode.binaryValue());
    }

    @Test
    public void testAsText() {
        assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        assertEquals("test", pojoNode.asText("defaultValue"));
    }

    @Test
    public void testAsBoolean() {
        assertFalse(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.0001);
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = null;
        SerializerProvider ctxt = null;
        pojoNode.serialize(gen, ctxt);
    }

    @Test
    public void testGetPojo() {
        assertEquals("test", pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode otherNode = new POJONode("test");
        assertTrue(pojoNode.equals(otherNode));
    }

    @Test
    public void testHashCode() {
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", pojoNode.toString());
    }
}