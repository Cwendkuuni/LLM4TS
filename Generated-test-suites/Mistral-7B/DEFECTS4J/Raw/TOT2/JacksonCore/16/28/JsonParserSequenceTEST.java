import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonParserSequenceTest {

 private static final String TEST_JSON_1 = "{\"name\": \"John\", \"age\": 30}";
 private static final String TEST_JSON_2 = "{\"city\": \"New York\", \"country\": \"USA\"}";

 @Test
 public void testNextToken_WhenParserSequenceEmpty_ThenNullReturned() throws IOException {
 JsonParser parserSequence = JsonParserSequence.createFlattened(getJsonParser(TEST_JSON_1), getJsonParser(TEST_JSON_2));
 JsonToken token = parserSequence.nextToken();
 Assert.assertNull(token);
 }

 @Test
 public void testNextToken_WhenFirstParserReturnsNull_ThenNextParserReturned() throws IOException {
 JsonParser parserSequence = JsonParserSequence.createFlattened(getJsonParser(TEST_JSON_1), getJsonParser(TEST_JSON_2));
 parserSequence.nextToken(); // consume the first token
 JsonToken token = parserSequence.nextToken();
 Assert.assertNotNull(token);
 }

 @Test
 public void testNextToken_WhenFirstParserReturnsToken_ThenNextParserReturnsToken() throws IOException {
 JsonParser parserSequence = JsonParserSequence.createFlattened(getJsonParser(TEST_JSON_1), getJsonParser(TEST_JSON_2));
 JsonToken token1 = parserSequence.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token1.getClass());
 JsonToken token2 = parserSequence.nextToken();
 Assert.assertEquals(JsonToken.FIELD_NAME, token2.getClass());
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_WhenFirstParserThrowsJsonParseException_ThenNextParserDoesNotReturn() throws IOException {
 JsonParser parserSequence = JsonParserSequence.createFlattened(getInvalidJsonParser(), getJsonParser(TEST_JSON_2));
 parserSequence.nextToken(); // consume the first token
 JsonToken token = parserSequence.nextToken();
 Assert.assertNull(token);
 }

 @Test
 public void testNextToken_WhenFirstParserReturnsEOF_ThenNextParserReturnsEOF() throws IOException {
 JsonParser parserSequence = JsonParserSequence.createFlattened(getJsonParser(TEST_JSON_1), getJsonParser(TEST_JSON_2));
 JsonToken token1 = parserSequence.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token1.getClass());
 JsonToken eof1 = parserSequence.nextToken();
 Assert.assertEquals(JsonToken.END_OBJECT, eof1.getClass());
 JsonToken token2 = parserSequence.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token2.getClass());
 JsonToken eof2 = parserSequence.nextToken();
 Assert.assertEquals(JsonToken.END_OBJECT, eof2.getClass());
 }

 @Test
 public void testContainedParsersCount() throws IOException {
 JsonParser parserSequence = JsonParserSequence.createFlattened(getJsonParser(TEST_JSON_1), getJsonParser(TEST_JSON_2));
 int containedParsersCount = parserSequence.containedParsersCount();
 Assert.assertEquals(2, containedParsersCount);
 }

 private JsonParser getJsonParser(String jsonString) throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser(mapper.readTree(jsonString));
 return parser;
 }

 private JsonParser getInvalidJsonParser() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 ArrayNode arrayNode = factory.createArrayNode();
 ObjectNode objectNode = factory.createObjectNode();
 arrayNode.add(objectNode);
 return factory.createParser(arrayNode);
 }

}