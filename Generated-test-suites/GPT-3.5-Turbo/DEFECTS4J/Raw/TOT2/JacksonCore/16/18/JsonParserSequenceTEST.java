import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser mockParser1 = Mockito.mock(JsonParser.class);
        JsonParser mockParser2 = Mockito.mock(JsonParser.class);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(mockParser1, mockParser2);
        
        assertNotNull(sequence);
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser mockParser1 = Mockito.mock(JsonParser.class);
        JsonParser mockParser2 = Mockito.mock(JsonParser.class);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {mockParser1, mockParser2});
        
        Mockito.when(mockParser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        Mockito.when(mockParser2.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
    }

    @Test
    public void testSwitchToNext() {
        JsonParser mockParser1 = Mockito.mock(JsonParser.class);
        JsonParser mockParser2 = Mockito.mock(JsonParser.class);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {mockParser1, mockParser2});
        
        assertTrue(sequence.switchToNext());
        assertEquals(mockParser2, sequence.delegate);

        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser mockParser1 = Mockito.mock(JsonParser.class);
        JsonParser mockParser2 = Mockito.mock(JsonParser.class);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {mockParser1, mockParser2});
        
        sequence.close();
        
        Mockito.verify(mockParser1, Mockito.times(1)).close();
        Mockito.verify(mockParser2, Mockito.times(1)).close();
    }
}