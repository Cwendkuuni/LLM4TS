import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

@RunWith(MockitoJUnitRunner.class)
public class JsonParserSequenceTest {
  
  @Test
  public void testCreateFlattened() {
    JsonParser first = Mockito.mock(JsonParser.class);
    JsonParser second = Mockito.mock(JsonParser.class);
    JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
    assertEquals(2, sequence.containedParsersCount());
  }
  
  @Test
  public void testClose() throws Exception {
    JsonParser first = Mockito.mock(JsonParser.class);
    JsonParser second = Mockito.mock(JsonParser.class);
    JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
    sequence.close();
    Mockito.verify(first).close();
    Mockito.verify(second).close();
  }
  
  @Test
  public void testNextToken() throws Exception {
    JsonParser first = Mockito.mock(JsonParser.class);
    JsonParser second = Mockito.mock(JsonParser.class);
    Mockito.when(first.nextToken()).thenReturn(JsonToken.START_ARRAY);
    Mockito.when(second.nextToken()).thenReturn(JsonToken.FIELD_NAME);
    JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
    
    assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
    assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
  }
  
  @Test
  public void testSwitchToNext() {
    JsonParser first = Mockito.mock(JsonParser.class);
    JsonParser second = Mockito.mock(JsonParser.class);
    JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
    
    assertTrue(sequence.switchToNext());
    assertEquals(second, sequence.delegate);
  }
}