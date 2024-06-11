import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

public class TreeTraversingParserTest {
    
    private TreeTraversingParser parser;
    private JsonNode jsonNode;

    @Before
    public void setUp() {
        jsonNode = new JsonNode();
        parser = new TreeTraversingParser(jsonNode);
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.skipChildren().nextToken());
    }

    @Test
    public void testIsClosed() throws IOException, JsonParseException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        assertEquals(null, parser.getCurrentName());
    }

    @Test
    public void testGetNumberType() throws IOException {
        assertEquals(null, parser.getNumberType());
    }

    @Test
    public void testGetIntValue() throws IOException {
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        assertEquals(0, parser.getLongValue());
    }

    @Test
    public void testGetEmbeddedObject() {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        assertNull(parser.getBinaryValue(Base64Variant.MIME));
    }

}