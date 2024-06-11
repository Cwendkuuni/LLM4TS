import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testClose() {
        JsonParserSequence sequence = new JsonParserSequence(null);
        try {
            sequence.close();
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testNextToken() {
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        // Test typical use case
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        
        // Test edge case
        while (sequence.nextToken() != null) {
            // Loop until the last token is reached
        }
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        
        // Test error scenario
        sequence.close(); // Close the sequence
        
        try {
            sequence.nextToken();
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertNotNull(e);
        } catch (JsonParseException e) {
            fail("IOException should have been thrown instead of JsonParseException");
        }
    }

    @Test
    public void testContainedParsersCount() {
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();
        JsonParser parser3 = new JsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
        
        assertEquals(3, sequence.containedParsersCount());
    }
}