import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;
    private JsonNode emptyArrayNode;
    private JsonNode emptyObjectNode;
    private JsonNode nullNode;
    private JsonNode booleanNode;
    private JsonNode textNode;
    private JsonNode numberNode;

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        emptyArrayNode = mapper.createArrayNode();
        emptyObjectNode = mapper.createObjectNode();
        nullNode = null;
        booleanNode = JsonNodeFactory.instance.booleanNode(true);
        textNode = JsonNodeFactory.instance.textNode("test");
        numberNode = JsonNodeFactory.instance.numberNode(123);
    }

    @Test
    public void testNextToken_EmptyArray() throws Exception {
        parser = new TreeTraversingParser(emptyArrayNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test
    public void testNextToken_EmptyObject() throws Exception {
        parser = new TreeTraversingParser(emptyObjectNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test
    public void testNextToken_NullNode() throws Exception {
        parser = new TreeTraversingParser(nullNode);
        assertNull(parser.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_NonNumericValue() throws Exception {
        parser = new TreeTraversingParser(textNode);
        parser.nextToken();
        parser.getDoubleValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetCurrentName_NotFieldName() throws Exception {
        parser = new TreeTraversingParser(numberNode);
        parser.getCurrentName();
    }
}