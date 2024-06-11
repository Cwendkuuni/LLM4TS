import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser first = new JsonFactory().createParser("{}");
        JsonParser second = new JsonFactory().createParser("{}");
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        assertNotNull(sequence);
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser first = new JsonFactory().createParser("{}");
        JsonParser second = new JsonFactory().createParser("{}");
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        assertTrue(sequence.switchToNext());
        assertTrue(sequence.switchToNext());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser first = new JsonFactory().createParser("{}");
        JsonParser second = new JsonFactory().createParser("{}");
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        sequence.close();
        // Check if both parsers are closed
        assertTrue(first.isClosed());
        assertTrue(second.isClosed());
    }

    @Test
    public void testNextToken() throws IOException {
        JsonParser first = new JsonFactory().createParser("{}");
        JsonParser second = new JsonFactory().createParser("{}");
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
    }
}