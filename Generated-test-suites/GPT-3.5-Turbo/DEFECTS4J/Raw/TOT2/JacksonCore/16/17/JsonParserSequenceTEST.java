import static org.junit.Assert.*;
import org.junit.Test;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser first = new JsonParser();
        JsonParser second = new JsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testSwitchToNext() {
        JsonParser first = new JsonParser();
        JsonParser second = new JsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        sequence.switchToNext();
        
        assertEquals(second, sequence.delegate);
    }

    @Test
    public void testNextToken() {
        JsonParser first = new JsonParser();
        JsonParser second = new JsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        // Add test scenario for JsonParseException
        try {
            sequence.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // expected
        }
    }

    @Test
    public void testClose() {
        JsonParser first = new JsonParser();
        JsonParser second = new JsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        // Add test scenario for IOException
        try {
            sequence.close();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
}