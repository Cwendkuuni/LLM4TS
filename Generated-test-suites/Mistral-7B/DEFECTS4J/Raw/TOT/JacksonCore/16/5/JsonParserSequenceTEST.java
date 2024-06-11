import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParserSequence sequence;

 @Before
 public void setup() throws IOException {
 parser1 = new JsonFactory().createParser("[]");
 parser2 = new JsonFactory().createParser("{}");
 sequence = JsonParserSequence.createFlattened(parser1, parser2);
 }

 @Test
 public void testContainedParsersCount() {
 assertEquals(2, sequence.containedParsersCount());
 }

 @Test
 public void testClose() throws IOException {
 sequence.close();
 assertEquals(JsonToken.END_ARRAY, parser1.nextToken());
 assertEquals(JsonToken.END_OBJECT, parser2.nextToken());
 assertNull(parser1.nextToken());
 assertNull(parser2.nextToken());
 }

 @Test
 public void testNextToken() throws IOException, JsonParseException {
 JsonToken token = sequence.nextToken();
 assertEquals(JsonToken.START_ARRAY, token.getTypeId());
 token = sequence.nextToken();
 assertEquals(JsonToken.END_ARRAY, token.getTypeId());

 token = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getTypeId());
 token = sequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getTypeId());
 assertEquals("", token.getText());
 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_NULL, token.getTypeId());
 token = sequence.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getTypeId());

 token = sequence.nextToken();
 assertNull(token);
 }

 @Test
 public void testSwitchToNextParser() throws IOException {
 assertFalse(sequence.switchToNext());
 assertEquals(JsonToken.START_ARRAY, parser1.nextToken().getTypeId());
 assertEquals(JsonToken.END_ARRAY, parser1.nextToken().getTypeId());
 assertFalse(parser1.hasNext());

 assertTrue(sequence.switchToNext());
 assertEquals(JsonToken.START_OBJECT, parser2.nextToken().getTypeId());
 assertEquals(JsonToken.FIELD_NAME, parser2.nextToken().getTypeId());
 assertEquals("", parser2.nextToken().getText());
 assertEquals(JsonToken.VALUE_NULL, parser2.nextToken().getTypeId());
 assertEquals(JsonToken.END_OBJECT, parser2.nextToken().getTypeId());
 assertFalse(parser2.hasNext());
 }
}