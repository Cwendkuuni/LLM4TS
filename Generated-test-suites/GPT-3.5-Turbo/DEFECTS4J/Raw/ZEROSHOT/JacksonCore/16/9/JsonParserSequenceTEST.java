import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.*;

public class JsonParserSequenceTest {

  @Test
  public void testCreateFlattened() throws IOException, JsonParseException {
    JsonParser p1 = new JsonFactory().createParser("{}");
    JsonParser p2 = new JsonFactory().createParser("[]");

    JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);

    assertEquals(2, sequence.containedParsersCount());
  }

  @Test
  public void testContainedParsersCount() throws IOException, JsonParseException {
    JsonParser p1 = new JsonFactory().createParser("{}");
    JsonParser p2 = new JsonFactory().createParser("[]");

    JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);

    assertEquals(2, sequence.containedParsersCount());
  }

  @Test
  public void testClose() throws IOException, JsonParseException {
    JsonParser p1 = new JsonFactory().createParser("{}");
    JsonParser p2 = new JsonFactory().createParser("[]");

    JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);

    sequence.close();

    assertTrue(p1.isClosed());
    assertTrue(p2.isClosed());
  }

  @Test
  public void testNextToken() throws IOException, JsonParseException {
    JsonParser p1 = new JsonFactory().createParser("{}");
    JsonParser p2 = new JsonFactory().createParser("[]");

    JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);

    for (JsonToken token : JsonToken.values()) {
      assertEquals(token, sequence.nextToken());
    }
  }

}