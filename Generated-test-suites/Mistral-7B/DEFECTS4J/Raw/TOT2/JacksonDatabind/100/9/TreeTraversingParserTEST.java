import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

public class TreeTraversingParserTest {

    @Test
    public void testParseEmptyJsonObject() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{}"));
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseEmptyJsonArray() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("[]"));
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseJsonObjectWithStringValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\": \"John Doe\"}"));
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("John Doe", parser.getText());
        parser.close();
    }

    @Test
    public void testParseJsonObjectWithNumberValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"age\": 30}"));
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testParseJsonArrayWithStringValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("[\"apple\", \"banana\"]"));
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("apple", parser.getText());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("banana", parser.getText());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseJsonArrayWithNumberValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("['1', 2, 3]"));
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(2, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(3, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testParseInvalidJson() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{1: \"name\"}"));
        parser.nextToken(); // consume "{"
        parser.nextToken(); // consume "1:"
        parser.nextToken(); // consume invalid token
        parser.close();
    }

    @Test
    public void testSkipChildren() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}"));
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        // "name"
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // consume "{"
        // "first"
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("first", parser.getCurrentName());
        parser.nextToken(); // consume "John"
        parser.skipChildren();
        // "last"
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("last", parser.getCurrentName());
        parser.nextToken(); // consume "Doe"
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testCloseParser() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{}"));
        parser.nextToken();
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testCloseParserTwice() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{}"));
        parser.nextToken();
        parser.close();
        parser.close();
    }

    private JsonNode createJsonNode(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(new StringReader(json));
    }

    private static class NodeCursor extends BaseParserMinimalBase.NodeCursor {

        protected JsonNode _currentNode;

        public NodeCursor(JsonNode n, JsonParser p) {
            super(p);
            _currentNode = n;
        }

        public JsonNode currentNode() {
            return _currentNode;
        }

        public String getCurrentName() {
            JsonNode node = _currentNode;
            return node.isObject() ? node.fieldNames().next().getName() : null;
        }

        public void overrideCurrentName(String name) {
            _currentNode = _currentNode.withName(name);
        }

        public String toString() {
            return _currentNode.toString();
        }
    }

    static class RootCursor extends NodeCursor {
        public RootCursor(JsonNode n, JsonParser p) {
            super(n, p);
        }

        public boolean currentHasChildren() {
            return _currentNode.has(JsonPointer.getInstance("/"));
        }

        public NodeCursor iterateChildren() {
            return new NodeCursor(_currentNode.get(JsonPointer.getInstance("/")), _parser);
        }
    }

    static class ArrayCursor extends NodeCursor {
        public ArrayCursor(JsonNode n, JsonParser p) {
            super(n, p);
        }

        public boolean currentHasChildren() {
            return _currentNode.isArray();
        }

        public NodeCursor iterateChildren() {
            return new NodeCursor(_currentNode.elements(), _parser);
        }
    }

    static class ObjectCursor extends NodeCursor {
        public ObjectCursor(JsonNode n, JsonParser p) {
            super(n, p);
        }

        public boolean currentHasChildren() {
            return _currentNode.isObject();
        }

        public NodeCursor iterateChildren() {
            return new NodeCursor(_currentNode.fields(), _parser);
        }
    }

    private JsonParser _parser;

    static class POJONode extends NodeBase {
        private Object _pojo;

        public Object getPojo() {
            return _pojo;
        }

        @SuppressWarnings("unchecked")
        public POJONode(JsonNode node) {
            super(node);
            _pojo = node.asPOJO();
        }
    }

    static class BinaryNode extends NodeBase {
        private byte[] _binaryValue;

        public byte[] binaryValue() {
            return _binaryValue;
        }

        public BinaryNode(JsonNode node) {
            super(node);
            _binaryValue = node.binaryValue();
        }
    }

    private static class NodeBase extends JsonNode {
        protected JsonParser _parser;

        public NodeBase(JsonNode node) {
            super(node);
            _parser = null;
        }

        public JsonParser getParser() {
            return _parser;
        }

        public void setParser(JsonParser parser) {
            _parser = parser;
        }
    }

    private static JsonFactory _factory = new JsonFactory();

    private JsonParser createJsonParser(String json) throws IOException {
        JsonParser parser = _factory.createParser(new StringReader(json));
        return new TreeTraversingParser(parser);
    }
}