import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    @Test
    public void testConstructor() {
        JsonNode node = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertNotNull(parser);
    }
    
    @Test
    public void testSetCodec() {
        JsonNode node = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertNull(parser.getCodec());
        parser.setCodec(new ObjectMapper());
        assertNotNull(parser.getCodec());
    }
    
    @Test
    public void testNextToken() {
        JsonNode node = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        try {
            assertNull(parser.getCurrentName());
            assertNull(parser.nextToken());
            assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSkipChildren() {
        JsonNode node = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            parser.skipChildren();
            assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetText() {
        JsonNode node = new ObjectMapper().createObjectNode();
        ((ObjectNode)node).put("key", "value");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        try {
            assertEquals("key", parser.getCurrentName());
            assertEquals("value", parser.getText());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetNumberValue() {
        JsonNode node = new ObjectMapper().createObjectNode();
        ((ObjectNode)node).put("key", 123);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        try {
            assertEquals("key", parser.getCurrentName());
            assertEquals(123, parser.getIntValue());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetBinaryValue() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().createObjectNode());
        assertNull(parser.getBinaryValue(Base64Variants.MIME_NO_LINEFEEDS));
    }

}