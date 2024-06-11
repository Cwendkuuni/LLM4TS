
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.node.*;
import java.io.IOException;
import java.math.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.PackageVersion;

public class TreeTraversingParserTest {

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonNode node = new ObjectNode(null, null);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken()); // No more content
        assertTrue(parser.isClosed());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        ObjectNode objectNode = new ObjectNode(null, null);
        objectNode.put("name", "Alice");
        objectNode.put("age", 30);
        
        ArrayNode arrayNode = new ArrayNode(null, null);
        arrayNode.add(objectNode);
        
        TreeTraversingParser parser = new TreeTraversingParser(arrayNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
        
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.nextToken()); // No more content
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws IOException, JsonParseException {
        ObjectNode objectNode = new ObjectNode(null, null);
        objectNode.put("name", "Alice");
        objectNode.put("age", 30);
        
        TreeTraversingParser parser = new TreeTraversingParser(objectNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Alice", parser.getText());
        
        assertEquals("age", parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.getCurrentName()); // No current name
    }

    @Test
    public void testGetValue() throws IOException, JsonParseException {
        ObjectNode objectNode = new ObjectNode(null, null);
        objectNode.put("string", "Hello");
        objectNode.put("integer", 10);
        objectNode.put("decimal", 5.5);
        objectNode.put("nullValue", (JsonNode)null);
        
        TreeTraversingParser parser = new TreeTraversingParser(objectNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("string", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello", parser.getText());
        
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("integer", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(10, parser.getIntValue());
        
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("decimal", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(5.5, parser.getDoubleValue(), 0.001);
        
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("nullValue", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertNull(parser.getText());
        
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        String base64String = "SGVsbG8gV29ybGQ=";
        node.put("binary", base64String.getBytes());
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("binary", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, parser.nextToken());

        byte[] binaryValue = parser.getBinaryValue(Base64Variants.MIME_NO_LINEFEEDS);
        assertNotNull(binaryValue);
        assertArrayEquals(base64String.getBytes(), binaryValue);
        
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

}

