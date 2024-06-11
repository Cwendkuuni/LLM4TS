import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest {

  private JsonParserSequence jsonParserSequence;
  private JsonParser firstParser;
  private JsonParser secondParser;

  @Before
  public void setUp() {
    firstParser = mock(JsonParser.class);
    secondParser = mock(JsonParser.class);
    jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
  }

  @Test
  public void testClose() throws IOException {
    jsonParserSequence.close();
    verify(firstParser).close();
    verify(secondParser).close();
  }

  @Test
  public void testNextToken() throws IOException, JsonParseException {
    JsonToken firstToken = JsonToken.START_OBJECT;
    JsonToken secondToken = JsonToken.FIELD_NAME;

    when(firstParser.nextToken()).thenReturn(firstToken, null);
    when(secondParser.nextToken()).thenReturn(secondToken, null);

    assertEquals(firstToken, jsonParserSequence.nextToken());
    assertEquals(secondToken, jsonParserSequence.nextToken());
    assertNull(jsonParserSequence.nextToken());
  }

  @Test
  public void testContainedParsersCount() {
    int expectedCount = 2;
    int actualCount = jsonParserSequence.containedParsersCount();
    assertEquals(expectedCount, actualCount);
  }

  @Test
  public void testSingleParser() {
    JsonParser singleParser = mock(JsonParser.class);
    jsonParserSequence = new JsonParserSequence(new JsonParser[] { singleParser });

    assertEquals(singleParser, jsonParserSequence.delegate);
    assertEquals(1, jsonParserSequence.containedParsersCount());
  }

  @Test
  public void testEmptyParsersArray() {
    JsonParser[] emptyParsersArray = new JsonParser[0];
    jsonParserSequence = new JsonParserSequence(emptyParsersArray);

    assertNull(jsonParserSequence.delegate);
    assertEquals(0, jsonParserSequence.containedParsersCount());
  }

  @Test
  public void testSwitchToNext() {
    JsonParser thirdParser = mock(JsonParser.class);
    JsonParser fourthParser = mock(JsonParser.class);

    jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser, thirdParser, fourthParser });

    assertTrue(jsonParserSequence.switchToNext());
    assertEquals(secondParser, jsonParserSequence.delegate);

    assertTrue(jsonParserSequence.switchToNext());
    assertEquals(thirdParser, jsonParserSequence.delegate);

    assertTrue(jsonParserSequence.switchToNext());
    assertEquals(fourthParser, jsonParserSequence.delegate);

    assertFalse(jsonParserSequence.switchToNext());
  }

}