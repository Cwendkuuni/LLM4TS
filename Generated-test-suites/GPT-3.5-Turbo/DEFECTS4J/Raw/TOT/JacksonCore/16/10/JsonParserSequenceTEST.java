import static org.junit.Assert.*;
import org.junit.Test;

public class JsonParserSequenceTest {

  @Test
  public void testClose() {
    // Create the mock JsonParsers
    JsonParser parser1 = mock(JsonParser.class);
    JsonParser parser2 = mock(JsonParser.class);
    
    // Create the JsonParserSequence
    JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
    
    // Call the close method
    try {
      parserSequence.close();
    } catch (IOException e) {
      fail("IOException occurred");
    }
    
    // Verify that the close method was called on both parsers
    verify(parser1).close();
    verify(parser2).close();
  }

  @Test
  public void testNextTokenNoException() {
    // Create the mock JsonParsers
    JsonParser parser1 = mock(JsonParser.class);
    JsonParser parser2 = mock(JsonParser.class);
    
    // Set the expected return values for nextToken
    when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
    when(parser2.nextToken()).thenReturn(JsonToken.FIELD_NAME);
    
    // Create the JsonParserSequence
    JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
    
    try {
      // Call the nextToken method
      JsonToken token = parserSequence.nextToken();

      // Verify that the nextToken method was called on the first parser
      verify(parser1).nextToken();
      
      // Verify that the nextToken method was not called on the second parser
      verify(parser2, never()).nextToken();
      
      // Verify the returned token is correct
      assertEquals(JsonToken.START_OBJECT, token);
    } catch (IOException | JsonParseException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testNextTokenException() {
    // Create the mock JsonParsers
    JsonParser parser1 = mock(JsonParser.class);
    JsonParser parser2 = mock(JsonParser.class);
    
    // Set the expected return values for nextToken
    when(parser1.nextToken()).thenReturn(null);
    when(parser2.nextToken()).thenReturn(JsonToken.START_ARRAY);
    
    // Create the JsonParserSequence
    JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
    
    try {
      // Call the nextToken method
      JsonToken token = parserSequence.nextToken();

      // Verify that the nextToken method was called on both parsers
      verify(parser1).nextToken();
      verify(parser2).nextToken();
      
      // Verify the returned token is correct
      assertEquals(JsonToken.START_ARRAY, token);
    } catch (IOException | JsonParseException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testContainedParsersCount() {
    // Create the mock JsonParsers
    JsonParser parser1 = mock(JsonParser.class);
    JsonParser parser2 = mock(JsonParser.class);
    
    // Create the JsonParserSequence
    JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
    
    // Verify the containedParsersCount method returns the correct count
    assertEquals(2, parserSequence.containedParsersCount());
  }

}